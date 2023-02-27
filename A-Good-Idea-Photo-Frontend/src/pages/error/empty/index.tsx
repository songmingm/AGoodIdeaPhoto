import React from 'react';
import { Empty } from 'antd';

/**
 * 数据为空页面
 */
export default () => {
  return <Empty style={{ height: '100%' }} description={'空空如也...'} />;
};
