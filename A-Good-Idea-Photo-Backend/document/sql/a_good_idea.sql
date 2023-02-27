/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : agoodidea.mysql.rds.aliyuncs.com:3306
 Source Schema         : a_good_idea

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 21/12/2022 12:11:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `album_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图库名称',
  `bucket_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应的minio bucket',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图库封面',
  `user_num` int NULL DEFAULT 1 COMMENT '当前分享人数',
  `max_user` int NULL DEFAULT 1 COMMENT '可加入最大人数',
  `album_type` int NULL DEFAULT NULL COMMENT '图库类型\r\n（公共，私有，共享）',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述信息',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `album-name`(`album_name` ASC) USING BTREE COMMENT '图库名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 10027 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES (10024, '华硕精选壁纸', 'agood-hsjxbz-1669413319581', 'thumb-1669413320563.png', 1, 99, 1, '华硕精选壁纸', 10015, '2022-11-26 05:55:20', 0);
INSERT INTO `album` VALUES (10025, '测试相册', 'agood--1669413654133', 'thumb-1671443181169.jpg', 1, 1, 1, '测试', 10015, '2022-11-26 06:00:54', 0);
INSERT INTO `album` VALUES (10026, '我的秘密', 'agood-wdmm-1669965612291', 'thumb-1669965613074.jpg', 1, 1, 2, '我的秘密', 10015, '2022-12-02 15:20:12', 0);

-- ----------------------------
-- Table structure for photo
-- ----------------------------
DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `object_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `meta` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片的元数据',
  `thumb_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图文件名',
  `album_id` bigint NULL DEFAULT NULL COMMENT '所属相册',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10028 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of photo
-- ----------------------------
INSERT INTO `photo` VALUES (10010, '1669551141630.png', NULL, 'thumb-1669551141630.png', 10025, '2022-11-27 20:12:26', 10015);
INSERT INTO `photo` VALUES (10011, '1669551146235.jpg', NULL, 'thumb-1669551146235.jpg', 10025, '2022-11-27 20:12:27', 10015);
INSERT INTO `photo` VALUES (10012, '1669551202732.jpg', NULL, 'thumb-1669551202732.jpg', 10025, '2022-11-27 20:13:24', 10015);
INSERT INTO `photo` VALUES (10021, '1670485761486.png', NULL, 'thumb-1670485761486.png', 10024, '2022-12-08 15:49:25', 10017);
INSERT INTO `photo` VALUES (10023, '1671172908988.png', NULL, 'thumb-1671172908988.png', 10025, '2022-12-16 14:41:52', 10015);
INSERT INTO `photo` VALUES (10024, '1671172911846.jpg', NULL, 'thumb-1671172911846.jpg', 10025, '2022-12-16 14:41:53', 10015);
INSERT INTO `photo` VALUES (10025, '1671436477966.jpg', NULL, 'thumb-1671436477966.jpg', 10026, '2022-12-19 15:54:39', 10015);
INSERT INTO `photo` VALUES (10026, '1671436479523.png', NULL, 'thumb-1671436479523.png', 10026, '2022-12-19 15:54:42', 10015);
INSERT INTO `photo` VALUES (10027, '1671510025105.jpg', NULL, 'thumb-1671510025105.jpg', 10025, '2022-12-20 12:20:27', 10016);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `realname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `gender` int NULL DEFAULT NULL COMMENT '性别',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `delete_flag` int NULL DEFAULT 0 COMMENT '逻辑删除\r\n0：未删除\r\n1：已删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE COMMENT '用户名唯一约束'
) ENGINE = InnoDB AUTO_INCREMENT = 10018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10015, 'mmsong', '宋明明', '1669484116982.jpg', '$2a$10$VpU6RCTZioCuMyUvrdPWBuMbBDf.fGKe8V0hVjo8SJrL4qS2K7fPG', 1, 'mmsong@yeah.net', 0, '2022-11-21 15:57:15', '2022-12-19 17:51:05');
INSERT INTO `user` VALUES (10016, 'shixiaochun', '石小春', '1669621456924.jpg', '$2a$10$ee5KVBGFsoyXd0tZv60XrO2PjpJcb3.4IaJGogaNnrPR13RMBpBiO', 2, '1299463902@qq.com', 0, '2022-11-23 07:02:57', NULL);
INSERT INTO `user` VALUES (10017, 'cuiwu', '崔牛逼', '1670485742376.jpg', '$2a$10$K9JNQQzSdoFlzA2dShye8e5DonhqpIDObqXcVCjaVcpecaWvubM6q', 2, '2946803165@qq.com', 0, '2022-12-08 15:48:39', NULL);

-- ----------------------------
-- Table structure for user_album
-- ----------------------------
DROP TABLE IF EXISTS `user_album`;
CREATE TABLE `user_album`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `album_id` bigint NOT NULL COMMENT '图库id',
  PRIMARY KEY (`album_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_album
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
