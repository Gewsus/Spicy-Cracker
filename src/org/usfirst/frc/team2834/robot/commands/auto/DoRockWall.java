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
        addParallel(new TimedAngler(0.1, 2.1));
        addSequential(new TimedHaloDrive(0.6, 0.0, false, 2.0));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.25));
        addSequential(new SetDriveSixWheels(false));
    }
}
