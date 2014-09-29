package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;
@TableAonn(tableName = "extra_rmb_ajust")
public class ExtraRmbAdjust {
	private int id;
	private int ctime;
	
	private double rmb;
	private double btc_extra;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	public double getRmb() {
		return rmb;
	}
	public void setRmb(double rmb) {
		this.rmb = rmb;
	}
	public double getBtc_extra() {
		return btc_extra;
	}
	public void setBtc_extra(double btc_extra) {
		this.btc_extra = btc_extra;
	}
	
	public String getCtimeString() {
		return TimeUtil.fromUnixTime(ctime);
	}
	
	
}
