import React, { useEffect, useState } from 'react';
import { deleteAlbumApi, getAlbumsApi } from '@/services/photoService';
import { Avatar, Badge, Card, Col, Image, message, Modal, Row } from 'antd';
import { Link } from '@@/exports';
import Empty from '@/pages/error/empty';
import {
  DeleteOutlined,
  ExclamationCircleOutlined,
  PictureOutlined,
  SettingOutlined
} from '@ant-design/icons';
import loadError from '@/assets/img/loading-error.png';

const { Meta } = Card;
/**
 * 图库卡片
 */
const AlbumCard: React.FC = () => {
  const [albums, setAlbums] = useState<PhotoType.Album[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  /**
   * 获取全部公共图库
   */
  const doGetAllPublicAlbum = async () => {
    const hide = message.loading('正在加载中...');
    try {
      const res = await getAlbumsApi();
      setAlbums(res.data);
      setLoading(false);
    } catch (e: any) {
      await message.error('加载失败：' + e.message);
    } finally {
      hide();
    }
  };
  /**
   * 删除图库
   * @param albumId 图库id
   */
  const doDeleteAlbum = async (albumId: number) => {
    const hide = message.loading('正在删除..');
    try {
      const res = await deleteAlbumApi(albumId);
      await message.success(res.msg);
    } catch (e: any) {
      message.error('删除失败：' + e.message);
    } finally {
      hide();
    }
  };
  /**
   * 确认框
   */
  const confirmBtn = async (albumId: number) => {
    Modal.confirm({
      icon: <ExclamationCircleOutlined />,
      title: '删除警告',
      content: '确定要删除这个相册吗？',
      centered: true,
      onOk: () => doDeleteAlbum(albumId)
    });
  };
  useEffect(() => {
    doGetAllPublicAlbum().then();
  }, []);

  return albums.length > 0 ? (
    <Row wrap gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }} justify={'center'}>
      {albums.map((value) => (
        <Col key={value.id}>
          <Badge.Ribbon text={value.albumType}>
            <Card
              key={value.id}
              style={{
                width: '300px',
                marginBottom: '10px'
              }}
              title={value.albumName}
              loading={loading}
              hoverable
              cover={
                <Image width={300} src={value.coverUrl} fallback={loadError} />
              }
              actions={[
                <Link to={'/album/detail/' + value.id} key="setting">
                  <SettingOutlined />
                </Link>,
                <DeleteOutlined
                  key="delete"
                  onClick={async () => {
                    await confirmBtn(value.id);
                  }}
                />,
                <Link to={'/album/photos/' + value.id} key="photos">
                  <PictureOutlined />
                </Link>
              ]}
            >
              <Meta
                avatar={<Avatar size={'large'} src={value.createByAvatarUrl} />}
                title={value.createBy}
                description={value.createTime}
              />
            </Card>
          </Badge.Ribbon>
        </Col>
      ))}
    </Row>
  ) : (
    <Empty />
  );
};

export default AlbumCard;
