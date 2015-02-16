package com.tpofof.conmon.client.timer;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pofof.conmon.model.TestCase;

public class TimerResult {

	private long startTime;
	private long duration;
	private long size;
	private TestCase testCase;

	@JsonProperty
	public long getStartTime() {
		return startTime;
	}

	@JsonProperty
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return duration in ms
	 */
	@JsonProperty
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            This should be the duration in ms.
	 */
	@JsonProperty
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the asset size in bytes.
	 */
	@JsonProperty
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            This should be the size in bytes.
	 */
	@JsonProperty
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the download speed in MB/s (Megabits per sec)
	 */
	@JsonProperty
	public double getSpeed() {
		return getSize() <= 0 || getDuration() <= 0 ? -1
				: ((double) getSize() / 1024.0 / (double) getDuration());
	}

	@JsonProperty
	public TestCase getTestCase() {
		return testCase;
	}

	@JsonProperty
	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this)
				.append("speed", getSpeed())
				.toString();
	}
}
