package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Basic command to run the Shooter for a period of time.
 */
public class TimedShooter extends Command {
	
	private double power;

    public TimedShooter(double power, double seconds) {
        super("Timed Shooter: [" + power + "] [" + seconds + "]", seconds);
        requires(Robot.shooter);
        //requires(Robot.pusher);
        this.power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.disable();
    	//Robot.pusher.setPusherPosition(Pusher.IN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooter.setShooterOutput(power, power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.setShooterOutput(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
