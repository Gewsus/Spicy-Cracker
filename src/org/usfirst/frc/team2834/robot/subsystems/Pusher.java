package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.PusherOut;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Contains one servo to push the ball into the shooting wheels
 * 
 * This must be a separate subsystem so that it does not deactivate the shooter wheels
 * when the pusher command is called.
 */
public class Pusher extends Subsystem implements RobotMap {
    
    Servo pusher;
	//Victor pusher;
    public static final double PUSHED = 0.25;
    public static final double IN = 1.0;

    public Pusher() {
    	super("Pusher");
    	pusher = new Servo(SHOOTER_PUSH_ACTUATOR);
    	//pusher = new Victor(SHOOTER_PUSH_ACTUATOR);
    	setPusherPosition(IN);
    }
    
    public void setPusherPosition(double pos) {
    	pusher.set(pos);
    }
    
    /*public void setOutput(double output) {
    	pusher.set(output);
    }*/
    
    public void initDefaultCommand() {
        setDefaultCommand(new PusherOut(false));
    }
}

