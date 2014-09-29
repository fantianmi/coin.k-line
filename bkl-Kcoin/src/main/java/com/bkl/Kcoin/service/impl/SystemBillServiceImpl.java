package com.bkl.Kcoin.service.impl;

import java.sql.Connection;

import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Deal;
import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.bkl.Kcoin.utils.FrontUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.TimeUtil;

public class SystemBillServiceImpl implements SystemBillService {
	GeneralDao<BillDetail> billDetailDao = null;
	
	public SystemBillServiceImpl() {
		billDetailDao = DaoFactory.createGeneralDao(BillDetail.class);
	}
	
	public SystemBillServiceImpl(Connection conn) {
		billDetailDao = DaoFactory.createGeneralDao(BillDetail.class,conn);
	}

	public long doRecharge(Cash cash,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_RMB_RECHARGE_CONFIRM);
		bill.setTypestring("人民币充值");
		
		bill.setRmb_amount(cash.getAmount());
		bill.setRefid(cash.getId());
		return billDetailDao.save(bill);
	}
	
	private BillDetail getBillDetailFromUserInfo(User user) {
		BillDetail bill = new BillDetail();
		bill.setUser_id(user.getId());
		bill.setBtc_amount(0);
		bill.setBtc_account(user.getBtc());
		bill.setBtc_frozen_account(user.getBtc_frozen());
		
		bill.setRmb_account(0);
		bill.setRmb_account(user.getRmb());
		bill.setRmb_frozen_account(user.getRmb_frozen());
		
		bill.setCtime((int)TimeUtil.getUnixTime());
		return bill;
	}
	
	public long saveWithdraw(Cash cash,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_RMB_WITHDRAW_SAVE);
		bill.setTypestring("人民币提现保存");
		bill.setRmb_amount(-cash.getAmount());
		bill.setRmb_frozen_amount(cash.getAmount());
		return billDetailDao.save(bill);
	}
	
	public long doWithdraw(Cash cash,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_RMB_WITHDRAW_CONFIRM);
		bill.setTypestring("人民币取现确认");
		bill.setRmb_frozen_amount(-cash.getAmount());
		bill.setRefid(cash.getId());
		return billDetailDao.save(bill);
	}
	
	
	public long cancelWithdraw(Cash cash,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_RMB_WITHDRAW_CANCEL);
		bill.setTypestring("人民币取现取消");
		bill.setRmb_amount(cash.getAmount());
		bill.setRmb_frozen_amount(-cash.getAmount());
		bill.setRefid(cash.getId());
		return billDetailDao.save(bill);
	}
	

	public long doBtcRengou(Subscribe subscribe, User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_RENGOU);
		bill.setTypestring("BTC认购");
		double rmbAmount = DoubleUtil.multiply(subscribe.getPrice(), subscribe.getAmount());
		
		bill.setBtc_amount(subscribe.getAmount());
		bill.setRmb_amount(-rmbAmount);
		bill.setRefid(subscribe.getId());
		return billDetailDao.save(bill);
	}
	
	public long doBtcTransferIn(Transfer transfer, User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_RECHARGE_CONFIRM);
		bill.setTypestring("BTC充值");
		bill.setBtc_amount(transfer.getAmount());
		bill.setRefid(transfer.getId());
		return billDetailDao.save(bill);
	}
	
	public long saveBtcTransferOut(Transfer transfer,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_WITHDRAW_SAVE);
		bill.setTypestring("BTC提现保存");
		bill.setBtc_amount(-transfer.getAmount());
		bill.setBtc_frozen_amount(transfer.getAmount());
		return billDetailDao.save(bill);
	}
	
	public long doBtcTransferOut(Transfer transfer, User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_WITHDRAW_CONFIRM);
		bill.setTypestring("BTC取现确认");
		bill.setBtc_frozen_amount(-transfer.getAmount());
		bill.setRefid(transfer.getId());
		return billDetailDao.save(bill);
	}
	
	public long cancelBtcTransferOut(Transfer transfer, User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_WITHDRAW_CANCEL);
		bill.setTypestring("BTC提现取消");
		bill.setBtc_amount(transfer.getAmount());
		bill.setBtc_frozen_amount(-transfer.getAmount());
		bill.setRefid(transfer.getId());
		return billDetailDao.save(bill);
	}
	
	public long buyBtc(Trade trade,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_BUY_ENTRUST);
		bill.setTypestring("BTC买入委托");
		double totalAmount = DoubleUtil.multiply(trade.getAmount(), trade.getPrice());
		bill.setRmb_amount(-totalAmount);
		bill.setRmb_frozen_amount(totalAmount);
		bill.setRefid(trade.getId());
		return billDetailDao.save(bill);
	}
	
	public long sellBtc(Trade trade,User user) {
		BillDetail bill = getBillDetailFromUserInfo(user);
		bill.setType(BillDetail.TYPE_BTC_SELL_ENTRUST);
		bill.setTypestring("BTC卖出委托");
		bill.setBtc_amount(-trade.getAmount());
		bill.setBtc_frozen_amount(trade.getAmount());
		bill.setRefid(trade.getId());
		return billDetailDao.save(bill);
	}
	
	public long dealBuyBtc(Trade buyTrade,User buyUser,Deal deal, double buyDealPrice) {
		BillDetail buybill = getBillDetailFromUserInfo(buyUser);
		buybill.setType(BillDetail.TYPE_BTC_BUY_DEAL);
		buybill.setTypestring("BTC买入交易");
		double btcBuyAmount = deal.getAmount();
		double rmbBuyAmount = DoubleUtil.multiply(buyDealPrice, btcBuyAmount);
		buybill.setBtc_amount(btcBuyAmount);
		buybill.setRmb_frozen_amount(-rmbBuyAmount);
		buybill.setRefid(deal.getId());
		long ret = billDetailDao.save(buybill);
		return ret;
	}
	
	public long dealSellBtc(Trade sellTrade,User sellUser,Deal deal, double sellDealPrice) {
		BillDetail sellbill = getBillDetailFromUserInfo(sellUser);
		sellbill.setType(BillDetail.TYPE_BTC_SELL_DEAL);
		sellbill.setTypestring("BTC卖出交易");
		double btcSellAmount = deal.getAmount();
		double rmbSellAmount = DoubleUtil.multiply(sellDealPrice, btcSellAmount);
		sellbill.setRmb_amount(rmbSellAmount);
		sellbill.setBtc_frozen_amount(-btcSellAmount);
		sellbill.setRefid(deal.getId());
		return billDetailDao.save(sellbill);
	}
	
	public long cancelBuyBtcTrade(Trade trade,User buyUser) {
		double undeal = DoubleUtil.subtract(trade.getAmount(), trade.getDeal());
		double returnRmb = DoubleUtil.subtract(DoubleUtil.multiply(trade.getPrice(), trade.getAmount()), trade.getDeal_rmb());
		BillDetail buybill = getBillDetailFromUserInfo(buyUser);
		buybill.setType(BillDetail.TYPE_BTC_BUY_ENTRUST_CANCEL);
		buybill.setTypestring("取消btc买入委托");
		buybill.setRmb_amount(returnRmb);
		buybill.setRmb_frozen_amount(-returnRmb);
		buybill.setRefid(trade.getId());
		long ret = billDetailDao.save(buybill);
		return ret;
	}
	
	public long cancelSellBtcTrade(Trade trade,User sellUser) {
		double undeal = DoubleUtil.subtract(trade.getAmount(), trade.getDeal());
		BillDetail sellbill = getBillDetailFromUserInfo(sellUser);
		sellbill.setType(BillDetail.TYPE_BTC_SELL_ENTRUST_CANCEL);
		sellbill.setTypestring("取消btc卖出委托");
		sellbill.setBtc_amount(undeal);
		sellbill.setBtc_frozen_amount(-undeal);
		sellbill.setRefid(trade.getId());
		long ret = billDetailDao.save(sellbill);
		return ret;
	}
	
	public long returnRmbOnBtcBuyTradeComplete(Trade trade,User buyUser) {
		BillDetail buybill = getBillDetailFromUserInfo(buyUser);
		buybill.setType(BillDetail.TYPE_RETURN_RMB_ON_BTC_BUY_ENTRUST_COMPLETE);
		buybill.setTypestring("btc买入委托差价返还");
		double returnRmb = DoubleUtil.subtract(DoubleUtil.multiply(trade.getPrice(), trade.getAmount()), trade.getDeal_rmb());
		buybill.setRmb_amount(returnRmb);
		buybill.setRmb_frozen_amount(-returnRmb);
		buybill.setRefid(trade.getId());
		long ret = billDetailDao.save(buybill);
		return ret;
	}

	@Override
	public long doRecommendPaid(User paidUser, double btcAmount) {
		BillDetail buybill = getBillDetailFromUserInfo(paidUser);
		buybill.setTypestring("推荐用户奖励");
		buybill.setBtc_amount(btcAmount);
		return billDetailDao.save(buybill);
	}
	
	public void adjustUserMoneyByExtraCoin(double totalBtcExtra, double totalRmb) {
		String sql = "insert into billdetail (type,typestring,ctime,user_id,rmb_amount,rmb_frozen_amount,btc_amount,btc_frozen_amount,rmb_account,rmb_frozen_account,btc_account,btc_frozen_account) select %s,'人民币分红',UNIX_TIMESTAMP(),id,btc_extra*%s/%s,0,0,0,rmb,rmb_frozen,btc,btc_frozen from user";
		sql = String.format(sql, BillDetail.TYPE_ADJUST_RMB_BY_BTCEXTRA,FrontUtil.formatDouble(totalRmb), FrontUtil.formatDouble(totalBtcExtra));
		
		billDetailDao.exec(sql);
	}

}
