package com.bkl.Kcoin.listener;

import java.util.concurrent.atomic.AtomicInteger;

import com.bkl.Kcoin.service.DealService;
import com.bkl.Kcoin.service.TradeQuery;
import com.bkl.Kcoin.service.impl.DealServiceImpl;

public class DealListener {
	private static Object lockObject = new Object();
	private static boolean isInit = false;
	private static AtomicInteger counter = new AtomicInteger();
	static {
		init();
	}
	
	public static void init() {
		if (isInit) {
			return;
		}
		isInit = true;
		DealThread dt = new DealThread();
		dt.start();
		
	}
	
	public static void incTrade() {
		counter.incrementAndGet();
		synchronized (lockObject) {
			lockObject.notifyAll();
		}
	}
	
	static class DealThread extends Thread {
		public void run() {
			System.out.println("DealThread start!!!!!!!!!!!!!!!!");
			while (true) {
				synchronized (lockObject) {
					if (counter.get() == 0) {
						try {
							System.out.println("DealThread wait start!!!!!!!!!!!!!!!!");
							lockObject.wait();
							System.out.println("DealThread wait finish!!!!!!!!!!!!!!!!");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (counter.get() > 0) {
						
						try {
							DealService ds = DealServiceImpl.getInstance();
							ds.runDeal();
							TradeQuery.flushQuery();
							PlanListener.notifyPlan();
						} catch (Exception e) {
							e.printStackTrace();
						}
						counter.set(0);
					}
				}
			}
		}
	}
	
}
