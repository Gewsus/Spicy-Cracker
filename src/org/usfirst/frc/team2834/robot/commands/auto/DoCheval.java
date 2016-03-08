package org.usfirst.frc.team2834.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DoCheval extends CommandGroup {
    
    public  DoCheval() {
        super("Do Cheval");
        addSequential(new TimedHaloDrive(-0.2, 0.0, false, 0.1));
        addSequential(new WaitCommand(0.6));
        addParallel(new TimedHaloDrive(0.25, 0.0, false, 4));
        addSequential(new TimedAngler(-0.5, 1));
        addSequential(new TimedHaloDrive(0.5, 0.0, false, 2.2));
        //addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.4));
        addSequential(new TimedAngler(0.5, 1));
        //addSequential(new TimedShooterAngle(0.4, 1));
        //addSequential(new RotateAngle(180, 0));
    }
}
