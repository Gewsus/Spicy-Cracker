package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command is a dummy to cancel any commands running on Shooter or Angler
 * 
 */
public class FreeShooter extends Command {

    public FreeShooter() {
    	super("Free shooter");
        requires(Robot.shooter);
        requires(Robot.angler);
        requires(Robot.pusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.disable();
    	Robot.shooter.setShooterOutput(0, 0);
    	Robot.angler.disable();
    	Robot.angler.setOutput(0.0);
    	Robot.pusher.setPusherPosition(Pusher.IN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
