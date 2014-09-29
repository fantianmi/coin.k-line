package com.bkl.Kcoin.servlet;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.CashService;
import com.bkl.Kcoin.service.impl.CashServiceImpl;
import com.bkl.Kcoin.utils.UserUtil;
import com.km.common.config.Config;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.MD5Util;
import com.km.common.utils.ServletUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class CashServlet extends CommonServlet {
	
	/**
	 * 新建人民币充值记录
	 * @throws Exception
	 */
	public void saveRecharge(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cash cash = ServletUtil.readObjectServletQuery(request,Cash.class);
		CashService cashSer = new CashServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		cash.setUser_id(user.getId());
		cash.setStatus(0);
		long retid = cashSer.saveRecharge(cash);
		ServletUtil.writeCommonReplyOnSaveDb(retid, response);
	}
	
	/**
	 * 修改人民币充值记录
	 * @throws Exception
	 */
	public void updateRecharge(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cash cash = ServletUtil.readObjectServletQuery(request,Cash.class);
		CashService cashSer = new CashServiceImpl();
		User user = UserUtil.getCurrentUser(request);
		long retid = cashSer.updateRecharge(cash,user.getId()); //有安全问题，后续fix
		ServletUtil.writeCommonReplyOnSaveDb(retid, response);
	}
	
	/**
	 * 保存人民币提现记录
	 * @throws Exception
	 */
	public void saveWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!UserUtil.isExistSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_TRADE_PASSWORD_NOT_SET, response);
			return;
		}
		if (!UserUtil.checkSecretPassword(request)) {
			ServletUtil.writeCommonReply(null,RetCode.USER_SECRET_ERROR, response);
			return;
		}
		User user = UserUtil.getCurrentUser(request);
		Cash withdraw = ServletUtil.readObjectServletQuery(request,Cash.class);

		
		withdraw.setStatus(0);
		if (StringUtils.isBlank(withdraw.getBank()) || "-1".equals(withdraw.getBank())) {
			withdraw.setBank(user.getBank());
		}
		if (StringUtils.isBlank(withdraw.getCard())) {
			withdraw.setCard(user.getBank_account());
		}
		if (StringUtils.isBlank(withdraw.getBank_number())) {
			withdraw.setBank_number(user.getBank_number());
		}
		if (StringUtils.isBlank(withdraw.getMobile())) {
			withdraw.setMobile(user.getMobile());
		}
		CashService cashSer = new CashServiceImpl();
		withdraw.setUser_id(user.getId());
		long retid = cashSer.saveWithdraw(withdraw);
		ServletUtil.writeCommonReplyOnSaveDb(retid, response);
	}
	
	
	/**
	 * 用户取消人民币提现 系统bug，不应该让用户取消提现
	 * @throws Exception
	 */
