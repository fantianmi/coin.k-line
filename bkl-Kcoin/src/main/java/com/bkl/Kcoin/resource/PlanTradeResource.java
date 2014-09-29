package com.bkl.Kcoin.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.utils.ServletUtil;

public class PlanTradeResource {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void newPlan(@FormParam("price") Double price, @FormParam("quantity") Double quantity,
			@FormParam("tradeType") Integer tradeType, @FormParam("password") String password) {
		Map map = new HashMap();
		map.put("price", price);
		map.put("quantity", quantity);
		map.put("tradeType", tradeType);
		map.put("password", password);
		try {
			ServletUtil.writeObjectReply(map, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
}
