package com.bkl.Kcoin.entity;

import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.TableAonn;

/***
 * 认购对象
 * @author chaozheng
 *
 */
@TableAonn(tableName = "subscribe")
public class Subscribe {

	private long id;
	
	private long user_id;
	
	private double price;
	
	private double amount;
	
	private long ctime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = DoubleUtil.formatDouble(price);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = DoubleUtil.formatDouble(amount);
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

}
