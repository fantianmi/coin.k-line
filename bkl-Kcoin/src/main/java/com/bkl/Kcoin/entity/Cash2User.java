package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;
import com.km.common.utils.TimeUtil;

@TableAonn(tableName = "cash")
public class Cash2User extends Cash {
	public String email;
	
	public String realName;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
}