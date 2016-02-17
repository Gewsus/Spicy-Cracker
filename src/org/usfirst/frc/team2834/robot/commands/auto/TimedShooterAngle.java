package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimedShooterAngle extends Command {
	
	double power;
	double seconds;

    public TimedShooterAngle(double power, double seconds) {
        super("Timed Shooter Angle: " + power + "|" + seconds);
        requires(Robot.shooterAngle);
        this.power = power;
        this.seconds = seconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(seconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooterAngle.setOutput(power);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterAngle.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
