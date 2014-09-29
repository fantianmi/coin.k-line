package com.bkl.Kcoin.constants;

/**
 * 业务系统常量定义
 * 
 * @author chaozheng
 * 
 */
public interface Constants {

	/**
	 * 比特币充值/买入
	 */
	int TRADE_TYPE_BTC_IN = 1;

	/**
	 * 比特币卖出/提现
	 */
	int TRADE_TYPE_BTC_OUT = 2;

	/**
	 * 莱特币充值/买入
	 */
	int TRADE_TYPE_LTC_IN = 3;

	/**
	 * 莱特币卖出/提现
	 */
	int TRADE_TYPE_LTC_OUT = 4;

	/**
	 * 人民币充值
	 */
	int RMB_RECHARGE = 1;
	
	/**
	 * 人民币提现
	 */
	int RMB_WITHDRAW = 2;
	
	/**
	 * 是否显示莱特币，0 不显示， 1 显示
	 */
	int SHOW_LTC = 0;
}
