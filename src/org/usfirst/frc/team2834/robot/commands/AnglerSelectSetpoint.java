package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Angler;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AnglerSelectSetpoint extends Command {

	private final double STEP_VALUE = 5.0;
	
    public AnglerSelectSetpoint() {
    	super("Select angle setpoint");
        requires(Robot.angler);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double setpoint;
    	double y = Robot.oi.operator.getY();
    	if(y >= 0.5) {
    		setpoint = Angler.UPPER_SETPOINT;
    	} else if(y < 0.5 && y > -0.5) {
    		setpoint = Angler.MIDDLE_SETPOINT;
    	} else {
    		setpoint = Angler.LOWER_SETPOINT;
    	}
    	Robot.angler.reset();
    	Robot.angler.setSetpoint(setpoint);
    	Robot.angler.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.oi.stepUp.get()) {
    		Robot.angler.setSetpointRelative(STEP_VALUE);
    	} else if (Robot.oi.stepDown.get()) {
    		Robot.angler.setSetpointRelative(-STEP_VALUE);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.oi.cancelShooter.get();
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
