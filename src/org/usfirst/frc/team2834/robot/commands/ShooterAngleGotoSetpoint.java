package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterAngleGotoSetpoint extends Command {
	
	double setpoint;

    public ShooterAngleGotoSetpoint(double setpoint) {
        super("Shooter Angle goto Setpoint: " + setpoint);
        requires(Robot.shooterAngle);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooterAngle.setOutput(0.0);
    	Robot.shooterAngle.setSetpoint(setpoint);
    	Robot.shooterAngle.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.shooterAngle.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterAngle.disable();
    	Robot.shooterAngle.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
