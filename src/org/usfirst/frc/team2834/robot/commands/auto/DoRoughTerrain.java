package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoRoughTerrain extends CommandGroup {
    
    public  DoRoughTerrain() {
        super("Do Rough Terrain");
        addParallel(new TimedAngler(0.1, 2));
        addSequential(new TimedHaloDrive(0.5, 0.0, true, 1.5));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.25));
    }
}
