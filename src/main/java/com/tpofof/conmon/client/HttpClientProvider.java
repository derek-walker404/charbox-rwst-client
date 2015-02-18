package com.tpofof.conmon.client;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;

import com.google.common.collect.Maps;

public final class HttpClientProvider {

	private static final Map<Long, HttpClient> clientMap = Maps.newConcurrentMap();
	
	private HttpClientProvider() { }
	
	public static HttpClient get() {
		long tid = Thread.currentThread().getId();
		if (!clientMap.containsKey(tid)) {
			clientMap.put(tid, new HttpClient());
		}
		return clientMap.get(tid);
	}
}