/*	public void cancelWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		User user = UserUtil.getCurrentUser(request);
		Cash withdraw = ServletUtil.readObjectServletQuery(request,Cash.class);
		
		CashService cashSer = new CashServiceImpl();
		RetCode ret = cashSer.cancelWithdraw(withdraw.getId(),user.getId());
		ServletUtil.writeCommonReply(null,ret, response);
	}*/
	
	/**
	 * 分页查询人民币充值记录
	 * @throws Exception
	 */
	public void getRechargePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CashService cashSer = new CashServiceImpl();
		Page page = ServletUtil.getPage(request);
		User user = UserUtil.getCurrentUser(request);
		PageReply<Cash> recharges = cashSer.getRechargePage(page,user.getId());
		ServletUtil.writeOkCommonReply(recharges, response);
	}
	

	public void pay(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CashService cashSer = new CashServiceImpl();
		User user = UserUtil.getCurrentUser(request);

		Double amount = Double.parseDouble(request.getParameter("amount"));
		Integer type = Integer.parseInt(request.getParameter("type"));

		Cash cash = new Cash();
		cash.setUser_id(user.getId());
		cash.setType(type);
		cash.setFin_type(0);
		cash.setAmount(amount);
		cash.setStatus(0);
		long cashId = cashSer.saveRecharge(cash);

		if (cashId <= 0) {
			ServletUtil.writeCommonReplyOnSaveDb(cashId, response);
			return;
		}

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head>");
		out.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		out.print("</head>");
		out.print("<body onLoad='document.dinpayForm.submit();'>");
		out.print("正在跳转 ...");
		out.print("<form name='dinpayForm' method='post' action='" + Config.getString("pay.url")  +"' >");
		out.print("<br><!--商 家 号：--><input Type='hidden' Name='merchant_code' value='" + Config.getString("pay.merchant.code")  +"'>");
		out.print("<br><!--服务类型：--><input Type='hidden' Name='service_type' value='direct_pay'>");
		out.print("<input type='hidden' name='input_charset' value='UTF-8'/>");
		out.print("<input Type='hidden' Name='interface_version' value='V3.0'/>");
		out.print("<br><!--签名方式：--><input Type='hidden' Name='sign_type' value='MD5'/>");
		out.print("<br><!--订 单 号：--><input Type='hidden' Name='order_no' value='" + cashId  +"'/>");
		out.print("<br><!--时间字段：--><input Type='hidden' Name='order_time' value='" + TimeUtil.getNowDateTime()  +"'/>");
		out.print("<br><!--订单金额：--><input Type='hidden' Name='order_amount' value='" + amount  +"'/>");
		out.print("<br><!--商品名：--><input Type='hidden' Name='product_name' value='" + Config.getString("pay.product.name") + "'/>");
		out.print("<br><!--客户端IP：--><input Type='hidden' Name='client_ip' value='" + getIP(request)  +"'>");
		out.print("<br><!--公用业务扩展参数：--><input Type='hidden' Name='extend_param' value=''>");
		out.print("<br><!--公用回传参数：--><input Type='hidden' Name='extra_return_param' value=''>");
		out.print("<br><!--商品编号：--><input Type='hidden' Name='product_code' value='CASH_" + cashId  +"'>");
		out.print("<br><!--商品描述：--><input Type='hidden' Name='product_desc' value=''>");
		out.print("<br><!--商品数量：--><input Type='hidden' Name='product_num' value=''>");
		out.print("<br><!--页面跳转同步通知地址：--><input Type='hidden' Name='notify_url' value='" + Config.getString("pay.notify.url")  +"'>");
		out.print("<br><!--商品展示URL：--><input Type='hidden' Name='show_url' value=''>");
		//out.print("<br><input Type='submit' Name='submit' value='智付在线支付'>");
		out.print("</form>");
		out.print("</body>");
		//out.print("<script type='text/javascript'>document.dinpayForm.submit();</script>");
		out.print("</html>");
		out.flush();
		
	}
	
	
	public void pay4huichao(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CashService cashSer = new CashServiceImpl();
		User user = UserUtil.getCurrentUser(request);

		Double amount = Double.parseDouble(request.getParameter("amount"));
		Integer type = Integer.parseInt(request.getParameter("type"));

		Cash cash = new Cash();
		cash.setUser_id(user.getId());
		cash.setType(type);
		cash.setFin_type(0);
		cash.setAmount(amount);
		cash.setStatus(0);
		long cashId = cashSer.saveRecharge(cash);

		if (cashId <= 0) {
			ServletUtil.writeCommonReplyOnSaveDb(cashId, response);
			return;
		}

		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out = response.getWriter();
		
		String md5src;  //加密字符串    
	    md5src = Config.getString("pay.merchant.code") +"&"+ cashId +"&"+ amount +"&"+ Config.getString("pay.return.url") +"&"+ Config.getString("pay.md5.key") ;
	    
	    String SignInfo; //MD5加密后的字符串
	    SignInfo = MD5Util.md5(md5src);//MD5检验结果
	    
		out.print("<html>");
		out.print("<head>");
		out.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		out.print("</head>");
		out.print("<body onLoad='document.E_FORM.submit();'>");
		out.print("正在跳转 ...");
		out.print("<form name='E_FORM' method='post' action='" + Config.getString("pay.url")  +"' >");
		out.print("<br><!--商 家 号：--><input Type='hidden' Name='MerNo' value='" + Config.getString("pay.merchant.code")  +"'>");
		out.print("<br><!--订 单 号：--><input Type='hidden' Name='BillNo' value='" + cashId  +"'/>");
		out.print("<br><!--订单金额：--><input Type='hidden' Name='Amount' value='" + amount  +"'/>");
		out.print("<br><!--商品返回URL：--><input Type='hidden' Name='ReturnURL' value='" + Config.getString("pay.return.url")  +"'>");
		out.print("<br><!--商品返回URL：--><input Type='hidden' Name='AdviceURL' value='" + Config.getString("pay.notify.url")  +"'>");
		
		out.print("<br><!--SignInfo：--><input Type='hidden' Name='SignInfo' value='" + SignInfo  +"'/>");
		out.print("<br><!--Remark：--><input Type='hidden' Name='Remark' value='" + Config.getString("pay.product.name")  +"'/>");
		out.print("<br><!--defaultBankNumber：--><input Type='hidden' Name='defaultBankNumber' value='ICBC'/>");
		
		
		out.print("<br><!--时间字段：--><input Type='hidden' Name='orderTime' value='" + TimeUtil.getNowDateTime()  +"'/>");
		
		out.print("<br><!--商品名：--><input Type='hidden' Name='products' value='" + Config.getString("pay.product.name") + "'/>");

		
		out.print("</form>");
		out.print("</body>");
		//out.print("<script type='text/javascript'>document.dinpayForm.submit();</script>");
		out.print("</html>");
		out.flush();
		
	}
	
	
	
	public void querypay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}
	
	private String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.contains(","))
			ip = ip.substring(0, ip.indexOf(','));
		return ip;
	}
}
