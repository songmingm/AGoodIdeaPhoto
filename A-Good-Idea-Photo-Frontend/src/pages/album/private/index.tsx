import React from 'react';
import AlbumCard from '@/pages/album/private/AlbumCard';
import { FloatButton } from 'antd';
import Create from '../create';
import { IconFont } from '@/configs/settings';
import { CommentOutlined } from '@ant-design/icons';

export default () => {
  return (
    <>
      <AlbumCard />
      <FloatButton.Group
        trigger="click"
        tooltip={'更多操作'}
        icon={<IconFont type={'agood-xiangce'} />}
        type="primary"
      >
        <Create />
        <FloatButton icon={<CommentOutlined />} />
        <FloatButton.BackTop
          visibilityHeight={-1}
          tooltip={'返回顶部'}
          icon={<IconFont type={'agood-huojian1'} />}
        />
      </FloatButton.Group>
    </>
  );
};
