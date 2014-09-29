package com.bkl.Kcoin.entity;

import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.TableAonn;

@TableAonn(tableName = "user")
public class TestUser {
	private int id;
	/***
	 * 邮箱.作为帐号登陆
	 */
	private String email = "";
	private String password = "";
	private String pin = "";
	/**
	 * 用户姓名
	 */
	private String name = "";
	
	/**
	 * 用户呢称
	 */
	private String nick_name = "";
	
	/**
	 * 证件类型
	 */
	private int identity_type;
	
	/**
	 * 证件号
	 */
	private String identity_no;
	
	/**
	 * 创建时间
	 */
	private int ctime;
	
	/**
	 * 人民币金额
	 */
	private double rmb;
	
	/**
	 * 人民币冻结部分金额
	 */
	private double rmb_frozen;
	
	/***
	 * 比特币金额
	 */
	private double btc;
	
	/***
	 * 比特币冻结部分金额
	 */
	private double btc_frozen;
	
	/***
	 * 莱特币金额
	 */
	private double ltc;
	
	/***
	 * 莱特币冻结部分金额
	 */
	private double ltc_frozen;
	
	
	private int email_validated ;
	private int mobile_validated ;
	private String secret = "";
	private String address = "";
	private float received;
	private int auth_trade;
	private int auth_withdraw ;
	private int secret_installed ;
	
	/***
	 * 银行名称
	 */
	private String bank;
	
	/***
	 * 银行卡号
	 */
	private String bank_account;
	
	/***
	 * 比特币充值地址,由系统动态分配
	 */
	private String btc_in_addr;
	
	/***
	 * 比特币提现地址,由用户指定
	 */
	private String btc_out_addr;
	
	/***
	 * 莱特币充值地址,由系统动态分配
	 */
	private String ltc_in_addr;
	
	/***
	 * 莱特币提现地址,由用户指定
	 */
	private String ltc_out_addr;
	
	/***
	 * 登陆密码检验
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String password) {
		if (StringUtils.isBlank(password)) {
			return false;
		}
		return password.equals(this.password);
	}
	
	/***
	 * 交易密码检验
	 * @param password
	 * @return
	 */
	public boolean checkSecretPassword(String password) {
		if (StringUtils.isBlank(password)) {
			return false;
		}
		return password.equals(this.secret);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
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
		this.rmb = DoubleUtil.formatDouble(rmb);
	}
	public double getRmb_frozen() {
		return rmb_frozen;
	}
	public void setRmb_frozen(double rmb_frozen) {
		this.rmb_frozen = DoubleUtil.formatDouble(rmb_frozen);
	}
	public double getBtc() {
		return btc;
	}
	public void setBtc(double btc) {
		this.btc = DoubleUtil.formatDouble(btc);
	}
	public double getBtc_frozen() {
		return btc_frozen;
	}
	public void setBtc_frozen(double btc_frozen) {
		this.btc_frozen = DoubleUtil.formatDouble(btc_frozen);
	}
	
	public double getLtc() {
		return ltc;
	}

	public void setLtc(double ltc) {
		this.ltc = ltc;
	}

	public double getLtc_frozen() {
		return ltc_frozen;
	}

	public void setLtc_frozen(double ltc_frozen) {
		this.ltc_frozen = ltc_frozen;
	}

	public int getEmail_validated() {
		return email_validated;
	}
	public void setEmail_validated(int email_validated) {
		this.email_validated = email_validated;
	}
	public int getMobile_validated() {
		return mobile_validated;
	}

	public void setMobile_validated(int mobile_validated) {
		this.mobile_validated = mobile_validated;
	}

	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getReceived() {
		return received;
	}
	public void setReceived(float received) {
		this.received = received;
	}
	public int getAuth_trade() {
		return auth_trade;
	}
	public void setAuth_trade(int auth_trade) {
		this.auth_trade = auth_trade;
	}
	public int getAuth_withdraw() {
		return auth_withdraw;
	}
	public void setAuth_withdraw(int auth_withdraw) {
		this.auth_withdraw = auth_withdraw;
	}
	public int getSecret_installed() {
		return secret_installed;
	}
	public void setSecret_installed(int secret_installed) {
		this.secret_installed = secret_installed;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public String getBtc_in_addr() {
		return btc_in_addr;
	}

	public void setBtc_in_addr(String btc_in_addr) {
		this.btc_in_addr = btc_in_addr;
	}

	public String getBtc_out_addr() {
		return btc_out_addr;
	}

	public void setBtc_out_addr(String btc_out_addr) {
		this.btc_out_addr = btc_out_addr;
	}

	public String getLtc_in_addr() {
		return ltc_in_addr;
	}

	public void setLtc_in_addr(String ltc_in_addr) {
		this.ltc_in_addr = ltc_in_addr;
	}

	public String getLtc_out_addr() {
		return ltc_out_addr;
	}

	public void setLtc_out_addr(String ltc_out_addr) {
		this.ltc_out_addr = ltc_out_addr;
	}

	public int getIdentity_type() {
		return identity_type;
	}

	public void setIdentity_type(int identity_type) {
		this.identity_type = identity_type;
	}

	public String getIdentity_no() {
		return identity_no;
	}

	public void setIdentity_no(String identity_no) {
		this.identity_no = identity_no;
	}

}