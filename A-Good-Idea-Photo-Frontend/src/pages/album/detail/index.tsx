import {
  getAlbumDetailApi,
  updateAlbumApi,
  updateAlbumCoverApi
} from '@/services/photoService';
import {
  ProCard,
  ProForm,
  ProFormDigit,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import { useParams } from '@umijs/max';
import {
  Avatar,
  Col,
  Form,
  Image,
  message,
  Modal,
  Row,
  Switch,
  Tag,
  Upload
} from 'antd';
import React, { useEffect, useState } from 'react';
import loadError from '@/assets/img/loading-error.png';
import { UploadProps } from 'antd/es/upload/interface';
import { beforeUpload } from '@/configs/settings';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { AlbumVerification } from '@/pages/album/_data';

/**
 * 图库详细信息
 */
export default () => {
  const params = useParams();
  // album id 路由动态参数
  const albumId = Number(params.id);
  const [album, setAlbum] = useState<PhotoType.AlbumDetail>();
  const [form] = Form.useForm<PhotoType.UpdateAlbumRequest>();
  const [readonly, setReadonly] = useState(true);

  async function doGetAlbumDetail() {
    const hide = message.loading('正在加载中...');
    try {
      const res = await getAlbumDetailApi(albumId);
      form.setFieldsValue(res.data);
      setAlbum(res.data);
    } catch (e: any) {
      message.error('加载失败：' + e.message);
    } finally {
      hide();
    }
  }

  /**
   * 修改相册信息
   */
  async function doUpdateAlbum(params: PhotoType.UpdateAlbumRequest) {
    const hide = message.loading('正在提交...');
    try {
      const res = await updateAlbumApi(params, albumId);
      message.success(res.msg);
      await doGetAlbumDetail();
      setReadonly(true);
    } catch (e: any) {
      message.error('修改失败：' + e.message);
    } finally {
      hide();
    }
  }

  /**
   * 修改封面
   */
  async function doUpdateCover(image: any) {
    const hide = message.loading('正在修改...');
    try {
      const res = await updateAlbumCoverApi(image, albumId);
      message.success(res.msg);
      await doGetAlbumDetail();
      return res;
    } catch (e: any) {
      message.error('修改失败：' + e.message);
    } finally {
      hide();
    }
  }

  /**
   * 封面上传参数设置
   */
  const coverUrlProps: UploadProps = {
    maxCount: 1,
    customRequest: (fileDetail) => {
      doUpdateCover(fileDetail.file).then(fileDetail.onSuccess);
    },
    beforeUpload: beforeUpload
  };
  /**
   * 确认修改框
   */
  const confirmBtn = async (params: PhotoType.UpdateAlbumRequest) => {
    Modal.confirm({
      icon: <ExclamationCircleOutlined />,
      title: '修改警告',
      content: '确定要修改信息吗？',
      centered: true,
      onOk: () => doUpdateAlbum(params)
    });
  };

  useEffect(() => {
    doGetAlbumDetail().then();
  }, []);

  // 控制提交按钮的显示与隐藏
  const subBtn = !readonly
    ? {
        searchConfig: {
          submitText: '修改'
        },
        submitButtonProps: {
          size: 'middle'
        }
      }
    : !readonly;
  const readOnlyBtn = () => {
    return (
      <Switch
        checked={!readonly}
        checkedChildren="编辑"
        unCheckedChildren="仅读"
        onChange={() => {
          setReadonly(!readonly);
        }}
      />
    );
  };
  return (
    <>
      <div>
        <Row gutter={[12, 12]}>
          <Col xs={22} offset={1}>
            <ProCard title="相册详细信息" extra={readOnlyBtn()}>
              <ProForm<PhotoType.UpdateAlbumRequest>
                readonly={readonly}
                form={form}
                layout={'horizontal'}
                size={'middle'}
                requiredMark={false}
                // @ts-ignore
                submitter={subBtn}
                onFinish={async (formData) => {
                  await confirmBtn(formData);
                }}
              >
                <ProFormText
                  name="albumName"
                  label="相册名称"
                  rules={[...AlbumVerification.albumName]}
                />
                <div style={{ marginTop: '10px', marginBottom: '20px' }}>
                  <span>相册类型(不可修改)：</span>
                  <span>
                    <Tag color="purple">{album?.albumType}</Tag>
                  </span>
                </div>
                <div style={{ marginTop: '10px', marginBottom: '20px' }}>
                  <span>
                    相册封面：
                    <Upload
                      name="coverUrl"
                      {...coverUrlProps}
                      disabled={readonly}
                    >
                      <Image
                        width={200}
                        style={{ borderRadius: '10px' }}
                        preview={false}
                        fallback={loadError}
                        src={album?.coverUrl}
                      />
                    </Upload>
                  </span>
                </div>
                <ProFormDigit
                  name="maxUser"
                  label="相册最大人数"
                  min={1}
                  rules={[...AlbumVerification.maxUser]}
                />
                <div style={{ marginTop: '10px', marginBottom: '20px' }}>
                  当前人数：
                  <span style={{ marginLeft: '10px' }}>{album?.userNum}</span>
                </div>
                <div style={{ marginTop: '10px', marginBottom: '20px' }}>
                  创建人：
                  <span>
                    <Avatar
                      shape={'square'}
                      src={album?.createByAvatarUrl}
                    ></Avatar>
                  </span>
                  <span style={{ marginLeft: '10px' }}>{album?.createBy}</span>
                </div>
                <div style={{ marginTop: '10px', marginBottom: '20px' }}>
                  <span>创建时间：{album?.createTime}</span>
                </div>
                <ProFormTextArea
                  name="remarks"
                  label="描述信息"
                  rules={[...AlbumVerification.remarks]}
                />
              </ProForm>
            </ProCard>
          </Col>
        </Row>
      </div>
    </>
  );
};
