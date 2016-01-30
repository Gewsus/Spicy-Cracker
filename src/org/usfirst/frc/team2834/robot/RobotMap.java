package org.usfirst.frc.team2834.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public interface RobotMap {
    //Joystick USB number
	public static final int RIGHT_DRIVE_USB = 0;
	public static final int LEFT_DRIVE_USB = 1;
	public static final int OPERATOR_USB = 2;
	
	//Joystick buttons
	public static final int SET_DRIVE_MOTORS_BUTTON = 1;
	public static final int SET_DRIVE_REVERSE_BUTTON  = 1;
	public static final int SHOOT_BUTTON = 1;
	public static final int SHOOTER_STEP_UP_BUTTON = 3;
	public static final int SHOOTER_STEP_DOWN_BUTTON = 2;
	public static final int DEFAULT_SHOOTER_SETPOINT_BUTTON = 4;
	public static final int AUTO_SHOOTER_SETPOINT_BUTTON = 5;
	public static final int SHOOTER_ANGLE_OVERRIDE_BUTTON = 6;
	public static final int SHOOTER_OVERRIDE_BUTTON = 7;
	public static final int SELECT_SHOOTER_ANGLE_SETPOINT_BUTTON = 10;
	
	//Drivetrain motor PWM ports
	public static final int FRONT_LEFT_DRIVETRAIN = 0;
	public static final int FRONT_RIGHT_DRIVETRAIN = 1;
	public static final int MIDDLE_LEFT_DRIVETRAIN = 2;
	public static final int MIDDLE_RIGHT_DRIVETRAIN = 3;
	public static final int BACK_LEFT_DRIVETRAIN = 4;
	public static final int BACK_RIGHT_DRIVETRAIN = 5;
	
	//Shooter ports
	public static final int LEFT_SHOOTER_MOTOR = 6;
	public static final int LEFT_SHOOTER_ENCODER_A = 0;
	public static final int LEFT_SHOOTER_ENCODER_B = 1;
	public static final int RIGHT_SHOOTER_MOTOR = 7;
	public static final int RIGHT_SHOOTER_ENCODER_A = 2;
	public static final int RIGHT_SHOOTER_ENCODER_B = 3;
	public static final int SHOOTER_PUSH_ACTUATOR = 9;
	public static final int SHOOTER_ANGLE_MOTOR = 8;
	public static final int SHOOTER_ANGLE_ENCODER = 0;
}
