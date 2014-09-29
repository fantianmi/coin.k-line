package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Cash2User;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.task.PayRecommendTask;
import com.bkl.Kcoin.task.SystemTaskPool;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.bkl.Kcoin.utils.PayUtil;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.DbUtil;
import com.km.common.utils.PrintUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class CashServiceImpl implements com.bkl.Kcoin.service.CashService {
	GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class);

	GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);

	public long saveRecharge(Cash cash) {
		if (cash.getAmount() == null ) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_OR_PRICE_NULL);
			return 0;
		}
		if (cash.getAmount() < CoinConfig.getCashMinRechargeAmount()) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_THAN_LIMIT);
			return 0;
		}
		if (DoubleUtil.exceedPrecision(cash.getAmount(), CoinConfig.getCashAmountMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		cash.setCtime(TimeUtil.getUnixTime());
		cash.setType(Cash.TYPE_RMB_RECHARGE);
		cash.setStatus(Cash.STATUS_UNCONFIRM);
		return cashDao.save(cash);
	}
	
	public long updateRecharge(Cash cash,long user_id) {
		Condition userCon = DbUtil.generalEqualWhere("user_id", user_id);
		return cashDao.update(cash,new Condition[] {userCon});
	}
	
	public List<Cash> getRechargeList(long user_id) {
		Condition userCon = DbUtil.generalEqualWhere("user_id", user_id);
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_RECHARGE);
		List<Cash> recharges = cashDao.findList(new Condition[] { userCon, typeCon }, new String[] { "ctime desc" });
		return recharges;
	}
	
	public Cash getRecharge(long cashId) {
		Condition userCon = DbUtil.generalEqualWhere("id", cashId);
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_RECHARGE);
		Cash recharge = cashDao.find(new Condition[] { userCon, typeCon }, new String[] { "ctime desc" });
		return recharge;
	}
	
	public PageReply<Cash> getRechargePage(Page page,long user_id) {
		Condition userCon = DbUtil.generalEqualWhere("user_id", user_id);
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_RECHARGE);
		PageReply<Cash> recharges = cashDao.getPage(page,new Condition[] { userCon, typeCon }, new String[] { "ctime desc" });
		return recharges;
	}
	
	public List<Cash2User> getRecharge2UserList() {
		GeneralDao<Cash2User> cash2UserDao = DaoFactory.createGeneralDao(Cash2User.class);
		String sql = "select r.id,r.user_id,r.name,r.bank,r.card,r.amount,r.ctime,r.status,r.type,u.email,u.name as realName from `cash` r,`user` u where  r.user_id=u.id and r.type=? order by ctime desc";
		List<Cash2User> recharges = cash2UserDao.findListBySQL(sql, Cash.TYPE_RMB_RECHARGE);
		return recharges;
	}

	@Override
	public PageReply<Cash> getRechargeList(Map searchMap, Page page) {
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_RECHARGE);
		PageReply<Cash> recharges = cashDao.getPage(page, new Condition[] { typeCon }, new String[]{"ctime desc"}, searchMap);
		return recharges;
	}

	public PageReply<Cash2User> getRecharge2UserPage(Map searchMap, Page page) {
		GeneralDao<Cash2User> cash2UserDao = DaoFactory.createGeneralDao(Cash2User.class);
		String sql = "select r.id,r.user_id,r.name,r.bank,r.card,r.amount,r.ctime,r.status,r.type,r.fin_type,u.email,u.name as realName from `cash` r,`user` u where  r.user_id=u.id and r.type=1 order by  ctime desc";
		PageReply<Cash2User> recharges = cash2UserDao.getPage(sql,page, searchMap);
		return recharges;
	}
	
	public PageReply<Cash2User> getWithdraw2UserPage(Map searchMap, Page page) {
		GeneralDao<Cash2User> cash2UserDao = DaoFactory.createGeneralDao(Cash2User.class);
		String sql = "select r.id,r.user_id,r.name,r.bank,r.card,r.amount,r.ctime,r.status,r.type,r.fin_type,r.bank_number,r.mobile,u.email from `cash` r,`user` u where  r.user_id=u.id and r.type=2 order by r.status,ctime desc";
		PageReply<Cash2User> withdraw2Users = cash2UserDao.getPage(sql,page, searchMap);
		return withdraw2Users;
	}
	@Override
	public PageReply<Cash> getWithdrawList(Map searchMap, Page page) {
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_WITHDRAW);
		PageReply<Cash> withdraws = cashDao.getPage(page, new Condition[] { typeCon },new String[]{"ctime desc"}, searchMap);
		return withdraws;
	}

	@Override
	public RetCode doRecharge(long rechargeId, int adminId) {
		Connection conn = null;
		try {
			conn = DaoFactory.createConnection();
			GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
			GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class,conn);
			UserBillService userBillServ = new UserBillServiceImpl(conn);
			SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
			Cash cash = cashDao.find(rechargeId);
			if (cash == null || cash.getType() != Cash.TYPE_RMB_RECHARGE) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (cash.getStatus() != Cash.STATUS_UNCONFIRM){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Cash.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
			
			cash = cashDao.find(rechargeId);
			if (cash == null || cash.getType() != Cash.TYPE_RMB_RECHARGE) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (cash.getStatus() != Cash.STATUS_UNCONFIRM){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			User user = userDao.find("id", cash.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double userAmount = user.getRmb();
			double newUserAmout = DoubleUtil.add(userAmount, cash.getAmount());
			user.setRmb(newUserAmout);
			userDao.save(user);
			cash.setAdmin_id(adminId);
			cash.setStatus(Cash.STATUS_CONFIRM);
			cashDao.save(cash);
			
			userBillServ.doRecharge(cash,user);
			systemBillServ.doRecharge(cash,user);
			
			userDao.unLockTable();
			userDao.commit();
			
			SystemTaskPool.add(new PayRecommendTask(cash.getUser_id()));
			return RetCode.OK;
	} catch (Exception e) {
		e.printStackTrace();
		//userDao.rollback();
		throw new RuntimeException(e);
	} finally {
		DaoFactory.closeConnection(conn);
	}
}

	public long saveWithdraw(Cash cash) {
		if (cash.getAmount() == null ) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_OR_PRICE_NULL);
			return 0;
		}
		if (cash.getAmount() < CoinConfig.getCashMinWithdrawAmount()) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_THAN_LIMIT);
			return 0;
		}
		if (DoubleUtil.exceedPrecision(cash.getAmount(), CoinConfig.getCashAmountMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		
		Connection conn = null;
		try {
		conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class,conn);
		SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
		
		userDao.beginTransaction();
		userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Cash.class), userDao.getTableName(BillDetail.class));
		
		User user = userDao.find(cash.getUser_id());
		if (user.getRmb() < cash.getAmount()) {
			Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
			return 0;
		}
		
		double userAccountAmount = user.getRmb();
		double userFrozenAccountAmount = user.getRmb_frozen();
		user.setRmb(DoubleUtil.subtract(userAccountAmount, cash.getAmount()));
		user.setRmb_frozen(DoubleUtil.add(userFrozenAccountAmount, cash.getAmount()));
		
		cash.setCtime(TimeUtil.getUnixTime());
		cash.setStatus(Cash.STATUS_UNCONFIRM);

		cash.setType(Cash.TYPE_RMB_WITHDRAW);
		
		userDao.save(user);
		long ret = cashDao.save(cash);
		systemBillServ.saveWithdraw(cash, user);
		userDao.unLockTable();
		userDao.commit();
		return ret;
		}catch (Exception e) {
			//userDao.rollback();
			throw new RuntimeException(e);
		}
		finally {
			DaoFactory.closeConnection(conn);
		}
	}

	public List<Cash> getWithdrawList(long user_id) {
		Condition userCon = DbUtil.generalEqualWhere("user_id", user_id);
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_WITHDRAW);
		List<Cash> withdraws = cashDao.findList(new Condition[] { userCon, typeCon }, new String[] { "ctime desc" });
		return withdraws;
	}
	public List<Cash> getLast20WithdrawList(long user_id) {
		Condition userCon = DbUtil.generalEqualWhere("user_id", user_id);
		Condition typeCon = DbUtil.generalEqualWhere("type", Cash.TYPE_RMB_WITHDRAW);
		List<Cash> withdraws = cashDao.findList(new Condition[] { userCon, typeCon }, new String[] { "ctime desc" }, 20);
		return withdraws;
	}
	public List<Cash2User> getWithdraw2UserList() {
		GeneralDao<Cash2User> cash2UserDao = DaoFactory.createGeneralDao(Cash2User.class);
		String sql = "select r.id,r.user_id,r.name,r.bank,r.card,r.amount,r.ctime,r.status,r.type,u.email from `cash` r,`user` u where  r.user_id=u.id and r.type=? order by ctime desc";
		List<Cash2User> withdraw2Users = cash2UserDao.findListBySQL(sql, Cash.TYPE_RMB_WITHDRAW);
		return withdraw2Users;
	}

	public RetCode doWithdraw(long withdrawId,int adminId) {
		Connection conn = null;
		try {
			conn = DaoFactory.createConnection();
			GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
			GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class,conn);
			SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
			UserBillService userBillServ = new UserBillServiceImpl(conn);
			
			Cash cash = cashDao.find(withdrawId);
			if (cash == null  || cash.getType() != Cash.TYPE_RMB_WITHDRAW) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (cash.getStatus() == Cash.STATUS_CONFIRM){
				return RetCode.ORDER_CONFIRM_YET;
			}
			if (cash.getStatus() == Cash.STATUS_CANCEL){
				return RetCode.ORDER_CONFIRM_CANCEL;
			}
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Cash.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
			
			User user = userDao.find(cash.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double orderAmount = cash.getAmount();
			double newUserFrozenAmout =DoubleUtil.subtract(user.getRmb_frozen(), orderAmount);
			if (newUserFrozenAmout < 0) {
				return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
			}
			user.setRmb_frozen(newUserFrozenAmout);
			
			long ret = userDao.save(user);
			cash.setStatus(Cash.STATUS_CONFIRM);
			cash.setAdmin_id(adminId);
			ret = cashDao.save(cash);
			userBillServ.doWithdraw(cash,user);
			systemBillServ.doWithdraw(cash,user);
			
			userDao.commit();
			return RetCode.OK;
		} catch (Exception e) {
			//userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
		
	}
	
	public RetCode doCancelWithdraw(long withdrawId,int adminId) {
		Connection conn = null;
		try {
			conn = DaoFactory.createConnection();
			GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
			GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class,conn);
			SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
			UserBillService userBillServ = new UserBillServiceImpl(conn);


			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Cash.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
			
			Cash cash = cashDao.find(withdrawId);
			if (cash == null  || cash.getType() != Cash.TYPE_RMB_WITHDRAW) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (cash.getStatus() == Cash.STATUS_CONFIRM){
				return RetCode.ORDER_CONFIRM_YET;
			}
			if (cash.getStatus() == Cash.STATUS_CANCEL){
				return RetCode.ORDER_CONFIRM_CANCEL;
			}
			
			User user = userDao.find(cash.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double orderAmount = cash.getAmount();
			double newUserFrozenAmout = DoubleUtil.subtract(user.getRmb_frozen(), orderAmount);
			if (newUserFrozenAmout < 0) {
				return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
			}
			
			user.setRmb_frozen(newUserFrozenAmout);
			double newUserAmout = DoubleUtil.add(user.getRmb(), orderAmount);
			user.setRmb(newUserAmout);
			userDao.save(user);
			
			cash.setStatus(Cash.STATUS_CANCEL);
			cash.setAdmin_id(adminId);
			cashDao.save(cash);
			
//			userBillServ.doCancelWithdraw(cash,user);
			systemBillServ.cancelWithdraw(cash,user);
			
			userDao.commit();
			return RetCode.OK;
		} catch (Exception e) {
			//userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	
	public static void main(String[] args) {
		CashServiceImpl cashServ = new CashServiceImpl();
		RetCode ret = cashServ.doRecharge(4,-1);
		System.out.println(ret);
	}
}
