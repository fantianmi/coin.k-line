package com.bkl.Kcoin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.SubscribeService;
import com.bkl.Kcoin.service.impl.SubscribeServiceImpl;
import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.ServletUtil;
import com.km.common.vo.RetCode;

public class SubscribeServlet extends CommonServlet {

	public void doSubscribe(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = UserUtil.getCurrentUser(request);
		if (null == user) {
			ServletUtil.writeCommonReply(null, RetCode.SESSION_TIMEOUT, response);
			return;
		}
		String tradePasswd = StringUtils.defaultIfEmpty(request.getParameter("tradePwd"), "");
		if(!user.checkSecretPassword(tradePasswd)) {
			ServletUtil.writeCommonReply(null, RetCode.USER_SECRET_ERROR, response);
			return;
		}
		
		Subscribe subscribe = ServletUtil.readObjectServletQuery(request, Subscribe.class);
		SubscribeService subscribeSrv = new SubscribeServiceImpl();
		subscribe.setUser_id(user.getId());
		RetCode ret = subscribeSrv.doSubscribe(subscribe);
		ServletUtil.writeCommonReply(null, ret, response);
	}
}
