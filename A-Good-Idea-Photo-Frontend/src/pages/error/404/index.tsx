import { Button, Result, Space } from 'antd';
import React from 'react';
import { history } from '@umijs/max';

/**
 * 404 Not Found路由
 */
export default () => {
  return (
    <Result
      status="404"
      title="404"
      subTitle="抱歉，您访问的页面可能不存在。"
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
