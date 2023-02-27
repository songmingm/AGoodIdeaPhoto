import React from 'react';
import { Empty } from 'antd';

/**
 * 未登录
 */
export default () => (
  <Empty
    image="https://gw.alipayobjects.com/zos/antfincdn/ZHrcdLPrvN/empty.svg"
    imageStyle={{ height: 200 }}
    description={<span>您还没有登录</span>}
  ></Empty>
);
