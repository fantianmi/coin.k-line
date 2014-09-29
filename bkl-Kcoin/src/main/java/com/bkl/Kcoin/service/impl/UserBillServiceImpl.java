package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Deal;
import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.bkl.Kcoin.utils.FrontUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.DbUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Condition;

public class UserBillServiceImpl implements UserBillService {
	GeneralDao<Bill> billDao = DaoFactory.createGeneralDao(Bill.class);
	
	public UserBillServiceImpl() {
		billDao = DaoFactory.createGeneralDao(Bill.class);
	}
	
	public UserBillServiceImpl(Connection conn) {
		billDao = DaoFactory.createGeneralDao(Bill.class,conn);
	}
	
	@Override
	public List<Bill> findBillList(long user_id, int billtype) throws Exception {
		Condition user_con = DbUtil.generalEqualWhere("user_id", user_id);
		List<Bill> bills = new ArrayList<Bill>();
		
		if (billtype == 0) {//获取全部
			bills = billDao.findList(new Condition[]{user_con}, new String[]{"ctime desc", "id desc"});
		} else {//获取未成交
			Condition billtype_con = DbUtil.generalEqualWhere("type", billtype);
			bills = billDao.findList(new Condition[]{user_con,billtype_con}, new String[]{"ctime desc", "id desc"});
		}
	
		return bills;
	}
	
	public Bill findBill(int trade_id) {
		return billDao.find("trade_id", trade_id);
	}
	
	private Bill getBillFromUserInfo(User user) {
		Bill bill = new Bill();
		bill.setUser_id(user.getId());
		bill.setBtc_amount(0);
		bill.setBtc_account(DoubleUtil.add(user.getBtc(),user.getBtc_frozen()));
		
		
		bill.setRmb_account(0);
		bill.setRmb_account(DoubleUtil.add(user.getRmb(),user.getRmb_frozen()));
		
		bill.setCtime((int)TimeUtil.getUnixTime());
		return bill;
	}
	
	public long doRecharge(Cash cash,User user) {
		Bill bill = getBillFromUserInfo(user);
		bill.setType(Bill.TYPE_RMB_RECHARGE_CONFIRM);
		
		bill.setRmb_amount(cash.getAmount());
		return billDao.save(bill);
	}

	public long doWithdraw(Cash cash,User user) {
		Bill bill = getBillFromUserInfo(user);
		bill.setType(Bill.TYPE_RMB_WITHDRAW_CONFIRM);
		bill.setRmb_amount(-cash.getAmount());
		return billDao.save(bill);
	}
	
//	public long doCancelWithdraw(Cash cash,User user) {
//		Bill bill = getBillFromUserInfo(user);
//		bill.setType(Bill.TYPE_RMB_WITHDRAW_CANCEL);
//		bill.setRmb_amount(cash.getAmount());
//		return billDao.save(bill);
//	}
	
	public long doBtcRengou(Subscribe subscribe, User user) {
		Bill bill = getBillFromUserInfo(user);
		bill.setType(Bill.TYPE_BTC_RENGOU);
		
		double rmbAmount = DoubleUtil.multiply(subscribe.getPrice(), subscribe.getAmount());
		
		bill.setBtc_amount(subscribe.getAmount());
		bill.setRmb_amount(-rmbAmount);
		bill.setCtime((int)TimeUtil.getUnixTime());
		return billDao.save(bill);
	}
	
	public long doBtcTransferIn(Transfer transfer, User user) {
		Bill bill = getBillFromUserInfo(user);
		bill.setType(Bill.TYPE_BTC_RECHARGE_CONFIRM);
		bill.setBtc_amount(transfer.getAmount());
		
		bill.setCtime((int)TimeUtil.getUnixTime());
		return billDao.save(bill);
	}
	
	public long doBtcTransferOut(Transfer transfer, User user) {
		Bill bill = getBillFromUserInfo(user);
		bill.setType(Bill.TYPE_BTC_WITHDRAW_CONFIRM);
		bill.setBtc_amount(-transfer.getAmount());
		return billDao.save(bill);
	}
	
	

	@Override
	public long dealBuyBtc(Trade buyTrade, User buyUser, Deal deal, double buyDealPrice) {
		Bill buybill = getBillFromUserInfo(buyUser);
		buybill.setType(Bill.TYPE_BTC_BUY_DEAL);
		double btcAmount = deal.getAmount();
		double rmbAmount = DoubleUtil.multiply(buyDealPrice, btcAmount);
		buybill.setBtc_amount(btcAmount);
		buybill.setRmb_amount(-rmbAmount);
		buybill.setTrade_id(buyTrade.getId());
		
		return billDao.save(buybill);
	}

	@Override
	public long dealSellBtc(Trade sellTrade, User sellUser, Deal deal, double sellDealPrice) {
		Bill sellbill = getBillFromUserInfo(sellUser);
		sellbill.setType(Bill.TYPE_BTC_SELL_DEAL);
		double btcAmount = deal.getAmount();
		double rmbAmount = DoubleUtil.multiply(sellDealPrice, btcAmount);
		sellbill.setBtc_amount(-btcAmount);
		sellbill.setRmb_amount(rmbAmount);
		sellbill.setTrade_id(sellTrade.getId());
		return billDao.save(sellbill);
	}

	@Override
	public long doRecommendPaid(User paidUser, double btcAmount) {
		Bill bill = getBillFromUserInfo(paidUser);
		bill.setType(BillDetail.TYPE_BTC_RECOMMED);
		bill.setBtc_amount(btcAmount);
		return billDao.save(bill);
	}
	
	public void adjustUserMoneyByExtraCoin(double totalBtcExtra, double totalRmb) {
		String sql = "insert into bill (type,ctime,user_id,btc_amount,rmb_amount,btc_account,rmb_account) select %s,UNIX_TIMESTAMP(),id,0,btc_extra*%s/%s,btc,rmb from user";
		sql = String.format(sql, Bill.TYPE_ADJUST_RMB_BY_BTCEXTRA,FrontUtil.formatDouble(totalRmb), FrontUtil.formatDouble(totalBtcExtra));
		
		billDao.exec(sql);
	}
	
}
