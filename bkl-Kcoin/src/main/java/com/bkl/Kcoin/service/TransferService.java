package com.bkl.Kcoin.service;
//package com.km.coins.service;
//
//import java.util.Map;
//
//import com.km.coins.entity.Transfer;
//import com.km.common.vo.Page;
//import com.km.common.vo.PageReply;
//import com.km.common.vo.RetCode;
//
///**
// * @author chaozheng
// *
// */
//public interface TransferService {
//	
//	/***
//	 * 根据ID查找X币充值/提现记录
//	 * @param id	主键
//	 * @return	充值/提现记录对象
//	 */
//	Transfer getTransfer(long id);
//	
//	/***
//	 * 保存X币充值/提现记录
//	 * @param transfer
//	 * @return
//	 */
//	long saveTransfer(Transfer transfer);
//	
//    /***
//     * 分页查询比特币提现记录
//     * @param searchMap	搜索条件
//     * @param page	分页参数
//     */
//    public PageReply<Transfer> getBTCTransferOutPage(Map searchMap, Page page);
//	
//	/**
//     * 分页查询比特币充值记录
//     * @param searchMap	搜索条件
//     * @param page	分页参数
//	 */
//	public PageReply<Transfer> getBTCTransferInPage(Map searchMap, Page page);
//	
//	/***
//	 * 分页查询莱特币提现记录
//	 * @param searchMap	搜索条件
//	 * @param page	分页参数
//	 */
//	public PageReply<Transfer> getLTCTransferOutPage(Map searchMap, Page page);
//	
//	/**
//	 * 分页查询莱特币充值记录
//	 * @param searchMap	搜索条件
//	 * @param page	分页参数
//	 */
//	public PageReply<Transfer> getLTCTransferInPage(Map searchMap, Page page);
//	
//	/***
//	 * 分页查询X币充值/提现记录
//	 * @param searchMap	搜索条件
//	 * @param page	分页参数
//	 * @param type	币种
//	 */
//	public PageReply<Transfer> getTransferByType(Map searchMap, Page page, Integer type);
//	
//}
