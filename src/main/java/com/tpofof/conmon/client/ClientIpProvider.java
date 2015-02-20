package com.tpofof.conmon.client;

import static com.tpofof.conmon.client.ApplicationSettings.IP_SERVICE_URL;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.tpofof.utils.Config;
import com.tpofof.utils.JsonUtils;

public final class ClientIpProvider {

	private ClientIpProvider() { }
	
	public static String getIp() {
		String ip = "";
		try {
			GetMethod gm = new GetMethod(Config.get().getString(IP_SERVICE_URL));
			if (HttpClientProvider.get().executeMethod(gm) == 200) {
				ip = JsonUtils.parse(gm.getResponseBodyAsString()).get("ip").asText();
			}
			gm.releaseConnection();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
