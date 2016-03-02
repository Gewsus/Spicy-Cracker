package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DoPortculis extends CommandGroup {
    
    public  DoPortculis() {
    	super("Do Portculis");
        addSequential(new TimedAngler(-0.2, 1));
        addSequential(new WaitCommand(0.3));
        addSequential(new TimedHaloDrive(0.0, 0.3, false, 0.3));
        addParallel(new TimedAngler(1.0, 3));
        addSequential(new TimedHaloDrive(0.0, 0.3, false, 3));
    }
}
