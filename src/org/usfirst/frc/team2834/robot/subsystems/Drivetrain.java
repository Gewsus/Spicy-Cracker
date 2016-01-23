package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.HaloDrive;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for basic robot movement
 */
public class Drivetrain extends Subsystem implements RobotMap {
	
	private boolean reverse = false; //false: standard drive, true: reverse drive
	public boolean driveMotors = false; //false: 4, true: 6
	private double centerScale = 0.4; //Apply an exponential scale function to the input
    
    Victor motors[] = {
    	new Victor(FRONT_LEFT_DRIVETRAIN),
    	new Victor(BACK_LEFT_DRIVETRAIN),
    	new Victor(MIDDLE_LEFT_DRIVETRAIN),
    	new Victor(FRONT_RIGHT_DRIVETRAIN),
    	new Victor(BACK_RIGHT_DRIVETRAIN),
    	new Victor(MIDDLE_RIGHT_DRIVETRAIN)
    };
    
    public Drivetrain() {
    	super("Drivetrain");
    	setReverse(true);
    }

    public void haloDrive(double power, double rotate) {
    	rotate *= reverse ? -1.0 : 1.0;
    	double left = power + rotate;
    	double right = power - rotate;
    	setOutput(left, right, false);
    }
    
    public void setOutput(double left, double right, boolean scaleCenter) {
    	motors[0].set(left);
    	motors[1].set(left);
    	motors[3].set(right);
    	motors[4].set(right);
    	//Scale output values
    	left = scaleCenter ? scale(left, centerScale) : left;
    	right = scaleCenter ? scale(right, centerScale) : right;
    	//These motor will only activate when Drivetrain is in 6 wheel-mode
    	motors[2].set(driveMotors ? left : 0.0);
		motors[5].set(driveMotors ? right : 0.0);
    }
    
    private double scale(double input, double scaler) {
    	return input * Math.pow(Math.abs(input), scaler - 1.0);
    }
    
    public void setZero() {
    	setOutput(0, 0, false);
    }
    
    public boolean isReverse() {
		return reverse;
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

