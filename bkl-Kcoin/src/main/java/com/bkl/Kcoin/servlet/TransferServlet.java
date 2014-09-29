package com.bkl.Kcoin.servlet;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.helper.TransactionHelper;
import com.bkl.Kcoin.service.CoinService;
import com.bkl.Kcoin.service.impl.BtcCoinServiceImpl;
import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.ServletUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.RetCode;

public class TransferServlet extends CommonServlet {

	public void notifyRecharge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = UserUtil.getCurrentUser(request);
		if(user!=null) {
			TransactionHelper.loadTransaction(user.getEmail());
		}
		ServletUtil.writeCommonReply("", RetCode.OK, response);
	}
	
	public void saveBtcWithdraw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 检查是否设置交易密码
		User user = UserUtil.getCurrentUser(request);
		if (StringUtils.isEmpty(user.getSecret())) {
			ServletUtil.writeCommonReply(null, RetCode.USER_TRADE_PASSWORD_NOT_SET, response);
			return;
		}
		
		// TODO 连续输错密码检查
		// TODO 检查帐号是否被冻结


		// 检查交易密码是否正确
		String tradePwd = request.getParameter("tradePwd");
		boolean checkPwd = user.checkSecretPassword(tradePwd);
		if (!checkPwd) {
			ServletUtil.writeCommonReply(null, RetCode.USER_SECRET_ERROR, response);
			return;
		}
		
		Transfer transfer = ServletUtil.readObjectServletQuery(request, Transfer.class);
		
		// TODO 检查余额是否充足
		
		// TODO 检查提现额度是否超过当日限制
		
		transfer.setUser_id(user.getId());
		transfer.setType(Transfer.TYPE_BTC_WITHDRAW);
		transfer.setAddress(user.getBtc_out_addr());
		transfer.setCtime(TimeUtil.getUnixTime());
		transfer.setStatus(Transfer.STATUS_NORMAL);
		
		CoinService coinSrv = new BtcCoinServiceImpl();
		long transferId = coinSrv.saveCoinWithdraw(transfer);
		ServletUtil.writeCommonReplyOnSaveDb(transferId, response);
	}
	

}
