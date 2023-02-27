/**
 * 运行时配置
 * 全局初始化数据配置，用于 Layout 用户信息和权限初始化
 * 更多信息见文档：https://next.umijs.org/docs/api/runtime-config#getinitialstate
 */
import { RunTimeLayoutConfig } from '@@/plugin-layout/types';
import { projectSettings } from '@/configs/settings';
import type { RequestConfig } from 'umi';
import { history } from 'umi';
import GlobalFooter from '@/components/GlobalFooter';
import React from 'react';
import GlobalHeaderRight from '@/components/GlobalHeader/GlobalHeaderRight';
import { getLoginUserDataApi } from '@/services/userService';
import { appList } from '../config/applist';
import { parseTokenHandler } from '@/pages/user/_data';

/**
 * 全局初始化数据配置，用于 Layout 用户信息和权限初始化
 * 更多信息见文档：https://next.umijs.org/docs/api/runtime-config#getinitialstate
 */
export async function getInitialState(): Promise<InitialState> {
  const defaultState: InitialState = {
    loginUser: undefined
  };
  // 获取用户的信息
  try {
    const res = await getLoginUserDataApi();
    defaultState.loginUser = res.data;
  } catch (e: any) {}
  return defaultState;
}

/**
 * 全局布局设置
 */
export const layout: RunTimeLayoutConfig = () => {
  return {
    title: projectSettings.title,
    logo: projectSettings.logo,
    menu: {
      locale: false
    },
    contentStyle: { paddingInline: 0, minHeight: '90vh' },
    appList: [...appList],
    fixedHeader: true,
    disableMobile: true,
    layout: 'top',
    footerRender: () => <GlobalFooter />,
    rightContentRender: () => <GlobalHeaderRight />
  };
};

const isDev = process.env.NODE_ENV === 'development';

/**
 * 不需要请求拦截添加token的请求路径
 */
const ignoreUrl: string[] = [
  '/user/register',
  '/user/loginByAccount',
  '/user/loginByEmail',
  '/user/emailcode',
  '/album/public',
  '/album/toview/*'
];

/**
 * 全局请求配置
 * https://umijs.org/docs/max/request
 */
export const request: RequestConfig = {
  baseURL: isDev ? 'http://localhost:5777' : 'http://101.43.217.193:5777',
  timeout: 10000,
  withCredentials: false,
  errorConfig: {
    errorThrower() {},
    errorHandler() {}
  },
  requestInterceptors: [
    (url, options) => {
      // 如果时不需要认证的请求，直接放行，否则需要携带认证信息
      if (ignoreUrl.includes(url)) {
        return { url, options };
      }
      const loginState = parseTokenHandler();
      if (!loginState) {
      }
      // 请求头添加token信息
      options.headers.token = loginState?.token;
      return { url, options };
    }
  ],
  responseInterceptors: [
    (response) => {
      // 不再需要异步处理读取返回体内容，可直接在 data 中读出，部分字段可在 config 中找到
      if (response.status !== 200) {
        throw new Error('网络可能开小差了');
      }
      const data: any = response.data;
      if (data.status === 3004) {
        history.push('/user/login');
      } else if (data.status === 5000) {
        throw new Error(data.msg);
      }
      return response;
    }
  ]
};
