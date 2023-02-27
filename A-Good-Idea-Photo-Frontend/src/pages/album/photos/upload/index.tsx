import React from 'react';
import { useParams } from '@@/exports';
import { FloatButton, message } from 'antd';
import { ModalForm } from '@ant-design/pro-components';
import { PlusSquareOutlined } from '@ant-design/icons';
import { ProFormUploadDragger } from '@ant-design/pro-form';
import { uploadPhotosApi } from '@/services/photoService';

/**
 * 图片上传表单
 */
export default () => {
  const params = useParams();
  const albumId = Number(params.id);
  /**
   * 图片上传
   * @param images 图片数组
   */
  const doPhotoUpload = async (images: any[]) => {
    const data = new FormData();
    images.forEach((image) => {
      data.append('images', image.originFileObj);
    });
    const hide = message.loading('正在上传中...');
    const res = await uploadPhotosApi(albumId, data);
    message.success(res.msg);
    window.location.reload();
    try {
    } catch (e: any) {
      message.error('上传失败：' + e.message);
    } finally {
      hide();
    }
  };

  return (
    <ModalForm<{ images: any[] }>
      title="上传图片"
      trigger={<FloatButton icon={<PlusSquareOutlined />} />}
      width={400}
      onFinish={async (formData) => {
        await doPhotoUpload(formData.images);
        return true;
      }}
    >
      <ProFormUploadDragger
        name="images"
        description="可以选择最多5张图片"
        max={5}
        accept=".png,.jpg,.jpeg"
        fieldProps={{
          listType: 'picture'
        }}
      />
    </ModalForm>
  );
};
