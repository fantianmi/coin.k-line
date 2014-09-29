package com.bkl.Kcoin.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.PlanTrade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.form.PlanTradeForm;
import com.bkl.Kcoin.service.PlanTradeService;
import com.bkl.Kcoin.service.impl.PlanTradeServiceImpl;
import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.constant.ErrorCode;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.ServletUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.RetCode;

/**
 * @author chaozheng
 * 
 */
public class PlanTradeServlet extends CommonServlet {

	/***
	 * 创建一个计划委托。
	 * TODO完善验证码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void newPlan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PlanTradeForm form = ServletUtil.readObjectServletQuery(request, PlanTradeForm.class);
//
//		boolean checkForm = form.validate();
//		if (!checkForm) {
//			ServletUtil.writeCommonReply(null, form.getErrorCode(), response);
//			return;
//		}
		if (!CoinConfig.isEnableTrade()) {
			ServletUtil.writeCommonReply(null,RetCode.ORDER_DISABLE, response);
			return;
		}
		User user = UserUtil.getCurrentUser(request);
		if (null == user) {
			ServletUtil.writeCommonReply(null, RetCode.FORBID, response);
			return;
		}
		boolean checkPasswd = user.checkSecretPassword(form.getPassword());
		if (!checkPasswd) {
			ServletUtil.writeCommonReply(null, RetCode.USER_SECRET_ERROR, response);
			return;
		}
		PlanTrade planTrade = new PlanTrade();
		planTrade.setCreate_time(TimeUtil.getUnixTime());
		planTrade.setPrice(form.getPrice());
		planTrade.setQuantity(form.getQuantity());
		planTrade.setUser_id(user.getId());
		planTrade.setTrade_type(form.getTradeType());
		planTrade.setStatus(PlanTrade.STATUS_NORMAL);
		PlanTradeService planTradeSrv = new PlanTradeServiceImpl();
		long id = planTradeSrv.saveOrUpdatePlan(planTrade);
		if (id > 0) {
			ServletUtil.writeCommonReply(null, RetCode.OK, response);
		} else {
			ServletUtil.writeCommonReply(null, RetCode.ERROR, response);
		}
	}
	
	/***
	 * 撤销一个计划委托。
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void cancelPlan(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		User user = UserUtil.getCurrentUser(request);
		if (null == user) {
			ServletUtil.writeCommonReply(null, RetCode.FORBID, response);
			return;
		}
		
		String planIdStr = request.getParameter("id") == null ? "0" : request.getParameter("id");
		long planId = 0;
		try {
			planId = Long.parseLong(planIdStr);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		if (planId <= 0) {
			ServletUtil.writeCommonReply(null, RetCode.ERROR, response);
			return;
		}
		
		GeneralDao<PlanTrade> planDao = DaoFactory.createGeneralDao(PlanTrade.class);
		PlanTrade plan = planDao.find(planId);
		if (null == plan) {
			ServletUtil.writeCommonReply(null, RetCode.ERROR, response);
			return;
		}
		
		// 非法用户
		int planUserId = plan.getUser_id();
		if (planUserId != user.getId()) {
			ServletUtil.writeCommonReply(null, RetCode.NOPERM, response);
			return;
		}
		
		if (PlanTrade.STATUS_EFFECTIVE == plan.getStatus() || PlanTrade.STATUS_DELETED == plan.getStatus()
				|| PlanTrade.STATUS_DISABLED == plan.getStatus()) {
			ServletUtil.writeCommonReply(null, RetCode.FORBID, response);
			return;
		}
		
		//修改为删除状态
		plan.setStatus(PlanTrade.STATUS_DELETED);
		planDao.update(plan);
		
		ServletUtil.writeCommonReply(null, RetCode.OK, response);
	}

	public void listPlan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = UserUtil.getCurrentUser(request);
		PlanTradeService planTradeSrv = new PlanTradeServiceImpl();
		List<PlanTrade> plans = planTradeSrv.getUserPlan(user.getId());
		ServletUtil.writeCommonReply(plans, RetCode.OK, response);
	}
}
