package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoCenterGoal extends Command {
	
    public AutoCenterGoal() {
    	super("Auto center on goal", 5);
        requires(Robot.drivetrain);
        requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//double angle = Robot.vision.getGamma() * (180.0 / Math.PI);
    	//The camera is upside-down so the angle should be negative
    	//Robot.vision.calculate();
    	Timer.delay(0.25);
    	Robot.drivetrain.reset();
    	//Robot.drivetrain.setSetpoint(Robot.drivetrain.getYaw());
    	Robot.drivetrain.setSetpointRelative(Robot.vision.getGammaD());
    	Robot.drivetrain.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(0.0, 0.0, true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.drivetrain.onTarget() || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.disable();
    	Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
