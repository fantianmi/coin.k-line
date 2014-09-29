package com.bkl.Kcoin.utils;

import java.io.IOException;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author jackmao
 * @date 2014-3-6
 * @since 
 */
public class HttpGetUtil {
	public String doGet(String URL){
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);  
		client.getHttpConnectionManager().getParams().setSoTimeout(10000);
		GetMethod method = new GetMethod(URL);
		try {
			client.executeMethod(method);
			String result = method.getResponseBodyAsString();
			System.out.println(result);
			return result;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
