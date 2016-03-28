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
	public double startingAngle = 0.0; //The robot may start pointing in different directions
	private double autoRotate = 0.0;
	private final double CENTER_SCALE = 0.4; //Apply an exponential scale function to the input
	private final double TOLERANCE = 2; //Value on either side of setpoint to register on target
	//private final double FEED_FORWARD = 0.7; //Value required for the drivetrain to actually move.  Thanks build team.

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
    	super("Drivetrain", 0.001, 0.0001, 0.0);
    	setInputRange(-180.0, 180.0);
    	setOutputRange(-1.0, 1.0);
    	getPIDController().setContinuous(true);
    	setAbsoluteTolerance(TOLERANCE);
    	getPIDController().setToleranceBuffer(5);
    	
    	setReverse(true);
    	gyro = new AHRS(SerialPort.Port.kMXP, AHRS.SerialDataType.kProcessedData, (byte) 50);
    	gyro.zeroYaw();
    }

    public void haloDrive(double power, double rotate, boolean autoAdjust) {
    	//The rotate axis must be inverted if when the robot is in reverse
    	if(autoAdjust) {
    		rotate += autoRotate;
    	}
    	if(reverse) {
    		rotate *= -1.0;
    	}
    	double left = power + rotate;
    	double right = power - rotate;
    	setOutput(left, right, true);
    }
    
	@Override
    public void setSetpointRelative(double setpointRelative) {
    	setSetpoint(coerceToYawRange(setpointRelative + returnPIDInput()));
    }
    
	public void reset() {
		getPIDController().reset();
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
    public double scale(double input, double scaler) {
    	return input * Math.pow(Math.abs(input), scaler - 1.0);
    }
    
    public void setZero() {
    	setOutput(0, 0, false);
    }
    
    public void rezero() {
    	gyro.zeroYaw();
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

    public double getYaw() {
    	return coerceToYawRange(gyro.getYaw() + startingAngle);
    }
    
    public double getPitch() {
    	return gyro.getPitch();
    }
    
    public double getRoll() {
    	return gyro.getRoll();
    }
    
	public void initDefaultCommand() {
        setDefaultCommand(new HaloDrive());
    }

	public boolean isUpSlope() {
		return getPitch() > 5 || getRoll() > 5;
	}
	
	public boolean isDownSlope() {
		return getPitch() < -5 || getRoll() < -5;
	}
	
	public boolean isStable() {
		return Math.abs(gyro.getRawGyroX()) < 5 && Math.abs(gyro.getRawGyroY()) < 5;
	}
	
	private double coerceToYawRange(double yaw) {
		if(yaw < -180) {
    		return coerceToYawRange(yaw += 360);
    	} else if(yaw > 180) {
    		return coerceToYawRange(yaw -= 360);
    	}
		return yaw;
	}
	
	@Override
	protected double returnPIDInput() {
		return getYaw();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (onTarget()) {
			autoRotate = 0.0;
		} else {
			//The robot this year does not rotate unless a considerable amount of power is applied
			//This code adds a constant feed forward to the PID output to ensure the robot does this.
			/*if (output < 0.0) {
				output -= FEED_FORWARD;
			} else if (output > 0.0) {
				output += FEED_FORWARD;
			}
			autoRotate = output;*/
			autoRotate = scale(output, 0.1);
		}
	}

	@Override
	public boolean onTarget() {
		return Math.abs(getPIDController().getError()) < TOLERANCE;
	}
	
	@Override
	public void dashboardInit() {
		//SmartDashboard.putData("Gyro", gyro);
		//SmartDashboard.putData("Gyro PID", getPIDController());
	}
	
	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putNumber("Gyro Yaw", getYaw());
		SmartDashboard.putNumber("Gyro Pitch", getPitch());
		SmartDashboard.putBoolean("Gyro connected", gyro.isConnected());
		SmartDashboard.putBoolean("Reverse", isReverse());
		SmartDashboard.putBoolean("Drive Wheels", isDriveMotorsSix());
		//SmartDashboard.putNumber("Auto Rotate", autoRotate);
		//SmartDashboard.putBoolean("Gyro On Target", onTarget());
		SmartDashboard.putNumber("Gyro Setpoint", getSetpoint());
		//SmartDashboard.putBoolean("On Goal", Math.abs(Robot.vision.getGamma()) < TOLERANCE * Math.PI / 180.0);
	}
}

