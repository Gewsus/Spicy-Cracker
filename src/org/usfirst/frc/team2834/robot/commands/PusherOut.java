package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the position of the pusher to be either out or in.
 */
public class PusherOut extends Command {

	private double position;
	
    public PusherOut(boolean out) {
    	super("Pusher Out: [" + out + "]");
        requires(Robot.pusher);
        position = out ? Pusher.OUT : Pusher.IN;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.setPusherPosition(position);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
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
