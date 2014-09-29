package com.bkl.Kcoin.servlet;

import org.apache.http.client.HttpClient;  
import org.apache.http.client.ResponseHandler;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.BasicResponseHandler;  
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * OKCoin API Example
 * @author ok
 * 2013/08/16
 * httpclient 请使用3.1版本
 */
public class OKCoinApiTicker {
	//api接口地址
	private final static String TICKER_URI = "https://www.okcoin.com/api/depth.do?ok=1";
	public static void testApi(){
	    try {
	    	HttpClient httpclient = new DefaultHttpClient(); 
			//post请求
			HttpGet httpget = new HttpGet(TICKER_URI);  
			ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
			String responseBody = httpclient.execute(httpget, responseHandler);  
			//////////////////////////////////////////////////////////////////////////////////////////
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			System.out.println(responseBody);
			//////////////////////////////////////////////////////////////////////////////////////////
			//释放连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testApi();
	}
	
	
}
