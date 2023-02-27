/**
 * 用户类型定义
 */
declare namespace UserType {
  /**
   * 用户登录参数
   */
  interface UserLoginRequest {
    username: string;
    password: string;
  }

  /**
   * 用户注册
   */
  interface UserRegisterRequest {
    username: string;
    password: string;
    password2: string;
    realname: string;
    gender: number;
    email: string;
    captcha: string;
  }

  /**
   * 登录用户信息
   */
  interface LoginUserData {
    username: string;
    realname: string;
    avatarUrl: string;
    email: string;
    createTime: string;
    gender: number;
  }

  /**
   * 登录状态
   */
  interface LoginState {
    token: string;
    isLogin: boolean;
  }

  /**
   * 用户修改
   */
  interface UserUpdateRequest {
    username: string;
    password: string;
    realname: string;
    gender: number;
    email: string;
  }
}
