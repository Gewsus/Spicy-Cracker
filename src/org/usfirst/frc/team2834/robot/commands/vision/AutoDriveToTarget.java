package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveToTarget extends Command {

	private double power = 0.0;
	
    public AutoDriveToTarget() {
    	super("Auto Drive to Target");
        requires(Robot.drivetrain);
        requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Robot.vision.calculate();
    	Timer.delay(0.25);
    	power = Robot.vision.getZeta();
    	setTimeout(0.9);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(power, 0.0, false);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.haloDrive(-power, 0.0, false);
    	Timer.delay(0.1);
    	Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
