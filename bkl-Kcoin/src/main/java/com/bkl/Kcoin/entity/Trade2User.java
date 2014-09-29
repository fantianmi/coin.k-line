package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "trade")
public class Trade2User extends Trade {
	public String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
}