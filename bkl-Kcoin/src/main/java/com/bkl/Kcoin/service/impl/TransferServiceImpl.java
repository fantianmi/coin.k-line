package com.bkl.Kcoin.service.impl;
//package com.km.coins.service.impl;
//
//import java.util.Map;
//
//import com.km.coins.constants.Constants;
//import com.km.coins.entity.Transfer;
//import com.km.coins.entity.User;
//import com.km.coins.service.TransferService;
//import com.km.common.dao.DaoFactory;
//import com.km.common.dao.GeneralDao;
//import com.km.common.utils.DbUtil;
//import com.km.common.vo.Condition;
//import com.km.common.vo.Page;
//import com.km.common.vo.PageReply;
//import com.km.common.vo.RetCode;
//
///**
// * @author chaozheng
// * 
// */
//public class TransferServiceImp implements TransferService {
//
//	GeneralDao<Transfer> dao = DaoFactory.createGeneralDao(Transfer.class);
//	GeneralDao<User> userDao = DaoFactory.createGeneralDao(User.class);
//	
//	@Override
//	public Transfer getTransfer(long id) {
//		return dao.find(id);
//	}
//	
//	@Override
//	public long saveTransfer(Transfer transfer) {
//		return dao.save(transfer);
//	}
//
//	@Override
//	public PageReply<Transfer> getBTCTransferOutPage(Map searchMap, Page page) {
//		return getTransferByType(searchMap, page, Constants.TRADE_TYPE_BTC_OUT);
//	}
//
//	@Override
//	public PageReply<Transfer> getBTCTransferInPage(Map searchMap, Page page) {
//		return getTransferByType(searchMap, page, Constants.TRADE_TYPE_BTC_IN);
//	}
//	
//	@Override
//	public PageReply<Transfer> getLTCTransferOutPage(Map searchMap, Page page) {
//		return getTransferByType(searchMap, page, Constants.TRADE_TYPE_LTC_OUT);
//	}
//
//	@Override
//	public PageReply<Transfer> getLTCTransferInPage(Map searchMap, Page page) {
//		return getTransferByType(searchMap, page, Constants.TRADE_TYPE_LTC_IN);
//	}
//
//	@Override
//	public PageReply<Transfer> getTransferByType(Map searchMap, Page page, Integer type) {
//		Condition typeCondtion = DbUtil.generalEqualWhere("type", type);
//		return dao.getPage(page, new Condition[] { typeCondtion },new String[]{}, searchMap);
//	}
//
//}
