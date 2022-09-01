/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50712
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 50712
 File Encoding         : 65001

 Date: 01/09/2022 14:27:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for consumer
-- ----------------------------
DROP TABLE IF EXISTS `consumer`;
CREATE TABLE `consumer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `power` int(11) NOT NULL DEFAULT 0,
  `phone` bigint(20) NOT NULL,
  `state` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 201910098280 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for floor
-- ----------------------------
DROP TABLE IF EXISTS `floor`;
CREATE TABLE `floor`  (
  `floor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fid` bigint(20) NOT NULL,
  `floor_num` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `create_time` date NOT NULL,
  `picture` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `video` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_show` int(11) NOT NULL DEFAULT 0,
  `yl_1` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yl_2` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`floor_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '层' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum`  (
  `fid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lp_id` int(11) NOT NULL,
  `title` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` bigint(20) NOT NULL,
  `publish_time` date NOT NULL,
  `floor_num` int(11) NOT NULL DEFAULT 1,
  `show_forum` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '贴子' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for little_plate
-- ----------------------------
DROP TABLE IF EXISTS `little_plate`;
CREATE TABLE `little_plate`  (
  `lp_id` int(11) NOT NULL AUTO_INCREMENT,
  `lp_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` bigint(20) NOT NULL,
  `show_lp` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`lp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '小板块' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for p_lp
-- ----------------------------
DROP TABLE IF EXISTS `p_lp`;
CREATE TABLE `p_lp`  (
  `plate_id` int(11) NOT NULL,
  `lp_id` int(11) NOT NULL,
  PRIMARY KEY (`plate_id`, `lp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '大版与小板块之间的关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for plate
-- ----------------------------
DROP TABLE IF EXISTS `plate`;
CREATE TABLE `plate`  (
  `plate_id` int(11) NOT NULL AUTO_INCREMENT,
  `plate_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` bigint(20) NOT NULL,
  `show_plate` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`plate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '版块' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
