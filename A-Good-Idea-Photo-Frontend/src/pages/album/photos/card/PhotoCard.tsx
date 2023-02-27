import React, { useEffect, useState } from 'react';
import { useParams } from '@@/exports';
import {
  Avatar,
  Card,
  DatePicker,
  Image,
  Input,
  List,
  message,
  Modal,
  Space
} from 'antd';
import { deletePhotoApi, getPhotosByAlbumApi } from '@/services/photoService';
import loadError from '@/assets/img/loading-error.png';
import {
  DeleteOutlined,
  EditOutlined,
  EllipsisOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons';

/**
 * 照片列表
 */
const PhotoCard: React.FC = () => {
  const params = useParams();
  // album id 路由动态参数
  const albumId = Number(params.id);
  const [photos, setPhotos] = useState<IPage<PhotoType.Photo>>();
  const [queryParam, setQueryParam] = useState<{
    createBy?: string;
    createTime?: string;
  }>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [param, setParam] = useState<PhotoType.PhotoCardSearchRequest>({
    albumId: albumId,
    current: 1,
    size: 5
  });

  /**
   * 获取照片信息
   */
  const doGetPhotosByAlbum = async () => {
    const hide = message.loading('正在加载中...');
    try {
      const res = await getPhotosByAlbumApi(param);
      setPhotos(res.data);
      setLoading(false);
    } catch (e: any) {
      message.error('加载失败：' + e.message);
    } finally {
      hide();
    }
  };
  const doSearch = () => {
    setParam({
      ...param,
      createBy: queryParam.createBy,
      createTime: queryParam.createTime
    });
  };

  /**
   *  删除照片
   */
  const doDeletePhoto = async (photoID: number) => {
    const hide = message.loading('正在删除...');
    try {
      const res = await deletePhotoApi(albumId, photoID);
      message.success(res.msg);
      await doGetPhotosByAlbum();
    } catch (e: any) {
      message.error('删除失败：' + e.message);
    } finally {
      hide();
    }
  };

  /**
   * 确认框
   */
  const confirmBtn = async (photoID: number) => {
    Modal.confirm({
      icon: <ExclamationCircleOutlined />,
      title: '删除警告',
      content: '确定要删除这张图片吗？',
      centered: true,
      onOk: () => doDeletePhoto(photoID)
    });
  };
  const search = () => (
    <div style={{ textAlign: 'right', marginRight: '5px' }}>
      <Space>
        <DatePicker
          placeholder={'该日期后的图片'}
          onChange={(date, dateString) => {
            setQueryParam({ ...queryParam, createTime: dateString });
          }}
        />
        <Input.Search
          enterButton
          loading={loading}
          style={{ width: 180 }}
          placeholder={'请输入创建人'}
          onChange={(event) => {
            setQueryParam({ ...queryParam, createBy: event.target.value });
          }}
          onSearch={() => doSearch()}
        />
      </Space>
    </div>
  );
  useEffect(() => {
    doGetPhotosByAlbum().then();
  }, [param]);

  return (
    <>
      <List
        loading={loading}
        itemLayout="vertical"
        dataSource={photos?.records}
        size="small"
        grid={{ xs: 1, sm: 1, md: 2, lg: 3 }}
        pagination={{
          pageSize: param.size,
          total: photos?.total,
          onChange: (page) => {
            setParam({ ...param, current: page });
          }
        }}
        split={false}
        header={search()}
        renderItem={(item) => (
          <Card
            bordered={false}
            style={{
              marginBottom: '10px',
              marginLeft: '5px',
              marginRight: '5px'
            }}
            actions={[
              <DeleteOutlined
                key="delete"
                onClick={async () => {
                  await confirmBtn(item.id);
                }}
              />,
              <EditOutlined key="edit" />,
              <EllipsisOutlined key="ellipsis" />
            ]}
          >
            <List.Item
              key={item.id}
              extra={<Image width={250} src={item.url} fallback={loadError} />}
            >
              <List.Item.Meta
                avatar={<Avatar size={'large'} src={item.createByAvatarUrl} />}
                title={item.createBy}
                description={item.createTime}
              />
            </List.Item>
          </Card>
        )}
      />
    </>
  );
};
export default PhotoCard;
