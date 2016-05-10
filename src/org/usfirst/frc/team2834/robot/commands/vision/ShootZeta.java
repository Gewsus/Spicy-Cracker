package org.usfirst.frc.team2834.robot.commands.vision;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets shooter speed based on vertical position on screen
 */
public class ShootZeta extends Command {

    public ShootZeta() {
        super("Shoot Zeta");
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.reset();
    	Robot.shooter.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double s = Shooter.DEFAULT_SETPOINT + (Robot.vision.getZeta() * 6300);
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
