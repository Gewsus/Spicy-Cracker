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
	int AUTO_ROTATE_BUTTON = 3;
	int SHOOT_BUTTON = 1;
	int LOW_GOAL_SETPOINT_BUTTON = 3;
	int AUTO_SETPOINT_BUTTON = 2;
	int DEFAULT_SETPOINT_BUTTON = 4;
	int DEFENSE_SETPOINT_BUTTON = 5;
	int ANGLER_OVERRIDE_BUTTON = 6;
	int SHOOTER_OVERRIDE_BUTTON = 7;
	int SELECT_ANGLER_SETPOINT_BUTTON = 10;
	int CANCEL_SHOOTER_BUTTON = 9;
	int SHOOTER_VIEW_TOGGLE_BUTTON = 2;
	int SHOOTER_INTAKE_BUTTON = 8;
	int RESUME_HALO_BUTTON = 2;
	int DETATCH_DRAWBRIDGE_BUTTON = 12;
	int FULL_SETPOINT_BUTTON = 11;
	
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
	int ANGLER_MOTOR = 8;
	int ANGLER_ENCODER_A = 6;
	int ANGLER_ENCODER_B = 7;
}
