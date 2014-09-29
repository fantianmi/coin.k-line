package com.bkl.Kcoin.service;

import java.util.List;

import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Deal;
import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.User;
import com.km.common.vo.RetCode;

public interface UserBillService {
	/**
	 * 查询用户账单列表
	 * @param user_id
	 * @param billType 0表示全部账单 1表示人民币充值，2表示人民币取现，3表示BTC充值，4表示BTC取现，5表示BTC买入，6表示BTC卖出,7表示人民币分红
	 * @return
	 * @throws Exception
	 */
	public List<Bill> findBillList(long user_id, int billType)throws Exception;
	
	public long doRecharge(Cash cash,User user);
	
	public long doWithdraw(Cash cash,User user);
	
//	public long doCancelWithdraw(Cash cash,User user);
	public long doBtcRengou(Subscribe subscribe, User user);
	public long doBtcTransferIn(Transfer transfer, User user);
	
	public long doBtcTransferOut(Transfer transfer, User user);
	

	
	/**
	 * btc交易的买入btc用户的用户流水
	 * @param buyTrade
	 * @param buyUser
	 * @param deal
	 * @return
	 */
	public long dealBuyBtc(Trade buyTrade, User buyUser,Deal deal, double buyDealPrice);
	/**
	 * btc交易的卖出btc用户的用户流水
	 * @param buyTrade
	 * @param buyUser
	 * @param deal
	 * @return
	 */
	public long dealSellBtc(Trade sellTrade, User sellUser,Deal deal, double sellDealPrice);
	
	public Bill findBill(int trade_id);
	
	long doRecommendPaid(User paidUser, double btcAmount);
	
	void adjustUserMoneyByExtraCoin(double totalBtcExtra, double totalRmb);
}
