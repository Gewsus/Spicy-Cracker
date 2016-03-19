package org.usfirst.frc.team2834.robot.commands;

import org.usfirst.frc.team2834.robot.commands.auto.SetDriveReverse;
import org.usfirst.frc.team2834.robot.commands.auto.WaitForTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class TestAutoLineUp extends CommandGroup {
    
    public  TestAutoLineUp() {
        super("Test auto line up");
        addSequential(new WaitForTarget());
		addSequential(new AutoCenterGoal());
		addSequential(new SetDriveReverse(true));
		/*switch(position) {
		case 0:
		case 1:
			addSequential(new TimedHaloDrive(0.5, 0, true, 0.6));
			break;
		case 4:
			addSequential(new TimedHaloDrive(0.5, 0, true, 0.4));
			break;
		case 3:
			addSequential(new TimedHaloDrive(0.5, 0, true, 0.5));
			break;
		default:
			addSequential(new TimedHaloDrive(0.5, 0, true, 0.3));
			break;
		}*/
		addSequential(new AutoDriveToTarget());
		//addSequential(new TimedHaloDrive(-0.1, 0, true, 0.1));
		//addSequential(new AutoCenterGoal());
		addSequential(new AutoDriveToTarget());
		addSequential(new AutoCenterGoal());
		addParallel(new ShooterSetSetpoint());
		addSequential(new WaitAndShoot());
		addSequential(new FreeShooter());
    }
    
    private class WaitAndShoot extends CommandGroup {
    	public WaitAndShoot() {
    		addSequential(new WaitCommand(2));
			addSequential(new ShooterPushToShoot());
			addSequential(new ShooterPushToShoot());
    	}
    }
}
