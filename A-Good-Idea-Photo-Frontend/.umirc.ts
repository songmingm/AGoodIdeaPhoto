import { defineConfig } from '@umijs/max';
import { routes } from './config/routes';

/**
 * umi全局化配置
 */
export default defineConfig({
  antd: {
    style: 'less'
  },
  history: { type: 'hash' },
  initialState: {},
  request: {},
  model: {},
  layout: {},
  // @ts-ignore
  mock: false,
  routes: [...routes],
  npmClient: 'yarn'
});
