package com.bkl.Kcoin.service.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.UserService;
import com.bkl.Kcoin.wallet.AddressAdapt;
import com.keep.framework.multicoin.core.Wallet;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.MD5Util;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public class UserServiceImpl implements UserService {
	GeneralDao<User> dao = DaoFactory.createGeneralDao(User.class);
	
	public UserServiceImpl() {

	}

	@Override
	public Long save(User user) {
		return dao.save(user);
	}
	
	@Override
	public Long createUser(User user) {
		if (CoinConfig.isEnableBitcoinWallet()) {
			// new btc address
			String btcAddr = Wallet.INSTANCE.getNewAddress(user.getEmail());
			user.setBtc_in_addr(btcAddr);
		}
		
		user.saveMD5Password(user.getPassword());
		
		if (!exist(user.getEmail())) {
			Long id = save(user);
			return id;
		}
		return 0L;
	}
	
	@Override
	public User get(long id) {
		return dao.find("id", id);
	}
	
	@Override
	public User find(String email) {
		return dao.find("email", email);
	}

	@Override
	public User findByBtcAddr(String btcAddr) {
		return dao.find("btc_in_addr", btcAddr);
	}

	@Override
	public User findByLtcAddr(String btcAddr) {
		return dao.find("ltc_in_addr", btcAddr);
	}
	@Override
	public RetCode login(String email, String password) {
		User user = find(email);
		if (user == null) {
			return RetCode.USERNAME_OR_PASSWORD_ERROR;
		}
		if (user.getEmail_validated() == 0 && !CoinConfig.isCnVersion()) {
			return RetCode.USER_NOT_ACTIVE;
		}
		if (user.getPassword().equals(MD5Util.md5(password))) {
			return RetCode.OK;
		} 
		return RetCode.USERNAME_OR_PASSWORD_ERROR;
	}

	@Override
	public boolean exist(String username) {
		User user = find(username);
		return user != null;
	}
	
	@Override
	public PageReply<User> findUser(Map searchMap, Page page) {
		GeneralDao<User> dao = DaoFactory.createGeneralDao(User.class);
		PageReply<User> users = dao.getPage(page, searchMap);
		return users;
	}
	
	public static void main(String[] args) {
		GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
		GeneralDao<Cash> cashDao = DaoFactory.createGeneralDao(Cash.class);
		User user = new User();
		user.setEmail("ggjucheng2@163.com");
		user.setName("ggjucheng2");
		userDao.save(user);
		userDao.save(user);
		userDao.save(user);
		userDao.save(user);
		
		Cash cash = new Cash();
		cash.setAmount(1.0);
		cashDao.save(cash);
		cashDao.save(cash);
		userDao.save(user);
		cashDao.save(cash);
		
	}
	


}
