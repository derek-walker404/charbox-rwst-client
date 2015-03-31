package com.tpofof.conmon.client;

import com.tpofof.conmon.client.heartbeat.HeartbeatMain;

public class Main {

	private static void error() {
		System.err.println("Executable required. First arg must be one of the following: { timer, heartbeat }");
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			error();
		} else {
			if (args[0].toLowerCase().equals("timer")) {
				TimerMain.main(new String[0]);
			} else if (args[0].toLowerCase().equals("heartbeat")) {
				HeartbeatMain.main(new String[0]);
			} else {
				error();
			}
		}
	}
}
