package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Deal;
import com.bkl.Kcoin.entity.Deal2User;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.DealService;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.TimeUtil;

public abstract class DealServiceImpl implements DealService {
	boolean nextBuy = true;
	boolean nextSell = true;
	
	boolean hasNextBuy;
	boolean hasNextSell;
	
	Trade currentBuy;
	Trade currentSell;
	
	public static DealService getInstance() {
		String tradeMethod = CoinConfig.getTradeMethod();
		if (tradeMethod == null || tradeMethod.equals("seller")) {
			return new DealServiceForSellerImpl();
		}
		if (tradeMethod.equals("match")) {
			return new DealServiceForMatchImpl();
		}
		
		throw new RuntimeException("invalid trade method:" + tradeMethod);
		
	}
	
	@Override
	public void runDeal() throws Exception {
		Connection conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		GeneralDao<Trade> tradeDao = DaoFactory.createGeneralDao(Trade.class,conn);
		GeneralDao<Deal> dealDao = DaoFactory.createGeneralDao(Deal.class,conn);
		Statement buystmt = null;
		Statement sellstmt = null;
		ResultSet buyrs = null;
		ResultSet sellrs = null;
		try {
			
			buystmt = conn.createStatement();
			sellstmt = conn.createStatement();
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Trade.class), userDao.getTableName(Deal.class), userDao.getTableName(BillDetail.class), userDao.getTableName(Bill.class));
			buyrs = buystmt.executeQuery("select * from trade where type=1 and status=0 order by price desc,ctime");
			sellrs = sellstmt.executeQuery("select * from trade where type=2 and status=0 order by price, ctime");
			
			getCurrentTrade(buyrs, sellrs);
		    while (hasNextBuy && hasNextSell) {
		    	dealWithBuySell(currentBuy, currentSell,conn);
		    	getCurrentTrade(buyrs, sellrs);
		    	
		    }
			userDao.unLockTable();
			userDao.commit();

