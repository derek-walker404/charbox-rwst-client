package co.charbox.rwsp.config;

import static co.charbox.rwsp.ApplicationSettings.CONMON_SERVER_URL_KEY;
import static co.charbox.rwsp.ApplicationSettings.DEVICE_ID_KEY;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import co.charbox.core.utils.Config;
import co.charbox.core.utils.JsonUtils;
import co.charbox.domain.model.Device;
import co.charbox.domain.model.DeviceConfiguration;
import co.charbox.domain.model.TestCase;
import co.charbox.rwsp.HttpClientProvider;

import com.fasterxml.jackson.databind.JsonNode;

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
		PostMethod gm = new PostMethod(deviceUrl);
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
	
	public static boolean heartbeat() {
		String testCasesUrl = DEVICE_URL + "/hb";
		PostMethod gm = new PostMethod(testCasesUrl);
		try {
			int statusCode = HttpClientProvider.get().executeMethod(gm);
			JsonNode response = JsonUtils.parse(gm.getResponseBodyAsString());
			gm.releaseConnection();
			return statusCode == 200 && response != null && response.has("success") && response.get("success").asBoolean();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
