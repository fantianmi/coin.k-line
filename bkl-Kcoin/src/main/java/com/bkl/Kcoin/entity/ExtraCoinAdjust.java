package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;
@TableAonn(tableName = "extra_coin_ajust")
public class ExtraCoinAdjust {
	private int id;
	private int user_id;
	private int ctime;
	
	private double btc;
	private double btc_extra;
	private double btc_amount;
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
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	public double getBtc() {
		return btc;
	}
	public void setBtc(double btc) {
		this.btc = btc;
	}
	public double getBtc_extra() {
		return btc_extra;
	}
	public void setBtc_extra(double btc_extra) {
		this.btc_extra = btc_extra;
	}
	public double getBtc_amount() {
		return btc_amount;
	}
	public void setBtc_amount(double btc_amount) {
		this.btc_amount = btc_amount;
	}
	
}
