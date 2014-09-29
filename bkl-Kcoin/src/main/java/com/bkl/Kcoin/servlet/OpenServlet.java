package com.bkl.Kcoin.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.AdminUser;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.RecommendDetail;
import com.bkl.Kcoin.entity.Subscribe2User;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.AdminService;
import com.bkl.Kcoin.service.CashService;
import com.bkl.Kcoin.service.SubscribeService;
import com.bkl.Kcoin.service.TradeQuery;
import com.bkl.Kcoin.service.TradeService;
import com.bkl.Kcoin.service.UserService;
import com.bkl.Kcoin.service.impl.AdminServiceImpl;
import com.bkl.Kcoin.service.impl.CashServiceImpl;
import com.bkl.Kcoin.service.impl.SubscribeServiceImpl;
import com.bkl.Kcoin.service.impl.TradeServiceImpl;
import com.bkl.Kcoin.service.impl.UserServiceImpl;
import com.bkl.Kcoin.utils.EmailUtil;
import com.bkl.Kcoin.utils.RequestUtil;
import com.bkl.Kcoin.utils.UserUtil;
import com.bkl.Kcoin.vo.LastDealInfo;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.servlet.CommonServlet;
import com.km.common.utils.MD5Util;
import com.km.common.utils.ServletUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.utils.ValidUtils;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class OpenServlet extends CommonServlet {

	private Log log = LogFactory.getLog(OpenServlet.class);

	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = ServletUtil.readObjectServletQuery(request, User.class);
		UserService userServ = new UserServiceImpl();
		User userFound = userServ.find(user.getEmail());
		if (userFound == null) {
			ServletUtil.writeCommonReply(null,
					RetCode.USERNAME_OR_PASSWORD_ERROR, response);
			return;
		}

		// 邮件验证
		if (userFound.getEmail_validated() == 0
				&& CoinConfig.enableEmailActive()) {
			ServletUtil.writeCommonReply(null, RetCode.USER_NOT_ACTIVE,
					response);
			return;
		}

		// 用户被禁用
		if (userFound.getFrozen() == 1) {
			ServletUtil.writeCommonReply(null, RetCode.USER_STATUS_FREEZE,
					response);
			return;
		}

		if (!userFound.checkPassword(user.getPassword())) {
			ServletUtil.writeCommonReply(null,
					RetCode.USERNAME_OR_PASSWORD_ERROR, response);
			return;
		}
		request.getSession(true).setAttribute("username", user.getEmail());
		log.info("user: " + user.getEmail() + " login, ip address: "
				+ RequestUtil.getRemoteAddress(request));
		// RetCode ret = userServ.login(user.getEmail(),user.getPassword());
		// if (ret == RetCode.OK) {
		// request.getSession(true).setAttribute("username", user.getEmail());
		// log.info("user: " + user.getEmail() + " login, ip address: " +
		// RequestUtil.getRemoteAddress(request));
		// //response.sendRedirect("../index.jsp");
		// }

		// 返回是否实名认证
		boolean hasConfirmRealName = true;
		if (!CoinConfig.isCnVersion()) {
			hasConfirmRealName = userFound.getRealname_validated() == 1;
		}
		ServletUtil.writeCommonReply(hasConfirmRealName, RetCode.OK, response);
	}

	public void reg(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = ServletUtil.readObjectServletQuery(request, User.class);
		UserService userServ = new UserServiceImpl();
		RetCode ret = RetCode.OK;
		if (userServ.exist(user.getEmail())) {
			ret = RetCode.USER_EXIST;
			ServletUtil.writeCommonReply(null, ret, response);
			return;
		} else if (!ValidUtils.emailFormat(user.getEmail())) {
			ret = RetCode.EMAIL_ERROR;
			ServletUtil.writeCommonReply(null, ret, response);
			return;
		}
		user.setCtime(TimeUtil.getUnixTime());
		user.setReg_ip(RequestUtil.getRemoteAddress(request));
		int recommended_user_id = 0;
		try {
			String recommend = request.getParameter("r");
			// String recommend = null;
			if (recommend == null || "null".equals(recommend))
				recommend = (String) request.getSession(true).getAttribute(
						"recommend");
			if (recommend != null) {
				recommended_user_id = Integer.parseInt(recommend);
			}
		} catch (NumberFormatException e) {

		}
		long userId = userServ.createUser(user);

		// 保存推荐人信息
		if (recommended_user_id > 0) {
			GeneralDao<RecommendDetail> rdDao = DaoFactory
					.createGeneralDao(RecommendDetail.class);
			RecommendDetail recommendDetail = new RecommendDetail();
			recommendDetail.setStatus(0);
			recommendDetail.setRecommended_id(recommended_user_id);
			recommendDetail.setUser_id(userId);
			rdDao.save(recommendDetail);
		}

		final User userTemp = user;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					EmailUtil.sendActiveMail(userTemp);
				} catch (EmailException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();

		ServletUtil.writeCommonReply(null, ret, response);
	}
	
	public void activeUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String email = request.getParameter("email");

		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		User user = userDao.find("email", email);
		if (user != null) {
			int emailValidated = user.getEmail_validated();
			if (emailValidated == 0) {
				user.setEmail_validated(1);
				userDao.update(user);
				response.sendRedirect("/html/activate-success.html");
				return;
			} else {
				response.sendRedirect("/html/activate-duplicate.html");
				return;
			}
		}
		response.sendRedirect("/html/activate-failure.html");
	}

	public void chcekregname(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = ServletUtil.readObjectServletQuery(request, User.class);
		UserService userServ = new UserServiceImpl();
		boolean isUserExisted = userServ.exist(user.getEmail());
		RetCode ret = RetCode.OK;
		if (isUserExisted) {
			ret = RetCode.USER_EXIST;
		}
		ServletUtil.writeCommonReply(null, ret, response);
	}

	public void resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GeneralDao<User> dao = DaoFactory.createGeneralDao(User.class);
		String email = StringUtils.defaultString(request.getParameter("email"),
				"");
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			ServletUtil.writeCommonReply(null, RetCode.EMAIL_ERROR, response);
			return;
		}
		UserService userSrv = new UserServiceImpl();
		User user = userSrv.find(email);
		if (user == null) {
			ServletUtil
					.writeCommonReply(null, RetCode.USER_NOT_EXIST, response);
			return;
		}
		// try {
		Timestamp overTime = new Timestamp(
				System.currentTimeMillis() + 15 * 60 * 1000);
		user.setPasswd_modify_overtime(overTime.getTime() / 1000);
		dao.update(user);
		final User tempUser = user;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EmailUtil.sendResetPasswordMail(tempUser);
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		ServletUtil.writeCommonReply(null, RetCode.OK, response);
		// } catch(EmailException e) {
		// e.printStackTrace();
		// ServletUtil.writeCommonReply(null, RetCode.EMAIL_SEND_ERROR,
		// response);
		// }
	}

	public void doResetPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GeneralDao<User> dao = DaoFactory.createGeneralDao(User.class);
		String newPassword = StringUtils.defaultString(
				request.getParameter("newPassword"), "");
		String newPassword2 = StringUtils.defaultString(
				request.getParameter("newPassword2"), "");

		if (newPassword.length() < 6 || newPassword.length() > 16) {
			ServletUtil.writeCommonReply(null, RetCode.PASSWORD_ILLEGAL,
					response);
			return;
		}
		if (!StringUtils.equals(newPassword, newPassword2)) {
			ServletUtil.writeCommonReply(null, RetCode.TWO_PASSWORD_NOT_EQUAL,
					response);
			return;
		}

		UserService userSrv = new UserServiceImpl();
		User user = null;
		long userId = Long.parseLong(StringUtils.defaultString(
				request.getParameter("userId"), "0"));
		String token = StringUtils.defaultString(request.getParameter("token"),
				"");
		if (userId > 0) {
			user = userSrv.get(userId);
			String key = user.getEmail() + "$$"
					+ (user.getPasswd_modify_overtime());
			if (user.getPasswd_modify_overtime() > TimeUtil.getUnixTime()
					&& DigestUtils.sha512Hex(key).equals(token)) {
				user.saveMD5Password(newPassword);
				dao.update(user);
				ServletUtil.writeCommonReply(null, RetCode.OK, response);
				return;
			}
		}
		ServletUtil.writeCommonReply(null, RetCode.ERROR, response);
	}

	public void loginandredirect(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = ServletUtil.readObjectServletQuery(request, User.class);
		UserService userServ = new UserServiceImpl();

		RetCode ret = userServ.login(user.getEmail(), user.getPassword());
		if (ret == RetCode.OK) {
			request.getSession(true).setAttribute("username", user.getEmail());
			response.sendRedirect("../index.jsp");
			return;
		}
		ServletUtil.writeCommonReply(null, ret, response);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession(true).invalidate();
		ServletUtil.writeOkCommonReply(true, response);
	}

	public void loginadmin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AdminUser user = ServletUtil.readObjectServletQuery(request,
				AdminUser.class);
		AdminService adminUserServ = new AdminServiceImpl();

		boolean isok = adminUserServ.login(user.getUsername(),
				user.getPassword());
		if (isok) {
			request.getSession(true).setAttribute("adminusername",
					user.getUsername());
		}

		if (!isok) {
			ServletUtil.writeCommonReply(null,
					RetCode.USERNAME_OR_PASSWORD_ERROR, response);
			return;
		} else {
			ServletUtil.writeCommonReply(null, RetCode.OK, response);
			return;
		}
	}

	public void getLastDealBtcSellList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TradeService tradeServ = new TradeServiceImpl();

		List<Trade> trades = tradeServ.getLastDealBtcSellList();
		ServletUtil.writeOkCommonReply(trades, response);
	}

	public void getLastDealBuySellList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TradeService tradeServ = new TradeServiceImpl();

		List<Trade> trades = tradeServ.getLastDealBtcBuyList();
		ServletUtil.writeOkCommonReply(trades, response);
	}

	public void getLastUnDealBtcSellList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TradeService tradeServ = new TradeServiceImpl();

		List<Trade> trades = tradeServ.getLastUnDealBtcSellList();
		ServletUtil.writeOkCommonReply(trades, response);
	}

	public void getLastUnDealBtcBuyList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TradeService tradeServ = new TradeServiceImpl();

		List<Trade> trades = tradeServ.getLastUnDealBtcBuyList();
		ServletUtil.writeOkCommonReply(trades, response);
	}

	public void getLastDealInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LastDealInfo lastDealInfo = TradeQuery.getLastDealInfo();
		ServletUtil.writeOkCommonReply(lastDealInfo, response);
	}

	public void klineData2(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		String areaKey = "";
		// 五分钟
		if ("1".equals(type)) {
			areaKey = "floor(deal.ctime - deal.ctime % (5 * 60))";
		} else {
			areaKey = "floor(deal.ctime - (deal.ctime % (24 * 60 * 60)))";
		}
		// 开盘查询SQL
		String open_sql = "select price,%s min from deal inner join trade on deal.buy_trade=trade.id inner join (select min(id) id from deal group by %s) b on deal.id=b.id";
		// 最高价查询SQL
		String high_sql = "select max(price) price,%s min from deal inner join trade on deal.buy_trade=trade.id group by %s";
		// 最低价查询SQL
		String lower_sql = "select min(price) price,%s min from deal inner join trade on deal.buy_trade=trade.id  group by %s";
		// 收盘查询SQL
		String close_sql = "select price,%s min from deal inner join trade on deal.buy_trade=trade.id  inner join (select max(id) id from deal group by %s) b on deal.id=b.id";
		// 成交量查询SQL
		String volume_sql = "select sum(amount) amount,ctime,%s min from deal group by %s";

		// 开盘价
		Connection conn = DaoFactory.createConnection();
		QueryRunner runner = new QueryRunner();
		Map<Long, Map<String, Object>> openData = runner.query(conn,
				String.format(open_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> highData = runner.query(conn,
				String.format(high_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> lowData = runner.query(conn,
				String.format(lower_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> closeData = runner.query(conn,
				String.format(close_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> volumeData = runner.query(conn,
				String.format(volume_sql, areaKey, areaKey),
				new KeyedHandler<Long>(3), new Object[] {});
		List<Object[]> ls = new ArrayList<Object[]>();
		if (openData != null && openData.size() > 0) {
			for (Object o : openData.keySet()) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Object open = openData.get(o) == null ? 0L : openData.get(o)
						.get("price");
				Object high = highData.get(o) == null ? 0L : highData.get(o)
						.get("price");
				Object lower = lowData.get(o) == null ? 0L : lowData.get(o)
						.get("price");
				Object close = closeData.get(o) == null ? 0L : closeData.get(o)
						.get("price");
				Object volume = volumeData.get(o) == null ? 0L : volumeData
						.get(o).get("amount");
				Object[] obj = new Object[] { (Long) o * 1000, open, high,
						lower, close, volume };
				ls.add(obj);

				// System.out.println(sdf.format(new Date((Long)obj[0])) + " " +
				// obj[1] + " " + obj[2] + " " + obj[3] + " " + obj[4]);
			}
		}

		// 按时间排序
		Collections.sort(ls, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return (int) ((Long) o1[0] - (Long) o2[0]);
			}

		});
		ServletUtil.writeOkCommonReply(ls, response);
	}

	public void klineData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		String areaKey = "";
		// 五分钟
		if ("1".equals(type)) {
			areaKey = "floor(deal.ctime - deal.ctime % (5 * 60))";
		} else {
			areaKey = "floor(deal.ctime - (deal.ctime % (24 * 60 * 60)))";
		}
		// 开盘查询SQL
		// String open_sql =
		// "select price,%s min from deal inner join trade on deal.buy_trade=trade.id inner join (select min(id) id from deal group by %s) b on deal.id=b.id";
		String open_sql = "select price as price,%s min from deal join (select min(id) id from deal group by %s)b on deal.id=b.id order by min ";
		// 最高价查询SQL
		// String high_sql =
		// "select max(price) price,%s min from deal inner join trade on deal.buy_trade=trade.id group by %s";
		String high_sql = "select max(price) price,%s min from deal group by %s";
		// 最低价查询SQL
		// String lower_sql =
		// "select min(price) price,%s min from deal inner join trade on deal.buy_trade=trade.id  group by %s";
		String lower_sql = "select min(price) price,%s min from deal  group by %s";
		// 收盘查询SQL
		// String close_sql =
		// "select price,%s min from deal inner join trade on deal.buy_trade=trade.id  inner join (select max(id) id from deal group by %s) b on deal.id=b.id";
		String close_sql = "select price as price,%s min from deal join (select max(id) id from deal group by %s)b on deal.id=b.id order by min";
		// 成交量查询SQL
		String volume_sql = "select sum(amount) amount,ctime,%s min from deal group by %s";

		// 开盘价
		Connection conn = DaoFactory.createConnection();
		QueryRunner runner = new QueryRunner();
		Map<Long, Map<String, Object>> openData = runner.query(conn,
				String.format(open_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> highData = runner.query(conn,
				String.format(high_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> lowData = runner.query(conn,
				String.format(lower_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> closeData = runner.query(conn,
				String.format(close_sql, areaKey, areaKey),
				new KeyedHandler<Long>(2), new Object[] {});
		Map<Long, Map<String, Object>> volumeData = runner.query(conn,
				String.format(volume_sql, areaKey, areaKey),
				new KeyedHandler<Long>(3), new Object[] {});
		List<Object[]> ls = new ArrayList<Object[]>();
		if (openData != null && openData.size() > 0) {
			for (Object o : openData.keySet()) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Object open = openData.get(o) == null ? 0L : openData.get(o)
						.get("price");
				Object high = highData.get(o) == null ? 0L : highData.get(o)
						.get("price");
				Object lower = lowData.get(o) == null ? 0L : lowData.get(o)
						.get("price");
				Object close = closeData.get(o) == null ? 0L : closeData.get(o)
						.get("price");
				Object volume = volumeData.get(o) == null ? 0L : volumeData
						.get(o).get("amount");
				Object[] obj = new Object[] { (Long) o * 1000, open, high,
						lower, close, volume };
				ls.add(obj);

				// System.out.println(sdf.format(new Date((Long)obj[0])) + " " +
				// obj[1] + " " + obj[2] + " " + obj[3] + " " + obj[4]);
			}
		}

		// 按时间排序
		Collections.sort(ls, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return (int) ((Long) o1[0] - (Long) o2[0]);
			}

		});
		ServletUtil.writeOkCommonReply(ls, response);
	}

	public void payNotify(HttpServletRequest request,
			HttpServletResponse response) {
		Log log = LogFactory.getLog("dinpay");
		log.info("jsp pay notify start!!!");
		/* *
		 * 功能：智付页面跳转同步通知页面 版本：3.0 日期：2013-08-01 说明：
		 * 以下代码仅为了方便商户安装接口而提供的样例具体说明以文档为准，商户可以根据自己网站的需要，按照技术文档编写。
		 */
		// 获取智付反馈的信息
		// 商户号
		String merchant_code = request.getParameter("merchant_code");

		// 通知类型
		String notify_type = request.getParameter("notify_type");

		// 通知校验ID
		String notify_id = request.getParameter("notify_id");

		// 接口版本
		String interface_version = request.getParameter("interface_version");

		// 签名方式
		String sign_type = request.getParameter("sign_type");

		// 签名
		String dinpaySign = request.getParameter("sign");

		// 商家订单号
		String order_no = request.getParameter("order_no");

		// 商家订单时间
		String order_time = request.getParameter("order_time");

		// 商家订单金额
		String order_amount = request.getParameter("order_amount");

		// 回传参数
		String extra_return_param = request.getParameter("extra_return_param");

		// 智付交易定单号
		String trade_no = request.getParameter("trade_no");

		// 智付交易时间
		String trade_time = request.getParameter("trade_time");

		// 交易状态 SUCCESS 成功 FAILED 失败
		String trade_status = request.getParameter("trade_status");

		// 银行交易流水号
		String bank_seq_no = request.getParameter("bank_seq_no");

		/**
		 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推， 同时将商家支付密钥key放在最后参与签名，组成规则如下：
		 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
		 **/

		// 组织订单信息
		StringBuilder signStr = new StringBuilder();
		if (null != bank_seq_no && !bank_seq_no.equals("")) {
			signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
		}
		if (null != extra_return_param && !extra_return_param.equals("")) {
			signStr.append("extra_return_param=").append(extra_return_param)
					.append("&");
		}
		signStr.append("interface_version=V3.0").append("&");
		signStr.append("merchant_code=").append(merchant_code).append("&");
		if (null != notify_id && !notify_id.equals("")) {
			signStr.append("notify_id=").append(notify_id)
					.append("&notify_type=").append(notify_type).append("&");
		}

		signStr.append("order_amount=").append(order_amount).append("&");
		signStr.append("order_no=").append(order_no).append("&");
		signStr.append("order_time=").append(order_time).append("&");
		signStr.append("trade_no=").append(trade_no).append("&");
		signStr.append("trade_status=").append(trade_status).append("&");
		if (null != trade_time && !trade_time.equals("")) {
			signStr.append("trade_time=").append(trade_time).append("&");
		}
		String key = "guanjiande13005533509";
		signStr.append("key=").append(key);
		String signInfo = signStr.toString();

		String retString = "";
		String msgString = "";
		String payheader = "order_no=" + order_no + ",order_amount="
				+ order_amount + ",order_time=" + order_time + " ";

		// 将组装好的信息MD5签名
		String sign = DigestUtils.md5Hex(signInfo); // 注意与支付签名不同 此处对String进行加密
		// 比较智付返回的签名串与商家这边组装的签名串是否一致
		if (dinpaySign == null) {
			msgString = payheader + "签名为空，请联系管理员";
			log.error(msgString);
			log.info("jsp pay notify complete!!!");
			return;
		}
		if (dinpaySign.equals(sign)) {
			// 验签成功
			/**
			 * 此处进行商户业务操作 业务结束
			 */

			CashService cashServ = new CashServiceImpl();
			if (order_no == null) {
				msgString = payheader + "支付id为空，请联系管理员";
				log.error(msgString);
				log.info("jsp pay notify complete!!!");
				return;
			}
			int cashId = Integer.parseInt(order_no);
			Cash cash = cashServ.getRecharge(cashId);
			if (cash.getAmount() != Double.parseDouble(order_amount)) {
				msgString = payheader + "支付金额和订单金额不匹配，请联系管理员";
				log.error(msgString);
				log.info("jsp pay notify complete!!!");
				return;
			}
			RetCode ret = cashServ.doRecharge(cashId, -1);// 支付确认的订单都是-1的管理员id
			if (ret != RetCode.OK && ret != RetCode.ORDER_CONFIRM_YET) {
				msgString = payheader + "订单支付确认失败，请联系管理员 ";
				log.error(msgString);
				return;
			}
			msgString = payheader + "订单支付成功 ";
			log.error(msgString);
			log.info("jsp pay notify complete!!!");
			try {
				response.getWriter().print("SUCCESS");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;

		} else {

			// 验签失败 业务结束
			msgString = payheader + "验签失败 ";
			log.error(msgString);
			log.info("jsp pay notify complete!!!");
			return;
		}
	}
	public void payReturn4HuiChao(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Log log = LogFactory.getLog("huichao");
		log.info("jsp pay return start!!!");
		String CharacterEncoding = "UTF-8";
		request.setCharacterEncoding(CharacterEncoding);
		String BillNo = request.getParameter("BillNo");
		String Amount = request.getParameter("Amount");
		String tradeOrder = request.getParameter("tradeOrder");
		String Succeed = request.getParameter("Succeed");
		String Result = request.getParameter("Result");
		String SignMD5info = request.getParameter("SignMD5info");
		String MD5key = Config.getString("pay.md5.key");
		String md5src = BillNo + "&" + Amount + "&" + Succeed + "&" + MD5key;
		String md5sign; // MD5加密后的字符串
		md5sign = MD5Util.md5(md5src);// MD5检验结果

		String msgString = "";
		String payheader = "BillNo=" + BillNo + ",Amount=" + Amount;
		if (SignMD5info == null) {
			msgString = "签名为空，请联系管理员";
			log.error(msgString);
			response.getWriter().print(msgString);
			log.info("jsp pay return complete!!!");
			return;
		}
		if (!md5sign.equals(SignMD5info)) {
			// 验签成功
			CashService cashServ = new CashServiceImpl();
			if (BillNo == null) {
				msgString = payheader + "支付id为空，请联系管理员";
				log.error(msgString);
				log.info("jsp pay return complete!!!");
				return;
			}
			int cashId = Integer.parseInt(BillNo);
			Cash cash = cashServ.getRecharge(cashId);
			if (cash.getAmount() != Double.parseDouble(Amount)) {
				msgString = payheader + "支付金额和订单金额不匹配，请联系管理员";
				log.error(msgString);
				response.getWriter().print(msgString);
				log.info("jsp pay return complete!!!");
				return;
			}
			RetCode ret = cashServ.doRecharge(cashId, -1);// 支付确认的订单都是-1的管理员id
			if (ret != RetCode.OK && ret != RetCode.ORDER_CONFIRM_YET) {
				msgString = payheader + "订单支付确认失败，请联系管理员 ";
				log.error(msgString);
				response.getWriter().print(msgString);
				return;
			}
			msgString = payheader + "订单支付成功 ";
			log.error(msgString);
			log.info("jsp pay return complete!!!");
			try {
				//response.getWriter().print("SUCCESS");
				response.sendRedirect("/rechargeCny.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;

		} else {

			// 验签失败 业务结束
			msgString = payheader + "验签失败 ";
			log.error(msgString);
			response.getWriter().print(msgString);
			log.info("jsp pay return complete!!!");
			return;
		}
	}
	
	public void payNotify4HuiChao(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Log log = LogFactory.getLog("huichao");
		log.info("jsp pay notify start!!!");
		String CharacterEncoding = "UTF-8";
		request.setCharacterEncoding(CharacterEncoding);
		String BillNo = request.getParameter("BillNo");
		String Amount = request.getParameter("Amount");
		String tradeOrder = request.getParameter("tradeOrder");
		String Succeed = request.getParameter("Succeed");
		String Result = request.getParameter("Result");
		String SignMD5info = request.getParameter("SignMD5info");
		String MD5key = Config.getString("pay.md5.key");
		String md5src = BillNo + "&" + Amount + "&" + Succeed + "&" + MD5key;
		String md5sign; // MD5加密后的字符串
		md5sign = MD5Util.md5(md5src).toUpperCase();// MD5检验结果

		String msgString = "";
		String payheader = "BillNo=" + BillNo + ",Amount=" + Amount;
		if (SignMD5info == null) {
			msgString = "签名为空，请联系管理员";
			log.error(msgString);
			response.getWriter().print(msgString);
			log.info("jsp pay notify complete!!!");
			return;
		}
		if (md5sign.equals(SignMD5info)) {
			// 验签成功
			CashService cashServ = new CashServiceImpl();
			if (BillNo == null) {
				msgString = payheader + "支付id为空，请联系管理员";
				log.error(msgString);
				log.info("jsp pay return complete!!!");
				return;
			}
			int cashId = Integer.parseInt(BillNo);
			Cash cash = cashServ.getRecharge(cashId);
			if (cash.getAmount() != Double.parseDouble(Amount)) {
				msgString = payheader + "支付金额和订单金额不匹配，请联系管理员";
				log.error(msgString);
				response.getWriter().print(msgString);
				log.info("jsp pay notify complete!!!");
				return;
			}
			RetCode ret = cashServ.doRecharge(cashId, -1);// 支付确认的订单都是-1的管理员id
			if (ret != RetCode.OK && ret != RetCode.ORDER_CONFIRM_YET) {
				msgString = payheader + "订单支付确认失败，请联系管理员 ";
				log.error(msgString);
				response.getWriter().print(msgString);
				return;
			}
			msgString = payheader + "订单支付成功 ";
			log.error(msgString);
			log.info("jsp pay notify complete!!!");
			try {
				response.getWriter().print("ok");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;

		} else {

			// 验签失败 业务结束
			msgString = payheader + "验签失败 ";
			log.error(msgString);
			response.getWriter().print(msgString);
			log.info("jsp pay notify complete!!!");
			return;
		}
	}

	/**
	 * 分页查询认购
	 * 
	 * @throws Exception
	 */
	public void getSubscribePage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SubscribeService subscribeSrv = new SubscribeServiceImpl();
		Page page = ServletUtil.getPage(request);
		PageReply<Subscribe2User> subscribes = subscribeSrv
				.getSubscribePage(page);
		ServletUtil.writeOkCommonReply(subscribes, response);
	}
}
