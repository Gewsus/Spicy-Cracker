
package org.usfirst.frc.team2834.robot;

import org.usfirst.frc.team2834.robot.commands.TimedHaloDrive;
import org.usfirst.frc.team2834.robot.subsystems.Drivetrain;
import org.usfirst.frc.team2834.robot.subsystems.ExampleVision;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
    public static Drivetrain drivetrain = new Drivetrain();
    public static ExampleVision vision = new ExampleVision();
    private Command auto;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called once before autonomous period starts
	 */
    public void autonomousInit() {
    	//Select autonomous mode based on input from dashboard
    	String autoSelected = SmartDashboard.getString("Auto Selector", "Do Nothing");
		switch(autoSelected) {
		case "Drive to OW":
			auto = new TimedHaloDrive(1, 0, 3);
			break;
		//If no auto is selected, the dashboard is disconnected, or "Do Nothing" is selected, the robot will do nothing
		case "Do Nothing":
		default:
			auto = null;
			break;
		}
        if (auto != null) auto.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        if (auto != null) auto.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
