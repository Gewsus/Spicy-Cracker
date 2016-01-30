package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterPush extends Command {

	private double output;
	
    public ShooterPush(boolean out) {
    	super("Shooter Push");
        requires(Robot.pusher);
        output = out ? 1.0 : -1.0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(0.5);
    	Robot.pusher.setOutput(output);
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
    	Robot.pusher.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
