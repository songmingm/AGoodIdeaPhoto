/**
 * 用户相关路由
 * /user的子路由
 */
export const userRoutes = [
  {
    name: '用户登录',
    path: '/user/login',
    component: 'user/login',
    hideInMenu: true,
    headerRender: false
  },
  {
    name: '注册中心',
    path: '/user/register',
    component: 'user/register',
    headerRender: false,
    hideInMenu: true
  },
  {
    name: '个人中心',
    path: '/user/center',
    component: 'user/center',
    footerRender: false
  }
];

/**
 * 图库路由
 * /album子路由
 */
export const photoRoutes = [
  {
    name: '公共图库',
    path: '/album/public',
    component: 'album/public'
  },
  {
    name: '我的图库',
    path: '/album/private',
    component: 'album/private'
  },
  {
    name: '照片墙',
    path: '/album/photos/:id',
    component: 'album/photos/card',
    hideInMenu: true
  },
  {
    name: '创建图库',
    path: '/album/create',
    component: 'album/create',
    hideInMenu: true
  },
  {
    name: '图库信息',
    path: '/album/detail/:id',
    component: 'album/detail',
    hideInMenu: true
  }
];
