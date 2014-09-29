package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;

/***
 * 认购对象（带用户信息）
 * @author chaozheng
 *
 */
@TableAonn(tableName = "subscribe")
public class Subscribe2User extends Subscribe {
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
