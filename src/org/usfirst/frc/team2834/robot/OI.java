package org.usfirst.frc.team2834.robot;

import org.usfirst.frc.team2834.robot.commands.*;
import org.usfirst.frc.team2834.robot.subsystems.Shooter;

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
	public final JoystickButton shoot;
	public final JoystickButton stepUp;
	public final JoystickButton stepDown;
	public final JoystickButton defaultSetpoint;
	public final JoystickButton autoSetpoint;
	public final JoystickButton shooterOverride;
	public final JoystickButton angleOverride;
	public final JoystickButton selectSetpoint;
	
	public OI() {
		//Initialize joysticks
		rightDrive = new Joystick(RobotMap.RIGHT_DRIVE_USB);
		leftDrive = new Joystick(RobotMap.LEFT_DRIVE_USB);
		operator = new Joystick(RobotMap.OPERATOR_USB);
		
		//Initialize buttons
		setDriveMotors = new JoystickButton(rightDrive, SET_DRIVE_MOTORS_BUTTON);
		setDriveReverse = new JoystickButton(leftDrive, SET_DRIVE_REVERSE_BUTTON);
		shoot = new JoystickButton(operator, SHOOT_BUTTON);
		stepUp = new JoystickButton(operator, SHOOTER_STEP_UP_BUTTON);
		stepDown = new JoystickButton(operator, SHOOTER_STEP_DOWN_BUTTON);
		defaultSetpoint = new JoystickButton(operator, DEFAULT_SHOOTER_SETPOINT_BUTTON);
		autoSetpoint = new JoystickButton(operator, AUTO_SHOOTER_SETPOINT_BUTTON);
		shooterOverride = new JoystickButton(operator, SHOOTER_OVERRIDE_BUTTON);
		angleOverride = new JoystickButton(operator, SHOOTER_ANGLE_OVERRIDE_BUTTON);
		selectSetpoint = new JoystickButton(operator, SELECT_SHOOTER_ANGLE_SETPOINT_BUTTON);
		
		//Set button functions
		setDriveMotors.whenPressed(new SetDriveSixWheels(true));
		setDriveMotors.whenReleased(new SetDriveSixWheels(false));
		setDriveReverse.whenPressed(new ToggleDriveReverse());
		shoot.whenPressed(new ShooterPushToShoot());
		defaultSetpoint.whileHeld(new ShooterSetSetpoints(Shooter.DEFAULT_SETPOINT));
		shooterOverride.whileHeld(new ShooterOverride());
		angleOverride.whileHeld(new ShooterAngleOverride());
		selectSetpoint.whenPressed(new SelectAngleSetpoint());
	}
}

