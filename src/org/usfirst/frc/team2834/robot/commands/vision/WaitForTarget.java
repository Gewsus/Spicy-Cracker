package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Waits for the target to be on screen and a reasonable distance away.
 */
public class WaitForTarget extends Command {

    public WaitForTarget() {
        super("Wait For Target");
        requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Timer.delay(1);
    	//Robot.vision.calculate();
    	setTimeout(15);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut()
        		|| (Robot.vision.isGoal() && Robot.vision.getDistance() < 200)
        		;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
