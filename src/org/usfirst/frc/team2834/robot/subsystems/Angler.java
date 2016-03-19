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
    
	//AnalogAbsoluteEncoder angleEncoder;
	Victor anglerMotor;
	Encoder anglerEncoder;
	public static final double UPPER_SETPOINT = 0.0;
	public static final double VERTICAL_SETPOINT = 35.0;
	public static final double MIDDLE_SETPOINT = 25.0;
	public static final double LOWER_SETPOINT = 100.0;
	//private final double MAX_ANGLE_RADIANS = (7.0 / 9.0) * Math.PI;
	//private final double POWER_PROPORTION = 0.15;
	//private final int[] inputTable = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, Integer.MAX_VALUE};
	//private final double[] outputTable = {};
	
	public Angler() {
		super("Angler", 0.004, 0.0000025, 0.0);
		anglerMotor = new Victor(ANGLER_MOTOR);
		anglerEncoder = new Encoder(ANGLER_ENCODER_A, ANGLER_ENCODER_B);
		//Initialize PID values for the angle of the shooter
        setOutputRange(-0.5, 0.5);
        getPIDController().setAbsoluteTolerance(1);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
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
    
    /**
     * Build team was unable to remove their head from their ass, and as a result the shooter
     * cannot hold a position on its own.  This method uses the angle determined by the angle encoder
     * to determine how much power is needed to hold the shooter at a steady position.
     * 
     * Never trust the build team.  Never.
     * 
     * @return Power for a motor [-1.0, 1.0]
     */
    /*public double getPowerByAngle() {
    	return SmartDashboard.getNumber("Angle proportion", POWER_PROPORTION) * Math.cos(getRealAngle());
    }
    
    public double getRealAngle() {
    	return (1.0 - (anglerEncoder.getDistance() / LOWER_SETPOINT)) * MAX_ANGLE_RADIANS;
    }
    
    public double realAngleToVoltage(double angle) {
    	return (1.0 - (angle / MAX_ANGLE_RADIANS)) * LOWER_SETPOINT;
    }*/
    
	protected double returnPIDInput() {
		return anglerEncoder.getDistance();
	}

	protected void usePIDOutput(double output) {
		anglerMotor.set(-output);
	}

	/*public double getTableOutput() {
		int id = 0;
		double dist = angleEncoder.getDistance();
		//Find which id the distance corresponds to
		for(int i = 0; i < inputTable.length - 1; i++) {
			if(dist > inputTable[i] && dist < inputTable[i + 1]) {
				id = i;
			}
		}
		return (((outputTable[id + 1] - outputTable[id]) / 2.0) * (dist - inputTable[id])) + outputTable[id];
	}*/
	
	@Override
	public void dashboardInit() {
		SmartDashboard.putData("Angle PID", getPIDController());
		//SmartDashboard.putNumber("Angle proportion", POWER_PROPORTION);
	}

	@Override
	public void dashboardPeriodic() {
		SmartDashboard.putNumber("Angle Distance", anglerEncoder.getDistance());
		//SmartDashboard.putNumber("Angle Error", getPIDController().getError());
		//SmartDashboard.putNumber("Angle Power By Angle", getPowerByAngle());
		//SmartDashboard.putNumber("Angle Comp", getPowerByAngle() + getPIDController().get());
		SmartDashboard.putBoolean("Angle on Target", onTarget());
		//SmartDashboard.putNumber("Real Angle", getRealAngle());
	}
}

