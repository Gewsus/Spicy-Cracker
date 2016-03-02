package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStopIfFlat extends Command {
	
	private boolean stop = false;
	private double power;
	
    public DriveStopIfFlat(double power) {
    	super("Drive, Stop if Flat: " + power);
        requires(Robot.drivetrain);
        this.power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(power, 0, false);
    	if(!Robot.drivetrain.isUpSlope() && !Robot.drivetrain.isDownSlope()) {
    		Timer.delay(0.25);
    		if(!Robot.drivetrain.isUpSlope() && !Robot.drivetrain.isDownSlope()) {
    			stop = true;
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return stop;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.haloDrive(-0.1, 0.0, false);
		Timer.delay(0.25);
		Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drivetrain.setZero();
    }
}
