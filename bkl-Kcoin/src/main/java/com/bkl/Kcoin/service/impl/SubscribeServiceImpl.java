package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.Validate;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.Bill;
import com.bkl.Kcoin.entity.BillDetail;
import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.Subscribe2User;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.SubscribeService;
import com.bkl.Kcoin.service.SystemBillService;
import com.bkl.Kcoin.service.UserBillService;
import com.bkl.Kcoin.utils.DoubleUtil;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class SubscribeServiceImpl implements SubscribeService {
	
	@Override
	public PageReply<Subscribe2User> getSubscribePage(Page page) {
		GeneralDao<Subscribe2User> dao = DaoFactory.createGeneralDao(Subscribe2User.class);
		String sql = "select s.id,s.user_id, s.amount, s.price,s.ctime,u.email,u.name from `subscribe` s,`user` u where s.user_id=u.id  order by ctime desc";
		PageReply<Subscribe2User> subscribe2User = dao.getPage(page, sql);
		return subscribe2User;
	}

	@Override
	public RetCode doSubscribe(Subscribe subscribe) {
		Validate.notNull(subscribe);
		
		Connection conn = DaoFactory.createConnection();
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class, conn);
		GeneralDao<Subscribe> subscribeDao = DaoFactory.createGeneralDao(Subscribe.class, conn);
		SystemBillService systemBilServ = new SystemBillServiceImpl(conn);
		UserBillService userBillServ = new UserBillServiceImpl(conn);
		long userId = subscribe.getUser_id();
		
		// 参数非法
		if(subscribe.getAmount() != CoinConfig.getRengouAmount() || subscribe.getPrice() != CoinConfig.getRengouPrice()) {
			return RetCode.FORBID;
		}
		
		// 非法用户进行认购
		if(userId <= 0) {
			return RetCode.USER_NOT_EXIST;
		}
		
		// 认购功能没开启
		if (!CoinConfig.isEnableRengou()) {
			return RetCode.RENGOU_DISABLED;
		}
		
		//超过
		if (isOutOfLimit(userId)) {
			return RetCode.RENGOU_OUT_OF_LIMIT;
		}
		
		User user = userDao.find(userId);
		
		//余额不足
		double total = DoubleUtil.multiply(subscribe.getAmount(), subscribe.getPrice());
		if(user.getRmb() < total) {
			return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
		}
		
		userDao.beginTransaction();
		userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(Subscribe.class), userDao.getTableName(Bill.class), userDao.getTableName(BillDetail.class));
		
		
		try {
			double amount = subscribe.getAmount();
			if (CoinConfig.getRengouPoolSize() < amount) {
				CoinConfig.setRengouPoolSize(CoinConfig.getRengouPoolSize());
				userDao.commit();
				return RetCode.RENGOU_OVERFLOW;
			}
			
			CoinConfig.setRengouPoolSize(DoubleUtil.subtract(CoinConfig.getRengouPoolSize(), amount));
			
			//
			user.setRmb(DoubleUtil.subtract(user.getRmb(), total));
			user.setBtc(DoubleUtil.add(user.getBtc(), amount));
			userDao.update(user);
			
			subscribe.setCtime(TimeUtil.getUnixTime());
			long ret = subscribeDao.save(subscribe);
			subscribe.setId(ret);
			userBillServ.doBtcRengou(subscribe, user);
			systemBilServ.doBtcRengou(subscribe, user);
			userDao.unLockTable();
			userDao.commit();
			return RetCode.OK;
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			DaoFactory.closeConnection(conn);
		}
		
	}

	@Override
	public boolean isOutOfLimit(long userId) {
		GeneralDao<Subscribe> dao = DaoFactory.createGeneralDao(Subscribe.class);
		List<Subscribe> ls = dao.find("select s.* from `subscribe` s where s.user_id = ?", new Object[] { userId });
		if (ls != null && ls.size() >= CoinConfig.getRengouTimeLimit()) {
			return true;
		}
		return false;
	}

}
