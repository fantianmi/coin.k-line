package com.bkl.Kcoin.service;

import java.util.List;
import java.util.Timer;

import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.service.impl.ExtraServiceImpl;
import com.bkl.Kcoin.service.impl.TradeServiceImpl;
import com.bkl.Kcoin.vo.LastDealInfo;

public class TradeQuery {
	
	public static void main(String[] args) {
		System.out.println(getLastestBtcBuyPrice());
	}
	
	private static volatile double lastestBtcDealPrice;
	private static volatile double lastestBtcBuyPrice;
	private static volatile double lastestBtcSellPrice;
	
	private static volatile double maxBtcBuyPrice;
	private static volatile double minBtcSellPrice;
	
	private static volatile double last24HourBtcDealAmount;
	private static volatile double last24HourRmbDealAmount;
	private static volatile double last24HourBtcMaxDealPrice;
	private static volatile double last24HourBtcMinDealPrice;
	private static volatile List<Trade> lastDealBtcSellList;
	
	private static volatile List<Trade> lastDealBtcBuyList;

	private static volatile List<Trade> lastDealBtcList;
	private static volatile List<Trade> lastUnDealBtcSellList;
	private static volatile List<Trade> lastUnDealBtcBuyList;
	
	private static volatile List<Trade> lastUnDealBtcSellListForTradeBtc;
	
	private static volatile double totalPaidRecommendBtcAmount;
	
	private static volatile double totalExtraBtcAmount;
	static {
		flushQuery();
		startTimer();
	}
	
	private static void startTimer() {
		Timer timer = new Timer();
        timer.schedule(new FlushQueryTask(), 1000 * 60 * 30, 1000 * 60 * 30);
	}
	
	public synchronized static void flushQuery() {
		TradeService tradeServ = new TradeServiceImpl();
		ExtraService extraServ = new ExtraServiceImpl();
		lastestBtcDealPrice = tradeServ.getLastestBtcDealPrice();
		lastestBtcBuyPrice = tradeServ.getLastestBtcBuyPrice();
		lastestBtcSellPrice = tradeServ.getLastestBtcSellPrice();
		last24HourBtcDealAmount = tradeServ.getLast24HourBtcDealAmount();
		last24HourRmbDealAmount = tradeServ.getLast24HourRmbDealAmount();
		last24HourBtcMaxDealPrice = tradeServ.getLast24HourBtcMaxDealPrice();
		last24HourBtcMinDealPrice = tradeServ.getLast24HourBtcMinDealPrice();
		
		lastDealBtcSellList = tradeServ.getLastDealBtcSellList();
		lastDealBtcBuyList = tradeServ.getLastDealBtcBuyList();
		lastDealBtcList =  tradeServ.getLastDealBtcList();
		lastUnDealBtcSellList = tradeServ.getLastUnDealBtcSellList();
		lastUnDealBtcBuyList = tradeServ.getLastUnDealBtcBuyList();
		lastUnDealBtcSellListForTradeBtc = tradeServ.getLastUnDealBtcSellListForTradeBtc();
		
		totalExtraBtcAmount = extraServ.getTotalBtcExtraAmount();
		//maxBtcBuyPrice = tradeServ.getMaxBtcBuyPrice();
		//minBtcSellPrice = tradeServ.getMinBtcSellPrice();
	}
	
	//最新成交价
	public static double getLastestBtcDealPrice() {
		return lastestBtcDealPrice;
	}
	
	
	//最新买入价
	public static double getLastestBtcBuyPrice() {
		return lastestBtcBuyPrice;
	}
	//最新卖出价
	public static  double getLastestBtcSellPrice() {
		return lastestBtcSellPrice;
	}
	//24小时成交量
	public static double getLast24HourBtcDealAmount() {
		return last24HourBtcDealAmount;
	}
	//24小时最高成交价
	public static double getLast24HourBtcMaxDealPrice() {
		return last24HourBtcMaxDealPrice;
	}
	//24小时最低成交价
	public static double getLast24HourBtcMinDealPrice() {
		return last24HourBtcMinDealPrice;
	}
	
	//最近成交的卖出交易单
	public static List<Trade> getLastDealBtcSellList() {
		return lastDealBtcSellList;
	}
	//最近成交的买入交易单
	public static List<Trade>  getLastDealBtcBuyList() {
		return lastDealBtcBuyList;
	}
	
	//最近成交的BTC交易单(包括买入和卖出)
	public static List<Trade>  getLastDealBtcList() {
		return lastDealBtcList;
	}

	//最近的卖出委托单
	public static List<Trade> getLastUnDealBtcSellList() {
		return lastUnDealBtcSellList;	
	}
	//最近的买入委托单
	public static List<Trade> getLastUnDealBtcBuyList() {
		return lastUnDealBtcBuyList;
	}
	
	public static double getLast24HourRmbDealAmount() {
		return last24HourRmbDealAmount;
	}

	public static void setLast24HourRmbDealAmount(double last24HourRmbDealAmount) {
		TradeQuery.last24HourRmbDealAmount = last24HourRmbDealAmount;
	}

	public static List<Trade> getLastUnDealBtcSellListForTradeBtc() {
		return lastUnDealBtcSellListForTradeBtc;
	}
public static double getTotalExtraBtcAmount() {
		return totalExtraBtcAmount;
	}

	/*
	public static double getMaxBtcBuyPrice() {
		return maxBtcBuyPrice;
	}

	public static double getMinBtcSellPrice() {
		return minBtcSellPrice;
	}
*/
	public static LastDealInfo getLastDealInfo() {
		LastDealInfo lastDealInfo = new LastDealInfo();
		
		lastDealInfo.setLastestBtcDealPrice(lastestBtcDealPrice);
		lastDealInfo.setLastestBtcBuyPrice(lastestBtcBuyPrice);
		lastDealInfo.setLastestBtcSellPrice(lastestBtcSellPrice);
		lastDealInfo.setLast24HourBtcDealAmount(last24HourBtcDealAmount);
		lastDealInfo.setLast24HourBtcMaxDealPrice(last24HourBtcMaxDealPrice);
		lastDealInfo.setLast24HourBtcMinDealPrice(last24HourBtcMinDealPrice);
		
		return lastDealInfo;
	}
	
	static class FlushQueryTask extends java.util.TimerTask{ 
        @Override
        public void run() { 
        	try {
        	    flushQuery();
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
	
}
