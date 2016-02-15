package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutomaticTarget extends Command {

	private boolean seesGoal = false;
	
    public AutomaticTarget() {
        requires(Robot.drivetrain);
        requires(Robot.shooterAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(6.0);
    	Robot.vision.setProcessImage(true);
    	Timer.delay(0.25);//Wait for the vision to supply the first set of data
    	seesGoal = Robot.vision.isGoal();
    	if(seesGoal) {
	    	Robot.drivetrain.setSetpointRelative(Robot.vision.getBeta());
	    	Robot.shooterAngle.setSetpointRelative(Robot.shooterAngle.realAngleToVoltage(Robot.vision.getAlpha()));
	    	Robot.drivetrain.enable();
	    	Robot.shooterAngle.enable();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(seesGoal) {
        	return isTimedOut() || (Robot.drivetrain.onTarget() && Robot.shooterAngle.onTarget());
        }
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.disable();
    	Robot.shooterAngle.disable();
    	Robot.drivetrain.setZero();
    	Robot.shooterAngle.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
