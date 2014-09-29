package com.bkl.Kcoin.listener;

import java.util.concurrent.atomic.AtomicInteger;

import com.bkl.Kcoin.service.PlanTradeService;
import com.bkl.Kcoin.service.impl.PlanTradeServiceImpl;

public class PlanListener {
	//private static Object lockObject = new Object();
	private static boolean isInit = false;
	private static AtomicInteger counter = new AtomicInteger();
	private static  Thread planThread = null;
	static {
		init();
	}
	
	public static void init() {
		if (isInit) {
			return;
		}
		isInit = true;
		planThread = new PlanThread();
		planThread.start();
		
	}
	
	public static void notifyPlan() {
		counter.incrementAndGet();
		planThread.interrupt();
		/*synchronized (lockObject) {
			lockObject.notifyAll();
		}*/
	}
	
	static class PlanThread extends Thread {
		public void run() {
			System.out.println("PlanThread start!!!!!!!!!!!!!!!!");
			while (true) {
				try {
					System.out.println("PlanThread wait start!!!!!!!!!!!!!!!!");
					Thread.sleep(Integer.MAX_VALUE);
				} catch (InterruptedException e1) {
					//e1.printStackTrace();
				}
				System.out.println("PlanThread wait finish!!!!!!!!!!!!!!!!");
				if (counter.get() == 0) {
					continue;
				}
				if (counter.get() > 0) {
					PlanTradeService planSrv = new PlanTradeServiceImpl();
					try {
						planSrv.checkPlan();
					} catch (Exception e) {
						e.printStackTrace();
					}
					counter.set(0);
				}
			}
		}
	}
	
}
