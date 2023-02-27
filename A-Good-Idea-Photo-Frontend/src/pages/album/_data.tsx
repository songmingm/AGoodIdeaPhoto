/**
 * 图库常用信息
 */

/**
 * 图库类型下拉框
 */
export const AlbumTypeSelect = [
  { label: '公共图库', value: 1 },
  { label: '私有图库', value: 2 },
  { label: '共享图库', value: 3 }
];

/**
 * 创建图库校验信息
 */
export const AlbumVerification = {
  albumName: [
    {
      required: true,
      message: '请输入${label}！'
    }
  ],
  maxUser: [
    {
      required: true,
      message: '请输入${label}！'
    }
  ],
  albumType: [
    {
      required: true,
      message: '请选择${label}！'
    }
  ],
  remarks: [
    {
      required: true,
      message: '请输入${label}！'
    }
  ],
  coverImage: [
    {
      required: true,
      message: '请选择${label}！'
    }
  ]
};
