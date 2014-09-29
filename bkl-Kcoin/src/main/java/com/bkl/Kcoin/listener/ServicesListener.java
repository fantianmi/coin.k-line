package com.bkl.Kcoin.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bkl.Kcoin.CoinConfig;
import com.bkl.Kcoin.service.ExtraService;
import com.bkl.Kcoin.service.impl.ExtraServiceImpl;
import com.km.common.utils.CrontabRunner;

public class ServicesListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		try {
			if (CoinConfig.isEnableCoinExtra()) {
				class CoinExtraAjustTask implements Runnable {
					public void run() {
						ExtraService serv = new ExtraServiceImpl();
						serv.adjustUserExtraCoin();
					}
				}
				;
				CrontabRunner crun = new CrontabRunner(
						CoinConfig.getDefaultCoinExtraCronsExpression(),
						new CoinExtraAjustTask());
				crun.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
