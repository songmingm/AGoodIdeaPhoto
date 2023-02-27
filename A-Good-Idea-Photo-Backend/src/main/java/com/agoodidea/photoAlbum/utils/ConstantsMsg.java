package com.agoodidea.photoAlbum.utils;

public class ConstantsMsg {
    public static class Common {
        public static final String INSERT_SUCCESS = "新增成功";
        public static final String INSERT_ERROR = "新增失败";
        public static final String DELETE_SUCCESS = "删除成功";
        public static final String DELETE_ERROR = "删除失败";
        public static final String UPDATE_SUCCESS = "修改成功";
        public static final String UPDATE_ERROR = "修改失败";
        public static final String NETWORK_ERROR = "操作失败，请检查网络状态和服务状态是否正常";
    }

    public static class Encode {
        public static final String CH_CODE = "UTF-8";
    }

    public static class File {
        public static final String FILE_EXT_ERROR = "文件格式错误";
        public static final String FILE_NOT_NULL = "文件不可以为空";
        public static final String FILE_NAME_NOT_NULL = "文件不存在";
        public static final String FILE_DELETE_SUCCESS = "文件删除成功";
        public static final String UPLOAD_SUCCESS = "文件上传成功";
        public static final String UPLOAD_ERROR = "文件上传失败";
    }

    public static class User {
        public static final String ADMIN_HAS_EXIST = "该用户名已存在，换一个试试吧";
        public static final String NOT_LOGIN = "您可能还未登录";
        public static final String ADMIN_PASSWORD_ERROR = "登录失败，请检查密码是否输入正确";
        public static final String ADMIN_STATUS_ERROR = "该账号异常，请联系其他管理员";
        public static final String ADMIN_NO_EXIST = "查询不到管理员信息，请检查用户名是否正确";
        public static final String LOGIN_SUCCESS = "登录成功";
        public static final String REGISTER_SUCCESS = "注册成功";
    }

}
