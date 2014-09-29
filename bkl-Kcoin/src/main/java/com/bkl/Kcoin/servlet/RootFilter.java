package com.bkl.Kcoin.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.utils.UserUtil;

public class RootFilter implements Filter {
	private String sessionName = "";
	private String[] urls = {};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String page_url = req.getServletPath();
		String username = (String) req.getSession(true).getAttribute(
				sessionName);
		boolean isFilter = false;
		for (String url : urls) {
			if (url.equals(page_url)) {
				isFilter = true;
				break;
			}
		}
		if (!isFilter) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//未登录用户禁止进入用户中心
		if (username == null) {
			String forwardurl = ((HttpServletRequest)request).getServletPath();
			res.sendRedirect("/index.jsp?forward=" + URLEncoder.encode(forwardurl));
			return;
		}
		User user = UserUtil.getCurrentUser((HttpServletRequest)request);
		//用户登陆用户中心，但是没有填写用户资料，不允许进入其他页面。
		if (StringUtils.isEmpty(user.getName()) && !"/userinfo.jsp".equals(page_url)) {
			res.sendRedirect("/userinfo.jsp");
			return;
		}
		filterChain.doFilter(request, response);
		

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		sessionName = filterConfig.getInitParameter("session_name");
		String urlsString = filterConfig.getInitParameter("urls");
		if (urlsString != null) {
			urls = urlsString.split(",");
		}
		if (StringUtils.isEmpty(sessionName)) {
			sessionName = "username";
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
