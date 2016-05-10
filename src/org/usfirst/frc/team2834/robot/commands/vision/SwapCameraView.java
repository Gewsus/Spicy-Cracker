package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Swaps between the two camera views.
 */
public class SwapCameraView extends Command {
	
    public SwapCameraView() {
    	super("Swap Camera View");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(Robot.vision.isShooterView()) {
    		Robot.vision.useGroundView();
    	} else {
    		Robot.vision.useShooterView();
    	}
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
    	end();
    }
}
