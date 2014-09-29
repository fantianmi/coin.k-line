package com.bkl.Kcoin.entity;

import com.km.common.dao.TableAonn;

@TableAonn(tableName = "recommend_detail")
public class RecommendDetail {

	private long id;
	
	// 注册用户ID
	private long user_id;
	
	// 推荐人ID
	private long recommended_id;
	
	private double btc_amount;
	// 是否已经支付奖励
	private int status;

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

	public long getRecommended_id() {
		return recommended_id;
	}

	public void setRecommended_id(long recommended_id) {
		this.recommended_id = recommended_id;
	}

	public double getBtc_amount() {
		return btc_amount;
	}

	public void setBtc_amount(double btc_amount) {
		this.btc_amount = btc_amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
