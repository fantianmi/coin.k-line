package com.bkl.Kcoin.service;

import com.bkl.Kcoin.entity.Subscribe;
import com.bkl.Kcoin.entity.Subscribe2User;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

public interface SubscribeService {

	/**
	 * 分页查询认购列表
	 * @param page	分页对象
	 * @return	
	 */
	PageReply<Subscribe2User> getSubscribePage(Page page);
	
	/**
	 * 进行认购
	 * @param subscribe	认购对象
	 * @return
	 */
	RetCode doSubscribe(Subscribe subscribe);
	
	/**
	 * 检查该用户是否超出认购的次数限制
	 * @param userId
	 * @return
	 */
	boolean isOutOfLimit(long userId);
}
