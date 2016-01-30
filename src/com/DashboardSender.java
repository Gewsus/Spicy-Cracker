package com;

import org.usfirst.frc.team2834.robot.Robot;

/**
 * @author Adam Raine
 *
 * This interface makes it easier on the programmer to send data to the dashboard.
 * 
 * Any subsystem that needs to send specific data to the dashboard can implement
 * this interface.
 *
 */
public interface DashboardSender {
	
	public static final DashboardSender[] senders = {
			Robot.drivetrain,
			Robot.shooter,
			Robot.shooterAngle,
			Robot.shooter
	};
	
	public static void sendPeriodicData() {
		for(DashboardSender s : senders) {
			s.dashboardPeriodic();
		}
	}
	
	public static void sendInitData() {
		for(DashboardSender s : senders) {
			s.dashboardInit();
		}
	}
	
	public void dashboardInit();
	public void dashboardPeriodic();
}
