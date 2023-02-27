/**
 * 相册类型定义
 */
declare namespace PhotoType {
  /**
   * 图库
   */
  interface Album {
    id: number;
    albumName: string;
    createByAvatarUrl: string;
    coverUrl: string;
    albumType: string;
    createBy: string;
    createTime: string;
  }

  /**
   * 图库详细信息
   */
  interface AlbumDetail {
    // id: number;
    albumName: string;
    maxUser: number;
    userNum: number;
    remark: string;
    createByAvatarUrl: string;
    coverUrl: string;
    albumType: string;
    createBy: string;
    createTime: string;
  }

  /**
   * 图片信息
   */
  interface Photo {
    id: number;
    url: string;
    createBy: string;
    createTime: string;
    createByAvatarUrl: string;
    meta: string;
  }

  /**
   * 创建图库
   */
  interface CreateAlbumRequest {
    albumName: string;
    maxUser: number;
    albumType: number;
    remarks: string;
    coverImage: any[];
  }

  interface PhotoCardSearchRequest {
    current: number;
    size: number;
    albumId: number;
    createBy?: string;
    createTime?: string;
  }

  /**
   * 图库修改
   */
  interface UpdateAlbumRequest {
    albumName: string;
    maxUser: number;
    remarks: string;
  }
}
