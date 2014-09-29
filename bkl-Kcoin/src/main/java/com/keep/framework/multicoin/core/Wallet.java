package com.keep.framework.multicoin.core;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.keep.framework.multicoin.exception.MulticoinException;
import com.keep.framework.multicoin.message.Transaction;
import com.keep.framework.multicoin.message.ValidateInfo;
import com.keep.framework.multicoin.message.WalletInfo;
import com.keep.framework.multicoin.util.BeanUtil;
import com.keep.framework.multicoin.util.WalletRpcUtil;

public enum Wallet implements WalletInterface {

	INSTANCE;

	public BigDecimal getBalance(String account) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_BALANCE.toString());
		if (!StringUtils.isEmpty(account)) {
			message.put("params", new String[] { account });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return BigDecimal.valueOf(Double.valueOf(StringUtils.defaultIfEmpty(map.get("result").toString(), "0")));
		}
		return BigDecimal.ZERO;
	}

	public Map<String, BigDecimal> listAccounts() throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.LIST_ACCOUNTS.toString());
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return (Map) map.get("result");
		}
		return Collections.EMPTY_MAP;
	}

	public String getNewAddress(String account) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_NEW_ADDRESS.toString());
		if (!StringUtils.isEmpty(account)) {
			message.put("params", new String[] { account });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return map.get("result").toString();
		}
		return "";
	}

	@Override
	public String getAccount(String address) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_ACCOUNT.toString());
		if (!StringUtils.isEmpty(address)) {
			message.put("params", new String[] { address });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return map.get("result").toString();
		}
		return "";
	}
	
	@Override
	public String getAccountAddress(String account) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_ACCOUNT_ADDRESS.toString());
		message.put("params", new String[] { account });
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return map.get("result").toString();
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAddressesByAccount(String account) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_ADDRESS_BY_ACCOUNT.toString());
		message.put("params", new String[] { account });
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return (List<String>) map.get("result");
		}
		return Collections.EMPTY_LIST;
	}

	public WalletInfo getInfo() throws MulticoinException {
		return null;
	}

	public List<Transaction> listTransactions(String account, int count, int from) throws MulticoinException {

		List<Transaction> txs = new ArrayList<Transaction>();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.LIST_TRANSACTIONS.toString());
		message.put("params", new Object[] { account, count, from });
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			@SuppressWarnings("unchecked")
			List<Map<?, ?>> result = (List<Map<?, ?>>) map.get("result");
			if (result != null) {
				for (Map<?, ?> r : result) {
					Transaction tx = new Transaction();
					BeanUtil.toBean(tx, r);
					txs.add(tx);
				}
			}
		}
		Collections.sort(txs);
		return txs;
	}

	public Transaction getTransaction(String txId) throws MulticoinException {
		Transaction tx = new Transaction();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.GET_TRANSACTION.toString());
		if (!StringUtils.isEmpty(txId)) {
			message.put("params", new String[] { txId });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			Map<?, ?> result = (Map<?, ?>) map.get("result");
			if (result != null) {
				try {
					BeanUtils.populate(tx, result);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				List<Transaction.Detail> detailList = new ArrayList<Transaction.Detail>();
				@SuppressWarnings("unchecked")
				List<Map<?, ?>> details = (List<Map<?, ?>>) result.get("details");
				if (details != null) {
					for (Map<?, ?> d : details) {
						Transaction.Detail detail = new Transaction.Detail();
						BeanUtil.toBean(detail, d);
						detailList.add(detail);
					}
				}
				tx.setDetails(detailList);
			}
		}
		return tx;
	}

	@Override
	public ValidateInfo validateAddress(String address) throws MulticoinException {
		Validate.notEmpty(address, "地址不能为空。");

		ValidateInfo info = new ValidateInfo();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.VALIDATE_ADDRESS.toString());
		if (!StringUtils.isEmpty(address)) {
			message.put("params", new String[] { address });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			Map<?, ?> result = (Map<?, ?>) map.get("result");
			if (result != null) {
				BeanUtil.toBean(info, result);
			}
		}
		return info;
	}

	@Override
	public String sendToAddress(String address, Double amout) throws MulticoinException {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("id", UUID.randomUUID().toString());
		message.put("method", API.SEND_TO_ADDRESS.toString());
		if (!StringUtils.isEmpty(address)) {
			message.put("params", new Object[] { address, amout });
		}
		Map<?, ?> map = WalletRpcUtil.send(message);
		if (map != null) {
			return map.get("result").toString();
		}
		return "";
	}

	public static void main(String arg[]) {
		Wallet.INSTANCE
				.sendToAddress("n1U9oFGisSVBab8WDdvpSbqXMg5E3h3Avq", 1.1D);
	}

}
