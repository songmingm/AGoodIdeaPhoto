import {
  LoginForm,
  ProCard,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import { Button, Col, Image, message, Modal, Row, Switch } from 'antd';
import React, { useState } from 'react';
import { useModel } from '@umijs/max';
import { GenderSelect, UserVerification } from '@/pages/user/_data';
import { updateMyDataApi, uploadAvatarApi } from '@/services/userService';
import { ExclamationCircleOutlined, UploadOutlined } from '@ant-design/icons';
import Dragger from 'antd/es/upload/Dragger';
import { beforeUpload } from '@/configs/settings';
import { UploadProps } from 'antd/es/upload/interface';
import loadingError from '@/assets/img/loading-error.png';

/**
 * 个人中心界面
 */
const UserCenter: React.FC = () => {
  const [readonly, setReadonly] = useState(true);
  const { initialState } = useModel('@@initialState');
  const loginUserData = initialState?.loginUser;
  const [layout] = useState('half');

  /**
   * 修改我的信息
   * @param fields 表单数据
   */
  async function doUpdateMyData(fields: UserType.UserUpdateRequest) {
    const hide = message.loading('正在修改中...');
    try {
      const res = await updateMyDataApi({ ...fields });
      message.success(res.msg);
      await window.location.reload();
    } catch (e: any) {
      message.error('修改失败：' + e.getMessage);
    } finally {
      hide();
    }
  }

  /**
   * 设置头像
   */
  async function setMyAvatar(param: any) {
    const hide = message.loading('正在上传...');
    try {
      const res = await uploadAvatarApi(param);
      message.success(res.msg);
      await window.location.reload();
    } catch (e: any) {
      message.error('上传失败：' + e.getMessage);
    } finally {
      hide();
    }
  }

  const imageProps: UploadProps = {
    maxCount: 1,
    name: 'avatar',
    customRequest: (fileDetail) => setMyAvatar(fileDetail.file),
    beforeUpload: beforeUpload
  };

  /**
   * 确认框
   */
  const confirmBtn = async (params: UserType.UserUpdateRequest) => {
    Modal.confirm({
      icon: <ExclamationCircleOutlined />,
      title: '修改警告',
      content: '确定要修改信息吗？',
      centered: true,
      onOk: () => doUpdateMyData(params)
    });
  };

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
    <div>
      <Row gutter={[12, 12]}>
        <Col
          xs={24}
          xl={layout === 'half' ? 12 : 24}
          order={layout === 'output' ? 2 : 1}
        >
          <ProCard title="修改头像" tooltip="头像">
            <div style={{ width: 300, margin: 'auto' }}>
              <Image
                src={
                  loginUserData?.avatarUrl ? loginUserData.avatarUrl : 'error'
                }
                fallback={loadingError}
              />
            </div>
            <div style={{ width: 300, margin: 'auto' }}>
              <Dragger {...imageProps}>
                <Button type={'link'} icon={<UploadOutlined />}>
                  选择图片
                </Button>
              </Dragger>
            </div>
          </ProCard>
        </Col>
        <Col
          xs={24}
          xl={layout === 'half' ? 12 : 24}
          order={layout === 'output' ? 1 : 2}
        >
          <ProCard
            title="我的个人信息"
            extra={readOnlyBtn()}
            tooltip="修改我的信息"
          >
            <LoginForm<UserType.UserUpdateRequest>
              readonly={readonly}
              name="myData"
              layout={'vertical'}
              size={'middle'}
              requiredMark={false}
              // @ts-ignore
              submitter={subBtn}
              initialValues={{
                username: loginUserData?.username,
                realname: loginUserData?.realname,
                gender: loginUserData?.gender,
                createTime: loginUserData?.createTime,
                email: loginUserData?.email,
              }}
              onFinish={async (formData) => await confirmBtn(formData)}
            >
              <ProFormText
                name="username"
                rules={[...UserVerification.username]}
                label="昵称"
              />
              <ProFormText
                name="realname"
                rules={[...UserVerification.realname]}
                label="真实姓名"
              />
              <ProFormSelect
                name="gender"
                label="性别"
                rules={[...UserVerification.gender]}
                options={[...GenderSelect]}
                placeholder="请选择你的性别"
              />
              <ProFormText
                name="email"
                // @ts-ignore
                rules={[...UserVerification.email]}
                label="邮箱"
              />
              <ProFormText
                name="createTime"
                label="注册时间"
                tooltip="注册时间不可修改"
                disabled
              />
            </LoginForm>
          </ProCard>
        </Col>
      </Row>
    </div>
  );
};

export default UserCenter;
