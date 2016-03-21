package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PusherOut extends Command {

	private double output;
	
    public PusherOut(boolean out) {
    	super("Pusher Out: " + out, 0.5);
        requires(Robot.pusher);
        output = out ? Pusher.OUT : Pusher.IN;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.setPusherPosition(output);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Robot.pusher.setPusherPosition(Pusher.OFF);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
