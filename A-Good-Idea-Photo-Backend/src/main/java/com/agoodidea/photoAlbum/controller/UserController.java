package com.agoodidea.photoAlbum.controller;

import com.agoodidea.photoAlbum.model.dto.UserLoginByEmailRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserLoginRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserRegisterRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserUpdateRequestParam;
import com.agoodidea.photoAlbum.model.vo.LoginUserDataVO;
import com.agoodidea.photoAlbum.service.MailService;
import com.agoodidea.photoAlbum.service.UserService;
import com.agoodidea.photoAlbum.utils.CaptchaUtil;
import com.agoodidea.photoAlbum.utils.ConstantsMsg;
import com.agoodidea.photoAlbum.utils.ResponseJsonData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping("loginByAccount")
    private ResponseJsonData<String> userLoginByAccount(@Validated @RequestBody UserLoginRequestParam param) {
        String jwtJson = userService.loginByUsername(param);
        return ResponseJsonData.successForData(jwtJson);
    }

    @PostMapping("loginByEmail")
    private ResponseJsonData<String> userLoginByEmail(@Validated @RequestBody UserLoginByEmailRequestParam param) {
        String jwtJson = userService.loginByEmail(param);
        return ResponseJsonData.successForData(jwtJson);
    }

    @PostMapping("logout")
    private ResponseJsonData<String> userLogout() {
        userService.logout();
        return ResponseJsonData.successNoData();
    }

    @PostMapping("register")
    private ResponseJsonData<String> userRegister(@Validated @RequestBody UserRegisterRequestParam param) {
        Integer flag = userService.register(param);
        return flag > 0 ? ResponseJsonData.successNoData(ConstantsMsg.User.REGISTER_SUCCESS)
                : ResponseJsonData.failed(ConstantsMsg.Common.NETWORK_ERROR);
    }

    @GetMapping("emailcode")
    private ResponseJsonData<String> sendEmailCode(@RequestParam("email") String email) {
        String captcha = CaptchaUtil.builderCaptcha();
        Boolean flag = mailService.sendEmailCode(email, captcha);
        return flag ? ResponseJsonData.successNoData("验证码发送成功，5分钟内有效") :
                ResponseJsonData.failed("邮件发送失败");
    }

    @GetMapping("data")
    private ResponseJsonData<LoginUserDataVO> getLoginUserData() {
        return ResponseJsonData.successForData(userService.getLoginUserData());
    }

    @PutMapping("update")
    private ResponseJsonData<String> userUpdate(@RequestBody @Validated UserUpdateRequestParam param) {
        Boolean flag = userService.updateMyData(param);
        return flag ? ResponseJsonData.successNoData(ConstantsMsg.Common.UPDATE_SUCCESS)
                : ResponseJsonData.failed(ConstantsMsg.Common.UPDATE_ERROR);
    }

    @PostMapping("avatar")
    private ResponseJsonData<String> uploadAvatar(MultipartFile avatar) {
        Boolean flag = userService.uploadAvatar(avatar);
        return flag ? ResponseJsonData.successNoData(ConstantsMsg.File.UPLOAD_SUCCESS) :
                ResponseJsonData.failed(ConstantsMsg.File.UPLOAD_ERROR);

    }
}
