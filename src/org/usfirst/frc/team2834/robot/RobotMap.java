package org.usfirst.frc.team2834.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public interface RobotMap {
    //Joystick USB number
	int RIGHT_DRIVE_USB = 0;
	int LEFT_DRIVE_USB = 1;
	int OPERATOR_USB = 2;
	
	//Joystick buttons
	int SET_DRIVE_MOTORS_BUTTON = 1;
	int SET_DRIVE_REVERSE_BUTTON  = 1;
	int ROTATE_ON_TARGET = 3;
	int SHOOT_BUTTON = 1;
	int SHOOTER_STEP_UP_BUTTON = 3;
	int SHOOTER_STEP_DOWN_BUTTON = 2;
	int DEFAULT_SHOOTER_SETPOINT_BUTTON = 4;
	int AUTO_SHOOTER_SETPOINT_BUTTON = 5;
	int SHOOTER_ANGLE_OVERRIDE_BUTTON = 6;
	int SHOOTER_OVERRIDE_BUTTON = 7;
	int SELECT_SHOOTER_ANGLE_SETPOINT_BUTTON = 10;
	int CANCEL_SHOOTER_COMMANDS = 9;
	
	//Drivetrain motor PWM ports
	int FRONT_LEFT_DRIVETRAIN = 0;
	int FRONT_RIGHT_DRIVETRAIN = 1;
	int MIDDLE_LEFT_DRIVETRAIN = 2;
	int MIDDLE_RIGHT_DRIVETRAIN = 3;
	int BACK_LEFT_DRIVETRAIN = 4;
	int BACK_RIGHT_DRIVETRAIN = 5;
	
	//Shooter ports
	int LEFT_SHOOTER_MOTOR = 7;
	int LEFT_SHOOTER_ENCODER_A = 0;
	int LEFT_SHOOTER_ENCODER_B = 1;
	int RIGHT_SHOOTER_MOTOR = 6;
	int RIGHT_SHOOTER_ENCODER_A = 2;
	int RIGHT_SHOOTER_ENCODER_B = 3;
	int SHOOTER_PUSH_ACTUATOR = 9;
	int SHOOTER_ANGLE_MOTOR = 8;
	//int LEFT_SHOOTER_ANGLE_MOTOR = 10;
	int SHOOTER_ANGLE_ENCODER = 0;
}
