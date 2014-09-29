package com.bkl.Kcoin.service;

import java.util.List;

import com.bkl.Kcoin.entity.ExtraRmbAdjust;

public interface ExtraService {
	void adjustUserExtraCoin();
	public double getTotalBtcExtraAmount();
	void adjustUserMoneyByExtraCoin(double totalRmb);
	
	public List<ExtraRmbAdjust> getExtraRmbAjustList();
}
