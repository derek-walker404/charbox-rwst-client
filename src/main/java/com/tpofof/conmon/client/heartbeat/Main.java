package com.tpofof.conmon.client.heartbeat;

import com.tpofof.conmon.client.config.DeviceManager;

public class Main {

	public static void main(String[] args) {
		if (DeviceManager.heartbeat()) {
			System.out.println("Success");
		} else {
			System.err.println("Failed");
		}
	}
}
