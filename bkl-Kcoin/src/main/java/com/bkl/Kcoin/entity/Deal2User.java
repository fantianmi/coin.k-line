package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "deal")
public class Deal2User extends Deal {
	private String buy_email;
	private String sell_email;
	
	public String getBuy_email() {
		return buy_email;
	}
	public void setBuy_email(String buy_email) {
		this.buy_email = buy_email;
	}
	public String getSell_email() {
		return sell_email;
	}
	public void setSell_email(String sell_email) {
		this.sell_email = sell_email;
	}


	
}