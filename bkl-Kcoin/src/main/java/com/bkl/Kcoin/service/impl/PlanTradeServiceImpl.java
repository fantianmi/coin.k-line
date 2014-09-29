package com.bkl.Kcoin.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.bkl.Kcoin.constants.Constants;
import com.bkl.Kcoin.entity.PlanTrade;
import com.bkl.Kcoin.entity.Trade;
import com.bkl.Kcoin.entity.User;
import com.bkl.Kcoin.service.PlanTradeService;
import com.bkl.Kcoin.service.TradeQuery;
import com.bkl.Kcoin.service.TradeService;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;
import com.km.common.utils.DbUtil;
import com.km.common.utils.TimeUtil;
import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;

/**
 * @author chaozheng
 * 
 */
public class PlanTradeServiceImpl implements PlanTradeService {

	private static final Logger LOG = Logger.getLogger(PlanTradeServiceImpl.class);

	private GeneralDao<PlanTrade> planDao = DaoFactory.createGeneralDao(PlanTrade.class);

	@Override
	public long saveOrUpdatePlan(PlanTrade plan) {
		Validate.notNull(plan, "[参数错误] 计划委托不能为空.");
		return planDao.save(plan);
	}

	@Override
	public long disablePlan(PlanTrade plan) {
		Validate.notNull(plan, "[参数错误] 计划委托不能为空.");
		Validate.isTrue(
				!(PlanTrade.STATUS_EFFECTIVE == plan.getStatus() || PlanTrade.STATUS_DELETED == plan.getStatus()),
				"已经生效或已经删除的计划委托不能禁用.");
		plan.setStatus(PlanTrade.STATUS_DISABLED);
		return planDao.save(plan);
	}

	@Override
	public void delPlan(PlanTrade plan) {
		Validate.notNull(plan, "[参数错误] 计划委托不能为空.");
		planDao.delete(plan.getId());
	}

	@Override
	public void executePlan(PlanTrade plan) {
		if (null == plan || plan.getId() <= 0 || PlanTrade.STATUS_NORMAL != plan.getStatus()) {
			return;
		}
		boolean checkExcute = (plan.getTrade_type() == Constants.TRADE_TYPE_BTC_IN && 
				TradeQuery.getLastestBtcSellPrice() <= plan.getPrice()) || 
				(plan.getTrade_type() == Constants.TRADE_TYPE_BTC_OUT && 
				TradeQuery.getLastestBtcBuyPrice() >= plan.getPrice());
		if (checkExcute) {
			// create a trade
			long currentTime = TimeUtil.getUnixTime();
			Trade trade = new Trade();
			trade.setUser_id(plan.getUser_id());
			trade.setType(plan.getTrade_type());
			trade.setCtime(currentTime);
			trade.setStatus(Trade.STATUS_PROGRESS);
			trade.setAmount(plan.getQuantity());
			trade.setPrice(plan.getPrice());
			trade.setDeal(0);
	
			// update plan
			plan.setEffective_time(currentTime);
			plan.setStatus(PlanTrade.STATUS_EFFECTIVE);
			
			Connection conn = DaoFactory.createConnection();
			GeneralDao<PlanTrade> planDao = DaoFactory.createGeneralDao(PlanTrade.class, conn);
			GeneralDao<Trade> tradeDao = DaoFactory.createGeneralDao(Trade.class, conn);
			TradeService tradeSrv = new TradeServiceImpl();
			tradeDao.beginTransaction();
			tradeDao.lockTable(tradeDao.getTableName(PlanTrade.class));
			long tradeId = 0;
			try {
				if(plan.getTrade_type() == Constants.TRADE_TYPE_BTC_IN) {
					tradeId = tradeSrv.buyBtc(trade);
				} else if (plan.getTrade_type() == Constants.TRADE_TYPE_BTC_OUT) {
					tradeId = tradeSrv.sellBtc(trade);
				}
				if (tradeId > 0) {
					planDao.save(plan);
					tradeDao.unLockTable();
					conn.commit();
				}
				return;
			} catch (Exception e) {
				try {
					if (tradeId > 0) {
						conn.rollback();
					}
				} catch (SQLException e1) {
					LOG.error("executePlan roll back error.", e1);
					e1.printStackTrace();
				}
				throw new RuntimeException(e);
			} finally {
				DaoFactory.closeConnection(conn);
			}
		}
	}

	@Override
	public PageReply<PlanTrade> getUserPlan(int userId, Page page) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", userId);
		return planDao.getPage(page, new Condition[] { user_con }, new String[] { "create_time desc" });
	}

	@Override
	public List<PlanTrade> getUserPlan(int userId) {
		Condition user_con = DbUtil.generalEqualWhere("user_id", userId);
		return planDao.findList(new Condition[] { user_con }, new String[] { "create_time desc" });
	}

	@Override
	public void checkPlan() {
		LOG.debug("计划委托检查开始...");
		Connection conn = DaoFactory.createConnection();
		GeneralDao<PlanTrade> planDao = DaoFactory.createGeneralDao(PlanTrade.class, conn);
		Condition[] conditions = new Condition[] { DbUtil.generalEqualWhere("status", PlanTrade.STATUS_NORMAL) };
		List<PlanTrade> plans = planDao.findList(conditions, new String[] {});
		if (plans != null && plans.size() > 0) {
			for (PlanTrade plan : plans) {
				executePlan(plan);
			}
		}
		DaoFactory.closeConnection(conn);
		LOG.debug("计划委托检查结束,共处理" + (plans == null ? 0 : plans.size()) + "条计划.");
	}

}
