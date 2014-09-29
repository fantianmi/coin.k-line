/*
Navicat MySQL Data Transfer

Source Server         : mysql3307
Source Server Version : 50703
Source Host           : localhost:3307
Source Database       : coins

Target Server Type    : MYSQL
Target Server Version : 50703
File Encoding         : 65001

Date: 2014-09-29 23:41:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL DEFAULT '0',
  `username` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', '123456');

-- ----------------------------
-- Table structure for `bill`
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '1表示人民币充值，2表示人民币取现，3表示BTC充值，4表示BTC取现，5表示BTC买入，6表示BTC卖出',
  `ctime` int(11) DEFAULT NULL,
  `btc_amount` decimal(65,4) DEFAULT NULL COMMENT 'btc交易的金额',
  `rmb_amount` decimal(65,4) DEFAULT NULL COMMENT '人民币交易的金额',
  `btc_account` decimal(65,4) DEFAULT NULL COMMENT '用户btc余额',
  `rmb_account` decimal(65,4) DEFAULT NULL COMMENT '用户人民币余额',
  `trade_id` int(11) DEFAULT NULL COMMENT '交易id',
  `status` tinyint(4) DEFAULT NULL COMMENT '0表示未确认，1表示成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户帐户清单';

-- ----------------------------
-- Records of bill
-- ----------------------------

-- ----------------------------
-- Table structure for `billdetail`
-- ----------------------------
DROP TABLE IF EXISTS `billdetail`;
CREATE TABLE `billdetail` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '1表示人民币充值，2表示人民币取现，3表示BTC充值，4表示BTC取现，5表示BTC买入委托,6表示BTC卖出委托，7表示BTC买入交易,8表示BTC卖出交易,9表示取消btc买入委托,10表示取消btc卖出委托,11表示保存人民币提现,12表示取消人民币取现,13表示保存btc提现,14表示取消btc取现',
  `typestring` varchar(255) DEFAULT NULL COMMENT 'type的中文显示',
  `user_id` int(20) DEFAULT NULL,
  `ctime` int(11) DEFAULT NULL,
  `rmb_amount` decimal(65,4) DEFAULT NULL COMMENT '可用人民币交易的金额',
  `rmb_frozen_amount` decimal(65,4) DEFAULT NULL COMMENT '冻结人民币交易的金额',
  `btc_amount` decimal(65,4) DEFAULT NULL COMMENT '可用btc交易的金额',
  `btc_frozen_amount` decimal(65,4) DEFAULT NULL COMMENT '冻结btc交易的金额',
  `rmb_account` decimal(65,4) DEFAULT NULL COMMENT '用户人民币可用余额',
  `rmb_frozen_account` decimal(65,4) DEFAULT NULL COMMENT '用户人民币冻结的余额',
  `btc_account` decimal(65,4) DEFAULT NULL COMMENT '用户btc可用余额',
  `btc_frozen_account` decimal(65,4) DEFAULT NULL COMMENT '用户btc冻结的余额',
  `refid` bigint(20) DEFAULT '0' COMMENT '1表示人民币充值(cash表id)，2表示人民币取现(cash表id)，3表示BTC充值(transfer表id)，4表示BTC取现(transfer表id)，5表示BTC买入委托(trade表id),6表示BTC卖出委托(trade表id)，7表示BTC买入交易(deal表id),8表示BTC卖出交易(deal表id),9表示取消btc买入委托(trade表id),10表示取消btc卖出委托(trade表id),11表示保存人民币提现(无),12表示取消人民币取现(cash表id),13表示保存btc提现(无),14表示取消btc取现(transfer表id)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统流水';

-- ----------------------------
-- Records of billdetail
-- ----------------------------

-- ----------------------------
-- Table structure for `cash`
-- ----------------------------
DROP TABLE IF EXISTS `cash`;
CREATE TABLE `cash` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `amount` decimal(65,4) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bank` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '银行',
  `card` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '卡号',
  `admin_id` int(11) NOT NULL COMMENT '卡号',
  `ctime` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0表示未支付，1表示充值成功',
  `type` tinyint(4) DEFAULT NULL COMMENT '1表示人民币充值，2表示人民币取现',
  `fin_type` tinyint(4) DEFAULT '4',
  `bank_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='人民币充值';

-- ----------------------------
-- Records of cash
-- ----------------------------

-- ----------------------------
-- Table structure for `deal`
-- ----------------------------
DROP TABLE IF EXISTS `deal`;
CREATE TABLE `deal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `buy_trade` int(11) NOT NULL,
  `sell_trade` int(11) NOT NULL,
  `ctime` int(11) NOT NULL,
  `amount` decimal(65,4) NOT NULL,
  `price` decimal(65,4) DEFAULT '0.0000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of deal
-- ----------------------------

-- ----------------------------
-- Table structure for `extra_coin_ajust`
-- ----------------------------
DROP TABLE IF EXISTS `extra_coin_ajust`;
CREATE TABLE `extra_coin_ajust` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ctime` int(11) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `btc` decimal(64,4) DEFAULT NULL,
  `btc_extra` decimal(64,4) DEFAULT NULL,
  `btc_amount` decimal(64,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of extra_coin_ajust
-- ----------------------------

-- ----------------------------
-- Table structure for `extra_rmb_ajust`
-- ----------------------------
DROP TABLE IF EXISTS `extra_rmb_ajust`;
CREATE TABLE `extra_rmb_ajust` (
  `id` int(11) NOT NULL,
  `ctime` int(11) DEFAULT NULL,
  `rmb` int(11) NOT NULL,
  `btc_extra` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of extra_rmb_ajust
-- ----------------------------

-- ----------------------------
-- Table structure for `help`
-- ----------------------------
DROP TABLE IF EXISTS `help`;
CREATE TABLE `help` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `body` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `ctime` int(11) DEFAULT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of help
-- ----------------------------

-- ----------------------------
-- Table structure for `plantrade`
-- ----------------------------
DROP TABLE IF EXISTS `plantrade`;
CREATE TABLE `plantrade` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` int(11) NOT NULL,
  `trade_type` int(2) NOT NULL,
  `deal_price` decimal(65,4) NOT NULL,
  `price` decimal(65,4) NOT NULL,
  `quantity` decimal(65,4) NOT NULL,
  `total_price` decimal(65,4) NOT NULL,
  `status` int(2) NOT NULL,
  `effective_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plantrade
-- ----------------------------

-- ----------------------------
-- Table structure for `recommend_detail`
-- ----------------------------
DROP TABLE IF EXISTS `recommend_detail`;
CREATE TABLE `recommend_detail` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `recommended_id` int(11) NOT NULL,
  `btc_amount` decimal(64,4) DEFAULT '0.0000',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0代表未支付;1代表已经支付.',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of recommend_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `subscribe`
-- ----------------------------
DROP TABLE IF EXISTS `subscribe`;
CREATE TABLE `subscribe` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `price` decimal(65,4) DEFAULT NULL,
  `amount` decimal(65,4) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of subscribe
-- ----------------------------

-- ----------------------------
-- Table structure for `systemconfig`
-- ----------------------------
DROP TABLE IF EXISTS `systemconfig`;
CREATE TABLE `systemconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `configKey` varchar(255) NOT NULL,
  `configValue` varchar(1000) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of systemconfig
-- ----------------------------

-- ----------------------------
-- Table structure for `trade`
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '1表示比特币买入，2表示比特币卖出',
  `amount` decimal(65,4) unsigned NOT NULL COMMENT '比特币数量',
  `price` decimal(65,4) NOT NULL COMMENT '比特币价格',
  `deal` decimal(65,4) NOT NULL DEFAULT '0.0000' COMMENT '已经成交的数量,deal=amount表示完全成交',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未成交，1表示全部成交,2表示撤单',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `etime` int(11) NOT NULL COMMENT '结束时间',
  `user_id` int(11) NOT NULL,
  `deal_rmb` decimal(65,4) DEFAULT '0.0000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='比特币买入卖出记录';

-- ----------------------------
-- Records of trade
-- ----------------------------

-- ----------------------------
-- Table structure for `transfer`
-- ----------------------------
DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `txid` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易事务ID',
  `user_id` int(11) NOT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `amount` decimal(65,4) NOT NULL,
  `admin_id` int(11) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  `type` int(10) DEFAULT NULL COMMENT '1是btc充值，2是btc取现',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未确认，1表示已确认，2表示已取消',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of transfer
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `reg_ip` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '注册ip',
  `pin` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nick_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '昵称',
  `ctime` int(11) NOT NULL,
  `rmb` decimal(65,4) NOT NULL DEFAULT '0.0000' COMMENT '可用的rmb金额',
  `rmb_frozen` decimal(65,4) NOT NULL DEFAULT '0.0000' COMMENT '冻结的rmb金额',
  `btc` decimal(65,4) NOT NULL DEFAULT '0.0000' COMMENT '可用的btc数量',
  `btc_frozen` decimal(65,4) NOT NULL DEFAULT '0.0000' COMMENT '冻结的btc数量',
  `email_validated` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示邮箱未验证，1表示邮箱已经验证',
  `realname_validated` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未进行实名验证，1表示已经实名验证，2表示实名认证不通过',
  `mobile_validated` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示手机未验证，1表示手机已经验证',
  `secret` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `received` decimal(65,4) NOT NULL DEFAULT '0.0000',
  `auth_trade` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示不允许交易，1表示允许交易',
  `auth_withdraw` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示不允许取现，1表示允许取现',
  `secret_installed` tinyint(4) NOT NULL DEFAULT '0',
  `ltc_frozen` decimal(65,4) NOT NULL,
  `ltc` decimal(65,4) NOT NULL,
  `bank_account` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bank` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `btc_in_addr` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `btc_out_addr` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ltc_in_addr` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ltc_out_addr` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `identity_type` tinyint(4) DEFAULT '0' COMMENT '证件类型',
  `identity_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '证件号',
  `frozen` tinyint(4) DEFAULT '0' COMMENT '1表示用户被冻结;0表示用户状态正常',
  `passwd_modify_overtime` int(11) DEFAULT NULL COMMENT '忘记密码的修改限期',
  `bank_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `btc_extra` decimal(65,4) DEFAULT '0.0000',
  `btc_extra_frozen` decimal(65,4) DEFAULT '0.0000',
  `mobile` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
