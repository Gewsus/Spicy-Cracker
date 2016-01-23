package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterSetSetpoints extends Command {
	
	private double leftSetpoint;
	private double rightSetpoint;

    public ShooterSetSetpoints(double setpoint) {
    	this(setpoint, setpoint);
    }
    
    public ShooterSetSetpoints(double leftSetpoint, double rightSetpoint) {
    	super("Shooter set setpoint: " + leftSetpoint + " " + rightSetpoint, 5.0);
    	requires(Robot.shooter);
        this.leftSetpoint = leftSetpoint;
        this.rightSetpoint = rightSetpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setEnabled(true);
    	Robot.shooter.setShooterSetpoints(leftSetpoint, rightSetpoint);
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
    	Robot.shooter.setEnabled(false);
    	Robot.shooter.setShooterOutput(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
