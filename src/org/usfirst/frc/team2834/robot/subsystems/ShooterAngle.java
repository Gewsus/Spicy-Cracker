package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import com.AnalogAbsoluteEncoder;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Subsystem that controls what angle the shooter is at
 */
public class ShooterAngle extends PIDSubsystem implements RobotMap {
    
	AnalogAbsoluteEncoder angleEncoder;
	Victor angleMotor;
	//private double angleZero;  //Reading from the encoder at the start of the match;// Put methods for controlling this subsystem

	public ShooterAngle() {
		super("Shooter Angle", 0.005, 0.0, 0.0);
		angleEncoder = new AnalogAbsoluteEncoder(SHOOTER_ANGLE_ENCODER);
		angleMotor = new Victor(SHOOTER_ANGLE_MOTOR);
		
		//Initialize PID values for the angle of the shooter
        //setInputRange(0.015, 4.987);
        setOutputRange(-1.0, 1.0);
        setAbsoluteTolerance(0.014);
        //setContinuous(true);
        //zeroAngle();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void setSetpoint(double setpoint) {
    	/*if(setpoint > 90) {
			setpoint = 90;
		} else if(setpoint < 0) {
			setpoint = 0;
		}
		double newSetpoint = angleZero - (4.972 * setpoint / 360.0);
		if(newSetpoint < 0.015) {
			newSetpoint += 4.972;
		} else if(newSetpoint > 4.987) {
			newSetpoint -= 4.972;
		}*/
    	super.setSetpoint(setpoint);
    }
    
    public void setOutput(double output) {
    	angleMotor.set(output);
    }
    
    public void zero() {
    	angleEncoder.zero();
    }
    
	protected double returnPIDInput() {
		return angleEncoder.getDistance();
	}

	protected void usePIDOutput(double output) {
		angleMotor.set(output);
	}
}

