package com.bkl.Kcoin.service;

import com.bkl.Kcoin.entity.AdminUser;

//管理员 用户登陆，保存配置
public interface AdminService {
	//保存管理员用户信息
	public Long save(AdminUser user);
	//管理员登陆
    public boolean login(String username,String password);
    //根据管理员用户名查找
    public AdminUser find(String username);
    //查询是否存在管理员用户
    public boolean exist(String username);
}
