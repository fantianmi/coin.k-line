package com.bkl.Kcoin.utils;


import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.PlanTrade;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Transfer;
import com.keep.framework.multicoin.core.Wallet;
import com.keep.framework.multicoin.exception.MulticoinException;
import com.keep.framework.multicoin.message.Transaction;

public class FrontUtil {
	public static void main(String[] args) {
		System.out.println(formatDouble(0.0001));
	}
	public static String formatDouble(double value) {
		///java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		//return df.format(value);
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		//nf.setMaximumIntegerDigits(10);
		nf.setMaximumFractionDigits(100);
		nf.setGroupingUsed(false);  
		return nf.format(value);
	}
	
	public static String formatRmbDouble(double value) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		return df.format(value);
	}
	
	public static String formatCoinPriceDouble(double value) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
		return df.format(value);
	}
	
	public static String formatCoinAmountDouble(double value) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0");
		return df.format(value);
	}
	
	
	public static String getTradeTypeString(Trade trade) {
		int type = trade.getType();
		if (type == Trade.TYPE_BTC_BUY) {
			return CoinConfig.getDefaultCoinName() + "买入";
		}
		
		if (type == Trade.TYPE_BTC_SELL) {
			return CoinConfig.getDefaultCoinName() + "卖出";
		}
		
		if (type == Constants.TRADE_TYPE_LTC_IN) {
			return "LTC买入";
		}
		
		if (type == Constants.TRADE_TYPE_LTC_OUT) {
			return "LTC卖出";
		}

		return "未知类型";
	}
	
	public static String getBillTypeString(Bill bill) {
		int type = bill.getType();
		if (type == Bill.TYPE_RMB_RECHARGE_CONFIRM) {
			return "人民币充值";
		}
		if (type == Bill.TYPE_RMB_WITHDRAW_CONFIRM) {
			return "人民币取现";
		}
		if (type == Bill.TYPE_RMB_WITHDRAW_CANCEL) {
			return "人民币取现拒绝";
		}
		if (type == Bill.TYPE_BTC_RECHARGE_CONFIRM) {
			return CoinConfig.getDefaultCoinName() + "充值";
		}
		if (type == Bill.TYPE_BTC_WITHDRAW_CONFIRM) {
			return CoinConfig.getDefaultCoinName() + "取现";
		}
		if (type == Bill.TYPE_BTC_BUY_DEAL) {
			return  CoinConfig.getDefaultCoinName() + "买入";
		}
		if (type == Bill.TYPE_BTC_SELL_DEAL) {
			return CoinConfig.getDefaultCoinName() + "卖出";
		}
		if (type == Bill.TYPE_BTC_RENGOU) {
			return CoinConfig.getDefaultCoinName() + "认购";
		}
		if (type == Bill.TYPE_BTC_RECOMMED) {
			return "推荐用户奖励";
		}
		
		if (type == Bill.TYPE_ADJUST_RMB_BY_BTCEXTRA) {
			return "人民币分红";
		}
		return "未知类型";
	}
	
	public static String getTransferTypeString(Transfer transfer) {
		String typeString = "";
		int type = transfer.getType();
		switch (type) {
		case Constants.TRADE_TYPE_BTC_IN:
			typeString = CoinConfig.getDefaultCoinName() + "充值";
			break;
		case Constants.TRADE_TYPE_BTC_OUT:
			typeString = CoinConfig.getDefaultCoinName() + "取现";
			break;
		case Constants.TRADE_TYPE_LTC_IN:
			typeString = "LTC充值";
			break;
		case Constants.TRADE_TYPE_LTC_OUT:
			typeString = "LTC取现";
			break;
		default:
			typeString = "未知类型";
			break;
		}
		return typeString;
	}
	
	public static String getPlanTradeTypeString(PlanTrade plan) {
		if (Constants.TRADE_TYPE_BTC_IN == plan.getTrade_type()) {
			return CoinConfig.getDefaultCoinName() + "买入";
		}
		if (Constants.TRADE_TYPE_BTC_OUT == plan.getTrade_type()) {
			return CoinConfig.getDefaultCoinName() + "卖出";
		}
		if (Constants.TRADE_TYPE_LTC_IN == plan.getTrade_type()) {
			return "LTC买入";
		}
		if (Constants.TRADE_TYPE_LTC_OUT == plan.getTrade_type()) {
			return "LTC卖出";
		}
		return "未知类型";
	}
	public static String getCashStatusString(Cash cash) {
		Integer status = cash.getStatus();
		Integer type = cash.getType();
		if (status == null || type == null) {
			return "未知类型";
		}
		if (status == Cash.STATUS_UNCONFIRM) {
			return "尚未支付";
		}
		if (type == Cash.TYPE_RMB_RECHARGE) {
			if (status == Cash.STATUS_CONFIRM) {
				return "支付成功";
			} else {
				return "取消支付";
			}
		} else {
			if (status == Cash.STATUS_CONFIRM) {
				return "取现成功";
			} else {
				return "取消取现";
			}
		}
	}
	
	public static String getCashTypeString(Cash cash) {
		Integer status = cash.getStatus();
		Integer type = cash.getType();
		if (status == null || type == null) {
			return "未知类型";
		}
		if (type == Cash.TYPE_RMB_RECHARGE) {
			return "人民币充值";
		}
		if (type == Cash.TYPE_RMB_WITHDRAW) {
			return "人民币取现";
		}
		return "未知类型";
	}
	
	public static String getTransferStatusString(Transfer transfer) {
		int type = transfer.getType();
		int status = transfer.getStatus();
		if (Transfer.STATUS_DELETE == status) {
			return "已拒绝";
		}
		if (Transfer.STATUS_NORMAL == status) {
			return "正在审核";
		}
		if (Transfer.STATUS_CONFIRM == status && (Constants.TRADE_TYPE_BTC_IN == type || Constants.TRADE_TYPE_LTC_IN == type)) {
			return "充值成功";
		}
		if (Transfer.STATUS_CONFIRM == status && (Constants.TRADE_TYPE_BTC_OUT == type || Constants.TRADE_TYPE_LTC_OUT == type)) {
			return "取现成功";
		}
		return "未知状态";
	}
	
	public static String getTransferTxStatusString(Transfer transfer) {
		if (!CoinConfig.isEnableBitcoinWallet()) {
			return "未知";
		}
		String status = "未确认";
		if (Constants.TRADE_TYPE_BTC_IN == transfer.getType() && !StringUtils.isEmpty(transfer.getTxid())) {
			Transaction tx = Wallet.INSTANCE.getTransaction(transfer.getTxid());
			try {
				if (tx != null) {
					if (tx.getConfirmations() >= 6) {
						status = "已确认";
					}
				}
			} catch (MulticoinException e) {
				e.fillInStackTrace();
				status = "未知";
			}
		}
		return status;
	}
	
	public static String getPlanTradeTypeDisplayName(PlanTrade planTrade) {
		if (Constants.TRADE_TYPE_BTC_IN == planTrade.getTrade_type()) {
			return CoinConfig.getDefaultCoinName() + "买入";
		}
		if (Constants.TRADE_TYPE_BTC_OUT == planTrade.getTrade_type()) {
			return  CoinConfig.getDefaultCoinName() + "卖出";
		}
		return "未知类型";
	}
	
	public static String getPlanTradeStatusDisplayName(PlanTrade planTrade) {
		if (PlanTrade.STATUS_NORMAL == planTrade.getStatus()) {
			return "正常";
		}
		if (PlanTrade.STATUS_DISABLED == planTrade.getStatus()) {
			return "已停用";
		}
		if (PlanTrade.STATUS_DELETED == planTrade.getStatus()) {
			return "已删除";
		}
		if (PlanTrade.STATUS_EFFECTIVE == planTrade.getStatus()) {
			return "已生效";
		}
		return "未知状态";
	}
}
