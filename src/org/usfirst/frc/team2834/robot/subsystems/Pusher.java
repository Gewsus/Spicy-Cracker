package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import com.DashboardSender;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Contains one servo to push the ball into the shooting wheels
 * 
 * This must be a separate subsystem so that it does not deactivate the shooter wheels
 * when the pusher command is called.
 */
public class Pusher extends Subsystem implements RobotMap, DashboardSender {
    
    Servo pusher;
    public static final double OUT = 0.72;
    public static final double IN = 0.0;

    public Pusher() {
    	super("Pusher");
    	pusher = new Servo(SHOOTER_PUSH_ACTUATOR);
    	setPusherPosition(IN);
    }
    
    public void setPusherPosition(double pos) {
    	pusher.set(pos);
    }
    
    public void initDefaultCommand() {
    }

	@Override
	public void dashboardInit() {
	}

	@Override
	public void dashboardPeriodic() {
		//SmartDashboard.putNumber("Pusher Servo pos", pusher.get());
	}
}

