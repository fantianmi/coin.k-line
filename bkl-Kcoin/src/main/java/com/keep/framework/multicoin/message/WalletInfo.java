package com.keep.framework.multicoin.message;

import java.math.BigDecimal;

/**
 * 钱包信息
 * @author zhangchaozheng
 *
 */
public class WalletInfo {

	private int version;

	private int protocolversion;

	private int walletversion;

	private BigDecimal balance;

	private long blocks;

	private int timeoffset;

	private int connections;

	private String proxy;

	private double difficulty;

	private boolean testnet;

	private long keypoololdest;

	private long keypoolsize;

	private double paytxfee;

	private String errors;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getProtocolversion() {
		return protocolversion;
	}

	public void setProtocolversion(int protocolversion) {
		this.protocolversion = protocolversion;
	}

	public int getWalletversion() {
		return walletversion;
	}

	public void setWalletversion(int walletversion) {
		this.walletversion = walletversion;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public long getBlocks() {
		return blocks;
	}

	public void setBlocks(long blocks) {
		this.blocks = blocks;
	}

	public int getTimeoffset() {
		return timeoffset;
	}

	public void setTimeoffset(int timeoffset) {
		this.timeoffset = timeoffset;
	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	public boolean isTestnet() {
		return testnet;
	}

	public void setTestnet(boolean testnet) {
		this.testnet = testnet;
	}

	public long getKeypoololdest() {
		return keypoololdest;
	}

	public void setKeypoololdest(long keypoololdest) {
		this.keypoololdest = keypoololdest;
	}

	public long getKeypoolsize() {
		return keypoolsize;
	}

	public void setKeypoolsize(long keypoolsize) {
		this.keypoolsize = keypoolsize;
	}

	public double getPaytxfee() {
		return paytxfee;
	}

	public void setPaytxfee(double paytxfee) {
		this.paytxfee = paytxfee;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

}
