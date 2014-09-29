package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.List;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.ExtraCoinAdjust;
import com.bkl.Kcoin.entity.ExtraRmbAdjust;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.ExtraService;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.utils.FrontUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Condition;

public class ExtraServiceImpl implements ExtraService {

	GeneralDao<ExtraCoinAdjust> coinExtraDao = DaoFactory.createGeneralDao(ExtraCoinAdjust.class);
	
	public ExtraServiceImpl() {
		coinExtraDao = DaoFactory.createGeneralDao(ExtraCoinAdjust.class);
	}
	
	public ExtraServiceImpl(Connection conn) {
		coinExtraDao = DaoFactory.createGeneralDao(ExtraCoinAdjust.class,conn);
	}
	
	public double getTotalBtcExtraAmount() {
		return getTotalBtcExtraAmount(coinExtraDao);
	}
	
	private long saveExtraRmbAjust(double totalRmb, double totalBtcExtra,GeneralDao dao) {
		ExtraRmbAdjust extraRmbAjust = new ExtraRmbAdjust();
		extraRmbAjust.setCtime((int)TimeUtil.getUnixTime());
		extraRmbAjust.setBtc_extra(totalBtcExtra);
		extraRmbAjust.setRmb(totalRmb);
		return dao.save(extraRmbAjust);
	}
	
	private  double getTotalBtcExtraAmount(GeneralDao dao) {
		String sql = "select sum(btc_extra) from user";
		return dao.queryDouble(sql);
	}
	
	@Override
	public void adjustUserExtraCoin() {
		Connection conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class),userDao.getTableName(ExtraCoinAdjust.class));
			
			String sql = "update user set btc_extra=btc_extra + btc*" + FrontUtil.formatDouble(CoinConfig.getDefaultCoinExtraRate()) ;
			userDao.exec(sql);
			sql = "insert into extra_coin_ajust (ctime,user_id,btc,btc_extra,btc_amount) select UNIX_TIMESTAMP(),id,btc,btc_extra,btc*%s from user";
			sql = String.format(sql, FrontUtil.formatDouble(CoinConfig.getDefaultCoinExtraRate()));
			userDao.exec(sql);
			
			userDao.unLockTable();
			userDao.commit();
		} catch (Exception e) {
			userDao.rollback();
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
		finally {
			DaoFactory.closeConnection(conn);
			
		}
	}
	
	public void adjustUserMoneyByExtraCoin(double totalRmb) {
		Connection conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		SystemBillService systemBilServ = new SystemBillServiceImpl(conn);
		UserBillService userBillServ = new UserBillServiceImpl(conn);
    	
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class),userDao.getTableName(ExtraRmbAdjust.class),userDao.getTableName(Bill.class),userDao.getTableName(BillDetail.class));
			double totalBtcExtra = getTotalBtcExtraAmount(userDao);
			saveExtraRmbAjust(totalRmb, totalBtcExtra, userDao);
			String sql = "update user set rmb=rmb + btc_extra*%s/%s";
			sql = String.format(sql, FrontUtil.formatDouble(totalRmb),FrontUtil.formatDouble(totalBtcExtra));
					
			userDao.exec(sql);
			userBillServ.adjustUserMoneyByExtraCoin(totalBtcExtra, totalRmb);
			systemBilServ.adjustUserMoneyByExtraCoin(totalBtcExtra, totalRmb);
			userDao.unLockTable();
			userDao.commit();
		} catch (Exception e) {
			userDao.rollback();
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
		finally {
			DaoFactory.closeConnection(conn);
			
		}
	}
	
	public List<ExtraRmbAdjust> getExtraRmbAjustList() {
		GeneralDao<ExtraRmbAdjust> rmbExtraDao = DaoFactory.createGeneralDao(ExtraRmbAdjust.class);
		return  rmbExtraDao.findList(new Condition[]{}, new String[]{"ctime desc"});
	}
	
	public static void main(String[] args) {
		ExtraServiceImpl serv = new ExtraServiceImpl();
		serv.adjustUserMoneyByExtraCoin(10000);
	}
}
