package com.bkl.Kcoin.service;

import java.util.List;
import java.util.Map;

import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.Trade2User;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

/*
 btc买卖交易记录
 */
public interface TradeService {
	/**
	 * 登陆用户卖出BTC
	 * @param trade
	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0.
	 */
	public long sellBtc(Trade trade);
	
	/**
	 * 登陆用户买入BTC
	 * @param trade
	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0.
	 */
	public long buyBtc(Trade trade);
	
	/**
	 * 管理员后台:根据交易id，查询交易信息
	 * @param tradeid
	 * @return
	 */
	public Trade findTrade(long tradeid);
	
	/**
	 * 管理员后台:根据交易id，撤销交易,并把未成交的现金或者btc，归还到用户的现金帐户或者btc帐户
	 * @param tradeid
	 * @return 如果交易记录已经完成成交或者是已经撤销过，状态为部分成交，则返回false;撤销成功返回true。
	 */
	public RetCode cancelTrade(long tradeid);
	
	/**
	 * 登陆用户根据交易id查询交易信息
	 * @param tradeid 交易id
	 * @param user_id 登陆用户id
	 * @return
	 */
	public Trade findTrade(long tradeid, long user_id) ;
	/**
	 * 登陆用户:根据交易id，撤销交易,并把未成交的现金或者btc，归还到用户的现金帐户或者btc帐户
	 * @param tradeid
	 * @param user_id
	 * @return 如果交易记录已经完成成交或者是已经撤销过，状态为部分成交，则返回false;撤销成功返回true。
	 */
	public RetCode cancelTrade(long tradeid, long user_id);
	
	/**
	 * 管理员后台:获取btc卖出列表
	 * @return
	 */
	public List<Trade2User> getBtcSellList();
	/**
	 * 管理员后台:获取btc买入列表
	 * @return
	 */
	public List<Trade2User> getBtcBuyList();
	
	/**
	 * 管理员后台:获取全部交易列表(包括买入卖出交易)
	 * @return
	 */
	public List<Trade2User> getTradeList();

	
	/**
	 * 管理员后台:分页获取全部交易列表(包括买入卖出交易)
	 * @param page
	 * @return
	 */
	public PageReply<Trade2User> getTradePage(Map searchMap, Page page);
	
	/**
	 * 管理员后台:分页获取全部交易列表(包括买入卖出交易)
	 * @param page
	 * @return
	 */
	public PageReply<Trade2User> getTradePage(Page page);
	
	/**
	 * 登陆用户:获取全部交易列表(包括买入卖出交易)
	 * status = 0,获取未成交；status = 1，获取完全成交；status=2，获取撤单。status=3，获取部分成交，status=-1，获取全部。
	 * @return
	 */
	public List<Trade> getBtcTradeList(long user_id,int  status) ;
	/**
	 * 登陆用户:获取btc卖出列表
	 * @return
	 */
	public List<Trade> getBtcSellList(long user_id);
	/**
	 * 登陆:获取btc买入列表
	 * @return
	 */
	public List<Trade> getBtcBuyList(long user_id);
	
	/**
	 * 管理员后台:获取未成交的btc卖出列表
	 * @return
	 */
	public List<Trade> getUnDealBtcSellList();
	/**
	 * 管理员后台:获取未成交的btc买入列表
	 * @return
	 */
	public List<Trade> getUnDealBtcBuyList();
	
	/**
	 * 公开api:获取最近成交的btc成交列表
	 * @return
	 */
	public List<Trade> getLastDealBtcList();
	
	
	/**
	 * 公开api:获取最近成交的btc卖出列表
	 * @return
	 */
	public List<Trade> getLastDealBtcSellList();
	/**
	 * 公开api:获取最近成交的btc买入列表
	 * @return
	 */
	public List<Trade> getLastDealBtcBuyList();
	/**
	 * 公开api:获取最近委托的btc卖出列表
	 * @return
	 */
	public List<Trade> getLastUnDealBtcSellList();
	
	public List<Trade> getLastUnDealBtcSellListForTradeBtc();
	/**
	 * 公开api:获取最近委托的btc买入列表
	 * @return
	 */
	public List<Trade> getLastUnDealBtcBuyList();
	/**
	 * 获取委托的超时交易列表
	 * @return
	 */
	public List<Trade> getExpireTradeList();
	/**
	 * 公开api:获取最近btc成交价
	 * @return
	 */
	public  double getLastestBtcDealPrice();
	/**
	 * 公开api:获取最近btc买入价
	 * @return
	 */
	public  double getLastestBtcBuyPrice();
	/**
	 * 公开api:获取最近btc卖出价
	 * @return
	 */
	public  double getLastestBtcSellPrice();
	/**
	 * 公开api:获取最近24小时btc成交量
	 * @return
	 */
	public double getLast24HourBtcDealAmount();
	
	public double getLast24HourRmbDealAmount();
	
	/**
	 * 公开api:获取最近24小时btc最高成交价
	 * @return
	 */
	public double getLast24HourBtcMaxDealPrice();
	/**
	 * 公开api:获取最近24小时btc最低成交价
	 * @return
	 */
	public double getLast24HourBtcMinDealPrice();
	
	/**
	 * 最近10笔未成交的买入记录
	 */
	List<Trade> getTo10BtcBuyList(long userId);
	
	/**
	 * 最近10笔未成交的卖出记录
	 */
	List<Trade> getTo10BtcSellList(long userId);
	
	//public  double getMaxBtcBuyPrice();
	//public  double getMinBtcSellPrice();
 }
