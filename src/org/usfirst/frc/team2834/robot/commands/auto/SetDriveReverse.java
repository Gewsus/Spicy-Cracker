package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command to set weather or not the robot drives in reverse.
 */
public class SetDriveReverse extends Command {
	
	private boolean reverse;

    public SetDriveReverse(boolean reverse) {
    	super("Set Drive Reverse: " + reverse);
        requires(Robot.drivetrain);
        this.reverse = reverse;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.setReverse(reverse);
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
