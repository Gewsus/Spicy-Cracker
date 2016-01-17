package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.HaloDrive;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem implements RobotMap {
	
	private boolean reverse = false; //false: standard drive, true: reverse drive
	public boolean driveMotors = false; //false: 4, true: 6
    
    Victor motors[] = {
    	new Victor(FRONT_LEFT_DRIVETRAIN),
    	new Victor(BACK_LEFT_DRIVETRAIN),
    	new Victor(MIDDLE_LEFT_DRIVETRAIN),
    	new Victor(FRONT_RIGHT_DRIVETRAIN),
    	new Victor(BACK_RIGHT_DRIVETRAIN),
    	new Victor(MIDDLE_RIGHT_DRIVETRAIN)
    };
    
    public Drivetrain() {
    	motors[0].setInverted(false);
    	motors[1].setInverted(false);
    	motors[2].setInverted(false);
    	motors[3].setInverted(true);
    	motors[4].setInverted(true);
    	motors[5].setInverted(true);
    }

    public void haloDrive(double power, double rotate) {
    	rotate *= reverse ? -1.0 : 1.0;
    	double left = power + rotate;
    	double right = power - rotate;
    	motors[0].set(left);
    	motors[1].set(left);
    	motors[3].set(right);
    	motors[4].set(right);
    	//These motor will only activate when Drivetrain is in 6 wheel-mode
    	motors[2].set(driveMotors ? left : 0.0);
		motors[5].set(driveMotors ? right : 0.0);
    }
    
    public void setOutput(double left, double right) {
    	motors[0].set(left);
    	motors[1].set(left);
    	motors[3].set(right);
    	motors[4].set(right);
    	motors[2].set(driveMotors ? left : 0.0);
		motors[5].set(driveMotors ? right : 0.0);
    }
    
    public void setZero() {
    	motors[0].set(0);
    	motors[1].set(0);
    	motors[2].set(0);
    	motors[3].set(0);
    	motors[4].set(0);
    	motors[5].set(0);
    }
    
    public void setReverse(boolean reverse) {
    	this.reverse = reverse;
    	motors[0].setInverted(reverse);
    	motors[1].setInverted(reverse);
    	motors[2].setInverted(reverse);
    	motors[3].setInverted(!reverse);
    	motors[4].setInverted(!reverse);
    	motors[5].setInverted(!reverse);
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new HaloDrive());
    }
}

