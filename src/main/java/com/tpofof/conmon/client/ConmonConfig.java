package com.tpofof.conmon.client;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pofof.conmon.model.TestCase;
import com.tpofof.utils.JsonUtils;

public class ConmonConfig {

	private int trialsCount = 1;
	private int testInterval = 60; // minutes
	private List<TestCase> testCases;

	@JsonProperty
	public int getTrialsCount() {
		return trialsCount;
	}

	@JsonProperty
	public void setTrialsCount(int trialsCount) {
		this.trialsCount = trialsCount;
	}

	@JsonProperty
	public int getTestInterval() {
		return testInterval;
	}

	@JsonProperty
	public void setTestInterval(int testFrequency) {
		this.testInterval = testFrequency;
	}

	@JsonProperty
	public List<TestCase> getTestCases() {
		return testCases;
	}

	@JsonProperty
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	private static ConmonConfig defaultConfig;
	
	public static ConmonConfig getDefaultConfig() {
		if (defaultConfig == null) {
			defaultConfig = getConfig("client-config.json");
		}
		return defaultConfig;
	}
	
	public static ConmonConfig getConfig(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException("Config file must exist");
		}
		return JsonUtils.fromJson(file, ConmonConfig.class);
	}
}
