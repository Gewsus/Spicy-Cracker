package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PusherOff extends Command {

    public PusherOff() {
        super("Pusher Off");
        requires(Robot.pusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.setPusherPosition(Pusher.IN);
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
