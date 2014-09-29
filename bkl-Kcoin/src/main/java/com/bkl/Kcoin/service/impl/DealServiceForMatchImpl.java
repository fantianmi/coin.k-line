package com.bkl.Kcoin.service.impl;

import java.util.List;

import com.bkl.Kcoin.entity.Deal2User;
import com.bkl.Kcoin.service.DealService;
import com.bkl.Kcoin.service.TradeQuery;
import com.km.common.dao.DaoFactory;
import com.km.common.dao.GeneralDao;

public class DealServiceForMatchImpl extends DealServiceImpl {
	
	private double lastDealPrice;
	
	public DealServiceForMatchImpl() {
		this.lastDealPrice = TradeQuery.getLastestBtcDealPrice();
	}

	
	
	/**
	 * 
	 * 
	 *  撮合价格计算方法：
		撮合成交的前提是：买入价（A）必须大于或等于卖出价（B），即A>=B。
		计算依据：计算机在撮合时实际上是依据前一笔成交价而定出最新成交价的。
		假设：前一笔的成交价格为C，最新成交价为D；
		则，当
		A<=C时，D=A；（如果前一笔成交价高于 或等于买入价，则最新成交价就是买入价）
		B>=C时，D=B；（如果前一笔成交价低于或等于卖出价，则最新成交价就是卖出价）
		B<C<A时，D=C；（如果前一笔成交价在卖出价与买入价 之间，则最新成交价就是前一笔的成交价）
		撮合价的优点：既显示了公平性，又使成交价格具有相对连续性，避免了不必要的无规律跳跃。
	 * @param lastDealPrice
	 * @param buyPrice
	 * @param sellPrice
	 * @return
	 */
	private double getNewLastDealPrice(double lastDealPrice,double buyPrice, double sellPrice ) {
		if (buyPrice <= lastDealPrice) {
			return buyPrice;
		}
		if (sellPrice >= lastDealPrice) {
			return sellPrice;
		}
		if (sellPrice < lastDealPrice && lastDealPrice < buyPrice) {
			return lastDealPrice;
		}
		return sellPrice;
	}
	
	protected double getDealPrice(double buyPrice, double sellPrice) {
		this.lastDealPrice = getNewLastDealPrice(this.lastDealPrice, buyPrice, sellPrice);
		return this.lastDealPrice;
	}
	
	
	public List<Deal2User> getDealList() {
		GeneralDao<Deal2User> dao = DaoFactory.createGeneralDao(Deal2User.class);
		String sql = "select deal.id,deal.amount,deal.price,deal.ctime,deal.buy_email, u.email as sell_email,deal.buy_trade,deal.sell_trade from (select d.id,amount,price,d.ctime,buy_trade,u.email as buy_email,sell_trade from `deal` d, `user` u where d.buy_trade=u.id) deal,`user` u where deal.sell_trade= u.id order by deal.ctime desc";
		List<Deal2User> deals = dao.findListBySQL(sql);
		return deals;
	}
	
	public static void main(String[] args) throws Exception {
		DealService ds = new DealServiceForMatchImpl();
		ds.runDeal();
	}

}
