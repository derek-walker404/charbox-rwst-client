package com.tpofof.conmon.client.timer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimerResult {

	private long startTime;
	private long duration;
	private long pingDuration;
	private String testCaseId;
	private boolean outage = false;

	@JsonProperty
	public long getStartTime() {
		return startTime;
	}

	@JsonProperty
	public TimerResult setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	@JsonProperty
	public long getDuration() {
		return duration;
	}

	@JsonProperty
	public TimerResult setDuration(long duration) {
		this.duration = duration;
		return this;
	}

	@JsonProperty
	public long getPingDuration() {
		return pingDuration;
	}

	@JsonProperty
	public TimerResult setPingDuration(long pingDuration) {
		this.pingDuration = pingDuration;
		return this;
	}

	@JsonProperty
	public String getTestCaseId() {
		return testCaseId;
	}

	@JsonProperty
	public TimerResult setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
		return this;
	}

	@JsonProperty
	public boolean isOutage() {
		return outage;
	}

	@JsonProperty
	public TimerResult setOutage(boolean outage) {
		this.outage = outage;
		return this;
	}
	
}
