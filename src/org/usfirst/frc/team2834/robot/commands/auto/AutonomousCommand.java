package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.commands.*;
import org.usfirst.frc.team2834.robot.commands.vision.*;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonomousCommand extends CommandGroup {
    
    public  AutonomousCommand(int mode, int position, int defense, boolean direction) {
    	super("Autonomous Command: [" + mode + "] [" + position + "] [" + defense + "] [" + direction + "]");
    	
    	//If the robot starts facing backwards(i.e. for Cheval or Portculis) let the robot know
    	addSequential(new PusherOut(false));
    	addSequential(new SetDriveReverse(!direction));
    	
    	if (mode != 0) {
			if (position != 5) {
				if (mode >= 1) {
					addSequential(new DriveToOW());
					if (mode >= 2) {
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
						}
						if (mode >= 3) {
							if(position == 0) {
								addSequential(new TimedHaloDrive(0.6, 0.0, false, 0.6));
								addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.1));
							}
							if(position == 1) {
								if(direction) {
									addSequential(new RotateToAngle(180));
								} else {
									addSequential(new RotateToAngle(0));
								}
								addSequential(new TimedHaloDrive(0.6, 0.0, false, 0.83));
								addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.1));
							}
							switch(position) {
								case 0:
									addSequential(new RotateToAngle(55));
									break;
								case 1:
									addSequential(new RotateToAngle(55));
									break;
								case 2:
									addSequential(new RotateToAngle(15));
									break;
								case 3:
									addSequential(new RotateToAngle(-10));
									break;
								case 4:
									addSequential(new RotateToAngle(-20));
									break;
							}
							addSequential(new SetDriveReverse(true));
							//addSequential(new PusherOut(false));
							addParallel(new TimedShooter(-0.2, 1.5));
							addSequential(new WaitForTarget());
							addSequential(new AutoCenterGoal());
							addSequential(new AutoDriveToTarget());
							addSequential(new WaitCommand(0.25));
							addSequential(new PusherOut(false));
							addParallel(new ShootDistance());
							addSequential(new WaitCommand(1.5));
							//addSequential(new AutoDriveToTarget());
							//addSequential(new AutoDriveToTarget());
							addSequential(new AutoCenterGoal());
							addSequential(new PushToShoot());
							//addSequential(new PushToShoot());
							addSequential(new FreeShooter());
						} else {
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
    
    /*private class WaitAndShoot extends CommandGroup {
    	public WaitAndShoot() {
    		//addSequential(new WaitCommand(2));
			addSequential(new PushToShoot());
			addSequential(new PushToShoot());
    	}
    }*/
}
