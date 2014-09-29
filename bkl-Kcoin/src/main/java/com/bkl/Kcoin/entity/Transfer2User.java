package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;

@TableAonn(tableName = "transfer")
public class Transfer2User extends Transfer {
	public String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}