package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Trade2User;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.listener.DealListener;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.TradeQuery;
import com.bkl.Kcoin.service.TradeService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.DbUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class TradeServiceImpl implements TradeService {
	GeneralDao<Trade> tradeDao ;
	GeneralDao<User> userDao ;
	public TradeServiceImpl() {
		tradeDao = DaoFactory.createGeneralDao(Trade.class);
		userDao = DaoFactory.createGeneralDao(User.class);
	}
	
	
	@Override
	public long sellBtc(Trade trade) {
		if (trade.getPrice() <= 0) {
			Config.setRetCode(RetCode.ORDER_PRICE_LESS_OR_EQUAL_0);
			return 0;
		}
		if (trade.getAmount() <= 0) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_OR_EQUAL_0);
			return 0;
		}
		if (trade.getPrice() < CoinConfig.getTradeMinPrice()) {
			Config.setRetCode(RetCode.ORDER_PRICE_LESS_THAN_LIMIT);
			return 0;
		}
		if (trade.getAmount() < CoinConfig.getTradeMinAmount()) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_THAN_LIMIT);
			return 0;
		}
		
		if (DoubleUtil.exceedPrecision(trade.getPrice(), CoinConfig.getTradePriceMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_PRICE_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		if (DoubleUtil.exceedPrecision(trade.getAmount(), CoinConfig.getTradeAmountMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		User user = userDao.find(trade.getUser_id());
		if (trade.getAmount() > user.getBtc()) {
			Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
			return 0;
		}
		return tradeBtc(trade, Trade.TYPE_BTC_SELL);
	}
	
	public long buyBtc(Trade trade) {
		if (trade.getPrice() <= 0) {
			Config.setRetCode(RetCode.ORDER_PRICE_LESS_OR_EQUAL_0);
			return 0;
		}
		if (trade.getAmount() <= 0) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_OR_EQUAL_0);
			return 0;
		}
		if (trade.getPrice() < CoinConfig.getTradeMinPrice()) {
			Config.setRetCode(RetCode.ORDER_PRICE_LESS_THAN_LIMIT);
			return 0;
		}
		if (trade.getAmount() < CoinConfig.getTradeMinAmount()) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_THAN_LIMIT);
			return 0;
		}
		
		if (DoubleUtil.exceedPrecision(trade.getPrice(), CoinConfig.getTradePriceMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_PRICE_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		if (DoubleUtil.exceedPrecision(trade.getAmount(), CoinConfig.getTradeAmountMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		
		double totalRmb = DoubleUtil.multiply(trade.getAmount(), trade.getPrice());
		User user = userDao.find(trade.getUser_id());
		
		if (totalRmb > user.getRmb()) {
			Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
			return 0;
		}
		return tradeBtc(trade, Trade.TYPE_BTC_BUY);
	}

	private static synchronized long tradeBtc(Trade trade, int type)  {
		String userAccountColumn = "";
		String userFrozenAccountColumn = "";
		boolean isSell = true;

		if (type == Trade.TYPE_BTC_BUY) {
			userAccountColumn = "rmb";
			userFrozenAccountColumn = "rmb_frozen";
			isSell = false;
		}
		if (type == Trade.TYPE_BTC_SELL) {
			userAccountColumn = "btc";
			userFrozenAccountColumn = "btc_frozen";
			isSell = true;
		}
		trade.setCtime(TimeUtil.getUnixTime());
		trade.setType(type);
		
		Connection conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		GeneralDao<Trade> tradeDao = DaoFactory.createGeneralDao(Trade.class,conn);
		SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
		
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Trade.class), userDao.getTableName(BillDetail.class));
			
			User user = userDao.find(trade.getUser_id());
			double userAccountAmount = Double.parseDouble(BeanUtils.getProperty(user, userAccountColumn));
			double userFrozenAccountAmount = Double.parseDouble(BeanUtils.getProperty(user, userFrozenAccountColumn));
			double tradeAmount = trade.getAmount();
			double tradePrice = trade.getPrice();
			
			if (isSell) {//BTC卖出
				if (trade.getAmount() > user.getBtc()) {
					Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
					return 0;
				}
				userAccountAmount = DoubleUtil.subtract(userAccountAmount, tradeAmount);
				userFrozenAccountAmount = DoubleUtil.add(userFrozenAccountAmount, tradeAmount);
			} 
			else {//BTC买入
				tradeAmount = DoubleUtil.multiply(tradeAmount, tradePrice);
				if (tradeAmount > user.getRmb()) {
					Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
					return 0;
				}
				
				userAccountAmount = DoubleUtil.subtract(userAccountAmount, tradeAmount);
				userFrozenAccountAmount = DoubleUtil.add(userFrozenAccountAmount, tradeAmount);
			}
			
			
			BeanUtils.setProperty(user, userAccountColumn, userAccountAmount);
			BeanUtils.setProperty(user, userFrozenAccountColumn, userFrozenAccountAmount);
			
			
			long ret = userDao.save(user);

			ret = tradeDao.save(trade);
			
			
			if (type == Trade.TYPE_BTC_BUY) { //btc买入
				systemBillServ.buyBtc(trade,user);
			}
			if (type == Trade.TYPE_BTC_SELL) {//btc卖出
				systemBillServ.sellBtc(trade,user);
			}
			
			userDao.unLockTable();
			userDao.commit();
			DealListener.incTrade();
			TradeQuery.flushQuery();
			return ret;
		} catch (Exception e) {
			userDao.rollback();
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
		finally {
			DaoFactory.closeConnection(conn);
			
		}
	}
	public List<Trade2User> getTradeList() {
		GeneralDao<Trade2User> dao = DaoFactory.createGeneralDao(Trade2User.class);
		String sql = "select t.id,t.type, t.amount, t.price,t.deal,t.status,t.ctime,u.email from `trade` t,`user` u where t.user_id=u.id order by ctime desc";
		List<Trade2User> trades = dao.findListBySQL(sql);
		return trades;
	}
	public List<Trade2User> getBtcSellList() {
		GeneralDao<Trade2User> dao = DaoFactory.createGeneralDao(Trade2User.class);
		String sql = "select t.id,t.type, t.amount, t.price,t.deal,t.status,t.ctime,u.email from `trade` t,`user` u where t.user_id=u.id and type=2 order by ctime desc";
		List<Trade2User> trades = dao.findListBySQL(sql);
		return trades;
	}
	
	public List<Trade2User> getBtcBuyList() {
		GeneralDao<Trade2User> dao = DaoFactory.createGeneralDao(Trade2User.class);
		String sql = "select t.id,t.type, t.amount, t.price,t.deal,t.status,t.ctime,u.email from `trade` t,`user` u where t.user_id=u.id and type=1 order by ctime desc";
		List<Trade2User> trades = dao.findListBySQL(sql);
		return trades;
	}
	
	public PageReply<Trade2User> getTradePage(Map searchMap, Page page) {
		GeneralDao<Trade2User> dao = DaoFactory.createGeneralDao(Trade2User.class);
		String sql = "select t.id,t.type, t.amount, t.price,t.deal,t.status,t.ctime,u.email from `trade` t,`user` u where t.user_id=u.id  order by ctime desc";
		PageReply<Trade2User> trade2UserPage = dao.getPage(sql, page, searchMap);
		return trade2UserPage;
	}
	public PageReply<Trade2User> getTradePage(Page page) {
		GeneralDao<Trade2User> dao = DaoFactory.createGeneralDao(Trade2User.class);
		String sql = "select t.id,t.type, t.amount, t.price,t.deal,t.status,t.ctime,u.email from `trade` t,`user` u where t.user_id=u.id  order by ctime desc";
		PageReply<Trade2User> trade2UserPage = dao.getPage(page, sql);
		return trade2UserPage;
	}
	
	public List<Trade> getBtcTradeList(long user_id, int status) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", user_id);
		List<Trade> trades = new ArrayList<Trade>();
		
		if (status == -1) {//获取全部
			 trades = tradeDao.findList(new Condition[]{user_con}, new String[]{"ctime desc"});
		}
		
		if (status == Trade.STATUS_PROGRESS) {//获取未成交
			Condition status_con = DbUtil.generalEqualWhere("status", 0);
			Condition undeal_con = DbUtil.generalEqualWhere("deal", 0);
			 trades = tradeDao.findList(new Condition[]{user_con,status_con,undeal_con}, new String[]{"ctime desc"});
		}
		if (status == Trade.STATUS_SUCCESS) {//获取完成成交
			Condition status_con = DbUtil.generalEqualWhere("status", 1);
			 trades = tradeDao.findList(new Condition[]{user_con,status_con}, new String[]{"ctime desc"});
		}
		if (status == Trade.STATUS_CANCEL) {//获取已经撤单
			Condition status_con = DbUtil.generalEqualWhere("status", 2);
			 trades = tradeDao.findList(new Condition[]{user_con,status_con}, new String[]{"ctime desc"});
		}
		if (status == 3) {//获取部分成交
			Condition status_con = DbUtil.generalEqualWhere("status", 0);
			Condition deal_con = DbUtil.generalLargerWhere("deal", 0);
			trades = tradeDao.findList(new Condition[]{user_con,status_con,deal_con}, new String[]{"ctime desc"});
		}
		return trades;
	}
	public List<Trade> getBtcSellList(long user_id) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", user_id);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		List<Trade> trades = tradeDao.findList(new Condition[]{user_con,type_con}, new String[]{"ctime desc"});
		return trades;
	}
	
	
	public List<Trade> getBtcBuyList(long user_id) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", user_id);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		List<Trade> trades = tradeDao.findList(new Condition[]{user_con,type_con}, new String[]{"ctime desc"});
		return trades;	
	}
	@Override
	public List<Trade> getUnDealBtcSellList() {
		return null;
	}

	@Override
	public List<Trade> getUnDealBtcBuyList() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Trade> getLastDealBtcSellList() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_SUCCESS);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		List<Trade> trades = tradeDao.findList(new Condition[]{status_con,type_con}, new String[]{"ctime desc"}, 10);
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	public List<Trade> getLastDealBtcBuyList() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_SUCCESS);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		List<Trade> trades = tradeDao.findList(new Condition[]{status_con,type_con}, new String[]{"ctime desc"}, 10);
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	public List<Trade> getLastDealBtcList() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_SUCCESS);
		List<Trade> trades = tradeDao.findList(new Condition[]{status_con}, new String[]{"ctime desc"}, 20);
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	public List<Trade> getLastUnDealBtcSellList() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		//List<Trade> trades = tradeDao.findList(new Condition[]{status_con,type_con}, new String[]{"price","ctime desc"}, 10);
		List<Trade> trades = tradeDao.findListBySQL("select price,amount,deal from (select price,sum(amount) as amount,sum(deal) as deal from trade where status=0 and type=2 group by price) a order by price  limit 10;");
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	
	public List<Trade> getLastUnDealBtcSellListForTradeBtc() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		List<Trade> trades = tradeDao.findList(new Condition[]{status_con,type_con}, new String[]{"price","ctime desc"}, 10);
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	
	public List<Trade> getLastUnDealBtcBuyList() {
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		List<Trade> trades = tradeDao.findListBySQL("select price,amount,deal from (select price,sum(amount) as amount,sum(deal) as deal from trade where status=0 and type=1 group by price) a order by price desc limit 10;");
		if (trades == null) {
			return new ArrayList<Trade>();
		}
		return trades;
	}
	
	public List<Trade> getExpireTradeList() {
		String sql = "select *,from_unixtime(ctime) from `trade` where ctime <(unix_timestamp()-time_to_sec('24:00:00'))";
		return null;
	}
	@Override
	public Trade findTrade(long tradeid) {
		return tradeDao.find(tradeid);
	}
	@Override
	public Trade findTrade(long tradeid, long user_id) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", user_id);
		Condition id_con = DbUtil.generalEqualWhere("id", tradeid);
		return tradeDao.find(new Condition[]{id_con, user_con}, new String[]{});
	}
	@Override
	public RetCode cancelTrade(long tradeid, long user_id) {
		Trade trade = findTrade(tradeid, user_id);
		if (trade == null) {
			return RetCode.ORDER_NOTALLOW_ID_NULL;
		}
		return cancelTrade(tradeid);
	}
	
	public RetCode cancelTrade(long tradeid) {
		Connection conn = DaoFactory.createConnection();
		
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		GeneralDao<Trade> tradeDao = DaoFactory.createGeneralDao(Trade.class,conn);
		SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
		
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Trade.class), userDao.getTableName(BillDetail.class));
			
			Trade trade = tradeDao.find(tradeid);
			if (trade == null) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			
			int status = trade.getStatus() ;
			
			if (status != Trade.STATUS_PROGRESS){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			long order_user_id = trade.getUser_id();
			User user = userDao.find("id", order_user_id);
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double undeal = DoubleUtil.subtract(trade.getAmount(), trade.getDeal());
			if (undeal <= 0) {
				return RetCode.OK;
			}
			
			if (trade.getType() == Trade.TYPE_BTC_SELL) {//BTC卖出 
				double unDealBtcAmount = undeal;
				user.setBtc_frozen(DoubleUtil.subtract(user.getBtc_frozen(), unDealBtcAmount));
				user.setBtc(DoubleUtil.add(user.getBtc(), unDealBtcAmount));
				
			} else if (trade.getType() == Trade.TYPE_BTC_BUY) {//BT买入
				double returnRmb = DoubleUtil.subtract(DoubleUtil.multiply(trade.getPrice(), trade.getAmount()), trade.getDeal_rmb());
				user.setRmb_frozen(DoubleUtil.subtract(user.getRmb_frozen(), returnRmb));
				user.setRmb(DoubleUtil.add(user.getRmb(), returnRmb));
			} else {
				return RetCode.ORDER_TYPE_UNKNOW;
			}
			
			long ret = userDao.save(user);
			trade.setStatus(2);
			ret = tradeDao.save(trade);
			if (ret > 0) {//记录系统流水
				if (trade.getType() == Trade.TYPE_BTC_SELL) {//BTC卖出 
					systemBillServ.cancelSellBtcTrade(trade, user);
					
				} else if (trade.getType() == Trade.TYPE_BTC_BUY) {//BT买入
					systemBillServ.cancelBuyBtcTrade(trade, user);
				}
			}
			
			
			userDao.unLockTable();
			userDao.commit();
			TradeQuery.flushQuery();
			return RetCode.OK;
		} catch (RuntimeException e) {
			userDao.rollback();
			throw e;
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	
	public  double getLastestBtcDealPrice() {
		String sql = "select price from `deal` order by ctime desc limit 1";
		double dealAmount = tradeDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}
	public  double getLastestBtcBuyPrice() {
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		Condition status = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Trade trade = tradeDao.find(new Condition[]{type_con,status}, new String[]{"ctime desc"});
		if (trade == null) {
			return 0;
		}
		return trade.getPrice();
	}
	public  double getLastestBtcSellPrice() {
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		Condition status = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Trade trade = tradeDao.find(new Condition[]{type_con,status}, new String[]{"ctime desc"});
		if (trade == null) {
			return 0;
		}
		return trade.getPrice();
	}
	
/*
	public  double getMaxBtcBuyPrice() {
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Trade trade = tradeDao.find(new Condition[]{type_con,status_con}, new String[]{"price desc"});
		if (trade == null) {
			return 0;
		}
		return trade.getPrice();
	}
	public  double getMinBtcSellPrice() {
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		Condition status_con = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		Trade trade = tradeDao.find(new Condition[]{type_con, status_con}, new String[]{"price"});
		if (trade == null) {
			return 0;
		}
		return trade.getPrice();
	}
	
	*/
	
	public double getLast24HourBtcDealAmount() {
		String sql = "select sum(amount) from `deal` where ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double dealAmount = tradeDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}
	
	public double getLast24HourRmbDealAmount() {
		String sql = "select sum(amount * price) from `deal` where ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double dealAmount = this.tradeDao.queryDouble(sql, new Object[0]);
		return DoubleUtil.formatDouble(dealAmount);
	}
	  
	public double getLast24HourBtcMaxDealPrice() {
		String sql = "select max(price) from `deal` where ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double price = tradeDao.queryDouble(sql);
		return DoubleUtil.formatDouble(price);
	}
	public double getLast24HourBtcMinDealPrice() {
		String sql = "select min(price) from `deal` where ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double price = tradeDao.queryDouble(sql);
		return DoubleUtil.formatDouble(price);
	}
	
	public static void main(String[] args) {

		
		TradeServiceImpl trade = new TradeServiceImpl();
		int id =3;
		trade.cancelTrade(id);
		
	}

	@Override
	public List<Trade> getTo10BtcBuyList(long userId) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", userId);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_BUY);
		Condition status = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		List<Trade> trades = tradeDao.findList(new Condition[]{user_con,type_con,status}, new String[]{"ctime desc"},10);
		return trades;	
	}

	@Override
	public List<Trade> getTo10BtcSellList(long userId) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", userId);
		Condition type_con = DbUtil.generalEqualWhere("type", Trade.TYPE_BTC_SELL);
		Condition status = DbUtil.generalEqualWhere("status", Trade.STATUS_PROGRESS);
		List<Trade> trades = tradeDao.findList(new Condition[]{user_con,type_con,status}, new String[]{"ctime desc"},10);
		return trades;	
	}
}
