package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AnglerOverride extends Command {

    public AnglerOverride() {
    	super("Angle Override");
        requires(Robot.angler);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.angler.disable();
    	Robot.angler.setOutput(0.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.angler.setOutput(Robot.oi.operator.getY());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.angler.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
