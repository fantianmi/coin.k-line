package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.Transfer2User;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.CoinService;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.bkl.Kcoin.utils.PayUtil;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.DbUtil;
import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class BtcCoinServiceImpl implements CoinService {
	GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class);
	GeneralDao<Transfer2User> transfer2UserDao = DaoFactory.createGeneralDao(Transfer2User.class);
	GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
	
	@Override
	public Transfer getTransfer(long id) {
		return transferDao.find(id);
	}

	@Override
	public long saveTransfer(Transfer transfer) {
		return transferDao.save(transfer);
	}
	
	@Override
	public long saveCoinWithdraw(Transfer transfer) {
		
		if (transfer.getAmount() < CoinConfig.getCoinMinWithdrawAmount()) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_LESS_THAN_LIMIT);
			return 0;
		}
		if (DoubleUtil.exceedPrecision(transfer.getAmount(), CoinConfig.getCoinAmountMinDecimalPrecision())) {
			Config.setRetCode(RetCode.ORDER_AMOUNT_DECIMAL_PRECISION_EXCEED);
			return 0;
		}
		
		
		Connection conn = DaoFactory.createConnection();
		SystemBillService systemBillSrv = new SystemBillServiceImpl(conn);
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class, conn);
		GeneralDao<Transfer> tDao = DaoFactory.createGeneralDao(Transfer.class, conn);
		long userId = transfer.getUser_id();
		
		
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Transfer.class), userDao.getTableName(BillDetail.class));
			User user = userDao.find(userId);
			
			if (user.getBtc() < transfer.getAmount()) {
				Config.setRetCode(RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH);
				return 0;
			}
			//冻结提现资金
			double userBtcAmount = user.getBtc();
			double userBtcFrozenAmount = user.getBtc_frozen();
			user.setBtc(DoubleUtil.subtract(userBtcAmount, transfer.getAmount()));
			user.setBtc_frozen(DoubleUtil.add(userBtcFrozenAmount, transfer.getAmount()));
			
			userDao.save(user);
			long transferId = tDao.save(transfer);
			systemBillSrv.saveBtcTransferOut(transfer, user);
			userDao.unLockTable();
			userDao.commit();
			return transferId;
		} catch (Exception e) {
			userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	
	public RetCode cancelWithdraw(long transferid,int admin_id) {
		Connection conn = DaoFactory.createConnection();
		SystemBillService systemBillSrv = new SystemBillServiceImpl(conn);
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class, conn);
		GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class, conn);
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Transfer.class), userDao.getTableName(BillDetail.class));
			Transfer transfer = transferDao.find(transferid);
			if (transfer == null  || transfer.getType() != 2) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			
			if (transfer.getStatus() != 0){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			
			User user = userDao.find(transfer.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			
			double orderAmount = transfer.getAmount();
			double userBtcAmount = user.getBtc();
			double userBtcFrozenAmount = user.getBtc_frozen();
			user.setBtc(DoubleUtil.add(userBtcAmount,orderAmount));
			user.setBtc_frozen(DoubleUtil.subtract(userBtcFrozenAmount, orderAmount));
			if (user.getBtc_frozen() < 0) {
				return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
			}
			
			userDao.save(user);
			
			transfer.setStatus(2);
			transfer.setAdmin_id(admin_id);
			transferDao.save(transfer);
			systemBillSrv.cancelBtcTransferOut(transfer, user);
			userDao.unLockTable();
			userDao.commit();
			return RetCode.OK;
		} catch (Exception e) {
			userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	
	public RetCode cancelCoinWithdraw(long transferid) {
		Connection conn = DaoFactory.createConnection();
		SystemBillService systemBillSrv = new SystemBillServiceImpl(conn);
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class, conn);
		GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class, conn);
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Transfer.class), userDao.getTableName(BillDetail.class));
			Transfer transfer = transferDao.find(transferid);
			if (transfer == null  || transfer.getType() != 2) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			
			if (transfer.getStatus() != 0){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			
			User user = userDao.find(transfer.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			
			double orderAmount = transfer.getAmount();
			double userBtcAmount = user.getBtc();
			double userBtcFrozenAmount = user.getBtc_frozen();
			user.setBtc(DoubleUtil.add(userBtcAmount,orderAmount));
			user.setBtc_frozen(DoubleUtil.subtract(userBtcFrozenAmount, orderAmount));
			if (user.getBtc_frozen() < 0) {
				return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
			}
			
			userDao.save(user);
			
			transfer.setStatus(2);
			transferDao.save(transfer);
			systemBillSrv.cancelBtcTransferOut(transfer, user);
			userDao.unLockTable();
			userDao.commit();
			return RetCode.OK;
		} catch (Exception e) {
			userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	
	@Override
	public PageReply<Transfer2User> getTransferOutPage(Map searchMap, Page page) {
		String tableSql = "select t.id,t.address,t.txid,t.amount,t.ctime,t.type,t.status,u.email  from `transfer` t,`user` u where  t.user_id=u.id  and type=2 order by t.ctime desc";
		return transfer2UserDao.getPage(tableSql, page, searchMap);
	}

	@Override
	public PageReply<Transfer2User> getTransferInPage(Map searchMap, Page page) {
		String tableSql = "select t.id,t.address,t.txid,t.amount,t.ctime,t.type,t.status,u.email  from `transfer` t,`user` u where  t.user_id=u.id  and type=1 order by t.ctime desc";
		return transfer2UserDao.getPage(tableSql, page, searchMap);
	}

	@Override
	public RetCode confirmCoinIn(long id,int adminId) {
		Connection conn = null;
		try {
			conn = DaoFactory.createConnection();
			GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
			GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class,conn);
			UserBillService userBillServ = new UserBillServiceImpl(conn);
			SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
			
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Transfer.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
			
			Transfer transfer = transferDao.find(id);
			if (transfer == null || transfer.getType() != Transfer.TYPE_BTC_RECHARGE) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (transfer.getStatus() != Transfer.STATUS_NORMAL){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			User user = userDao.find("id", transfer.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double userAmount = user.getBtc();
			double newUserAmout = DoubleUtil.add(userAmount, transfer.getAmount());
			user.setBtc(newUserAmout);
			userDao.save(user);
			transfer.setAdmin_id(adminId);
			transfer.setStatus(Transfer.STATUS_CONFIRM);
			transferDao.save(transfer);
			
			userBillServ.doBtcTransferIn(transfer, user);
			systemBillServ.doBtcTransferIn(transfer, user);
			
			userDao.unLockTable();
			userDao.commit();
			
			return RetCode.OK;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}

	@Override
	public RetCode confirmCoinOut(long id,int adminId) {
		Connection conn = null;
		try {
			conn = DaoFactory.createConnection();
			GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
			GeneralDao<Transfer> transferDao = DaoFactory.createGeneralDao(Transfer.class,conn);
			SystemBillService systemBillServ = new SystemBillServiceImpl(conn);
			UserBillService userBillServ = new UserBillServiceImpl(conn);
			
			Transfer transfer = transferDao.find(id);
			if (transfer == null  || transfer.getType() != 2) {
				return RetCode.ORDER_NOTALLOW_ID_NULL;
			}
			if (transfer.getStatus() != 0){
				return RetCode.ORDER_CONFIRM_YET;
			}
			
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Transfer.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
			
			User user = userDao.find(transfer.getUser_id());
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			double orderAmount = transfer.getAmount();
			double newUserFrozenAmout =DoubleUtil.subtract(user.getBtc_frozen(), orderAmount);
			if (newUserFrozenAmout < 0) {
				return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
			}
			user.setBtc_frozen(newUserFrozenAmout);
			
			long ret = userDao.save(user);
			transfer.setStatus(1);
			transfer.setAdmin_id(adminId);
			ret = transferDao.save(transfer);
			userBillServ.doBtcTransferOut(transfer, user);
			systemBillServ.doBtcTransferOut(transfer,user);
			
			userDao.commit();
			return RetCode.OK;
		} catch (Exception e) {
			//userDao.rollback();
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}

	@Override
	public List<Transfer> getTransferInTop10(long userId) {
		Condition typeCon = DbUtil.generalEqualWhere("type", Transfer.TYPE_BTC_RECHARGE);
		Condition userCon = DbUtil.generalEqualWhere("user_id", userId);
		return transferDao.findList(new Condition[] { typeCon, userCon }, new String[] { "ctime desc" }, 10);
	}

	@Override
	public List<Transfer> getTransferOutTop10(long userId) {
		Condition typeCon = DbUtil.generalEqualWhere("type", Transfer.TYPE_BTC_WITHDRAW);
		Condition userCon = DbUtil.generalEqualWhere("user_id", userId);
		return transferDao.findList(new Condition[] { typeCon, userCon }, new String[] { "ctime desc" }, 10);
	}

	public static void main(String[] args) {
		BtcCoinServiceImpl transferServ = new BtcCoinServiceImpl();
		Transfer transfer = new Transfer();
		transfer.setAmount(100);
		transfer.setUser_id(1);
		transfer.setType(Transfer.TYPE_BTC_RECHARGE);
		//transferServ.saveTransfer(transfer);
		
		RetCode ret = transferServ.confirmCoinIn(2, -1);
		System.out.println(ret);
	}
}
