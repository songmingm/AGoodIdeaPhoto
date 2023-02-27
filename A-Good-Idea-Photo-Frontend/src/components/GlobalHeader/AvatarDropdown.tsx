import React from 'react';
import { useModel } from '@umijs/max';
import { Avatar, Button, Dropdown, MenuProps, message } from 'antd';
import { Link } from '@@/exports';
import { userLogout } from '@/services/userService';
import { history } from 'umi';
import { IconFont } from '@/configs/settings';

/**
 * 头像下拉
 */
const AvatarDropdown: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  const loginUser = initialState?.loginUser;

  /**
   * 退出登录
   */
  const doLogout = async () => {
    const hide = message.loading('正在退出中...');
    try {
      await userLogout();
      sessionStorage.removeItem('login-state');
    } catch (e: any) {
      message.error('操作失败');
    } finally {
      hide();
      // 清除登录用户信息
      await setInitialState({ ...initialState, loginUser: undefined });
      history.replace({ pathname: '/user/login' });
    }
  };
  const items: MenuProps['items'] = [
    {
      key: '2',
      label: <Link to={'/user/center'}>个人中心</Link>,
      icon: <IconFont type={'agood-gerenzhongxin'} />
    },
    {
      key: '3',
      label: <span onClick={doLogout}>退出登录</span>,
      icon: <IconFont type={'agood-tuichudenglu'} />
    }
  ];

  /**
   * 未登录时的界面
   */
  const NotLogin = () => {
    return (
      <Link to="/user/login">
        <Button type="primary" size="middle" ghost style={{ marginRight: 20 }}>
          登录
        </Button>
      </Link>
    );
  };

  /**
   * 已登录
   */
  const HasLogin = () => {
    return (
      <Dropdown menu={{ items }} arrow={true}>
        <Avatar size={'large'} src={loginUser?.avatarUrl} shape={'square'} />
      </Dropdown>
    );
  };

  return loginUser ? <HasLogin /> : <NotLogin />;
};

export default AvatarDropdown;
