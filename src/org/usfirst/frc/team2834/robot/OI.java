package org.usfirst.frc.team2834.robot;

import org.usfirst.frc.team2834.robot.commands.SetDriveSixWheels;
import org.usfirst.frc.team2834.robot.commands.ToggleDriveReverse;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements RobotMap{
	
	//Joysticks, two for driving, one for other functions
	public final Joystick rightDrive;
	public final Joystick leftDrive;
	public final Joystick operator;
	
	//Object references to individual buttons on each joystick
	public final JoystickButton setDriveMotors;
	public final JoystickButton setDriveReverse;
	
	public OI() {
		//Initialize joysticks
		rightDrive = new Joystick(RobotMap.RIGHT_DRIVE_USB);
		leftDrive = new Joystick(RobotMap.LEFT_DRIVE_USB);
		operator = new Joystick(RobotMap.OPERATOR_USB);
		
		//Initialize buttons
		setDriveMotors = new JoystickButton(rightDrive, SET_DRIVE_MOTORS_BUTTON);
		setDriveReverse = new JoystickButton(leftDrive, SET_DRIVE_REVERSE_BUTTON);
		
		//Set button functions
		setDriveMotors.whenPressed(new SetDriveSixWheels(true));
		setDriveMotors.whenReleased(new SetDriveSixWheels(false));
		setDriveReverse.whenPressed(new ToggleDriveReverse());
	}
}

