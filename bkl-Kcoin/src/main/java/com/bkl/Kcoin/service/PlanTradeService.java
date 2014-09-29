package com.bkl.Kcoin.service;

import java.util.List;

import com.bkl.Kcoin.entity.PlanTrade;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;

/**
 * <p>计划委托接口</p>
 * @author chaozheng
 *
 */
public interface PlanTradeService {

	/***
	 * 添加或修改计划委托。
	 * @param plan	计划委托
	 * @return	计划委托id
	 */
	public long saveOrUpdatePlan(PlanTrade plan);
	
	/***
	 * 暂停计划委托。
	 * @param plan	计划委托
	 * @return	计划委托id
	 */
	public long disablePlan(PlanTrade plan);
	
	/***
	 * 删除计划委托。
	 * @param plan	计划委托
	 */
	public void delPlan(PlanTrade plan);
	
	/***
	 * 执行计划委托。
	 * @param plan	计划委托
	 */
	public void executePlan(PlanTrade plan);
	
	/***
	 * 返回用户计划委托列表。不包括已经删除的委托。
	 * @param userId	用户ID, 该参数不能为空。
	 * @param page		分页参数。该参数为空时，返回所有记录。
	 * @return	计划委托列表
	 */
	public PageReply<PlanTrade> getUserPlan(int userId, Page page);
	
	/***
	 * 返回用户计划委托列表。不包括已经删除的委托。
	 * @param userId	用户ID, 该参数不能为空。
	 * @return	计划委托列表
	 */
	public List<PlanTrade> getUserPlan(int userId);
	
	/***
	 *  检查计划委托。当委托条件满足时，使之生效。
	 */
	public void checkPlan();
	
}
