package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetVisionProcessing extends Command {
	
	boolean processing;

    public SetVisionProcessing(boolean processing) {
    	super("Set Vision Processing: " + (processing ? "on" : "off"));
    	this.processing = processing;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.vision.setProcessImage(processing);
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
