/**
 * 在入口文件最前面被自动引入，可以考虑在此加入 polyfill。
 * Umi 区别于其他前端框架，没有显式的程序主入口，如 src/index.js
 * 所以在引用某些模块的时候，如果模块功能要求在程序主入口添加代码时，你就可以写到这个文件。
 */

/**
 * 通用返回信息类型
 */
interface ResponseJsonData<T> {
  status: number;
  msg: string;
  data: T;
}

/**
 * 分页信息
 */
interface IPage<T> {
  total: number;
  current: number;
  size: number;
  pages: number;
  records: T[];
}

/**
 * 全局初始化状态
 */
interface InitialState {
  loginUser?: UserType.LoginUserData;
}
