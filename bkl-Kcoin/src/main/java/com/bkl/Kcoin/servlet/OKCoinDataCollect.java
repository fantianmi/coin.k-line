package com.bkl.Kcoin.servlet;

import org.junit.Test;

import com.bkl.Kcoin.utils.HttpGetUtil;

/**
 * OKCoin API Example
 * 
 * @author ok 2013/08/16 httpclient 请使用3.1版本
 */
public class OKCoinDataCollect {
	public void gorun() {
		new Thread(new Runnable() {
			public void run() {
			int i = 1;
			while (true) {
				if (i == 1) {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				String getTikerURL = "https://www.okcoin.com/api/ticker.do?ok=1";
				String getDepthURL = "https://www.okcoin.com/api/depth.do?ok=1";
				String getDealLogURL = "https://www.okcoin.com/api/trades.do?since=5000&ok=1";
				HttpGetUtil httpGet = new HttpGetUtil();
				System.out.println("######ticker data######");
				System.out.println(httpGet.doGet(getTikerURL));
				System.out.println("######Depth(市场深度) data######");
				System.out.println(httpGet.doGet(getDepthURL));
				System.out.println("######交易历史 data######");
				System.out.println(httpGet.doGet(getDealLogURL));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}).start();
	}

	@Test
	public void testRun() {
		gorun();
	}

}
