package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;
@TableAonn(tableName = "billdetail")
public class BillDetail {
	public static final int TYPE_RMB_RECHARGE_CONFIRM = 1;
	public static final int TYPE_RMB_WITHDRAW_CONFIRM = 2;
	public static final int TYPE_BTC_RECHARGE_CONFIRM = 3;
	public static final int TYPE_BTC_WITHDRAW_CONFIRM = 4;
	
	public static final int TYPE_BTC_BUY_ENTRUST = 5;
	public static final int TYPE_BTC_SELL_ENTRUST = 6;
	public static final int TYPE_BTC_BUY_DEAL = 7;
	public static final int TYPE_BTC_SELL_DEAL = 8;
	
	public static final int TYPE_BTC_BUY_ENTRUST_CANCEL = 9;
	public static final int TYPE_BTC_SELL_ENTRUST_CANCEL = 10;
	
	public static final int TYPE_RMB_WITHDRAW_SAVE = 11;
	public static final int TYPE_RMB_WITHDRAW_CANCEL = 12;
	public static final int TYPE_BTC_WITHDRAW_SAVE = 13;
	public static final int TYPE_BTC_WITHDRAW_CANCEL = 14;
	
	public static final int TYPE_RETURN_RMB_ON_BTC_BUY_ENTRUST_COMPLETE = 15;
	public static final int TYPE_BTC_RENGOU = 16;
	
	public static final int TYPE_BTC_RECOMMED = 17;
	public static final int  TYPE_ADJUST_RMB_BY_BTCEXTRA = 18;
	
	private int id;
	private int user_id;
	private int trade_id;
	private int type;
	private String typestring;


	private int ctime;
	
	private double rmb_amount;
	private double rmb_frozen_amount;
	private double btc_amount;
	private double btc_frozen_amount;
	
	
	
	private double btc_account;
	private double btc_frozen_account;
	private double rmb_account;
	private double rmb_frozen_account;
	
	
	
	private long refid;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getCtime() {
		return ctime;
	}
	
	public String getCtimeString() {
		return TimeUtil.fromUnixTime(ctime);
	}
	
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}

	public double getBtc_amount() {
		return btc_amount;
	}

	public void setBtc_amount(double btc_amount) {
		this.btc_amount = btc_amount;
	}

	public double getRmb_amount() {
		return rmb_amount;
	}

	public void setRmb_amount(double rmb_amount) {
		this.rmb_amount = rmb_amount;
	}

	public double getBtc_account() {
		return btc_account;
	}

	public void setBtc_account(double btc_account) {
		this.btc_account = btc_account;
	}

	public double getRmb_account() {
		return rmb_account;
	}

	public void setRmb_account(double rmb_account) {
		this.rmb_account = rmb_account;
	}

	public int getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}

	public double getRmb_frozen_amount() {
		return rmb_frozen_amount;
	}

	public void setRmb_frozen_amount(double rmb_frozen_amount) {
		this.rmb_frozen_amount = rmb_frozen_amount;
	}

	public double getBtc_frozen_amount() {
		return btc_frozen_amount;
	}

	public void setBtc_frozen_amount(double btc_frozen_amount) {
		this.btc_frozen_amount = btc_frozen_amount;
	}

	public double getBtc_frozen_account() {
		return btc_frozen_account;
	}

	public void setBtc_frozen_account(double btc_frozen_account) {
		this.btc_frozen_account = btc_frozen_account;
	}

	public double getRmb_frozen_account() {
		return rmb_frozen_account;
	}

	public void setRmb_frozen_account(double rmb_frozen_account) {
		this.rmb_frozen_account = rmb_frozen_account;
	}
	
	public String getTypestring() {
		return typestring;
	}

	public void setTypestring(String typestring) {
		this.typestring = typestring;
	}

	public long getRefid() {
		return refid;
	}

	public void setRefid(long refid) {
		this.refid = refid;
	}
	
}
