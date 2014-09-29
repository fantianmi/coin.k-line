package com.bkl.Kcoin.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.TradeService;
import com.bkl.Kcoin.service.impl.TradeServiceImpl;
import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.ServletUtil;
import com.km.common.vo.RetCode;

public class TradeServlet extends CommonServlet {
	public void sellBtc(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!CoinConfig.isEnableTrade()) {
			ServletUtil.writeCommonReply(null,RetCode.ORDER_DISABLE, response);
			return;
		}
		if (!UserUtil.isExistSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_TRADE_PASSWORD_NOT_SET, response);
			return;
		}
		if (!UserUtil.checkSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_SECRET_ERROR, response);
			return;
		}
		Trade trade = ServletUtil.readObjectServletQuery(request,Trade.class);
		TradeService tradeServ = new TradeServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		trade.setUser_id(user.getId());
		trade.setType(Trade.TYPE_BTC_SELL);
		long retid = tradeServ.sellBtc(trade);
		ServletUtil.writeCommonReplyOnSaveDb(retid, response);
	}
	
	public void getBtcSellList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TradeService tradeServ = new TradeServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		
		List<Trade> trades = tradeServ.getBtcSellList(user.getId());	
		ServletUtil.writeOkCommonReply(trades, response);
	}
	public void getBtcBuyList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TradeService tradeServ = new TradeServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		
		List<Trade> trades = tradeServ.getBtcBuyList(user.getId());	
		ServletUtil.writeOkCommonReply(trades, response);
	}
	public void buyBtc(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!CoinConfig.isEnableTrade()) {
			ServletUtil.writeCommonReply(null,RetCode.ORDER_DISABLE, response);
			return;
		}
		if (!UserUtil.isExistSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_TRADE_PASSWORD_NOT_SET, response);
			return;
		}
		if (!UserUtil.checkSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_SECRET_ERROR, response);
			return;
		}
		
		
		Trade trade = ServletUtil.readObjectServletQuery(request,Trade.class);
		TradeService tradeServ = new TradeServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		trade.setUser_id(user.getId());
		trade.setType(Trade.TYPE_BTC_BUY);
		long retid = tradeServ.buyBtc(trade);
		ServletUtil.writeCommonReplyOnSaveDb(retid, response);
	}
	
	public void cancelTrade(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Trade trade = ServletUtil.readObjectServletQuery(request,Trade.class);
		TradeService tradeServ = new TradeServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		RetCode ret = tradeServ.cancelTrade(trade.getId(), user.getId());
		ServletUtil.writeCommonReply(null,ret, response);
	}
}
