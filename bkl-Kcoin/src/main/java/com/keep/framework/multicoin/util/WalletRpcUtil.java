package com.keep.framework.multicoin.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.keep.framework.multicoin.exception.ExceptionCode;
import com.keep.framework.multicoin.exception.MulticoinException;
import com.km.common.config.Config;

/**
 * @author zhangchaozheng
 * 
 */
public class WalletRpcUtil {

	private static Log logger = LogFactory.getLog(WalletRpcUtil.class);

	static String walletRpcUrl = Config.getString("wallet.rpc.url");
	static String rpcUser = Config.getString("wallet.rpc.user");
	static String rpcPassword = Config.getString("wallet.rpc.password");

	public static Map<?, ?> send(Map<?, ?> message) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(rpcUser, rpcPassword));
		HttpPost httpPost = new HttpPost(walletRpcUrl);
		httpPost.setHeader("Content-Type", "text/plain");
		StringEntity msgEntity = null;
		try {
			msgEntity = new StringEntity(JsonUtil.jsonToString(message), "application/json", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("unsupport encoding.");
		}
		httpPost.setEntity(msgEntity);
		
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response == null) {
				return null;
			}
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				StringWriter json = new StringWriter();
				IOUtils.copy(entity.getContent(), json);
				return JsonUtil.stringToJson(json.toString(), Map.class);
			} else if (statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				throw new MulticoinException(ExceptionCode.AUTH_FAILURE);
			} else if (statusLine.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				throw new MulticoinException(ExceptionCode.METHOD_NOT_FOUND);
			} else if (statusLine.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new MulticoinException(ExceptionCode.PARAMS_ERROR);
			} else {
				throw new MulticoinException(ExceptionCode.UNKNOW_EXCEPTION);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new MulticoinException(ExceptionCode.CONNET_FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MulticoinException(ExceptionCode.CONNET_FAILED);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
}
