package org.usfirst.frc.team2834.robot.subsystems;

import org.usfirst.frc.team2834.robot.RobotMap;

import com.DashboardSender;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem that controls what angle the shooter is at
 */
public class Angler extends PIDSubsystem implements RobotMap, DashboardSender {
    
	private Victor anglerMotor;
	private Encoder anglerEncoder;
	public static final double UPPER_SETPOINT = 0.0;
	public static final double VERTICAL_SETPOINT = 35.0;
	public static final double MIDDLE_SETPOINT = 25.0;
	public static final double LOWER_SETPOINT = 100.0;
	
	public Angler() {
		super("Angler", 0.004, 0.0000025, 0.0);
		anglerMotor = new Victor(ANGLER_MOTOR);
		anglerEncoder = new Encoder(ANGLER_ENCODER_A, ANGLER_ENCODER_B);
		//Initialize PID values for the angle of the shooter
        setOutputRange(-0.5, 0.5);
        getPIDController().setAbsoluteTolerance(1);
	}
	
    public void initDefaultCommand() {
        //Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setOutput(double output) {
    	anglerMotor.set(output);
    }
    
    public void reset() {
    	getPIDController().reset();
    }
    
    public void zero() {
    	anglerEncoder.reset();
    }
    
	protected double returnPIDInput() {
		return anglerEncoder.getDistance();
	}

	protected void usePIDOutput(double output) {
		anglerMotor.set(-output);
	}
	
	@Override
	public void dashboardInit() {
		SmartDashboard.putData("Angle PID", getPIDController());
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putNumber("Angle Distance", anglerEncoder.getDistance());
		SmartDashboard.putBoolean("Angle on Target", onTarget());
	}
}

