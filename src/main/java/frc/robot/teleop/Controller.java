package frc.robot.teleop;
import java.util.List;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.*;
import frc.robot.FieldLayout;
import frc.robot.RobotState;
import frc.robot.autos.actions.*;
import frc.robot.subsystems.*;
/**
 * A class that contains the controls for the robot.
 */
public class Controller {
    public TeleopActionExecutor m_TeleopActionExecutor = null;
    public XboxController mXboxController = null;
    public boolean prev_left_trigger = false;
    public boolean prev_right_trigger = false;
    private Elevator2 mElevator = null;
    
    public Controller(XboxController xboxController, TeleopActionExecutor teleopActionExecutor ) {
        mXboxController = xboxController;
        m_TeleopActionExecutor = teleopActionExecutor;
        mElevator = Elevator2.getInstance();
    }
    public void update() {
        //if(mXboxController.getPOV() == 180) {
        //    m_TeleopActionExecutor.runAction(new ElevatorAction(0));
        //}
        if (mXboxController.getYButtonPressed()) {
            mElevator.setTargetLevel(3);
        }
        if (mXboxController.getAButtonPressed()) {
            mElevator.setTargetLevel(2);
        }
        if (mXboxController.getBButtonPressed()) {
            mElevator.setTargetLevel(1);
        }
        if (mXboxController.getXButtonPressed()) {
            mElevator.setTargetLevel(0);
        }
/* 
        if (mXboxController.getYButtonPressed()) {
            m_TeleopActionExecutor.runAction(new ElevatorAction(0));
        }
        if (mXboxController.getAButtonPressed()) {
            m_TeleopActionExecutor.runAction(new ElevatorAction(1));
        }
        if (mXboxController.getBButtonPressed()) {
            m_TeleopActionExecutor.runAction(new ElevatorAction(2));
        }
        if (mXboxController.getXButtonPressed()) {
            m_TeleopActionExecutor.runAction(new ElevatorAction(3));
        }
*/
        if (mXboxController.getLeftTriggerAxis() > 0.5) {
            if (!prev_left_trigger) {
                prev_left_trigger = true;
                m_TeleopActionExecutor.runAction(new AlgaeShooterAction());
            }
        } else {
            prev_left_trigger = false;
        }
        if (mXboxController.getRightTriggerAxis() > 0.5) {
            if (!prev_right_trigger) {
                m_TeleopActionExecutor.runAction(new CoralShooterAction());
            }
        } else {
            prev_right_trigger = false;
        }

        if(mXboxController.getLeftBumperButtonPressed()) {
            m_TeleopActionExecutor.runAction(new AlgaeIntakeAction());
        }

        if(mXboxController.getRightBumperButtonPressed()) {
            m_TeleopActionExecutor.runAction(new CoralIntakeAction());
        }


        /* 
        if (mXboxController.getLeftBumperButtonPressed()) {
            int tag = FieldLayout.getClosestTag(RobotState.getInstance().getLatestFieldToVehicle().getTranslation());
            boolean isL3 = false;
            for (int num : new int[] {18, 20, 22, 7, 9, 11}) {
                if (num == tag) {
                    isL3 = true;
                }
            }
            m_TeleopActionExecutor.runAction(new SeriesAction(
                new ElevatorAction(isL3 ? 3 : 2),
                new SnapToTag(2),
                new AlgaeIntakeAction()
            ));
        }
        if (mXboxController.getRightBumperButtonPressed()) {
            m_TeleopActionExecutor.runAction(new SeriesAction(
                new SnapToTag(2),
                new CoralIntakeAction()
            ));
        }
        */
        //if (mXboxController.getPOV() == 0) {
        //    m_TeleopActionExecutor.runAction(new SnapToTag(2));
        //}
        //if (mXboxController.getPOV() == 90) {
        //    m_TeleopActionExecutor.runAction(new SnapToTag(1));
        //}
        //if (mXboxController.getPOV() == 270) {
        //    m_TeleopActionExecutor.runAction(new SnapToTag(0));
        //}
        if (mXboxController.getLeftStickButtonPressed()) {
            m_TeleopActionExecutor.abort();
        }
    }
}
