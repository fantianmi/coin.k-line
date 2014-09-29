package com.bkl.Kcoin.entity;

import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.TableAonn;
import com.km.common.utils.DbUtil;
import com.km.common.utils.PrintUtil;
import com.km.common.utils.TimeUtil;

/**
 * 该表用于记录人民币的充值/提现.
 * @author chaozheng
 *
 */
@TableAonn(tableName = "cash")
public class Cash {
	public static final int STATUS_UNCONFIRM = 0;//未支付
	public static final int STATUS_CONFIRM = 1;//已确认
	public static final int STATUS_CANCEL = 2;//已撤单
	public static final int TYPE_RMB_RECHARGE = 1;
	public static final int TYPE_RMB_WITHDRAW = 2;
	
	/***
	 * 主键.目前也用作订单号.
	 */
	private Integer id;
	
	/**
	 * 用户ID
	 */
	private Integer user_id;
	
	/**
	 * 执行确认的管理员id
	 */
	private Integer admin_id;
	
	/**
	 * 用户姓名
	 */
	private String name;
	
	/***
	 * 人民币充值/提现金额
	 */
	private Double amount;
	
	/***
	 * 人民币充值/提现卡号
	 */
	private String card ;
	
	/**
	 * 人民币充值/提现银行名称.如:中国银行
	 */
	private String bank ;
	
	/**
	 * 创建时间
	 */
	private Long ctime;
	
	/**
	 * 充值/提现状态
	 * <li>status=0	表示未处理.</li>
	 * <li>status=1	处理完成.</li>
	 */
	private Integer status;
	
	/**
	 * <li>type=1	表示人民币充值.</li>
	 * <li>type=2	表示人民币提现.</li>
	 */
	private Integer type;
	
	/**
	 * <li>type=0	表示智付通自动.</li>
	 * <li>type=3	表示支付宝人工.</li>
	 * <li>type=4	表示银行卡汇款.</li>
	 */
	private Integer fin_type;
	
	/**
	 * 行号：行号类似卡号，是每家营业网点的代码，用于提现转账减少手续费
	 */
	private String bank_number;
	
	private String mobile;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = DoubleUtil.formatDouble(amount);
	}

	public Long getCtime() {
		return ctime;
	}

	public String getCtimeString() {
		if (ctime != null) {
			return TimeUtil.fromUnixTime(ctime);
		} else {
			return "";
		}
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public Integer getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}

	public Integer getFin_type() {
		return fin_type;
	}

	public void setFin_type(Integer fin_type) {
		this.fin_type = fin_type;
	}

	public String getFin_typeString() {
		if (fin_type == null) {
			return "未知类型";
		}
		if (fin_type == 0) {
			return "智付通自动";
		}
		if (fin_type == 3) {
			return "支付宝人工";
		}
		if (fin_type == 4) {
			return "银行卡汇款";
		}
		return "未知类型";
	}
	
	public String getBank_number() {
		return bank_number;
	}

	public void setBank_number(String bank_number) {
		this.bank_number = bank_number;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static void main(String[] args)throws Exception {
		DbUtil dbUtil = new DbUtil();
		Cash cash = new Cash();
		cash.setId(1);
		cash.setName("郭炬城");
		String sql = "select r.id,r.user_id,r.name,r.bank,r.card,r.amount,r.ctime,r.status,r.type,u.email from `cash` r,`user` u where  r.user_id=u.id and r.type=? order by ctime desc";
		dbUtil.queryListObject(sql, Cash.class, 1);
		//dbUtil.saveObject("cash", cash);
		PrintUtil.print(dbUtil.queryListObject(sql, Cash.class, 1));
	}
}