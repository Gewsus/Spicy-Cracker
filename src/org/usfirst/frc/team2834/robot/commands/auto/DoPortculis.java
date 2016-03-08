package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
//import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DoPortculis extends CommandGroup {
    
    public  DoPortculis() {
    	super("Do Portculis");
    	addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.25));
        addSequential(new TimedAngler(-0.5, 1));
        //addSequential(new WaitCommand(0.3));
        addSequential(new TimedHaloDrive(0.3, 0.0, false, 1.25));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.15));
        addParallel(new TimedAngler(1.0, 2));
        addSequential(new TimedHaloDrive(0.3, 0.0, false, 2.2));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.25));
    }
}
