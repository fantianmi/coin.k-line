package com.bkl.Kcoin.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.AdminUser;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Cash2User;
import com.bkl.Kcoin.entity.Deal2User;
import com.bkl.Kcoin.entity.Trade2User;
import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.Transfer2User;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.CashService;
import com.bkl.Kcoin.service.CoinService;
import com.bkl.Kcoin.service.DealService;
import com.bkl.Kcoin.service.ExtraService;
import com.bkl.Kcoin.service.UserService;
import com.bkl.Kcoin.service.impl.BtcCoinServiceImpl;
import com.bkl.Kcoin.service.impl.CashServiceImpl;
import com.bkl.Kcoin.service.impl.DealServiceImpl;
import com.bkl.Kcoin.service.impl.ExtraServiceImpl;
import com.bkl.Kcoin.service.impl.TradeServiceImpl;
import com.bkl.Kcoin.service.impl.UserServiceImpl;
import com.bkl.Kcoin.utils.AdminUserUtil;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.keep.framework.multicoin.core.Wallet;
import com.keep.framework.multicoin.message.ValidateInfo;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.ServletUtil;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class AdminServlet extends CommonServlet {
	
	/**
	 * 激活用户
	 * TODO 重复激活
	 */
	public void activeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = Long.parseLong(request.getParameter("id"));
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = userDao.find(userId);
		if (user != null) {
			user.setEmail_validated(1);
			userDao.update(user);
		}
		ServletUtil.writeOkCommonReply("", response);
	}
	/**
	 * 禁用/启用用户
	 * TODO 重复禁用/启用
	 */
	public void frozen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = Long.parseLong(request.getParameter("id"));
		int frozen = Integer.parseInt(request.getParameter("frozen"));
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = userDao.find(userId);
		if (user != null && frozen != user.getFrozen()) {
			user.setFrozen(frozen);
			userDao.update(user);
		}
		ServletUtil.writeOkCommonReply("", response);
	}
	
	/**
	 * 确认/否定实名验证
	 * status=1表示确认;status=2表示否定
	 */
	public void confirmRealName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = Long.parseLong(request.getParameter("id"));
		int status = Integer.parseInt(request.getParameter("status"));
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = userDao.find(userId);
		if (user != null && user.getRealname_validated() == 0) {
			user.setRealname_validated(status);
			userDao.update(user);
		}
		ServletUtil.writeOkCommonReply("", response);
	}
		
	public void getTradeList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TradeServiceImpl tradeServ = new TradeServiceImpl();
		Page page = new Page();
		page.setPagenum(0);
		page.setPagesize(10);
		
		PageReply<Trade2User> trade2UserPage = tradeServ.getTradePage(page);
		ServletUtil.writeOkCommonReply(trade2UserPage, response);
	}
	
	public void getDealList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DealService dealServ = DealServiceImpl.getInstance();
		
		List<Deal2User> deals = dealServ.getDealList();
		ServletUtil.writeOkCommonReply(deals, response);
	}
	
	public void rundeal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DealService ds = DealServiceImpl.getInstance();
		ds.runDeal();
		ServletUtil.writeOkCommonReply(null, response);
	}
	
	/***
	 * 查询用户
	 */
	public void getUserDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = 0;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			e.fillInStackTrace();
		}
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = null;
		if (id > 0) {
			user = userDao.find(id);
		}
		ServletUtil.writeOkCommonReply(user, response);
	}	
	
	/***
	 * 分页查询用户
	 */
	public void getUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map searchMap = ServletUtil.getSearchMap(request);
		UserService userSrv = new UserServiceImpl();
		Page page = ServletUtil.getPage(request);
		PageReply<User> users = userSrv.findUser(searchMap, page);
		ServletUtil.writeOkCommonReply(users, response);
	}	
	
	/**
	 * 分页查询人民币充值
	 */
	public void getRechargePage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Page page = ServletUtil.getPage(request);
		Map searchMap = ServletUtil.getSearchMap(request);
		CashService cashSer = new CashServiceImpl();
		PageReply<Cash2User> recharges = cashSer.getRecharge2UserPage(searchMap,page);
		ServletUtil.writeOkCommonReply(recharges, response);
	}	
	
	/**
	 * 分页查询比特币充值记录
	 */
	public void getBTCTransferInPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CoinService coinSrv = new BtcCoinServiceImpl();
		Page page = ServletUtil.getPage(request);
		Map searchMap = ServletUtil.getSearchMap(request);
		PageReply<Transfer2User> transfers = coinSrv.getTransferInPage(searchMap, page);
		ServletUtil.writeOkCommonReply(transfers, response);
	}
	
	/**
	 * 分页查询比特币提现记录
	 */
	public void getBTCTransferOutPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CoinService coinSrv = new BtcCoinServiceImpl();
		Page page = ServletUtil.getPage(request);
		Map searchMap = ServletUtil.getSearchMap(request);
		PageReply<Transfer2User> transfers = coinSrv.getTransferOutPage(searchMap, page);
		ServletUtil.writeOkCommonReply(transfers, response);
	}
	
	/**
	 * 确定比特币充值成功
	 */
	public void confirmBtcIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Transfer transfer = ServletUtil.readObjectServletQuery(request, Transfer.class);
		CoinService coinSrv = new BtcCoinServiceImpl();
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		RetCode ret = coinSrv.confirmCoinIn(transfer.getId(),adminUser.getId());
		ServletUtil.writeCommonReply(null, ret, response);
	}
	
	/**
	 * 确定比特币提现成功
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void confirmBtcOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Transfer transfer = ServletUtil.readObjectServletQuery(request, Transfer.class);
		CoinService coinSrv = new BtcCoinServiceImpl();
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		RetCode ret = coinSrv.confirmCoinOut(transfer.getId(),adminUser.getId());
		ServletUtil.writeCommonReply(null, ret, response);
	}
	
	/**
	 * 分页查询人民币提现
	 */
	public void getWithdrawPage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Page page = ServletUtil.getPage(request);
		Map searchMap = ServletUtil.getSearchMap(request);
		CashService cashSer = new CashServiceImpl();
		PageReply<Cash2User> withdraws = cashSer.getWithdraw2UserPage(searchMap,page);
		ServletUtil.writeOkCommonReply(withdraws, response);
	}	
	
	/***
	 * 确定人民币充值
	 */
	public void confirmRecharge(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cash cash = ServletUtil.readObjectServletQuery(request,Cash.class);
		CashService cashServ = new CashServiceImpl();
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		RetCode ret = cashServ.doRecharge(cash.getId(),adminUser.getId());
		ServletUtil.writeCommonReply(null,ret, response);
	}
	
	/***
	 * 确定人民币提现
	 */
	public void confirmWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cash cash = ServletUtil.readObjectServletQuery(request,Cash.class);
		CashService cashServ = new CashServiceImpl();
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		RetCode ret = cashServ.doWithdraw(cash.getId(), adminUser.getId());
		ServletUtil.writeCommonReply(null,ret, response);
	}
	
	/***
	 * 取消人民币提现
	 */
	public void cancelWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cash cash = ServletUtil.readObjectServletQuery(request,Cash.class);
		CashService cashServ = new CashServiceImpl();
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		RetCode ret = cashServ.doCancelWithdraw(cash.getId(), adminUser.getId());
		ServletUtil.writeCommonReply(null,ret, response);
	}
	
	public void cancelBtcWithdraw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AdminUser adminUser = AdminUserUtil.getCurrentUser(request);
		Transfer transfer = ServletUtil.readObjectServletQuery(request,Transfer.class);
		
		CoinService coinSrv = new BtcCoinServiceImpl();
		RetCode ret = coinSrv.cancelWithdraw(transfer.getId(),adminUser.getId());
		ServletUtil.writeCommonReply(null,ret, response);
		
	}
	
	public void sendCoins(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 钱包没启用,时服务不可用
		if (!CoinConfig.isEnableBitcoinWallet()) {
			ServletUtil.writeCommonReply(null, RetCode.WALLET_DISABLED, response);
			return;
		}

		String toAddress = request.getParameter("toAddress");
		// 钱包可能存在启动异常
		ValidateInfo validateInfo = Wallet.INSTANCE.validateAddress(toAddress);
		if (validateInfo == null) {
			ServletUtil.writeCommonReply(null, RetCode.WALLET_NOT_START, response);
			return;
		}

		// 检查付款地址是否合法
		if (!validateInfo.isIsvalid()) {
			ServletUtil.writeCommonReply(null, RetCode.BTC_SENDCOINDS_TO_ADDR_ERROR, response);
			return;
		}

		// 检查金额是否合法
		String amountString = request.getParameter("amount");
		Double amount = 0D;
		try {
			amount = Double.valueOf(amountString);
		} catch (NumberFormatException e) {
			e.fillInStackTrace();
		}
		if (amount <= 0D) {
			ServletUtil.writeCommonReply(null, RetCode.BTC_SENDCOINDS_AMOUNT_LT_ZERO, response);
			return;
		}

		try {
			Wallet.INSTANCE.sendToAddress(toAddress, amount);
			ServletUtil.writeCommonReply(null, RetCode.OK, response);
		} catch (Exception e) {
			ServletUtil.writeCommonReply(null, RetCode.ERROR, response);
		}
	}
	
	public void viewIdentityPhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String picHome = req.getServletContext().getRealPath("/pic/user-identity");
		String absoluteUploadDir = picHome;
		if (!new File(picHome).isAbsolute()) {
			absoluteUploadDir = req.getServletContext().getRealPath(picHome);
		}
		
		if (!new File(absoluteUploadDir).isDirectory()) {
			com.km.common.utils.IOUtils.createDirRecursive(new File(absoluteUploadDir));
		}
		long id = Long.parseLong(StringUtils.defaultIfEmpty(req.getParameter("id"), "0"));
		File pic = new File(absoluteUploadDir, id + ".jpg");
		if (pic.exists()) {
			InputStream input = new FileInputStream(pic);
			OutputStream os = resp.getOutputStream();

			IOUtils.copy(input, os);
			os.flush();

			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(os);
		}

	}
	
	public void modifyUserMoney(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = Long.parseLong(request.getParameter("id"));
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = userDao.find(userId);
		if (user != null) {
			if (!StringUtils.isEmpty(request.getParameter("rmb"))) {
				try {
					double rmb = Double.parseDouble((request.getParameter("rmb")));
					user.setRmb(rmb);
				} catch (NumberFormatException e) {

				}
			}
			if (!StringUtils.isEmpty(request.getParameter("rmb_frozen"))) {
				try {
					double rmb_frozen = Double.parseDouble((request.getParameter("rmb_frozen")));
					user.setRmb_frozen(rmb_frozen);
				} catch (NumberFormatException e) {
					
				}
			}
			if (!StringUtils.isEmpty(request.getParameter("btc"))) {
				try {
					double btc = Double.parseDouble((request.getParameter("btc")));
					user.setBtc(btc);
				} catch (NumberFormatException e) {
					
				}
			}
			if (!StringUtils.isEmpty(request.getParameter("btc_frozen"))) {
				try {
					double btc_frozen = Double.parseDouble((request.getParameter("btc_frozen")));
					user.setBtc_frozen(btc_frozen);
				} catch (NumberFormatException e) {
					
				}
			}
			
			if (!StringUtils.isEmpty(request.getParameter("btc_extra"))) {
				try {
					double btc_extra = Double.parseDouble((request.getParameter("btc_extra")));
					user.setBtc_extra(btc_extra);
				} catch (NumberFormatException e) {
					
				}
			}
			if (!StringUtils.isEmpty(request.getParameter("btc_extra_frozen"))) {
				try {
					double btc_extra_frozen = Double.parseDouble((request.getParameter("btc_extra_frozen")));
					user.setBtc_extra_frozen(btc_extra_frozen);
				} catch (NumberFormatException e) {
					
				}
			}
			
			userDao.update(user);
		}
		ServletUtil.writeOkCommonReply("", response);
	}
	
	public void addExtraRmb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		double rmb =  Double.parseDouble(request.getParameter("rmb"));
		rmb = DoubleUtil.formatDouble(rmb);
		ExtraService serv = new ExtraServiceImpl();
		serv.adjustUserMoneyByExtraCoin(rmb);
		ServletUtil.writeOkCommonReply("", response);
	}
}
