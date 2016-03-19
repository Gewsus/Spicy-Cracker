package org.usfirst.frc.team2834.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShooterPushToShoot extends CommandGroup {
    
    public  ShooterPushToShoot() {
        addSequential(new PusherOut(true));
        addSequential(new WaitCommand(2.25));
        addSequential(new PusherOut(false));
    }
}
