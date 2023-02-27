import React from 'react';
import { IconFont, projectSettings } from '@/configs/settings';
import { message } from 'antd';
import { Link } from '@@/exports';
import { GenderSelect, UserVerification } from '@/pages/user/_data';
import style from '@/pages/user/index.less';
import { getEmailCaptchaApi, userRegisterApi } from '@/services/userService';
import {
  LoginForm,
  ProFormCaptcha,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import { useNavigate } from 'umi';

/**
 * 用户注册界面
 */
export default () => {
  const navigate = useNavigate();
  /**
   * 用户注册
   * @param fields 表单信息
   */
  const doUserRegister = async (fields: UserType.UserRegisterRequest) => {
    const hide = message.loading('正在注册中...');
    try {
      const res = await userRegisterApi({ ...fields });
      message.success(res.msg);
      navigate('/user/login', { replace: true });
    } catch (e: any) {
      message.error('注册失败：' + e.message);
    } finally {
      hide();
    }
  };

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
  return (
    <div className={style.form}>
      <LoginForm<UserType.UserRegisterRequest>
        logo={projectSettings.logo}
        title={projectSettings.title}
        subTitle={projectSettings.description}
        submitter={{
          searchConfig: {
            submitText: '点击注册'
          },
          submitButtonProps: {
            size: 'middle'
          }
        }}
        onFinish={async (formData) => {
          await doUserRegister(formData);
        }}
      >
        <ProFormText
          name="username"
          placeholder={'请输入用户名'}
          rules={[...UserVerification.username]}
          fieldProps={{
            prefix: <IconFont type={'agood-xingmingyonghumingnicheng'} />
          }}
        />
        <ProFormText
          name="realname"
          placeholder={'请输入真实姓名'}
          rules={[...UserVerification.realname]}
          fieldProps={{
            prefix: <IconFont type={'agood-zhenshixingming'} />
          }}
        />
        <ProFormText.Password
          name="password"
          placeholder={'请输入密码'}
          rules={[...UserVerification.password]}
          fieldProps={{
            prefix: <IconFont type={'agood-mima'} />
          }}
        />
        <ProFormText.Password
          name="password2"
          placeholder={'请再次确认密码'}
          rules={[
            ...UserVerification.password2,
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error('请确认两次输入的密码一致!'));
              }
            })
          ]}
          fieldProps={{
            prefix: <IconFont type={'agood-mima'} />
          }}
        />
        <ProFormSelect
          name="gender"
          rules={[...UserVerification.gender]}
          options={[...GenderSelect]}
          placeholder="请选择性别"
        />
        <ProFormText
          name="email"
          placeholder={'请输入邮箱'}
          // @ts-ignore
          rules={[...UserVerification.email]}
          fieldProps={{
            prefix: <IconFont type={'agood-youxiang'} />
          }}
        />
        <ProFormCaptcha
          name="captcha"
          phoneName="email"
          fieldProps={{
            prefix: <IconFont type={'agood-yanzhengma'} />
          }}
          countDown={360}
          placeholder={'请输入验证码'}
          rules={[...UserVerification.captcha]}
          captchaTextRender={(timing, count) => {
            if (timing) {
              return `${count} 获取验证码`;
            }
            return '获取验证码';
          }}
          onGetCaptcha={async (email) => {
            await doGetCaptcha(email);
          }}
        />
        <div style={{ marginBottom: 24 }}>
          <Link to="/user/login">已有账号，去登录</Link>
          <Link to="/" style={{ float: 'right' }}>
            返回主页
          </Link>
        </div>
      </LoginForm>
    </div>
  );
};
