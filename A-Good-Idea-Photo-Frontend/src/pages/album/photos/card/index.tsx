import React from 'react';
import { FloatButton } from 'antd';
import { IconFont } from '@/configs/settings';
import { CommentOutlined } from '@ant-design/icons';
import PhotoCard from '@/pages/album/photos/card/PhotoCard';
import Upload from '@/pages/album/photos/upload';

export default () => {
  return (
    <>
      <PhotoCard />
      <FloatButton.Group
        trigger="click"
        tooltip={'更多操作'}
        icon={<IconFont type={'agood-xiangce'} />}
        type="primary"
      >
        <Upload />
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
