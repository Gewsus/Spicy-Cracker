package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.commands.SetDriveSixWheels;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRamparts extends CommandGroup {
    
    public  DoRamparts() {
        super("Do Ramparts");
        addSequential(new SetDriveSixWheels(true));
        addParallel(new TimedAngler(0.1, 1.5));
        addSequential(new TimedHaloDrive(0.75, -0.15, true, 1.2));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.25));
        addSequential(new SetDriveSixWheels(false));
    }
}
