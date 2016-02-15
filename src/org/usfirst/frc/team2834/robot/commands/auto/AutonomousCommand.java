package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.Robot;
import org.usfirst.frc.team2834.robot.commands.FreeShooter;
import org.usfirst.frc.team2834.robot.commands.CenterOnGoal;
import org.usfirst.frc.team2834.robot.commands.ShooterPushToShoot;
import org.usfirst.frc.team2834.robot.commands.ShooterSetSetpoint;
import org.usfirst.frc.team2834.robot.commands.ToggleDriveReverse;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonomousCommand extends CommandGroup {
    
    public  AutonomousCommand() {
    	super("Autonomous Command");
    	//Select autonomous mode based on input from dashboard
    	int mode = (int) SmartDashboard.getNumber("Auto Mode", 0);
    	int position = (int) SmartDashboard.getNumber("Auto Position", 0);
    	int defense = (int) SmartDashboard.getNumber("Auto Defense", 0);
    	boolean direction = SmartDashboard.getBoolean("Auto Direction", false);
    	
    	//If the robot starts facing backwards, for Cheval or Portculis, let the robot know
    	if(direction) {
    		addSequential(new ToggleDriveReverse());
    		Robot.drivetrain.startingAngle = 180;
    	} else {
    		Robot.drivetrain.startingAngle = 0;
    	}
    	
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
								break;
						}
						if (mode >= 3) {
							switch(position) {
								case 0:
									addSequential(new RotateAngle(45, 0.25));
									break;
								case 1:
									addSequential(new RotateAngle(30, 0.25));
									break;
								case 2:
									addSequential(new RotateAngle(15, 0.25));
									break;
								case 3:
									addSequential(new RotateAngle(-10, 0.25));
									break;
								case 4:
									addSequential(new RotateAngle(-20, 0.25));
									break;
							}
							addSequential(new WaitCommand(2));
							addSequential(new WaitForTarget());
							addSequential(new CenterOnGoal());
							addSequential(new TimedHaloDrive(0.5, 0, true, 0.25));
							addSequential(new WaitCommand(2));
							addParallel(new CenterOnGoal());
							addSequential(new ShooterSetSetpoint(44500));
							addSequential(new WaitCommand(1));
							addSequential(new ShooterPushToShoot());
							addSequential(new FreeShooter());
						}
					}
				}
			} else {
				Robot.drivetrain.startingAngle = 90;
				if(mode >= 3) {
					addSequential(new ShooterSetSetpoint(44500));
					addSequential(new WaitCommand(1));
					addSequential(new ShooterPushToShoot());
					addSequential(new FreeShooter());
				}
			} 
		}
    }
}
