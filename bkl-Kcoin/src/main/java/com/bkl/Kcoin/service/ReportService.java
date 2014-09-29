package com.bkl.Kcoin.service;

public interface ReportService {
	double getTotalLiveRmb();
	double getTotalFrozenRmb();
	
	double getTotalLiveBtc();
	double getTotalFrozenBtc();
	
	double getLast24HourRechargeRmb();
	double getLast24HourWithdrawRmb();
}
