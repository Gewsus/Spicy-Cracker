package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import com.DashboardSender;

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
/**
 * @author Adam Raine
 *
 */
public class Shooter extends Subsystem implements RobotMap, DashboardSender {
	
	public PIDController leftPID;
	public PIDController rightPID;
	private Victor leftMotor;
	private Victor rightMotor;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private final double P = 0.0000006;
	private final double I = 0.0;
	private final double D = -0.000001;
	private final double F = 0.0000018;
	public static final double FULL_FUCKING_POWER = 500000.0;
	public static final double DEFAULT_SETPOINT = 49000.0;
	public static final double DEFENSE_SETPOINT = 46800.0;
	public static final double LOW_SETPOINT = 22000.0;
	public static final double SLOPE;
	private final double TOLERANCE = 2000;
	
	static {
		//Create a linear representation of shooter power based on distance
		SLOPE = (DEFENSE_SETPOINT - DEFAULT_SETPOINT) / 56.0;
	}
	
    // Initialize your subsystem here
    public Shooter() {
        super("Shooter");
        //Initialize motors and their respective sensors
        leftMotor = new Victor(LEFT_SHOOTER_MOTOR);
        rightMotor = new Victor(RIGHT_SHOOTER_MOTOR);
        leftEncoder = new Encoder(LEFT_SHOOTER_ENCODER_A, LEFT_SHOOTER_ENCODER_B, true, EncodingType.k4X);
        rightEncoder = new Encoder(RIGHT_SHOOTER_ENCODER_A, RIGHT_SHOOTER_ENCODER_B, false, EncodingType.k4X);
        
        //Invert one motor, encoder is already set to be inverted for this motor
        leftMotor.setInverted(true);
        rightMotor.setInverted(false);
        
        //Ensure encoders send rate values to PID controllers and not distance
        leftEncoder.setPIDSourceType(PIDSourceType.kRate);
        rightEncoder.setPIDSourceType(PIDSourceType.kRate);
        
        //Initialize PID values for the shooter
        leftPID = new PIDController(P, I, D, F, leftEncoder, leftMotor);
        leftPID.setOutputRange(0.0, 1.0);
        leftPID.setAbsoluteTolerance(5000.0);
        leftPID.setToleranceBuffer(5);
        rightPID = new PIDController(P, I, D, F, rightEncoder, rightMotor);
        rightPID.setOutputRange(0.0, 1.0);
        rightPID.setAbsoluteTolerance(5000.0);
        rightPID.setToleranceBuffer(5);
    }
    
    public void reset() {
    	leftPID.reset();
    	rightPID.reset();
    }
    
    public void enable() {
    	leftPID.enable();
    	rightPID.enable();
    }
    
    public void disable() {
    	leftPID.disable();
    	rightPID.disable();
    }
    
    public boolean isLeftOnTarget() {
    	return Math.abs(leftPID.getError()) < TOLERANCE;
    }
    public boolean isRightOnTarget() {
    	return Math.abs(rightPID.getError()) < TOLERANCE;
    }
    public void setShooterSetpoints(double leftSetpoint, double rightSetpoint) {
    	leftPID.setSetpoint(leftSetpoint);
    	rightPID.setSetpoint(rightSetpoint);
    }
    
    public void setShooterOutput(double left, double right) {
    	leftMotor.set(left);
    	rightMotor.set(right);
    }
    
    public void dashboardInit() {
        //SmartDashboard.putData("Shooter Left PID", leftPID);
    	//SmartDashboard.putData("Test Setpoint", new ShooterSetSetpoint(40000));
    	//SmartDashboard.putData("Right Shooter PID", rightPID);
    }
    
    @Override
	public void dashboardPeriodic() {
    	SmartDashboard.putNumber("Right Encoder", rightEncoder.getRate());
    	SmartDashboard.putNumber("Left Encoder", leftEncoder.getRate());
    	SmartDashboard.putNumber("Left Error", leftPID.getError());
    	SmartDashboard.putNumber("Right Error", rightPID.getError());
    	SmartDashboard.putBoolean("Left Ready", isLeftOnTarget());
    	SmartDashboard.putBoolean("Right Ready", isRightOnTarget());
	}

	public void initDefaultCommand() {
    }
}
