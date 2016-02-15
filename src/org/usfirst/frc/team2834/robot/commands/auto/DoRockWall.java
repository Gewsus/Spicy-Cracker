package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.commands.SetDriveSixWheels;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRockWall extends CommandGroup {
    
    public  DoRockWall() {
    	super("Do Rock Wall");
        addSequential(new SetDriveSixWheels(true));
        addSequential(new TimedHaloDrive(0.5, 0.0, true, 4));
        addSequential(new TimedHaloDrive(-0.1, 0.0, false, 0.1));
        addSequential(new SetDriveSixWheels(false));
    }
}
