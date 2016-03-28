package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterTestSetpoint extends Command {
    
    public ShooterTestSetpoint() {
    	super("Shooter Test Setpoints");
    	requires(Robot.shooter);
    	SmartDashboard.putNumber("Test Shooter Setpoint", 30000);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//new PusherOut(false).start();
    	Robot.shooter.reset();
    	Robot.shooter.enable();
    	double set = SmartDashboard.getNumber("Test Shooter Setpoint", 30000);
    	Robot.shooter.setShooterSetpoints(set, set);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.disable();
    	Robot.shooter.setShooterSetpoints(0, 0);
    	Robot.shooter.setShooterOutput(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
