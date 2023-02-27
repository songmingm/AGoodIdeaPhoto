import logo from "@/assets/logo.svg";
import { createFromIconfontCN } from "@ant-design/icons";
import { RcFile } from "antd/es/upload";
import { message, Upload } from "antd";

/**
 * 项目基本信息配置
 */
export const projectSettings = {
  logo: logo,
  title: 'A GOOD IDEA',
  description: '一个好注意'
};

/**
 * iconFont的在线图标链接
 */
export const IconFont = createFromIconfontCN({
  scriptUrl: '//at.alicdn.com/t/c/font_3774391_9n6c101r3iu.js'
});

/**
 * 文件上传限制格式 => 图片，尺寸限制 10M
 */
export const beforeUpload = (file: RcFile) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message
      .error(`${file.name} 不是图片，只能上传 JPG/PNG 格式的文件！`)
      .then();
  }
  return isJpgOrPng || Upload.LIST_IGNORE;
};
