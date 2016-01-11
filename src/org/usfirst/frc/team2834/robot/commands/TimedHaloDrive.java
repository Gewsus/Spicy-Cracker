package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimedHaloDrive extends Command {
	
	double power;
	double rotate;
	double seconds;

    public TimedHaloDrive(double power, double rotate, double seconds) {
        requires(Robot.drivetrain);
        this.power = power;
        this.rotate = rotate;
        this.seconds = seconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(seconds);
    	Robot.drivetrain.setZero();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(power, rotate);
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
