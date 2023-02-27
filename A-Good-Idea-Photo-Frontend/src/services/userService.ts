import { request } from "@umijs/max";

/**
 *  用户注册api
 * @param params 注册的参数信息
 */
export async function userRegisterApi(params: UserType.UserRegisterRequest) {
  return request<ResponseJsonData<string>>('/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  });
}

/**
 * 账号密码登录api
 * @param params 登录表单的信息
 */
export async function userLoginByAccountApi(params: UserType.UserLoginRequest) {
  return request<ResponseJsonData<string>>('/user/loginByAccount', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  });
}

/**
 * 邮箱登录api
 * @param params 邮箱与验证码
 */
export async function userLoginByEmailApi(params: any) {
  return request<ResponseJsonData<string>>('/user/loginByEmail', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: params
  });
}

/**
 * 获取邮箱验证码
 * @param param 邮箱
 */
export async function getEmailCaptchaApi(param: string) {
  return request<ResponseJsonData<string>>('/user/emailcode', {
    method: 'GET',
    params: {
      email: param
    }
  });
}

/**
 * 用户退出api
 */
export async function userLogout() {
  return request<ResponseJsonData<string>>('/user/logout', {
    method: 'POST'
  });
}

/**
 * 获取当前登录用户的信息
 */
export async function getLoginUserDataApi() {
  return request<ResponseJsonData<UserType.LoginUserData>>('/user/data', {
    method: 'GET'
  });
}

/**
 * 修改我的信息
 * @param params 表单数据
 */
export async function updateMyDataApi(params: UserType.UserUpdateRequest) {
  return request<ResponseJsonData<UserType.LoginUserData>>('/user/update', {
    method: 'PUT',
    data: params
  });
}

/**
 * 用户上传头像
 * @param param 图片
 */
export async function uploadAvatarApi(param: any) {
  return request<ResponseJsonData<string>>('/user/avatar', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: {
      avatar: param
    }
  });
}
