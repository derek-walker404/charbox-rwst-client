package com.tpofof.conmon.client;

import java.io.File;
import java.util.List;

import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.client.timer.TestCaseRunner;
import com.tpofof.utils.JsonUtils;

public class Main {
	
	private static void execute(String deviceConfigFilename, String tcFilename) {
		TestCaseRunner runner = new TestCaseRunner();
		File deviceConfigFile = new File(deviceConfigFilename);
		File tcFile = new File(tcFilename);
		if (deviceConfigFile.exists() && tcFile.exists()) {
			DeviceConfiguration deviceConfig = JsonUtils.fromJson(deviceConfigFile, DeviceConfiguration.class);
			String jsonContent = JsonUtils.fromJson(tcFile).toString();
			List<TestCase> tcs = JsonUtils.fromJsonList(jsonContent, TestCase.class);
			for (TestCase tc : tcs) {
				runner.run(deviceConfig, tc);
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length == 2) {
			execute(args[0], args[1]);
		} else {
			System.err.println("Required parameters:\n\t0: Device Configuration Filename\n\t1: Test Cases Filename");
		}
	}
}
