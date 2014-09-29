package com.bkl.Kcoin.utils;

import javax.servlet.http.HttpServletRequest;

import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.UserService;
import com.bkl.Kcoin.service.impl.UserServiceImpl;

public class UserUtil {
	public static User getCurrentUser(HttpServletRequest request) {
		UserService userServ = new UserServiceImpl();
		
		String username = (String)request.getSession(true).getAttribute("username");
		User user = null;
		if (username != null) {
			user = userServ.find(username);
		}
		return user;
	}
	
	public static boolean islogin(HttpServletRequest request) {
		String username = (String)request.getSession(true).getAttribute("username");
		if (username != null) {
			return true;
		}
		return false;
	}
	
	public static boolean checkSecretPassword(HttpServletRequest request) {
		String tradePwd = request.getParameter("tradePwd");
		User user = UserUtil.getCurrentUser(request);
		if (!user.checkSecretPassword(tradePwd)) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isExistSecretPassword(HttpServletRequest request) {
		User user = UserUtil.getCurrentUser(request);
		if (user.getSecret() == null || "".equals(user.getSecret().trim())) {
			return false;
		} else {
			return true;
		}
	}
}
