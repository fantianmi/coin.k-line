package com.bkl.Kcoin.service;

import java.util.List;
import java.util.Map;

import com.bkl.Kcoin.entity.Cash;
import com.bkl.Kcoin.entity.Cash2User;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

/**
 * 类职责：负责现金业务
 * @author ggjucheng
 *
 */

public interface CashService {
	/**
	 * 登陆用户保存用户充值记录
	 * @param cash
	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0.
	 */
	public long saveRecharge(Cash cash);
	
	public long updateRecharge(Cash cash,long user_id); 
	/**
	 * 登陆用户:查询充值列表
	 * @param user_id  登陆用户的id
	 * @return
	 */
	public List<Cash> getRechargeList(long user_id);
	
	public Cash getRecharge(long cashId);
	
	public PageReply<Cash> getRechargePage(Page page,long user_id);
	
    /***
     * 管理员后台：分页查询人民币充值
     * @param searchMap	搜索条件。
     * @param page	分页参数
     */
	public PageReply<Cash2User> getRecharge2UserPage(Map searchMap, Page page);
	

	
	/**
	 * 管理员后台：确认用户充值到帐
	 * @param rechargeId cash表记录的id
	 * @return 确认成功，返回true，否则返回false
	 */
	public RetCode doRecharge(long rechargeId, int adminId);
	
	/**
	 * 登陆用户保存取现记录
	 * @param cash
	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0
	 */
	public long saveWithdraw(Cash cash);
	
	/**
	 * 取消提现
	 * @param cashid
	 * @return
	 */
	//public RetCode cancelWithdraw(long cashid, long user_id) ;
	
	/**
	 *  登陆用户查询取现记录
	 * @param user_id 登陆用户id
	 * @return
	 */
	public List<Cash> getWithdrawList(long user_id);
	
	/**
	 *  登陆用户查询最近20笔取现记录
	 * @param user_id 登陆用户id
	 * @return
	 */
	public List<Cash> getLast20WithdrawList(long user_id);
	
    /***
     * 管理员后台：分页查询人民币充值
     * @param searchMap	搜索条件。
     * @param page	分页参数
     */
	public PageReply<Cash2User> getWithdraw2UserPage(Map searchMap, Page page);
	/**
	 * 管理员后台:获取用户的取现记录
	 * @return
	 */
	public List<Cash2User> getWithdraw2UserList();
	
    /***
     * 分页查询人民币充值
     * @param searchMap	搜索条件。
     * @param page	分页参数
     */
    public PageReply<Cash> getRechargeList(Map searchMap, Page page);
	
	/**
     * 分页查询人民币提现
     * @param searchMap	搜索条件。
     * @param page	分页参数
	 */
	public PageReply<Cash> getWithdrawList(Map searchMap, Page page);
	
	
	/**
	 * 管理员后台:查看用户的取现记录，后台转帐到用户，并确认
	 * @param withdrawId cash表记录的id
	 */
	public RetCode doWithdraw(long withdrawId,int adminId);
	
	/**
	 * 管理员取消人民币提现
	 * @param withdrawId cash表记录的id
	 */
	public RetCode doCancelWithdraw(long withdrawId,int adminId);
}
