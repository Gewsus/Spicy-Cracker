package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.commands.*;
import org.usfirst.frc.team2834.robot.commands.vision.*;
import org.usfirst.frc.team2834.robot.subsystems.Vision;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * This is a really big class so hold onto your ass.
 * 
 * It basically takes into account every situation we will be in for autonomous and
 * draws the best possible course for each one.
 * 
 */
public class AutonomousCommand extends CommandGroup {
    
    /**
     * @param mode What is the robot going to do?  Will it shoot?  will it cross a defense?  will it do jack shit?
     * @param position Mostly determines what rough angle to rotate before using vision tracking.
     * @param defense If and when the robot reaches a defense, which defense program should it use?
     * @param direction What direction is the robot facing?
     */
    public AutonomousCommand(int mode, int position, int defense, boolean direction) {
    	super("Autonomous Command: [" + mode + "] [" + position + "] [" + defense + "] [" + direction + "]");
    	
    	addSequential(new PusherOut(false));
    	//Reverse if the robot is backwards
    	addSequential(new SetDriveReverse(!direction));
    	
    	if (mode != 0) {
			if (position != 5) {
				if (mode >= 1) {
					//All programs require the robot to reach a defense except the spy program
					addSequential(new DriveToOW());
					if (mode >= 2) {
						//Select defense program
						switch(defense) {
							case 0:
								addSequential(new DoLowBar());
								break;
							case 1:
								addSequential(new DoMoat());
								break;
							case 2:
								addSequential(new DoRamparts());
								break;
							case 3:
								addSequential(new DoRockWall());
								break;
							case 4:
								addSequential(new DoRoughTerrain());
								break;
							case 5:
								addSequential(new DoCheval());
								break;
							case 6:
								addSequential(new DoPortculis());
								break;
							default:
								addSequential(new WaitCommand(15));
								System.out.print("Did not select defense");
								break;
						}
						if (mode >= 3) {
							//Certain positions may require different alignments with the goal
							double offset = Vision.DEFAULT_OFFSET;
							switch(position) {
							//The first two positions require the robot to move forward to align with the left goal
								case 0:
									if(direction) {
										addSequential(new RotateToAngle(180));
									} else {
										addSequential(new RotateToAngle(0));
									}
									addSequential(new TimedHaloDrive(0.6, 0.2, false, 0.6));
									addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.1));
									addSequential(new RotateToAngle(55));
									break;
								case 1:
									if(direction) {
										addSequential(new RotateToAngle(180));
									} else {
										addSequential(new RotateToAngle(0));
									}
									addSequential(new TimedHaloDrive(0.6, 0.0, false, 0.83));
									addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.1));
									addSequential(new RotateToAngle(55));
									addSequential(new TimedHaloDrive(-0.5, 0.0, false, 0.3));
									addSequential(new TimedHaloDrive(0.2, 0.0, false, 0.2));
									break;
							//Positions 3 and 4 require minimal rotating to see the goal
								case 2:
									addSequential(new RotateToAngle(15));
									break;
								case 3:
									addSequential(new RotateToAngle(-10));
									break;
								case 4:
							//Position 5 uses slight movement and an offset vision allignment
									addSequential(new RotateToAngle(-20));
									addSequential(new TimedHaloDrive(0.4, 0.0, false, 0.5));
									addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.2));
									offset = 0.5;
									break;
							}
							addSequential(new SetDriveReverse(true));
							//Ensure the ball does not get lodged in the shooter wheels and fire prematurely.
							addParallel(new TimedShooter(-0.2, 1));
							//Ensure a goal is in sight and is a reasonable distance away
							addSequential(new WaitForTarget());
							//Align with the goal
							addSequential(new AutoCenterGoal(offset));
							//Drive forward for a better position
							addSequential(new DriveDistance());
							addSequential(new WaitCommand(0.25));
							addSequential(new PusherOut(false));
							//Set shooter speed based on distance
							addParallel(new ShootDistance());
							//Align twice more
							addSequential(new AutoCenterGoal(offset));
							addSequential(new AutoCenterGoal(offset));
							//Wait shoot, and stop spinning the wheels
							addSequential(new WaitCommand(3));
							addSequential(new PushToShoot());
							addSequential(new FreeShooter());
						} else {
							//If not shooting, align straight and drive forward so we get the points for a cross
							if(direction) {
								addSequential(new RotateToAngle(180));
							} else {
								addSequential(new RotateToAngle(0));
							}
							addSequential(new TimedHaloDrive(0.5, 0, false, 0.5));
						}
					}
				}
			} else {
				Robot.drivetrain.startingAngle = 90;
				if(mode >= 3) {
					addParallel(new ShooterSetSetpoint());
					addSequential(new WaitCommand(5));
					addSequential(new PushToShoot());
					addSequential(new FreeShooter());
				}
			} 
		}
    }
}
