package com.bkl.Kcoin.entity;

import com.bkl.Kcoin.constants.Constants;
import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "transfer")
public class Transfer {
	public static final int STATUS_DELETE = 2;
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_CONFIRM = 1;
	
	public static final int TYPE_BTC_RECHARGE = 1;
	public static final int TYPE_BTC_WITHDRAW = 2;
	
	private int id;
	private String txid;
	private int user_id;
	
	private String address = "";
	private double amount;
	
	private int admin_id;
	private long ctime;
	private int status = 0;
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public int getStatus() {
		return status;
	}

	

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}
}