package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;
import org.usfirst.frc.team2834.robot.commands.HaloDrive;

import com.DashboardSender;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem for basic robot movement
 */
public class Drivetrain extends PIDSubsystem implements RobotMap, DashboardSender {
	
	private boolean reverse; //false: standard drive, true: reverse drive
	private boolean driveMotors = false; //false: 4, true: 6
	private double autoRotate = 0.0;
	private final double CENTER_SCALE = 0.4; //Apply an exponential scale function to the input
    
	/*
     * Order goes:
     * 0	3
     * 2	5
     * 1	4
     */
    private Victor motors[] = {
    	new Victor(FRONT_LEFT_DRIVETRAIN),
    	new Victor(BACK_LEFT_DRIVETRAIN),
    	new Victor(MIDDLE_LEFT_DRIVETRAIN),
    	new Victor(FRONT_RIGHT_DRIVETRAIN),
    	new Victor(BACK_RIGHT_DRIVETRAIN),
    	new Victor(MIDDLE_RIGHT_DRIVETRAIN)
    };   
    private AHRS gyro;
    
    public Drivetrain() {
    	super("Drivetrain", 0.006, 0.0, 0.0);
    	setInputRange(-180.0, 180.0);
    	setOutputRange(-1.0, 1.0);
    	getPIDController().setContinuous(true);
    	setAbsoluteTolerance(5.0);
    	
    	setReverse(false);
    	gyro = new AHRS(SerialPort.Port.kMXP, AHRS.SerialDataType.kProcessedData, (byte) 50);
    	gyro.zeroYaw();
    }

    public void haloDrive(double power, double rotate, boolean autoAdjust) {
    	//The rotate axis must be inverted if when the robot is in reverse
    	if(autoAdjust) {
    		rotate += autoRotate;
    	}
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
    	//Scale output values for the center wheels with the output of the outer wheels
    	if(scaleCenter) {
    		left = scale(left, CENTER_SCALE);
        	right = scale(right, CENTER_SCALE);
    	}
    	//These motor will only activate when Drivetrain is in 6 wheel-mode
    	motors[2].set(driveMotors ? left : 0.0);
		motors[5].set(driveMotors ? right : 0.0);
    }
    
    /**
     * 
     * Scales the input exponentially to the power of the scaler value
     * 
     * @param input Value to be scaled
     * @param scaler Scaled value that the input is to the power of.
     * @return scaled input value
     */
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
    
    public boolean isDriveMotorsSix() {
		return driveMotors;
	}

	public void setDriveMotors(boolean driveMotors) {
		this.driveMotors = driveMotors;
	}
	
    public void setAutoRotate(double autoRotate) {
		this.autoRotate = autoRotate;
	}

    public void setHoldSetpoint() {
    	setSetpoint(gyro.getYaw());
    }
    
	public void initDefaultCommand() {
        setDefaultCommand(new HaloDrive());
    }

	public boolean isUpSlope() {
		return gyro.getPitch() > 5;
	}
	
	public boolean isDownSlope() {
		return gyro.getPitch() < -5;
	}
	
	@Override
	protected double returnPIDInput() {
		return gyro.getYaw();
	}

	@Override
	protected void usePIDOutput(double output) {
		setAutoRotate(output);
	}

	@Override
	public void dashboardInit() {
		//SmartDashboard.putData("Gyro", gyro);
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putNumber("Gyro Yaw", gyro.getYaw());
		SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
		SmartDashboard.putBoolean("Gyro connected", gyro.isConnected());
		SmartDashboard.putBoolean("Reverse", isReverse());
		SmartDashboard.putBoolean("Drive Wheels", isDriveMotorsSix());
		SmartDashboard.putNumber("Auto Rotate", autoRotate);
	}
}

