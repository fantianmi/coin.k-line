package com.bkl.Kcoin.vo;

public enum BillType {
	RMB_RECHARGE(1,"人民币充值"),RMB_Withdraw(2, "人民币取现"),BTC_IN(3,"BTC充值"),	
	BTC_OUT(4,"BTC取现"),BTC_SELL(5,"BTC卖出"), BTC_BUY(6,"BTC买入");
	
	private int code;
	private String msg;
	private BillType(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int code() {
		return code;
	}
	
	public String msg() {
		return msg;
	}
}
