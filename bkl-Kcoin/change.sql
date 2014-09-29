alter table  cash add column(fin_type int)

--加入行号，减少手续费
alter table `user` add column(bank_number varchar(255))
alter table `cash` add column(bank_number varchar(255))


--交易买方的差价返还
alter table  trade add column(deal_rmb DECIMAL(65,4) default 0)
update trade set  deal_rmb=price*deal

DROP TABLE IF EXISTS `subscribe`;
CREATE TABLE `subscribe` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `price` decimal(65,4) DEFAULT NULL,
  `amount` decimal(65,4) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

alter table  deal drop column deal_price
alter table  deal add column(price DECIMAL(65,4) default 0)

alter table `user` add column(mobile varchar(255));
alter table `cash` add column(mobile varchar(255));

alter table `user` add column(btc_extra DECIMAL(65,4) default 0);
alter table `user` add column(btc_extra_frozen DECIMAL(65,4) default 0);

CREATE TABLE `extra_coin_ajust` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ctime` int(11) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `btc` decimal(64,4) DEFAULT NULL,
  `btc_extra` decimal(64,4) DEFAULT NULL,
  `btc_amount` decimal(64,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8

CREATE TABLE `recommend_detail` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `recommended_id` int(11) NOT NULL,
  `btc_amount` decimal(64,4) DEFAULT 0,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0代表未支付;1代表已经支付.',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

alter table bill CHANGE  `id` id int(11) unsigned zerofill NOT NULL AUTO_INCREMENT;
alter table billdetail CHANGE  `id` id int(11) unsigned zerofill NOT NULL AUTO_INCREMENT;


CREATE TABLE `extra_rmb_ajust` (
  `id` int(11) NOT NULL,
  `ctime` int(11) DEFAULT NULL,
  `rmb` int(11) NOT NULL,
  `btc_extra` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


update `user` set password=md5(password);
update `user` set secret=md5(secret) where secret!="" and secret is not null;


alter table user CHANGE  `id` id int(11) unsigned zerofill NOT NULL AUTO_INCREMENT;

delete from user;

insert into user(email,password,secret,rmb,btc,name,mobile) 
select uemail,upassword,utpassword,ab_cny,ab_btc,uname,uphone from `bitcoinbj`.`btc_user` user,`bitcoinbj`.`btc_account_book` account where user.uid = account.uid;
