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
	
	//Drivetrain motor PWM ports
	public static final int FRONT_LEFT_DRIVETRAIN = 0;
	public static final int FRONT_RIGHT_DRIVETRAIN = 1;
	public static final int MIDDLE_LEFT_DRIVETRAIN = 2;
	public static final int MIDDLE_RIGHT_DRIVETRAIN = 3;
	public static final int BACK_LEFT_DRIVETRAIN = 4;
	public static final int BACK_RIGHT_DRIVETRAIN = 5;
	
	//Joystick buttons
	public static final int SET_DRIVE_MOTORS_BUTTON = 1;
	public static final int SET_DRIVE_REVERSE_BUTTON  = 1;
}
