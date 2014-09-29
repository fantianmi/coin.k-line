package com.bkl.Kcoin.service.impl;

import java.sql.Connection;

import com.bkl.Kcoin.entity.RecommendDetail;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.RecommendDetailService;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;

/**
 * @author chaozheng
 *
 */
public class RecommendDetailServiceImpl implements RecommendDetailService {
	
	private GeneralDao<RecommendDetail> dao = null;
	
	public RecommendDetailServiceImpl() {
		dao = DaoFactory.createGeneralDao(RecommendDetail.class);
	}
	
	public RecommendDetailServiceImpl(Connection conn) {
		dao = DaoFactory.createGeneralDao(RecommendDetail.class, conn);
	}
	
	@Override
	public RecommendDetail getRecommendDetail(long userId) {
		try {
			RecommendDetail rd = dao.find("user_id", userId);
			return rd;
		} catch(Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}

	@Override
	public int countPaidRecommend(long recommend_user_id) {
		if (recommend_user_id > 0) {
			try {
				return dao.queryIngeger("select count(1) from " + dao.getTableName(RecommendDetail.class)
						+ " where status = 1 and  recommended_id = ?", recommend_user_id);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}
	
	public double getTotalPaidRecommendBtcAmount() {
		String sql = "select sum(btc_amount) from recommend_detail where status=1";
		return dao.queryDouble(sql);
	}
	
	public static void main(String[] args) {
		RecommendDetailServiceImpl srv = new RecommendDetailServiceImpl();
		System.out.println(srv.countPaidRecommend(3));
	}

}
