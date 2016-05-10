package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.commands.auto.TimedAngler;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Simple command sequence to lift shooter and put it back down
 * 
 * Ase the name suggests, this command is used to break contact with the drawbridge.
 */
public class AnglerDrawbridgeDetatch extends CommandGroup {
    
    public  AnglerDrawbridgeDetatch() {
        super("Angler Drawbridge Detatch");
        addSequential(new TimedAngler(0.7, 0.3));
        addSequential(new TimedAngler(-0.7, 0.2));
    }
}
