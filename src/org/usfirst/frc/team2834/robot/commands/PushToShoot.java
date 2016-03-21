package org.usfirst.frc.team2834.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class PushToShoot extends CommandGroup {
    
    public  PushToShoot() {
        addSequential(new PusherOut(true));
        addSequential(new WaitCommand(1.0));
        addSequential(new PusherOut(false));
    }
}
