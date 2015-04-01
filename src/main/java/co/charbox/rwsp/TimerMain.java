package co.charbox.rwsp;

import java.io.File;
import java.util.List;

import co.charbox.core.utils.JsonUtils;
import co.charbox.domain.model.DeviceConfiguration;
import co.charbox.domain.model.TestCase;
import co.charbox.rwsp.config.DeviceManager;
import co.charbox.rwsp.timer.TestCaseRunner;

public class TimerMain {
	
	protected static DeviceConfiguration getDeviceConfig() {
		return DeviceManager.getCurrentConfig(true);
	}
	
	protected static DeviceConfiguration getDeviceConfig(String configFilename) {
		File deviceConfigFile = new File(configFilename);
		if (deviceConfigFile.exists()) {
			return JsonUtils.fromJson(deviceConfigFile, DeviceConfiguration.class);
		}
		return null;
	}
	
	protected static List<TestCase> getTestCases() {
		return DeviceManager.getDeviceTestCases(true);
	}
	
	protected static List<TestCase> getTestCases(String tcFilename) {
		File tcFile = new File(tcFilename);
		if (tcFile.exists()) {
			String jsonContent = JsonUtils.fromJson(tcFile).toString();
			return JsonUtils.fromJsonList(jsonContent, TestCase.class);
		}
		return null;
	}
	
	private static void execute(String deviceConfigFilename, String tcFilename) {
		DeviceConfiguration deviceConfig = getDeviceConfig();
		List<TestCase> testCases = getTestCases();
		execute(deviceConfig, testCases);
	}
	
	private static void execute(DeviceConfiguration deviceConfig, List<TestCase> testCases) {
		TestCaseRunner runner = new TestCaseRunner();
		if (deviceConfig != null && testCases != null) {
			for (TestCase tc : testCases) {
				runner.run(deviceConfig, tc);
			}
		}
	}
	
	public static void main(String[] args) {
		if (args.length == 2) {
			execute(args[0], args[1]);
		} else if (args.length == 0) {
			execute(getDeviceConfig(), getTestCases());
		} else {
			System.err.println("Required parameters:\n\t0: Device Configuration Filename\n\t1: Test Cases Filename");
		}
	}
}
