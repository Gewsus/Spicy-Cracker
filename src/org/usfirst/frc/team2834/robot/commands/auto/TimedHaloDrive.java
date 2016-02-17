package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimedHaloDrive extends Command {
	
	double power;
	double rotate;
	
    public TimedHaloDrive(double power, double rotate, boolean sixWheel, double seconds) {
    	super("Timed Halo Drive: " + power + "|" + rotate + "|" + sixWheel + "|" + seconds, seconds);
        requires(Robot.drivetrain);
        this.power = power;
        this.rotate = rotate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.setZero();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(-power, rotate, false);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
