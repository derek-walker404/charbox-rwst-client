package com.tpofof.conmon.client.config;

import static com.tpofof.conmon.client.ApplicationSettings.CONMON_SERVER_URL_KEY;
import static com.tpofof.conmon.client.ApplicationSettings.DEVICE_ID_KEY;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.pofof.conmon.model.Device;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.client.HttpClientProvider;
import com.tpofof.utils.Config;
import com.tpofof.utils.JsonUtils;

public final class DeviceManager {

	public static final String DEVICE_URL;
	
	static {
		Config config = Config.get();
		DEVICE_URL = config.getString(CONMON_SERVER_URL_KEY) + "/devices/id/" + config.getInt(DEVICE_ID_KEY);
	}
	
	private static DeviceConfiguration defaultConfig;
	private static DeviceConfiguration currentConfig = null;
	private static Device currentDevice = null;
	private static List<TestCase> currentTestCases = null;

	private DeviceManager() { }
	
	public static Device initDevice() {
		String deviceUrl = DEVICE_URL;
		GetMethod gm = new GetMethod(deviceUrl);
		try {
			HttpClientProvider.get().executeMethod(gm);
			currentDevice = JsonUtils.fromJsonResponse(gm.getResponseBodyAsString(), Device.class);
			gm.releaseConnection();
			if (currentDevice != null ||  !currentDevice.isRegistered()) {
				return registerDevice();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentDevice;
	}
	
	private static Device registerDevice() {
		Device device = null;
		String deviceUrl = DEVICE_URL + "/register";
		GetMethod gm = new GetMethod(deviceUrl);
		try {
			HttpClientProvider.get().executeMethod(gm);
			currentDevice = JsonUtils.fromJsonResponse(gm.getResponseBodyAsString(), Device.class);
			gm.releaseConnection();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return device;
	}
	
	public static Device getDevice(boolean fetch) {
		if (fetch || currentDevice == null) {
			String deviceUrl = DEVICE_URL;
			GetMethod gm = new GetMethod(deviceUrl);
			try {
				HttpClientProvider.get().executeMethod(gm);
				currentDevice = JsonUtils.fromJsonResponse(gm.getResponseBodyAsString(), Device.class);
				gm.releaseConnection();
				if (currentDevice != null && !currentDevice.isRegistered()) {
					initDevice();
				}
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return currentDevice;
	}

	public static DeviceConfiguration getCurrentConfig(boolean fetch) {
		if (fetch || currentConfig == null) {
			Device device = getDevice(fetch);
			if (device != null && device.isRegistered()) {
				Config config = Config.get();
				String deviceConfigUrl = config.getString(CONMON_SERVER_URL_KEY) + "/configs/" + device.getConfigId();
				GetMethod gm = new GetMethod(deviceConfigUrl);
				try {
					HttpClientProvider.get().executeMethod(gm);
					currentConfig = JsonUtils.fromJsonResponse(gm.getResponseBodyAsString(), DeviceConfiguration.class);
					gm.releaseConnection();
				} catch (HttpException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return currentConfig != null
				? currentConfig
				: getDefaultConfig();
	}
	
	public static DeviceConfiguration getDefaultConfig() {
		if (defaultConfig == null) {
			defaultConfig = getConfig("client-config.json");
		}
		currentConfig = defaultConfig;
		return defaultConfig;
	}
	
	public static DeviceConfiguration getConfig(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException("Config file must exist");
		}
		currentConfig = defaultConfig;
		return JsonUtils.fromJson(file, DeviceConfiguration.class);
	}
	
	public static List<TestCase> getDeviceTestCases(boolean fetch) {
		if (fetch || currentTestCases == null || currentTestCases.isEmpty()) {
			String testCasesUrl = DEVICE_URL + "/testcases";
			GetMethod gm = new GetMethod(testCasesUrl);
			try {
				HttpClientProvider.get().executeMethod(gm);
				currentTestCases = JsonUtils.fromJsonListResponse(gm.getResponseBodyAsString(), TestCase.class);
				gm.releaseConnection();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return currentTestCases;
	}
}
