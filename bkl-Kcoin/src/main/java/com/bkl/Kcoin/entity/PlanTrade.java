package com.bkl.Kcoin.entity;

import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.TableAonn;

/**
 * 计划委托
 * 
 * @author chaozheng
 * 
 */
@TableAonn(tableName = "plantrade")
public class PlanTrade {

	/**
	 * 正常状态系统会按计划执行。
	 */
	public static final int STATUS_NORMAL = 1;
	/**
	 * <p>
	 * 计划生效
	 * </p>
	 * <p>
	 * 计划生效后，不允许进行删除和禁用操作。
	 * </p>
	 */
	public static final int STATUS_EFFECTIVE = 2;

	/**
	 * 停用状态。系统不会处理该计划。
	 */
	public static final int STATUS_DISABLED = -1;

	/***
	 * 删除状态。支持软删除，操作不可逆。
	 */
	public static final int STATUS_DELETED = -2;

	private int id;

	private int user_id;

	private long create_time;

	/***
	 * <p>
	 * 交易类型.
	 * </p>
	 * <li>BTC买入 = 1</li> <li>BTC卖出 = 2</li> <li>LTC买入 = 3</li> <li>LTC卖出 = 4</li>
	 */
	private int trade_type;

	/***
	 * <p>
	 * 状态.
	 * </p>
	 */
	private int status;

	/***
	 * <p>
	 * 当前成交价
	 * </p>
	 * <p>
	 * 在提交计划时,系统的成交价格.
	 * </p>
	 */
	private double deal_price;

	/***
	 * <p>
	 * 交易价格.
	 * </p>
	 */
	private double price;

	/**
	 * <p>
	 * 购买数量/卖出数量
	 * </p>
	 */
	private double quantity;

	/**
	 * <p>
	 * 总金额
	 * </p>
	 */
	private double total_price;

	private long effective_time;

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

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public void setEffective_time(long effective_time) {
		this.effective_time = effective_time;
	}

	public int getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}

	public double getDeal_price() {
		return deal_price;
	}

	public void setDeal_price(double deal_price) {
		this.deal_price = DoubleUtil.formatDouble(deal_price);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = DoubleUtil.formatDouble(price);
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = DoubleUtil.formatDouble(quantity);
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = DoubleUtil.formatDouble(total_price);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
