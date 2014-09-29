package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;
@TableAonn(tableName = "bill")
public class Bill {
	public static final int TYPE_RMB_RECHARGE_CONFIRM = BillDetail.TYPE_RMB_RECHARGE_CONFIRM;
	public static final int TYPE_RMB_WITHDRAW_CONFIRM = BillDetail.TYPE_RMB_WITHDRAW_CONFIRM;
	public static final int TYPE_BTC_RENGOU = BillDetail.TYPE_BTC_RENGOU;
	public static final int TYPE_BTC_RECHARGE_CONFIRM = BillDetail.TYPE_BTC_RECHARGE_CONFIRM;
	public static final int TYPE_BTC_WITHDRAW_CONFIRM = BillDetail.TYPE_BTC_WITHDRAW_CONFIRM;
	
	public static final int TYPE_RMB_WITHDRAW_CANCEL = BillDetail.TYPE_RMB_WITHDRAW_CANCEL;
	public static final int TYPE_BTC_WITHDRAW_CANCEL = BillDetail.TYPE_BTC_WITHDRAW_CANCEL;
	
	public static final int TYPE_BTC_BUY_DEAL = 5;
	public static final int TYPE_BTC_SELL_DEAL = 6;
	
	public static final int TYPE_BTC_RECOMMED = BillDetail.TYPE_BTC_RECOMMED;
	public static final int TYPE_ADJUST_RMB_BY_BTCEXTRA = BillDetail.TYPE_ADJUST_RMB_BY_BTCEXTRA;
	
	private int id;
	private int user_id;
	private int trade_id;
	private int type;
	private int ctime;
	
	private double btc_amount;
	private double rmb_amount;
	private double btc_account;
	private double rmb_account;
	
	private int status;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}
	
	
}
