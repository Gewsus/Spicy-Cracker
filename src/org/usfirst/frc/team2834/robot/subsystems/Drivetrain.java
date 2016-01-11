package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.HaloDrive;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem implements RobotMap {
    
    Victor motors[] = {
    	new Victor(FRONT_LEFT_DRIVETRAIN),
    	new Victor(BACK_LEFT_DRIVETRAIN),
    	new Victor(FRONT_RIGHT_DRIVETRAIN),
    	new Victor(BACK_RIGHT_DRIVETRAIN)
    };
    
    public Drivetrain() {
    	motors[0].setInverted(false);
    	motors[1].setInverted(false);
    	motors[2].setInverted(true);
    	motors[3].setInverted(true);
    }

    public void haloDrive(double power, double rotate) {
    	double left = power - rotate;
    	double right = power + rotate;
    	motors[0].set(left);
    	motors[1].set(left);
    	motors[2].set(right);
    	motors[3].set(right);
    }
    
    public void tankDrive(double left, double right) {
    	motors[0].set(left);
    	motors[1].set(left);
    	motors[2].set(right);
    	motors[3].set(right);
    }
    
    public void setZero() {
    	motors[0].set(0);
    	motors[1].set(0);
    	motors[2].set(0);
    	motors[3].set(0);
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new HaloDrive());
    }
}

