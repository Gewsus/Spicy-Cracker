package org.usfirst.frc.team2834.robot;

import org.usfirst.frc.team2834.robot.commands.*;
import org.usfirst.frc.team2834.robot.commands.auto.DriveDistance;
import org.usfirst.frc.team2834.robot.commands.vision.AutoCenterGoal;
import org.usfirst.frc.team2834.robot.commands.vision.AutoDriveToTarget;
import org.usfirst.frc.team2834.robot.commands.vision.ShootDistance;
import org.usfirst.frc.team2834.robot.commands.vision.ShootZeta;
import org.usfirst.frc.team2834.robot.commands.vision.TestAutoLineUp;
import org.usfirst.frc.team2834.robot.commands.vision.SwapCameraView;
import org.usfirst.frc.team2834.robot.subsystems.Shooter;

import com.DashboardSender;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements RobotMap, DashboardSender {
	
	//Joysticks, two for driving, one for other functions
	public final Joystick rightDrive;
	public final Joystick leftDrive;
	public final Joystick operator;
	
	//Object references to individual buttons on each joystick
	public final JoystickButton setDriveMotors;
	public final JoystickButton setDriveReverse;
	public final JoystickButton shoot;
	public final JoystickButton lowGoalSetpoint;
	public final JoystickButton autoSetpoint;
	public final JoystickButton defaultSetpoint;
	public final JoystickButton defenseSetpoint;
	public final JoystickButton shooterOverride;
	public final JoystickButton anglerOverride;
	public final JoystickButton selectAnglerSetpoint;
	public final JoystickButton cancelShooter;
	public final JoystickButton autoRotate;
	public final JoystickButton shooterViewToggle;
	public final JoystickButton shooterIntake;
	public final JoystickButton resumeHalo;
	public final JoystickButton detatchDrawbridge;
	public final JoystickButton fullSetpoint;
	
	public OI() {
		//Initialize joysticks
		rightDrive = new Joystick(RobotMap.RIGHT_DRIVE_USB);
		leftDrive = new Joystick(RobotMap.LEFT_DRIVE_USB);
		operator = new Joystick(RobotMap.OPERATOR_USB);
		
		//Initialize buttons
		setDriveMotors = new JoystickButton(rightDrive, SET_DRIVE_MOTORS_BUTTON);
		setDriveReverse = new JoystickButton(leftDrive, SET_DRIVE_REVERSE_BUTTON);
		shoot = new JoystickButton(operator, SHOOT_BUTTON);
		lowGoalSetpoint = new JoystickButton(operator, LOW_GOAL_SETPOINT_BUTTON);
		autoSetpoint = new JoystickButton(operator, AUTO_SETPOINT_BUTTON);
		defaultSetpoint = new JoystickButton(operator, DEFAULT_SETPOINT_BUTTON);
		defenseSetpoint = new JoystickButton(operator, DEFENSE_SETPOINT_BUTTON);
		shooterOverride = new JoystickButton(operator, SHOOTER_OVERRIDE_BUTTON);
		anglerOverride = new JoystickButton(operator, ANGLER_OVERRIDE_BUTTON);
		selectAnglerSetpoint = new JoystickButton(operator, SELECT_ANGLER_SETPOINT_BUTTON);
		cancelShooter = new JoystickButton(operator, CANCEL_SHOOTER_BUTTON);
		autoRotate = new JoystickButton(rightDrive, AUTO_ROTATE_BUTTON);
		shooterViewToggle = new JoystickButton(rightDrive, SHOOTER_VIEW_TOGGLE_BUTTON);
		shooterIntake = new JoystickButton(operator, SHOOTER_INTAKE_BUTTON);
		resumeHalo = new JoystickButton(leftDrive, RESUME_HALO_BUTTON);
		detatchDrawbridge = new JoystickButton(rightDrive, DETATCH_DRAWBRIDGE_BUTTON);
		fullSetpoint = new JoystickButton(operator, FULL_SETPOINT_BUTTON);
		
		//Set button functions
		setDriveMotors.whenPressed(new SetDriveSixWheels(true));
		setDriveMotors.whenReleased(new SetDriveSixWheels(false));
		setDriveReverse.whenPressed(new ToggleDriveReverse());
		shoot.whenPressed(new PushToShoot());
		defaultSetpoint.whileHeld(new ShooterSetSetpoint());
		defaultSetpoint.whenReleased(new FreeShooter());
		shooterOverride.whileHeld(new ShooterOverride());
		anglerOverride.whileHeld(new AnglerOverride());
		//selectSetpoint.whenPressed(new SelectAngleSetpoint());
		cancelShooter.whenPressed(new FreeShooter());
		autoRotate.whenPressed(new AutoCenterGoal());
		shooterViewToggle.whenPressed(new SwapCameraView());
		shooterIntake.whileHeld(new ShooterIntake());
		resumeHalo.whenPressed(new HaloDrive());
		defenseSetpoint.whileHeld(new ShooterSetSetpoint(Shooter.DEFENSE_SETPOINT));
		autoSetpoint.whileHeld(new ShootDistance());
		lowGoalSetpoint.whileHeld(new ShooterSetSetpoint(Shooter.LOW_SETPOINT));
		detatchDrawbridge.whenPressed(new AnglerDrawbridgeDetatch());
		fullSetpoint.whileActive(new ShooterSetSetpoint(Shooter.FULL_FUCKING_POWER));
	}

	@Override
	public void dashboardInit() {
		SmartDashboard.putData(new AnglerZero());
		SmartDashboard.putData(new ShootZeta());
		SmartDashboard.putData(new ShootDistance());
		SmartDashboard.putData(new PushToShoot());
		//SmartDashboard.putData(new RotateToAngle(90, 0));
		//SmartDashboard.putData(new DoLowBar());
		//SmartDashboard.putData(new DriveToOW());
		//SmartDashboard.putData(new AutoCenterGoal());
		//SmartDashboard.putData(new DoCheval());
		SmartDashboard.putData(new ShooterTestSetpoint());
		//SmartDashboard.putData(new ShooterSetpointByDistance());
		SmartDashboard.putData(new AnglerTestSetpoint());
		SmartDashboard.putData(new AutoDriveToTarget());
		SmartDashboard.putData(new TestAutoLineUp());
		SmartDashboard.putData(new DriveDistance());
		SmartDashboard.putData(Scheduler.getInstance());
	}

	@Override
	public void dashboardPeriodic() {
	}
}

