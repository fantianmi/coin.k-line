package com.bkl.Kcoin.service.impl;


public class DealServiceForSellerImpl extends DealServiceImpl {
	
	
	public DealServiceForSellerImpl() {
	}

	
	
	protected double getDealPrice(double buyPrice, double sellPrice) {
		return sellPrice;
	}


}
