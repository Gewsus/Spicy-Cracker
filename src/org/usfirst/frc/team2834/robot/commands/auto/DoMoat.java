package org.usfirst.frc.team2834.robot.commands.auto;

import org.usfirst.frc.team2834.robot.commands.SetDriveSixWheels;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DoMoat extends CommandGroup {
    
    public  DoMoat() {
        super("Do Moat");
        addSequential(new SetDriveSixWheels(true));
        addParallel(new TimedShooterAngle(0.1, 2));
        addSequential(new TimedHaloDrive(0.6, 0.0, true, 0.25));
        addSequential(new WaitCommand(0.5));
        addSequential(new TimedHaloDrive(1, 0.0, true, 0.6));
        addSequential(new WaitCommand(0.5));
        addSequential(new TimedHaloDrive(0.6, 0.0, true, 0.25));
        addSequential(new TimedHaloDrive(-0.25, 0.0, false, 0.75));
        addSequential(new SetDriveSixWheels(false));
    }
}
