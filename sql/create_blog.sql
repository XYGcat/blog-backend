/*
 Navicat Premium Data Transfer

 Source Server         : 云服务器
 Source Server Type    : MySQL
 Source Server Version : 50743 (5.7.43-log)
 Source Host           : 47.96.141.111:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50743 (5.7.43-log)
 File Encoding         : 65001

 Date: 24/10/2024 16:12:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bg_article
-- ----------------------------
DROP TABLE IF EXISTS `bg_article`;
CREATE TABLE `bg_article`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `article_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章标题 不能为空',
  `author_id` int(11) NULL DEFAULT 1 COMMENT '文章作者 不能为空',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类id 不能为空',
  `article_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章内容',
  `mdImgList` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章内容中的图片链接',
  `article_cover` varchar(1234) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'https://pic.imgdb.cn/item/65114060c458853aef1f9fa4.jpg' COMMENT '文章缩略图',
  `is_top` int(11) NULL DEFAULT 2 COMMENT '是否置顶 1 置顶 2 取消置顶',
  `status` int(11) NULL DEFAULT 1 COMMENT '文章状态  1 公开 2 私密 3 草稿箱',
  `type` int(11) NULL DEFAULT 1 COMMENT '文章类型 1 原创 2 转载 3 翻译',
  `origin_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原文链接 是转载或翻译的情况下提供',
  `view_times` int(11) NULL DEFAULT 0 COMMENT '文章访问次数',
  `article_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息 不能为空',
  `thumbs_up_times` int(11) NULL DEFAULT 0 COMMENT '文章点赞次数',
  `reading_duration` double NULL DEFAULT 0 COMMENT '文章阅读时长',
  `article_order` int(11) NULL DEFAULT NULL COMMENT '排序 1 最大 往后越小 用于置顶文章的排序',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `bg_article_tag`;
CREATE TABLE `bg_article_tag`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `article_id` int(11) NULL DEFAULT NULL COMMENT '文章id',
  `tag_id` int(11) NULL DEFAULT NULL COMMENT '标签id',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_category
-- ----------------------------
DROP TABLE IF EXISTS `bg_category`;
CREATE TABLE `bg_category`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `category_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称 唯一',
  `category_type` tinyint(2) NULL DEFAULT NULL COMMENT '分类类型（1：文章；2：资源导航）',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_comment
-- ----------------------------
DROP TABLE IF EXISTS `bg_comment`;
CREATE TABLE `bg_comment`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '评论父级id',
  `for_id` int(11) NULL DEFAULT NULL COMMENT '评论的对象id 比如说说id、文章id等',
  `type` int(11) NULL DEFAULT NULL COMMENT '评论类型 1 文章 2 说说 3 留言 ...',
  `from_id` int(11) NULL DEFAULT NULL COMMENT '评论人id',
  `from_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论人昵称',
  `from_avatar` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论人头像',
  `to_id` int(11) NULL DEFAULT NULL COMMENT '被回复的人id',
  `to_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被回复人的昵称',
  `to_avatar` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被回复人的头像',
  `content` varchar(555) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评论内容',
  `thumbs_up` int(11) NULL DEFAULT 0 COMMENT '评论点赞数',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_config
-- ----------------------------
DROP TABLE IF EXISTS `bg_config`;
CREATE TABLE `bg_config`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `bg_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '博客名',
  `bg_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '博客头像',
  `avatar_bg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '博客头像背景图',
  `personal_say` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人签名',
  `bg_notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '博客公告',
  `qq_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq链接',
  `we_chat_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信链接',
  `github_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'github链接',
  `gitee_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'gitee链接',
  `bili_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'bilibili链接',
  `view_time` bigint(20) NULL DEFAULT 0 COMMENT '博客被访问的次数',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_header
-- ----------------------------
DROP TABLE IF EXISTS `bg_header`;
CREATE TABLE `bg_header`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `bg_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景图',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `route_name` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_like
-- ----------------------------
DROP TABLE IF EXISTS `bg_like`;
CREATE TABLE `bg_like`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `type` int(11) NULL DEFAULT NULL COMMENT '点赞类型 1 文章 2 说说 3 留言 4 评论',
  `for_id` int(11) NULL DEFAULT NULL COMMENT '点赞的id 文章id 说说id 留言id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '点赞用户id',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_links
-- ----------------------------
DROP TABLE IF EXISTS `bg_links`;
CREATE TABLE `bg_links`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `site_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站名称',
  `site_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站描述',
  `site_avatar` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站头像',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `status` int(11) NULL DEFAULT NULL COMMENT '友链状态 1 待审核 2 审核通过',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_message
-- ----------------------------
DROP TABLE IF EXISTS `bg_message`;
CREATE TABLE `bg_message`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `message` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言内容',
  `color` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#676767' COMMENT '字体颜色',
  `font_size` int(11) NULL DEFAULT 12 COMMENT '字体大小',
  `bg_color` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景颜色',
  `bg_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景图片',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '留言用户的id',
  `like_times` int(11) NULL DEFAULT 0 COMMENT '点赞次数',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `font_weight` int(11) NULL DEFAULT 500 COMMENT '字体宽度',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '游客用户的昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_notify
-- ----------------------------
DROP TABLE IF EXISTS `bg_notify`;
CREATE TABLE `bg_notify`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `message` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知内容',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '通知给谁',
  `type` int(11) NULL DEFAULT NULL COMMENT '通知类型 1 文章 2 说说 3 留言 4 友链',
  `to_id` int(11) NULL DEFAULT NULL COMMENT '说说或者是文章的id 用于跳转',
  `isView` int(11) NULL DEFAULT 1 COMMENT '是否被查看 1 没有 2 已经查看',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_photo
-- ----------------------------
DROP TABLE IF EXISTS `bg_photo`;
CREATE TABLE `bg_photo`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `album_id` int(11) NULL DEFAULT NULL COMMENT '相册 id 属于哪个相册',
  `url` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 1 正常 2 回收站',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_photo_album
-- ----------------------------
DROP TABLE IF EXISTS `bg_photo_album`;
CREATE TABLE `bg_photo_album`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `album_name` varchar(26) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相册名称',
  `description` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相册描述信息',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `album_cover` varchar(555) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相册封面',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_recommend
-- ----------------------------
DROP TABLE IF EXISTS `bg_recommend`;
CREATE TABLE `bg_recommend`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `title` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐网站标题',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_resource
-- ----------------------------
DROP TABLE IF EXISTS `bg_resource`;
CREATE TABLE `bg_resource`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `site_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站名称',
  `site_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站链接',
  `site_img` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站图片',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站描述',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_tag
-- ----------------------------
DROP TABLE IF EXISTS `bg_tag`;
CREATE TABLE `bg_tag`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `tag_name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名称 唯一',
  `tag_type` tinyint(2) NULL DEFAULT NULL COMMENT '标签类型（1：文章；2：资源导航）',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tag_name`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_2`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_3`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_4`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_5`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_6`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_7`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_8`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_9`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_10`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_11`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_12`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_13`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_14`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_15`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_16`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_17`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_18`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_19`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_20`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_21`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_22`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_23`(`tag_name`) USING BTREE,
  UNIQUE INDEX `tag_name_24`(`tag_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_talk
-- ----------------------------
DROP TABLE IF EXISTS `bg_talk`;
CREATE TABLE `bg_talk`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发布说说的用户id',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说说内容',
  `status` int(11) NULL DEFAULT 1 COMMENT '说说状态 1 公开 2 私密 3 回收站',
  `is_top` int(11) NULL DEFAULT 2 COMMENT '是否置顶 1 置顶 2 不置顶',
  `like_times` int(11) NULL DEFAULT 0 COMMENT '点赞次数',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_talk_photo
-- ----------------------------
DROP TABLE IF EXISTS `bg_talk_photo`;
CREATE TABLE `bg_talk_photo`  (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `talk_id` int(11) NULL DEFAULT NULL COMMENT '说说的id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_t` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for bg_user
-- ----------------------------
DROP TABLE IF EXISTS `bg_user`;
CREATE TABLE `bg_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号，唯一',
  `password` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'ip属地',
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户QQ 用于联系',
  `role` int(11) NOT NULL DEFAULT 2 COMMENT '用户角色 1 管理员 2 普通用户',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除（逻辑删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `username_2`(`username`) USING BTREE,
  UNIQUE INDEX `username_3`(`username`) USING BTREE,
  UNIQUE INDEX `username_4`(`username`) USING BTREE,
  UNIQUE INDEX `username_5`(`username`) USING BTREE,
  UNIQUE INDEX `username_6`(`username`) USING BTREE,
  UNIQUE INDEX `username_7`(`username`) USING BTREE,
  UNIQUE INDEX `username_8`(`username`) USING BTREE,
  UNIQUE INDEX `username_9`(`username`) USING BTREE,
  UNIQUE INDEX `username_10`(`username`) USING BTREE,
  UNIQUE INDEX `username_11`(`username`) USING BTREE,
  UNIQUE INDEX `username_12`(`username`) USING BTREE,
  UNIQUE INDEX `username_13`(`username`) USING BTREE,
  UNIQUE INDEX `username_14`(`username`) USING BTREE,
  UNIQUE INDEX `username_15`(`username`) USING BTREE,
  UNIQUE INDEX `username_16`(`username`) USING BTREE,
  UNIQUE INDEX `username_17`(`username`) USING BTREE,
  UNIQUE INDEX `username_18`(`username`) USING BTREE,
  UNIQUE INDEX `username_19`(`username`) USING BTREE,
  UNIQUE INDEX `username_20`(`username`) USING BTREE,
  UNIQUE INDEX `username_21`(`username`) USING BTREE,
  UNIQUE INDEX `username_22`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
