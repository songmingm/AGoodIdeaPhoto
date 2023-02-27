import React from 'react';
import { useModel } from '@@/exports';
import UserCenter from '@/pages/user/center/UserCenter';
import NotLogin from '@/pages/error/noAuth';

export default () => {
  const { initialState } = useModel('@@initialState');
  const loginUser = initialState?.loginUser;

  if (loginUser) {
    return <UserCenter />;
  } else {
    return <NotLogin />;
  }
};
