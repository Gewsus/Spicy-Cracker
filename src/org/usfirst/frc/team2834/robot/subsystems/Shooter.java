package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem that controls the shooting functionality of the robot
 */
public class Shooter extends Subsystem implements RobotMap {
	
	PIDController leftPID;
	PIDController rightPID;
	Victor leftMotor;
	Victor rightMotor;
	Encoder leftEncoder;
	Encoder rightEncoder;
	

    // Initialize your subsystem here
    public Shooter() {
        super("Shooter");
        
        //Initialize motors and their respective sensors
        leftMotor = new Victor(LEFT_SHOOTER_MOTOR);
        rightMotor = new Victor(RIGHT_SHOOTER_MOTOR);
        leftEncoder = new Encoder(LEFT_SHOOTER_ENCODER_A, LEFT_SHOOTER_ENCODER_B, false, EncodingType.k4X);
        rightEncoder = new Encoder(RIGHT_SHOOTER_ENCODER_A, RIGHT_SHOOTER_ENCODER_B, true, EncodingType.k4X);
        
        //Invert one motor, encoder is already set to be inverted for this motor
        rightMotor.setInverted(true);
        
        //Ensure encoders send rate values to PID controllers and not distance
        leftEncoder.setPIDSourceType(PIDSourceType.kRate);
        rightEncoder.setPIDSourceType(PIDSourceType.kRate);
        
        //Initialize PID values for the shooter
        leftPID = new PIDController(0.0005, 0.0, 0.0, 0.0005, leftEncoder, leftMotor);
        leftPID.setInputRange(0.0, 2500.0);
        leftPID.setOutputRange(0.0, 1.0);
        leftPID.setAbsoluteTolerance(50.0);
        rightPID = new PIDController(0.0005, 0.0, 0.0, 0.0005, rightEncoder, rightMotor);
        rightPID.setInputRange(0.0, 2500.0);
        rightPID.setOutputRange(0.0, 1.0);
        rightPID.setAbsoluteTolerance(50.0);
        
        //Send one PIDController to the dashboard.
        //SmartDashboard will let you configure PID constants on the driver station instead of changing the code each time.
        SmartDashboard.putData("Shooter Left PID", leftPID);
    }
    
    public void setEnabled(boolean enable) {
    	if(enable) {
    		leftPID.enable();
    		rightPID.enable();
    	} else {
    		leftPID.disable();
    		rightPID.disable();
    	}
    }
    
    public boolean isLeftOnTarget() {
    	return leftPID.onTarget();
    }
    public boolean isRightOnTarget() {
    	return rightPID.onTarget();
    }
    
    public void setShooterSetpoints(double leftSetpoint, double rightSetpoint) {
    	leftPID.setSetpoint(leftSetpoint);
    	rightPID.setSetpoint(rightSetpoint);
    }
    
    public void setShooterOutput(double left, double right) {
    	leftMotor.set(left);
    	rightMotor.set(right);
    }
    
    public void initDefaultCommand() {
    	//Shooter does not need a default command
    }
}
