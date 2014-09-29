package com.keep.framework.multicoin.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.keep.framework.multicoin.exception.MulticoinException;
import com.keep.framework.multicoin.message.Transaction;
import com.keep.framework.multicoin.message.ValidateInfo;
import com.keep.framework.multicoin.message.WalletInfo;

/**
 * 钱包接口
 * 
 * @author zhangchaozheng
 */
public interface WalletInterface {

	/**
	 * 根据帐号获得钱包余额
	 * 
	 * @param account
	 * @return	帐户余额
	 */
	BigDecimal getBalance(String account) throws MulticoinException;

	/**
	 * 返回帐户明细余额
	 * 
	 * @return	帐户明细余额
	 */
	Map<String, BigDecimal> listAccounts() throws MulticoinException;
	
	/***
	 * 根据帐户名称,生成新的帐户地址。
	 * @param account
	 * @return	帐户地址
	 */
	String getNewAddress(String account) throws MulticoinException;
	
	/***
	 * 根据帐户地址，获得帐户名称。
	 * @param address	地址
	 * @return	帐户名称
	 */
	String getAccount(String address) throws MulticoinException;
	
	/***
	 * 根据帐户，获得帐户地址。
	 * @param account	帐户
	 * @return	帐户地址
	 */
	String getAccountAddress(String account) throws MulticoinException;
	
	/***
	 * 根据帐户名称，获得帐户地址集合。
	 * @param account 帐号
	 * @return	地址名称集合
	 */
	List<String> getAddressesByAccount(String account) throws MulticoinException;
	
	/**
	 * 返回钱包信息
	 * @return
	 */
	WalletInfo getInfo() throws MulticoinException;
	
	/**
	 * 根据帐户,获得交易事务详情.帐户为空时,返回所有帐户的信息.
	 * @param account	帐户
	 * @param count		数量.默认为10.
	 * @param from		开始记录数.默认为0.
	 * @return
	 */
	List<Transaction> listTransactions(String account, int count, int from) throws MulticoinException;
	
	/***
	 * 根据事务ID获得事务详情.
	 * @param txId
	 * @return	事务详情
	 */
	Transaction getTransaction(String txId) throws MulticoinException;
	
	/***
	 * 验证地址是否合法
	 * @param address
	 */
	ValidateInfo validateAddress(String address) throws MulticoinException;
	
	/**
	 * 转帐
	 * @param address
	 * @param amout
	 */
	String sendToAddress(String address, Double amout) throws MulticoinException;
	
}
