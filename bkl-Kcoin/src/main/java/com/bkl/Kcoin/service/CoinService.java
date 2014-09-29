package com.bkl.Kcoin.service;

import java.util.List;
import java.util.Map;

import com.bkl.Kcoin.entity.Transfer;
import com.bkl.Kcoin.entity.Transfer2User;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;
import com.km.common.vo.RetCode;

/**
 * <p>X币服务接口</p>
 * <p>适用于BTC/LTC等其它币种。</p>
 * @author chaozheng
 *
 */
public interface CoinService {

	/***
	 * 根据ID查找X币充值/提现记录
	 * @param id	主键
	 * @return	充值/提现记录对象
	 */
	Transfer getTransfer(long id);
	
	/***
	 * 保存X币充值/提现记录
	 * @param transfer
	 * @return
	 */
	long saveTransfer(Transfer transfer);
	
	/***
	 * 保存X币提现记录,需要 处理资金冻结情况
	 * @param transfer
	 * @return
	 */
	long saveCoinWithdraw(Transfer transfer);
	
	/***
	 * 撤销X币提现申请,需要 处理资金冻结情况
	 * @param transfer
	 * @return
	 */
	RetCode cancelWithdraw(long transferid,int admin_id);
	
    /***
     * 分页查询X币提现记录
     * @param searchMap	搜索条件
     * @param page	分页参数
     */
    PageReply<Transfer2User> getTransferOutPage(Map searchMap, Page page);
	
	/**
     * 分页查询X币充值记录
     * @param searchMap	搜索条件
     * @param page	分页参数
	 */
	PageReply<Transfer2User> getTransferInPage(Map searchMap, Page page);
	
	/***
	 * 确定X币充值成功
	 * @param id	主键
	 * @return	retcode
	 */
	RetCode confirmCoinIn(long id,int adminId);
	
	/***
	 * 确定X币提现成功
	 * @param id	主键
	 * @return	retcode
	 */
	RetCode confirmCoinOut(long id,int adminId);
	
	/***
	 * 获得用户最新十条X币充值记录
	 * @param userId	用户ID
	 * @return
	 */
	List<Transfer> getTransferInTop10(long userId);
	
	/***
	 * 获得用户最新十条X币提现记录
	 * @param userId	用户ID
	 * @return
	 */
	List<Transfer> getTransferOutTop10(long userId);

}
