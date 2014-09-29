package com.bkl.Kcoin.service;
//package com.km.coins.service;
//
//import java.util.List;
//
//import com.km.coins.entity.Transfer;
//import com.km.coins.entity.Transfer2User;
//import com.km.common.vo.RetCode;
//
///**
// * 类职责:负责BTC充值，提现
// * @author ggjucheng
// *
// */
//public interface BtcService {
//	/**
//	 * 登陆用户提交BTC充值记录
//	 * @param transfer
//	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0.
//	 */
//	public long saveBtcTransferIn(Transfer transfer);
//
//	/**
//	 * 登陆用户查询BTC充值记录列表
//	 * @param userid 登陆用户的id
//	 * @return
//	 */
//	public List<Transfer> getBtcTransferInList(long userid);
//	
//	/***
//	 * 管理员后台:查询所有BTC充值记录
//	 * @return
//	 */
//	public List<Transfer2User> getBtcTransferInList();
//	
//	/**
//	 * 管理员后台:确认用户充值的BTC已经到账
//	 * @param id btc转账表id
//	 * @return 确认成功，返回true，否则返回false
//	 */
//	public RetCode doBtcTransferIn(long id);
//	
//	/**
//	 * 登陆用户提交BTC提现记录
//	 * @param transfer
//	 * @return 保存成功，返回记录的id,id必须大于1;否则返回0.
//	 */
//	public long saveBtcTransferOut(Transfer transfer);
//	
//	/**
//	 * 登陆用户查询BTC取现记录列表
//	 * @param userid 登陆用户id
//	 * @return
//	 */
//	public List<Transfer> getBtcTransferOutList(long userid);
//	
//	/**
//	 * 管理员后台:查询所有用户的BTC取现记录
//	 * @return
//	 */
//	public List<Transfer2User> getBtcTransferOutList();
//	
//	/**
//	 * 管理员后台:确认用户BTC取现合法，转账后，确认已经审核通过
//	 * @param id btc转账表id
//	 * @return 确认成功，返回true;否则返回false。
//	 */
//	public RetCode doBtcTransferOut(long id);
//	
//}
