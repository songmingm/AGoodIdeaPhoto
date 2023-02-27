/**
 * 路由
 * 配置参考：https://umijs.org/docs/max/layout-menu#%E6%89%A9%E5%B1%95%E7%9A%84%E8%B7%AF%E7%94%B1%E9%85%8D%E7%BD%AE
 *
 */
import { photoRoutes, userRoutes } from './businessRoutes';

export const routes = [
  {
    name: '主页',
    path: '/',
    hideInMenu: true,
    redirect: '/album/public'
  },
  {
    name: '图库',
    path: '/album',
    flatMenu: true,
    routes: [...photoRoutes]
  },
  {
    name: '用户',
    path: '/user',
    flatMenu: true,
    routes: [...userRoutes]
  },
  {
    path: '/*',
    component: 'error/404',
    name: 'Not Found',
    hideInMenu: true
  }
];
