package com.bkl.Kcoin.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.bkl.Kcoin.utils.HttpGetUtil;


/**
 * OKCoin API Example
 * @author ok
 * 2013/08/16
 * httpclient 请使用3.1版本
 */
public class GetOKCoinDataListener implements ServletContextListener{
	private String getTikerURL="https://www.okcoin.com/api/ticker.do?ok=1";
	private String getDepthURL="https://www.okcoin.com/api/depth.do?ok=1";
	private String getDealLogURL="https://www.okcoin.com/api/trades.do?since=5000&ok=1";
	protected final static Logger log = Logger.getLogger(GetOKCoinDataListener.class);
	HttpGetUtil httpGet=new HttpGetUtil();
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.error("OKCoinDataListener Destroyed");  
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {  
            
            @Override  
            public void run() {  
                try {  
                    int i = 1;  
                    while(true){  
                        if(i == 1){  
                            Thread.sleep(60000);  
                            i++;  
                        }  
                        System.out.println("######ticker data######");
                		System.out.println(httpGet.doGet(getTikerURL));
                		System.out.println("######Depth(市场深度) data######");
                		System.out.println(httpGet.doGet(getDepthURL));
                		System.out.println("######交易历史 data######");
                		System.out.println(httpGet.doGet(getDealLogURL));
                        Thread.sleep(10000);  
                    }  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }).start();  
		
	}
	
}
