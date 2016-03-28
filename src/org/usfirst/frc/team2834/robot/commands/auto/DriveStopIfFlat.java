package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStopIfFlat extends Command {
	
	private double power;
	private double rotate;
	
    public DriveStopIfFlat(double power, double rotate) {
    	super("Drive, Stop if Flat: " + power + "|" + rotate);
        requires(Robot.drivetrain);
        this.power = power;
        this.rotate = rotate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(power, rotate, false);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return !Robot.drivetrain.isUpSlope() && !Robot.drivetrain.isDownSlope() && Robot.drivetrain.isStable();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.haloDrive(-0.2, 0.0, false);
		Timer.delay(0.25);
		Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drivetrain.setZero();
    }
}
