package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterSetpointAndShoot extends Command {

	double setpoint;
	boolean finish = false;
	
    public ShooterSetpointAndShoot(double setpoint) {
        requires(Robot.shooter);
        requires(Robot.pusher);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setShooterSetpoints(setpoint, setpoint);
    	Robot.shooter.setEnabled(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.shooter.isLeftOnTarget() && Robot.shooter.isRightOnTarget()); {
    		Robot.pusher.setPusherPosition(1.0);
    		Timer.delay(1.5);
    		finish = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finish;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}