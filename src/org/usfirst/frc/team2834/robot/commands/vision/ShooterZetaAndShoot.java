package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterZetaAndShoot extends Command {

	double setpoint;
	boolean finish = false;
	
    public ShooterZetaAndShoot(double setpoint) {
        requires(Robot.shooter);
        requires(Robot.pusher);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.setPusherPosition(Pusher.IN);
    	Robot.shooter.reset();
    	Robot.shooter.setShooterSetpoints(setpoint, setpoint);
    	Robot.shooter.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.shooter.isLeftOnTarget() && Robot.shooter.isRightOnTarget()) {
    		Robot.pusher.setPusherPosition(Pusher.OUT);
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
    	Robot.shooter.disable();
    	Robot.shooter.setShooterOutput(0, 0);
    	Robot.pusher.setPusherPosition(Pusher.IN);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
