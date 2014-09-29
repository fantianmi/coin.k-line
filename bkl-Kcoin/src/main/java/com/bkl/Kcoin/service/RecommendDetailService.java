package com.bkl.Kcoin.service;

import com.bkl.Kcoin.entity.RecommendDetail;

/**
 * 推广服务接口
 * @author chaozheng
 *
 */
public interface RecommendDetailService {

	/**
	 * 获得推荐人信息。
	 * @param userId	注册用户ID
	 * @return
	 */
	RecommendDetail getRecommendDetail(long userId);
	
	/**
	 * 统一推荐人已经支付的用户数。
	 * @param recommend_user_id	推荐人ID
	 * @return
	 */
	int countPaidRecommend(long recommend_user_id);
	
	double getTotalPaidRecommendBtcAmount();
}
