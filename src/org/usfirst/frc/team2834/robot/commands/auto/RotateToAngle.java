package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Rotates to a certain degree based on the robots zero position
 */
public class RotateToAngle extends Command {
	
	private double angle;
	private double forwardPower;

	public RotateToAngle(double angle) {
		this(angle, 0.0);
	}
	
    public RotateToAngle(double angle, double forwardPower) {
    	super("Rotate to Angle: [" + angle + "] [" + forwardPower + "]", 10);
        requires(Robot.drivetrain);
        this.angle = angle;
        this.forwardPower = forwardPower;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.reset();
    	Robot.drivetrain.setZero();
    	Robot.drivetrain.setSetpoint(angle);
    	Robot.drivetrain.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.haloDrive(-forwardPower, 0.0, true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.drivetrain.onTarget() || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.disable();
    	Robot.drivetrain.setSetpoint(Robot.drivetrain.getYaw());
    	Robot.drivetrain.setZero();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
