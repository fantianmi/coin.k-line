package com.bkl.Kcoin.service.impl;

import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.ReportService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;

public class ReportServiceImpl implements ReportService {

	GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
	@Override
	public double getTotalLiveRmb() {
		String sql = "select sum(rmb) from `user`";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

	@Override
	public double getTotalFrozenRmb() {
		String sql = "select sum(rmb_frozen) from `user`";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

	@Override
	public double getTotalLiveBtc() {
		String sql = "select sum(btc) from `user`";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

	@Override
	public double getTotalFrozenBtc() {
		String sql = "select sum(btc_frozen) from `user`";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

	@Override
	public double getLast24HourRechargeRmb() {
		String sql = "select sum(amount) from `cash` where type=1 and  status=1 and ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

	@Override
	public double getLast24HourWithdrawRmb() {
		String sql = "select sum(amount) from `cash` where type=2 and ctime >=(unix_timestamp()-time_to_sec('24:00:00'))";
		double dealAmount = userDao.queryDouble(sql);
		return DoubleUtil.formatDouble(dealAmount);
	}

}
