package com.bkl.Kcoin.utils;

import java.sql.Connection;

import org.apache.commons.beanutils.BeanUtils;

import com.bkl.Kcoin.entity.User;
import com.km.common.config.Config;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.vo.RetCode;

public class PayUtil {
	public PayUtil() {
		
	}
	
	public <T> RetCode mergeOrderAmountToUser(String userAmountColumnName,Class<T> orderEntityClazz,  long orderid, String orderAmountColumnName, boolean isIn) throws Exception{
		Connection conn = DaoFactory.createConnection();
		
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class,conn);
		
		GeneralDao orderDao = DaoFactory.createGeneralDao(orderEntityClazz,conn);
		Object orderObject = orderDao.find(orderid);
		if (orderObject == null) {
			return RetCode.ORDER_NOTALLOW_ID_NULL;
		}
		int status = Integer.parseInt(BeanUtils.getProperty(orderObject, "status")) ;
		if (status != 0){
			return RetCode.ORDER_CONFIRM_YET;
		}
		long order_user_id = Long.parseLong(BeanUtils.getProperty(orderObject, "user_id"));
		
		
		try {
			userDao.beginTransaction();
			userDao.lockTable(userDao.getTableName(User.class), userDao.getTableName(orderEntityClazz));
			
			User user = userDao.find("id", order_user_id);
			if (user == null) {
				return RetCode.ORDER_CANNOT_FOUND_USER;
			}
			
			double userAmount = Double.parseDouble(BeanUtils.getProperty(user, userAmountColumnName));
			double orderAmount = Double.parseDouble(BeanUtils.getProperty(orderObject, orderAmountColumnName));
			double newUserAmout = 0;
			if (isIn) {
				newUserAmout = DoubleUtil.add(userAmount, orderAmount);
			} else {
				newUserAmout =DoubleUtil.subtract(userAmount, orderAmount);
				if (newUserAmout < 0) {
					return RetCode.ORDER_USER_AMOUNT_NOT_ENOUGH;
				}
			}
			BeanUtils.setProperty(user, userAmountColumnName, newUserAmout);
			
			long ret = userDao.save(user);
			BeanUtils.setProperty(orderObject, "status", 1);
			ret = orderDao.save(orderObject);
			userDao.unLockTable();
			userDao.commit();
			return RetCode.OK;
		} catch (RuntimeException e) {
			userDao.unLockTable();
			userDao.rollback();
			throw e;
		} finally {
			DaoFactory.closeConnection(conn);
		}
	}
	

}
