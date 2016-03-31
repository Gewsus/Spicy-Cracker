package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoLowBar extends CommandGroup {
    
    public  DoLowBar() {
    	super("Do Low Bar");
        addSequential(new TimedAngler(-0.5, 0.6));
        addSequential(new TimedHaloDrive(0.5, 0.0, false, 1.5));
        addSequential(new TimedHaloDrive(-0.1, 0.0, false, 0.1));
        addSequential(new TimedAngler(0.5, 1));
    }
}
