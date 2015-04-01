package co.charbox.rwsp.heartbeat;

import co.charbox.rwsp.config.DeviceManager;

public class HeartbeatMain {

	public static void main(String[] args) {
		if (DeviceManager.heartbeat()) {
			System.out.println("Success");
		} else {
			System.err.println("Failed");
		}
	}
}
