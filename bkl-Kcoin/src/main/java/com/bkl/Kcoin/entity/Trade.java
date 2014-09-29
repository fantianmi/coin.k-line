package com.bkl.Kcoin.entity;

import java.text.DecimalFormat;

import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "trade")
public class Trade {
	public static int TYPE_BTC_BUY = 1;
	public static int TYPE_BTC_SELL = 2;
	
	public static int STATUS_PROGRESS = 0;
	public static int STATUS_SUCCESS = 1;
	public static int STATUS_CANCEL = 2;
	
	private int id;
	private int type; // 1表示比特币买入，2表示比特币卖出
	private double amount;
	private double price;
	private double deal;
	private double deal_rmb;//已经成交的人民币金额
	private int status= 0; //0表示未成交，1表示全部成交,2表示撤单
	private long ctime;//创建时间
	private long etime;//结束时间
	private int user_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	
	
	
	public void setType(int type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = DoubleUtil.formatDouble(amount);
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = DoubleUtil.formatDouble(price);
	}
	public double getDeal() {
		return deal;
	}
	
	public double getUndeal() {
		return DoubleUtil.subtract(amount, deal);
	}
	
	public void setDeal(double deal) {
		this.deal = DoubleUtil.formatDouble(deal);
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getStatusString() {
		if (status == STATUS_PROGRESS) {
			return "正在交易";
		}
		if (status == STATUS_SUCCESS) {
			return "交易成功";
		}
		if (status == STATUS_CANCEL) {
			return "部分成交";
		}
		return "未知状态";
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCtime() {
		return ctime;
	}
	
	public String getCtimeString() {
		return TimeUtil.fromUnixTime(ctime);
	}
	
	public String getCtimeOfTimeString() {
		return TimeUtil.fromUnixTime2Time(ctime);
	}
	
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	
	public String getEtimeString() {
		return TimeUtil.fromUnixTime(etime);
	}
	
	public String getEtimeOfTimeString() {
		return TimeUtil.fromUnixTime2Time(etime);
	}
	
	public long getEtime() {
		return etime;
	}
	public void setEtime(long etime) {
		this.etime = etime;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public double getDeal_rmb() {
		return deal_rmb;
	}
	public void setDeal_rmb(double deal_rmb) {
		this.deal_rmb = DoubleUtil.formatDouble(deal_rmb);;
	}
	public static void main(String[] args) {
		double a = DoubleUtil.subtract(40000000, 0);
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00"); 
		System.out.println(df.format(a)); 
		
		System.out.println(a);
		
	}

}