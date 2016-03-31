package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimedAngler extends Command {
	
	double power;
	double seconds;

    public TimedAngler(double power, double seconds) {
        super("Timed Angler: [" + power + "] [" + seconds + "]");
        requires(Robot.angler);
        this.power = power;
        this.seconds = seconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(seconds);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.angler.setOutput(power);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.angler.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
