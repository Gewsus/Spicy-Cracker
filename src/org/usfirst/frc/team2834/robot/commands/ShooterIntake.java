package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.subsystems.Pusher;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterIntake extends Command {

    public ShooterIntake() {
        super("Shooter Intake");
        requires(Robot.shooter);
        requires(Robot.pusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.pusher.setPusherPosition(Pusher.IN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooter.setShooterOutput(-0.5, -0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.setShooterOutput(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
