package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import com.AnalogAbsoluteEncoder;
import com.DashboardSender;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem that controls what angle the shooter is at
 */
public class ShooterAngle extends PIDSubsystem implements RobotMap, DashboardSender {
    
	AnalogAbsoluteEncoder angleEncoder;
	Victor angleMotor;
	//private double angleZero;  //Reading from the encoder at the start of the match
	public static final double UPPER_SETPOINT = 0.0;
	public static final double MIDDLE_SETPOINT = 16.0;
	public static final double LOWER_SETPOINT = 32.0;
	public static final double MAX_ANGLE_RADIANS = (7.0 / 9.0) * Math.PI;
	
	public ShooterAngle() {
		super("Shooter Angle", 0.0156, 0.0, 0.0);
		angleEncoder = new AnalogAbsoluteEncoder(SHOOTER_ANGLE_ENCODER);
		angleMotor = new Victor(SHOOTER_ANGLE_MOTOR);
		angleEncoder.setInverted(true);
		//Initialize PID values for the angle of the shooter
        //setInputRange(0.015, 4.987);
        setOutputRange(-0.5, 0.5);
        setAbsoluteTolerance(0.014);
        //setContinuous(true);
        //zeroAngle();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /*@Override
    public void setSetpoint(double setpoint) {
    	if(setpoint > 90) {
			setpoint = 90;
		} else if(setpoint < 0) {
			setpoint = 0;
		}
		double newSetpoint = angleZero - (4.972 * setpoint / 360.0);
		if(newSetpoint < 0.015) {
			newSetpoint += 4.972;
		} else if(newSetpoint > 4.987) {
			newSetpoint -= 4.972;
		}
    	super.setSetpoint(newSetpoint);
    }*/
    
    public void setOutput(double output) {
    	angleMotor.set(output);
    }
    
    public void zero() {
    	angleEncoder.zero();
    }
    
    /**
     * Build team was unable to remove their head from their ass, and as a result the shooter
     * cannot hold a position on its own.  This method uses the angle determined by the angle encoder
     * to determine how much power is needed to hold the shooter at a steady position.
     * 
     * Never trust the build team.  Never.
     * 
     * @return Power for a motor [-1.0, 1.0]
     */
    public double getPowerByAngle() {
    	return 0.67543 * Math.cos((1.0 - (angleEncoder.getDistance() / LOWER_SETPOINT)) * MAX_ANGLE_RADIANS);
    }
    
	protected double returnPIDInput() {
		return angleEncoder.getDistance();
	}

	protected void usePIDOutput(double output) {
		angleMotor.set(getPowerByAngle() + output);
	}

	@Override
	public void dashboardInit() {
		SmartDashboard.putData("Angle Encoder", angleEncoder);
		SmartDashboard.putData("Angle PID", getPIDController());
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putNumber("Angle Voltage", angleEncoder.getAnalogInput().getVoltage());
		SmartDashboard.putNumber("Angle Distance", angleEncoder.getDistance());
		SmartDashboard.putNumber("Angle Error", getPIDController().getError());
		SmartDashboard.putNumber("Angle Power By Angle", getPowerByAngle());
		SmartDashboard.putNumber("Angle Comp", getPowerByAngle() + getPIDController().get());
	}
}

