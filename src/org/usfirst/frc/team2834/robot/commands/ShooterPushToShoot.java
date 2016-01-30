package org.usfirst.frc.team2834.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterPushToShoot extends CommandGroup {
    
    public  ShooterPushToShoot() {
        addSequential(new ShooterPush(true));
        addSequential(new ShooterPush(false));
    }
}
