package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.ShooterAngle;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SelectAngleSetpoint extends Command {

    public SelectAngleSetpoint() {
        requires(Robot.shooterAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double setpoint;
    	double y = Robot.oi.operator.getY();
    	if(y >= 0.5) {
    		setpoint = ShooterAngle.UPPER_SETPOINT;
    	} else if(y < 0.5 && y > -0.5) {
    		setpoint = ShooterAngle.MIDDLE_SETPOINT;
    	} else {
    		setpoint = ShooterAngle.LOWER_SETPOINT;
    	}
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
