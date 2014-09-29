package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "deal")
public class Deal {
	private long id;
	private int buy_trade;
	private int sell_trade;
	private long ctime;
	private double amount;
	private double price;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getBuy_trade() {
		return buy_trade;
	}
	public void setBuy_trade(int buy_trade) {
		this.buy_trade = buy_trade;
	}
	public int getSell_trade() {
		return sell_trade;
	}
	public void setSell_trade(int sell_trade) {
		this.sell_trade = sell_trade;
	}
	public long getCtime() {
		return ctime;
	}
	public String getCtimeString() {
		return TimeUtil.fromUnixTime(ctime);
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}