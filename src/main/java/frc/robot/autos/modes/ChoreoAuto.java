package frc.robot.autos.modes;

import java.util.ArrayList;
import java.util.List;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.autos.AutoModeBase;
import frc.robot.autos.AutoModeEndedException;
import frc.robot.autos.actions.SwerveTrajectoryAction.ResetWheelTracker;
import frc.robot.autos.actions.*;
import frc.robot.autos.ChorAutoBuilder;


public class ChoreoAuto extends AutoModeBase {

    AutoRoutine routine = ChorAutoBuilder.getAutoFactory().newRoutine("choreoTest");

    public ChoreoAuto() {

    }

    public static AutoRoutine testAuto() {
        AutoRoutine routine = ChorAutoBuilder.getAutoFactory().newRoutine("testAuto");

        //load routine's trajectories
        AutoTrajectory driveTest1 = routine.trajectory("Drive Test 1");

        routine.active().onTrue(
          Commands.sequence(
                  driveTest1.resetOdometry(),
                  driveTest1.cmd() //actually run the trajectory
          )
        );
        return routine;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("ChoreoAutoMode: Running choreo auto mode!");
        testAuto();
        System.out.println("Finished auto!");
    }


}
