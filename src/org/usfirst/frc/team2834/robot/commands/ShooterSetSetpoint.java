package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterSetSetpoint extends Command {
	
	private double leftSetpoint;
	private double rightSetpoint;

	public ShooterSetSetpoint() {
    	this(Shooter.DEFAULT_SETPOINT, Shooter.DEFAULT_SETPOINT);
    }
	
    public ShooterSetSetpoint(double setpoint) {
    	this(setpoint, setpoint);
    }
    
    public ShooterSetSetpoint(double leftSetpoint, double rightSetpoint) {
    	super("Shooter set setpoint: " + leftSetpoint + " : " + rightSetpoint, 10);
    	requires(Robot.shooter);
        this.leftSetpoint = leftSetpoint;
        this.rightSetpoint = rightSetpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.reset();
    	Robot.shooter.setEnabled(true);
    	Robot.shooter.setShooterSetpoints(leftSetpoint, rightSetpoint);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.setEnabled(false);
    	Robot.shooter.setShooterSetpoints(0, 0);
    	Robot.shooter.setShooterOutput(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
