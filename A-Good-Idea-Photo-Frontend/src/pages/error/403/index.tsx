import { Button, Result, Space } from 'antd';
import React from 'react';
import { history } from '@umijs/max';

/**
 * 403 没有权限路由
 */
export default () => {
  return (
    <Result
      status="403"
      title="403"
      subTitle="抱歉，您似乎没有访问的权限。"
      extra={
        <Space size="large">
          <Button
            type="primary"
            onClick={() => {
              history.go(-1);
            }}
          >
            返回上一页
          </Button>
          <Button
            type="primary"
            onClick={() => {
              history.push('/');
            }}
          >
            返回主页面
          </Button>
        </Space>
      }
    />
  );
};
