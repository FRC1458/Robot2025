package frc.robot.autos;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.RobotContainer25;
import frc.robot.RobotState;
import frc.robot.autos.actions.ElevatorAction;
import frc.robot.autos.modes.ChoreoAuto;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.SwerveDrive;

public class ChorAutoBuilder {

    private SwerveDrive m_SwerveDrive;
    private static AutoFactory autoFactory;
    private final AutoChooser autoChooser;

    public ChorAutoBuilder(SwerveDrive drive) {

        m_SwerveDrive = drive;

        autoFactory = new AutoFactory(
                this::getKalmanOdometry, // A function that returns the current robot pose
                m_SwerveDrive::resetOdometry, // A function that resets the current robot pose to the provided Pose2d
                m_SwerveDrive::setTrajectory, // The drive subsystem trajectory follower
                true, // If alliance flipping should be enabled
                (Subsystem) m_SwerveDrive // The drive subsystem
        );

        //can bind commands for easier use
        autoFactory
                .bind("elevatorl2", Elevator.getInstance().goToLevel("L2"))
                .bind("elevatorl3", Elevator.getInstance().goToLevel("L3"))
                .bind("elevatorl4", Elevator.getInstance().goToLevel("L4"));

        autoChooser = new AutoChooser();

        autoChooser.addRoutine("Test Routine", ChoreoAuto::testAuto);

        SmartDashboard.putData(autoChooser);

        RobotModeTriggers.autonomous().whileTrue(autoChooser.selectedCommandScheduler());
    }

    private Pose2d getKalmanOdometry() {
        return RobotState.getInstance().getLatestFieldToVehicle();
    }

    public static AutoFactory getAutoFactory() {
        return autoFactory;
    }
}
