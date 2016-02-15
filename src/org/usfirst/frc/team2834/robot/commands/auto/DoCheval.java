package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DoCheval extends CommandGroup {
    
    public  DoCheval() {
        super("Do Cheval");
        addParallel(new TimedHaloDrive(0.25, 0.0, false, 4));
        addSequential(new TimedShooterAngle(-0.2, 1));
        addSequential(new TimedShooterAngle(0.4, 1));
        addSequential(new RotateAngle(180, 0));
    }
}
