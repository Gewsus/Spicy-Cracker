package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AnglerGotoSetpoint extends Command {
	
	double setpoint;

    public AnglerGotoSetpoint(double setpoint) {
        super("Angle goto Setpoint: " + setpoint);
        requires(Robot.angler);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.angler.setOutput(0.0);
    	Robot.angler.setSetpoint(setpoint);
    	Robot.angler.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.angler.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.angler.disable();
    	Robot.angler.setOutput(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