			return ;
		} catch (Exception e) {
			userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
			if (buystmt != null)buystmt.close();
			if (sellstmt != null)sellstmt.close();
			if (buyrs != null)buyrs.close();
			if (sellrs != null)sellrs.close();
		}
	}
	
	private void getCurrentTrade(ResultSet buyrs, ResultSet sellrs) throws SQLException {
		if (nextBuy) {
    		hasNextBuy = buyrs.next();
    		if (hasNextBuy) {
    			currentBuy = DaoFactory.fromResultSet(buyrs, Trade.class);
    		}
    	}
    	if (nextSell) {
    		hasNextSell = sellrs.next(); 		
    		if (hasNextSell) {
    			currentSell = DaoFactory.fromResultSet(sellrs, Trade.class);
    		}
    	}
		
    	
	}
	
	private void dealWithBuySell(Trade buyTrade, Trade sellTrade,Connection conn) throws Exception {
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		GeneralDao<Trade> tradeDao = DaoFactory.createGeneralDao(Trade.class,conn);
		GeneralDao<Deal> dealDao = DaoFactory.createGeneralDao(Deal.class,conn);
		SystemBillService systemBilServ = new SystemBillServiceImpl(conn);
		UserBillService userBillServ = new UserBillServiceImpl(conn);
    	
    	if (buyTrade.getAmount() <= buyTrade.getDeal()) {//买方已经完全成交
    		nextBuy = true;
    		nextSell = false;
    		return;
    	}
    	if (sellTrade.getAmount() <= sellTrade.getDeal()) {//卖方已经完全成交
    		nextSell = true;
    		return;
    	}
    	if (buyTrade.getPrice() < sellTrade.getPrice()) {
    		//nextBuy = false;
    		//nextSell = true; //卖出价格太高，买方不变，换下一个卖方
    		hasNextSell = false;//结束本次交易处理
    		return;
    	}
    	buyTrade.setEtime(TimeUtil.getUnixTime());
    	sellTrade.setEtime(TimeUtil.getUnixTime());
    	
    	double dealAmount = Math.min(DoubleUtil.subtract(buyTrade.getAmount(), buyTrade.getDeal()) ,  DoubleUtil.subtract(sellTrade.getAmount(), sellTrade.getDeal()));

    	
    	double dealPrice = getDealPrice(buyTrade.getPrice(), sellTrade.getPrice());
    	
    	User buyUser = userDao.find(buyTrade.getUser_id());
    	User sellUser = userDao.find(sellTrade.getUser_id());
    	
    	buyUser.setBtc(DoubleUtil.add(buyUser.getBtc(),dealAmount)); //btc可用数量+成交数量
    	double buyDealRmbAmount = DoubleUtil.multiply(dealPrice, dealAmount);
    	buyUser.setRmb_frozen(DoubleUtil.subtract(buyUser.getRmb_frozen(), buyDealRmbAmount));//人民币冻结金额-人民币成交金额
    	
    	
    	
    	sellUser.setRmb(DoubleUtil.add(sellUser.getRmb(), DoubleUtil.multiply(dealPrice, dealAmount)));//人民币可用金额+人民币成交金额
    	double sellDealBtcAmount = dealAmount;
    	sellUser.setBtc_frozen(DoubleUtil.subtract(sellUser.getBtc_frozen(),sellDealBtcAmount));//btc冻结数量-成交数量
    	
    
    	
    	Deal deal = new Deal();
    	deal.setAmount(dealAmount);
    	deal.setBuy_trade(buyTrade.getId());
    	deal.setSell_trade(sellTrade.getId());
    	deal.setCtime(TimeUtil.getUnixTime());
    	deal.setPrice(dealPrice);
    	
    	String updateBuyUserSql = String.format("update user set rmb_frozen=%s,btc=%s where id=%s", buyUser.getRmb_frozen(), buyUser.getBtc(), buyUser.getId());
    	String updateSellUserSql = String.format("update user set btc_frozen=%s,rmb=%s where id=%s", sellUser.getBtc_frozen(), sellUser.getRmb(), sellUser.getId());
    	
    	long ret = 1;
    	buyTrade.setDeal(DoubleUtil.add(buyTrade.getDeal(), dealAmount));
    	buyTrade.setDeal_rmb(DoubleUtil.add(buyTrade.getDeal_rmb(), DoubleUtil.multiply(dealPrice, dealAmount)));
    	if (buyTrade.getAmount() == buyTrade.getDeal()) {
    		buyTrade.setStatus(Trade.STATUS_SUCCESS);
    	}
    	if (ret > 0) ret = tradeDao.save(buyTrade);
    	if (ret > 0) ret = userDao.exec(updateBuyUserSql);
    	buyUser = userDao.find(buyTrade.getUser_id());//防止同一个用户，再更新一次数据库
    	if (ret > 0) ret = dealDao.save(deal);
    	deal.setId(ret);
    	if (ret > 0) ret = systemBilServ.dealBuyBtc(buyTrade, buyUser, deal, dealPrice);
    	if (ret > 0) ret = userBillServ.dealBuyBtc(buyTrade, buyUser, deal, dealPrice);
    	
    	sellTrade.setDeal(DoubleUtil.add(sellTrade.getDeal(),dealAmount));
    	sellTrade.setDeal_rmb(DoubleUtil.add(sellTrade.getDeal_rmb(), DoubleUtil.multiply(dealPrice, dealAmount)));
    	if (sellTrade.getAmount() == sellTrade.getDeal()) {
    		sellTrade.setStatus(Trade.STATUS_SUCCESS);
    	}
    	if (ret > 0) ret = tradeDao.save(sellTrade);
    	if (ret > 0) ret = userDao.exec(updateSellUserSql);
    	sellUser = userDao.find(sellTrade.getUser_id());//防止同一个用户，再更新一次数据库
    	if (ret > 0) ret = systemBilServ.dealSellBtc(sellTrade, sellUser, deal,dealPrice);
    	if (ret > 0) ret = userBillServ.dealSellBtc(sellTrade, sellUser, deal,dealPrice);
    	
    	//买入委托完全成交，需要返回差价
    	if (buyTrade.getStatus() == Trade.STATUS_SUCCESS) {
    		double returnRmb = DoubleUtil.subtract(DoubleUtil.multiply(buyTrade.getPrice(), buyTrade.getAmount()), buyTrade.getDeal_rmb());
    		buyUser = userDao.find(buyTrade.getUser_id());
    		buyUser.setRmb_frozen(DoubleUtil.subtract(buyUser.getRmb_frozen() , returnRmb));
    		buyUser.setRmb(DoubleUtil.add(buyUser.getRmb(), returnRmb));
    		updateBuyUserSql = String.format("update user set rmb_frozen=%s,rmb=%s where id=%s", buyUser.getRmb_frozen(), buyUser.getRmb(), buyUser.getId());
    		if (ret > 0) ret = userDao.exec(updateBuyUserSql);
    		if (ret > 0) ret = systemBilServ.returnRmbOnBtcBuyTradeComplete(buyTrade, buyUser);
    		
    	}
    	
    	if (ret >0) {
    		//dealDao.exec("update trade set status=1 where amount=deal");
    		userDao.commit();//每次成交，就commit一次。
    	} else {
    		userDao.rollback();
    	}
    	
    	nextBuy = false;
		nextSell = false;
    	return; //买卖价格合适
	}
	
	
	public List<Deal2User> getDealList() {
		GeneralDao<Deal2User> dao = DaoFactory.createGeneralDao(Deal2User.class);
		String sql = "select deal.id,deal.amount,deal.price,deal.ctime,deal.buy_email, u.email as sell_email,deal.buy_trade,deal.sell_trade from (select d.id,amount,price,d.ctime,buy_trade,u.email as buy_email,sell_trade from `deal` d, `user` u where d.buy_trade=u.id) deal,`user` u where deal.sell_trade= u.id order by deal.ctime desc";
		List<Deal2User> deals = dao.findListBySQL(sql);
		return deals;
	}
	
	abstract protected double getDealPrice(double buyPrice, double sellPrice) ;
	
	public static void main(String[] args) throws Exception {
		
	}

}
