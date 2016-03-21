package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.PusherOff;
import org.usfirst.frc.team2834.robot.commands.PusherOut;

import com.DashboardSender;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Contains one servo to push the ball into the shooting wheels
 * 
 * This must be a separate subsystem so that it does not deactivate the shooter wheels
 * when the pusher command is called.
 */
public class Pusher extends Subsystem implements RobotMap, DashboardSender {
    
    Servo pusher;
    //Relay pusher;
	//Victor pusher;
    /*public static final Relay.Value OUT = Relay.Value.kReverse;
    public static final Relay.Value OFF = Relay.Value.kOff;
    public static final Relay.Value IN = Relay.Value.kForward;*/
    public static final double OUT = 0.3;
    public static final double IN = 1.0;

    public Pusher() {
    	super("Pusher");
    	pusher = new Servo(SHOOTER_PUSH_ACTUATOR);
    	//pusher = new Relay(0);
    	//pusher = new Victor(SHOOTER_PUSH_ACTUATOR);
    	setPusherPosition(IN);
    }
    
    /*public void setPusherPosition(Relay.Value pos) {
    	pusher.set(pos);
    }*/
    
    public void setPusherPosition(double pos) {
    	pusher.set(pos);
    }
    
    /*public void setOutput(double output) {
    	pusher.set(output);
    }*/
    
    public void initDefaultCommand() {
        setDefaultCommand(new PusherOff());
    }

	@Override
	public void dashboardInit() {
		SmartDashboard.putData("Relay", pusher);
	}

	@Override
	public void dashboardPeriodic() {
		
	}
}

