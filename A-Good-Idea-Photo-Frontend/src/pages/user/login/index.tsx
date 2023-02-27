import {
  LoginForm,
  ProFormCaptcha,
  ProFormText
} from '@ant-design/pro-components';
import { message, Tabs } from 'antd';
import React, { useState } from 'react';
import style from '@/pages/user/index.less';
import { IconFont, projectSettings } from '@/configs/settings';
import { Link } from '@@/exports';
import {
  getEmailCaptchaApi,
  userLoginByAccountApi,
  userLoginByEmailApi
} from '@/services/userService';
import { useModel } from '@umijs/max';
import { useSearchParams } from 'umi';
import {
  loginSuccessForTokenHandler,
  LoginTypeTab,
  UserVerification
} from '@/pages/user/_data';

type LoginType = 'email' | 'account';

/**
 * 登录页面
 */
export default () => {
  const [loginType, setLoginType] = useState<LoginType>('account');
  const { refresh } = useModel('@@initialState');
  const [searchParams] = useSearchParams();

  /**
   * 获取邮箱验证码
   * @param filed 邮箱
   */
  const doGetCaptcha = async (filed: string) => {
    const hide = message.loading('正在获取验证码...');
    try {
      const res = await getEmailCaptchaApi(filed);
      message.success(res.msg);
    } catch (e: any) {
      message.error('验证码获取失败：' + e.message);
    } finally {
      hide();
    }
  };

  /**
   * 用户登录
   * @param fields 登录表单参数
   */
  const doLogin = async (fields: UserType.UserLoginRequest) => {
    const hide = message.loading('正在登录中...');
    if (loginType === 'account') {
      try {
        const res = await userLoginByAccountApi({ ...fields });
        loginSuccessForTokenHandler(res.data);
        await refresh;
        // 重定向到之前的页面
        window.location.href = searchParams.get('redirect') ?? '/';
      } catch (e: any) {
        message.error('登录失败：' + e.message);
      } finally {
        hide();
      }
    } else if (loginType === 'email') {
      try {
        const res = await userLoginByEmailApi({ ...fields });
        loginSuccessForTokenHandler(res.data);
        await refresh;
        // 重定向到之前的页面
        window.location.href = searchParams.get('redirect') ?? '/';
      } catch (e: any) {
        message.error('登录失败：' + e.message);
      } finally {
        hide();
      }
    }
  };
  return (
    <div className={style.form}>
      <LoginForm<UserType.UserLoginRequest>
        logo={projectSettings.logo}
        title={projectSettings.title}
        subTitle={projectSettings.description}
        onFinish={async (formData) => {
          await doLogin(formData);
        }}
      >
        <Tabs
          centered
          activeKey={loginType}
          onChange={(activeKey) => setLoginType(activeKey as LoginType)}
          items={LoginTypeTab}
        ></Tabs>
        {loginType === 'account' && (
          <>
            <ProFormText
              name="username"
              placeholder={'请输入用户名'}
              fieldProps={{
                size: 'large',
                prefix: <IconFont type={'agood-xingmingyonghumingnicheng'} />
              }}
              rules={[...UserVerification.username]}
            />
            <ProFormText.Password
              name="password"
              placeholder={'请输入用密码'}
              fieldProps={{
                size: 'large',
                prefix: <IconFont type={'agood-mima'} />
              }}
              rules={[...UserVerification.password]}
            />
          </>
        )}
        {loginType === 'email' && (
          <>
            <ProFormText
              fieldProps={{
                size: 'large',
                prefix: <IconFont type={'agood-youxiang'} />
              }}
              name="email"
              placeholder={'请输入邮箱'}
              // @ts-ignore
              rules={[...UserVerification.email]}
            />
            <ProFormCaptcha
              name="captcha"
              phoneName="email"
              countDown={360}
              fieldProps={{
                size: 'large',
                prefix: <IconFont type={'agood-yanzhengma'} />
              }}
              captchaProps={{
                size: 'large'
              }}
              captchaTextRender={(timing, count) => {
                if (timing) {
                  return `${count} 获取验证码`;
                }
                return '获取验证码';
              }}
              placeholder={'请输入验证码'}
              rules={[...UserVerification.captcha]}
              onGetCaptcha={async (email) => {
                await doGetCaptcha(email);
              }}
            />
          </>
        )}
        <div style={{ marginBottom: 15 }}>
          <Link to="/user/register">快速注册</Link>
          <Link to="/" style={{ marginLeft: 10 }}>
            返回主页
          </Link>
          <Link to="/user/register" style={{ float: 'right' }}>
            忘记密码？
          </Link>
        </div>
      </LoginForm>
    </div>
  );
};
