package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShootDistance extends Command {

    public ShootDistance() {
        super("Shoot Distance");
        requires(Robot.shooter);
        //requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.reset();
    	Robot.shooter.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//new PusherOut(false).start();
    	double s = Shooter.DEFAULT_SETPOINT + (Shooter.SLOPE * (Robot.vision.getDistance() - 116.0))/* +  1.5 * 0.5 * Math.pow((Robot.vision.getDistance() - 116.0), 2)*/;
    	Robot.shooter.setShooterSetpoints(s, s);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.disable();
    	Robot.shooter.setShooterOutput(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	
    }
}
