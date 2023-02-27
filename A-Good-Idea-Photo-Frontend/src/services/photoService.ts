import { request } from '@umijs/max';

/**
 * 获取所有公共图库
 */
export async function getAlbumsApi() {
  return request<ResponseJsonData<PhotoType.Album[]>>('/album/public', {
    method: 'GET'
  });
}

/**
 * 获取当前用户私有图库
 */
export async function getPrivateAlbumsApi() {
  return request<ResponseJsonData<PhotoType.Album[]>>('/album/private', {
    method: 'GET'
  });
}

/**
 * 获取图库内的照片信息
 */
export async function getPhotosByAlbumApi(
  param: PhotoType.PhotoCardSearchRequest
) {
  return request<ResponseJsonData<IPage<PhotoType.Photo>>>(
    '/album/photos/card',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      data: param
    }
  );
}

/**
 * 创建图库
 * @param params 参数信息
 * @param coverImage 封面图片
 */
export async function createAlbumApi(
  params: PhotoType.CreateAlbumRequest,
  coverImage: File
) {
  return request<ResponseJsonData<string>>('/album/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: {
      ...params,
      coverImage
    }
  });
}

/**
 * 删除图库
 * @param id 图库id
 */
export async function deleteAlbumApi(id: number) {
  return request<ResponseJsonData<string>>(`/album/del/${id}`, {
    method: 'DELETE'
  });
}

/**
 * 图片上传
 * @param albumId 相册id
 * @param images 上传图片
 */
export async function uploadPhotosApi(albumId: number, images: any) {
  return request<ResponseJsonData<string>>(`/album/photos/upload/${albumId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: images
  });
}

/**
 * 删除相册内的照片
 * @param albumID 相册id
 * @param photoID 照片id
 */
export async function deletePhotoApi(albumID: number, photoID: number) {
  return request<ResponseJsonData<string>>('/album/del/photo', {
    method: 'DELETE',
    params: {
      albumID: albumID,
      photoID: photoID
    }
  });
}

/**
 * 查询图库的详细信息
 * @param albumID 图库id
 */
export async function getAlbumDetailApi(albumID: number) {
  return request<ResponseJsonData<PhotoType.AlbumDetail>>(
    `/album/detail/${albumID}`
  );
}

/**
 * 修改图库信息
 * @param param 修改表单信息信息
 * @param albumId 图库id
 */
export async function updateAlbumApi(
  param: PhotoType.UpdateAlbumRequest,
  albumId: number
) {
  return request('/album/update', {
    method: 'PUT',
    data: {
      ...param,
      id: albumId
    }
  });
}

/**
 * 修改相册封面图片
 * @param image 封面图片
 * @param albumId 相册id
 */
export async function updateAlbumCoverApi(image: any, albumId: number) {
  return request<ResponseJsonData<string>>('/album/setCover', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: {
      cover: image,
      albumId: albumId
    }
  });
}
