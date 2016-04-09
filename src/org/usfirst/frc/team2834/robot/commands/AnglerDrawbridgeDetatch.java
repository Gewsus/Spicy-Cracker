package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.commands.auto.TimedAngler;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AnglerDrawbridgeDetatch extends CommandGroup {
    
    public  AnglerDrawbridgeDetatch() {
        super("Angler Drawbridge Detatch");
        addSequential(new TimedAngler(0.7, 0.3));
        addSequential(new TimedAngler(-0.7, 0.2));
    }
}
