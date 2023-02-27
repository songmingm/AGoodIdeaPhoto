/**
 * 注册校验信息
 */
export const UserVerification = {
  username: [
    {
      required: true,
      message: '请输入昵称！'
    }
  ],
  realname: [
    {
      required: true,
      message: '请输入真实姓名！'
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码！'
    }
  ],
  password2: [
    {
      required: true,
      message: '请再次输入密码！'
    }
  ],
  gender: [
    {
      required: true,
      message: '请选择性别！'
    }
  ],
  email: [
    {
      required: true,
      message: '请输入邮箱！'
    },
    {
      type: 'email',
      message: '邮箱格式错误！'
    }
  ],
  captcha: [
    {
      required: true,
      message: '请输入验证码！'
    }
  ]
};

/**
 * 性别下拉框
 */
export const GenderSelect = [
  { label: '哦爷们要战斗', value: 1 },
  { label: '娇滴滴的妹妹', value: 0 },
  { label: '偶从泰国回来', value: 2 }
];

/**
 * 登录方式Tab栏
 */
export const LoginTypeTab = [
  { label: '账号登录', key: 'account' },
  { label: '邮箱登录', key: 'email' }
];

/**
 * 登录成功处理token逻辑
 * @param token token令牌信息
 */
export function loginSuccessForTokenHandler(token: string) {
  const loginData = JSON.stringify({
    isLogin: true,
    token: token
  } as UserType.LoginState);
  sessionStorage.setItem('login-state', loginData);
}

/**
 * 获取sessionStorage中的认证信息，并且解析
 */
export function parseTokenHandler() {
  const loginData = sessionStorage.getItem('login-state');
  if (loginData) {
    return JSON.parse(loginData) as UserType.LoginState;
  }
}
