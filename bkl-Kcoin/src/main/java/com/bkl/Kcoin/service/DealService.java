package com.bkl.Kcoin.service;

import java.util.List;

import com.bkl.Kcoin.entity.Deal2User;

/*
 交易处理
 */
/**
 * 类职责：交易处理
 * @author Administrator
 *
 */
public interface DealService {
	/**
	 * 执行交易操作
	 * @throws Exception
	 */
	public void runDeal() throws Exception;
	
	/**
	 * 管理员后台:获取全部成交记录
	 * @return
	 */
	public List<Deal2User> getDealList();
}
