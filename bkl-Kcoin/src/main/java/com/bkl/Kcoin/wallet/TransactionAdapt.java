package com.bkl.Kcoin.wallet;

import java.math.BigInteger;

/**
 * <p>交易适配</p>
 * @author chaozheng
 *
 */
public final class TransactionAdapt {

	private String fromAddress;
	private String toAddress;
	private BigInteger exchangeBalance;
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public BigInteger getExchangeBalance() {
		return exchangeBalance;
	}
	public void setExchangeBalance(BigInteger exchangeBalance) {
		this.exchangeBalance = exchangeBalance;
	}
	
	
}
