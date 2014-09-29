package com.bkl.Kcoin.service.impl;

import com.bkl.Kcoin.entity.AdminUser;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.AdminService;
import com.bkl.Kcoin.service.UserService;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.PrintUtil;

public class AdminServiceImpl implements AdminService {
	GeneralDao<AdminUser> dao = DaoFactory.createGeneralDao(AdminUser.class);
	
	public AdminServiceImpl() {
		
	}

	@Override
	public Long save(AdminUser user) {
		return dao.save(user);
	}


	@Override
	public boolean login(String username, String password) {
		AdminUser user = find(username);
		if (user == null) {
			return false;
		}
		return user.getPassword().equals(password);
	}

	@Override
	public AdminUser find(String username) {
		return dao.find("username", username);
	}



	@Override
	public boolean exist(String username) {
		return find(username) != null;
	}
	
	public static void main(String[] args) {
		AdminService adminUserServ = new AdminServiceImpl();
		AdminUser user = new AdminUser();
		user.setUsername("admin");
		user.setPassword("123456");
		adminUserServ.save(user);
		
		PrintUtil.print(adminUserServ.exist("admin"));
		PrintUtil.print(adminUserServ.find("admin"));
		PrintUtil.print(adminUserServ.login("admin","123456"));
	}



}
