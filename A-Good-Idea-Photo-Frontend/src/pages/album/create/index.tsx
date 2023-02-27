import { IconFont } from '@/configs/settings';
import {
  ModalForm,
  ProForm,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormUploadButton
} from '@ant-design/pro-components';
import React from 'react';
import { AlbumTypeSelect, AlbumVerification } from '@/pages/album/_data';
import { FloatButton, message } from 'antd';
import { createAlbumApi } from '@/services/photoService';

export default () => {
  /**
   * 创建图库
   * @param fields 表单信息
   * @param coverImage 图片文件信息
   */
  const doCreateAlbum = async (
    fields: PhotoType.CreateAlbumRequest,
    coverImage: File
  ) => {
    const hide = message.loading('正在创建中...');
    try {
      const res = await createAlbumApi(fields, coverImage);
      message.success(res.msg);
      await window.location.reload();
    } catch (e: any) {
      message.error('创建失败：' + e.message);
    } finally {
      hide();
    }
  };

  return (
    <ModalForm<PhotoType.CreateAlbumRequest>
      title="创建图库"
      trigger={<FloatButton icon={<IconFont type={'agood-xinzeng1'} />} />}
      width={400}
      onFinish={async (formData) => {
        await doCreateAlbum(formData, formData.coverImage[0].originFileObj);
        return true;
      }}
    >
      <ProForm.Group>
        <ProFormText
          width="md"
          name="albumName"
          label="图库名称"
          tooltip={'图库名称请填写中文'}
          rules={[...AlbumVerification.albumName]}
        />
        <ProFormSelect
          width="md"
          name="albumType"
          label="图库类型"
          options={[...AlbumTypeSelect]}
          tooltip={'私有图库只有自己能访问，共享图库可以拉入小伙伴'}
          rules={[...AlbumVerification.albumType]}
        />
        <ProFormDigit
          width="md"
          name="maxUser"
          label="图库最大人数"
          rules={[...AlbumVerification.maxUser]}
        />
        <ProFormUploadButton
          label="相册封面"
          name="coverImage"
          max={1}
          rules={[...AlbumVerification.coverImage]}
        />
        <ProFormTextArea
          width="md"
          name="remarks"
          label="描述信息"
          rules={[...AlbumVerification.remarks]}
        />
      </ProForm.Group>
    </ModalForm>
  );
};
