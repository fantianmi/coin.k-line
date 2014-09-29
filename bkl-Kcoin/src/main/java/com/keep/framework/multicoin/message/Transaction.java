package com.keep.framework.multicoin.message;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易事务详情
 * 
 * @author zhangchaozheng
 * 
 */
public class Transaction implements Comparable<Transaction> {

	public final static String CATEGORY_SEND = "send";
	public final static String CATEGORY_RECEIVE = "receive";
	
	private String txid;

	// getTransaction api没值
	private String account;

	// getTransaction api没值
	private String address;

	// getTransaction api没值
	private String category;

	private Double amount;

	private int confirmations;

	private String blockhash;

	private int blockindex;

	private long blocktime;

	private long time;

	private long timereceived;

	private List<Detail> details;

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}

	public String getBlockhash() {
		return blockhash;
	}

	public void setBlockhash(String blockhash) {
		this.blockhash = blockhash;
	}

	public int getBlockindex() {
		return blockindex;
	}

	public void setBlockindex(int blockindex) {
		this.blockindex = blockindex;
	}

	public long getBlocktime() {
		return blocktime;
	}

	public void setBlocktime(long blocktime) {
		this.blocktime = blocktime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTimereceived() {
		return timereceived;
	}

	public void setTimereceived(long timereceived) {
		this.timereceived = timereceived;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public static class Detail {

		private String account;

		private String address;

		private String category;

		private BigDecimal amount;

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

	}

	@Override
	public int compareTo(Transaction t) {
		if (this.time == t.getTime()) {
			return 0;
		}
		if (this.time > t.getTime()) {
			return -1;
		}
		return 1;
	}
}
