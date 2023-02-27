package com.agoodidea.photoAlbum.service.impl;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.filter.EmailCodeAuthenticationToken;
import com.agoodidea.photoAlbum.mapper.AlbumMapper;
import com.agoodidea.photoAlbum.mapper.FileMapper;
import com.agoodidea.photoAlbum.mapper.UserMapper;
import com.agoodidea.photoAlbum.model.dto.UserLoginByEmailRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserLoginRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserRegisterRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserUpdateRequestParam;
import com.agoodidea.photoAlbum.model.entity.LoginUser;
import com.agoodidea.photoAlbum.model.entity.User;
import com.agoodidea.photoAlbum.model.vo.LoginUserDataVO;
import com.agoodidea.photoAlbum.service.UserService;
import com.agoodidea.photoAlbum.utils.CaptchaUtil;
import com.agoodidea.photoAlbum.utils.FileCommonUtl;
import com.agoodidea.photoAlbum.utils.JwtUtil;
import com.agoodidea.photoAlbum.utils.LoginContext;
import com.agoodidea.photoAlbum.utils.mapstruct.LoginUserDataMapStruct;
import com.agoodidea.photoAlbum.utils.mapstruct.UserRegisterMapStruct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson GSON = new Gson();
    private final UserMapper userMapper;
    private final AlbumMapper albumMapper;
    private final UserRegisterMapStruct userRegisterMapStruct;
    private final LoginUserDataMapStruct loginUserDataMapStruct;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final FileMapper fileMapper;
    @Value("${minio.avatar-bucket}")
    private String avatarBucket;

    public UserServiceImpl(UserMapper userMapper,
                           AlbumMapper albumMapper,
                           UserRegisterMapStruct userRegisterMapStruct,
                           LoginUserDataMapStruct loginUserDataMapStruct,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           StringRedisTemplate stringRedisTemplate,
                           FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.albumMapper = albumMapper;
        this.userRegisterMapStruct = userRegisterMapStruct;
        this.loginUserDataMapStruct = loginUserDataMapStruct;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.fileMapper = fileMapper;
    }

    /**
     * 用户认证，认证成功返回Authentication对象，认证失败抛出异常
     * 登录缓存时长：2小时
     * 封装用户信息到线程中
     */
    @Override
    public String loginByUsername(UserLoginRequestParam param) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
        Authentication authenticate = authenticationManager.authenticate(auth);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // redis存取登录用户的信息，认证成功，返回令牌信息
        stringRedisTemplate.opsForValue().set("login:" + loginUser.getUser().getId(), GSON.toJson(loginUser), 2, TimeUnit.HOURS);
        return JwtUtil.buildJwt(GSON.toJson(loginUser.getUser().getId()));
    }

    @Override
    public String loginByEmail(UserLoginByEmailRequestParam param) {
        EmailCodeAuthenticationToken auth = new EmailCodeAuthenticationToken(param.getEmail());
        Authentication authenticate = authenticationManager.authenticate(auth);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 开始校验验证码是否正确
        String loginCaptcha = stringRedisTemplate.opsForValue().get(param.getEmail());
        if (ObjectUtils.isEmpty(loginCaptcha)) {
            throw new BusinessException("验证码已过期，请重新获取验证码");
        }
        Boolean matchingCaptcha = CaptchaUtil.matchingCaptcha(loginCaptcha, param.getCaptcha());
        if (!matchingCaptcha) {
            throw new BusinessException("验证码错误");
        }
        // 匹配成功后，删除失效的验证码
        stringRedisTemplate.delete(param.getEmail());
        stringRedisTemplate.opsForValue()
                .set("login:" + loginUser.getUser().getId(), GSON.toJson(loginUser), 2, TimeUnit.HOURS);
        return JwtUtil.buildJwt(GSON.toJson(loginUser.getUser().getId()));
    }


    @Override
    @Transactional
    public Integer register(UserRegisterRequestParam param) {
        if (!param.getPassword().equals(param.getPassword2())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        LambdaQueryWrapper<User> w1 = new LambdaQueryWrapper<>();
        w1.eq(User::getEmail, param.getEmail());
        Long isExist = userMapper.selectCount(w1);
        if (isExist > 0) {
            throw new BusinessException("邮箱已被注册");
        }
        // 开始校验验证码是否正确
        String captcha = stringRedisTemplate.opsForValue().get(param.getEmail());
        if (ObjectUtils.isEmpty(captcha)) {
            throw new BusinessException("验证码已过期，请重新获取验证码");
        }
        Boolean matchingCaptcha = CaptchaUtil.matchingCaptcha(captcha, param.getCaptcha());
        if (!matchingCaptcha) {
            throw new BusinessException("验证码错误");
        }
        User user = userRegisterMapStruct.source2target(param);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());
        // 删除失效的验证码
        stringRedisTemplate.delete(param.getEmail());
        logger.info("用户：{}，注册成功", param.getUsername());
        return userMapper.insert(user);
    }

    @Override
    public LoginUserDataVO getLoginUserData() {
        Long myID = LoginContext.getLoginData().getUser().getId();
        User user = userMapper.selectById(myID);
        LoginUserDataVO userDataVO = loginUserDataMapStruct.source2target(user);
        String avatar = user.getAvatarUrl();
        if (!ObjectUtils.isEmpty(avatar)) {
            userDataVO.setAvatarUrl(fileMapper.preview(avatar, avatarBucket));
        }
        return userDataVO;
    }

    @Override
    public void logout() {
        Long uerid = LoginContext.getLoginData().getUser().getId();
        stringRedisTemplate.delete("login:" + uerid);
    }

    @Override
    @Transactional
    public Boolean updateMyData(UserUpdateRequestParam param) {
        Long myID = LoginContext.getLoginData().getUser().getId();
        User user = userMapper.selectById(myID);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户不存在");
        }
        user.setEmail(param.getEmail());
        user.setUsername(param.getUsername());
        user.setRealname(param.getRealname());
        user.setGender(param.getGender());
        user.setUpdateTime(new Date());
        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional
    public Boolean uploadAvatar(MultipartFile file) {
        // 修改后删除原有头像
        Long myID = LoginContext.getLoginData().getUser().getId();
        User user = userMapper.selectById(myID);
        byte[] imageBytes = FileCommonUtl.compressImageToBytes(file, 300, 300);
        InputStream inputSteam = FileCommonUtl.getImageInputSteam(imageBytes);
        String objectname = FileCommonUtl.renameFile(file.getOriginalFilename());
        fileMapper.putObjectInBucket(avatarBucket, inputSteam, imageBytes.length, objectname);
        user.setAvatarUrl(objectname);
        return userMapper.updateById(user) > 0;
    }

    /**
     * 加载用户权限信息，创建合集，共享合集
     *
     * @return 用户的图库集合权限
     */
    @Override
    public List<String> getUserPermissions() {
//        Long userId = LoginContext.getLoginData().getUser().getId();
//        LambdaQueryWrapper<Album> w1 = new LambdaQueryWrapper<>();
//        w1.select(Album::getBucketName).eq(Album::getCreateBy, userId);
//        List<Album> albums = albumMapper.selectList(w1);
//        return albums.stream()
//                .map(Album::getBucketName)
//                .distinct()
//                .collect(Collectors.toList());
//    }
        return null;
    }
}



