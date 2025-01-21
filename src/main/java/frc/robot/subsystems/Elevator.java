package frc.robot.subsystems;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends Subsystem {
    TalonFX leftMotor;
    TalonFX rightMotor;

    double height;

    XboxController xbox1;

    public static Elevator mInstance;

    public Elevator (int leftId, int rightId, XboxController controller) {
        leftMotor = new TalonFX(leftId);
        rightMotor = new TalonFX(rightId);

        rightMotor.setControl(new Follower(leftId, true));
        leftMotor.setNeutralMode(NeutralModeValue.Coast);
        rightMotor.setNeutralMode(NeutralModeValue.Coast);

        xbox1 = controller;
    }

    public static Elevator getInstance(int leftId, int rightId, XboxController controller) {
		if (mInstance == null) {
			mInstance = new Elevator(leftId, rightId, controller);
		}
		return mInstance;
	}
    //TODO Make this work periodically
    public void periodic() {
        height = leftMotor.getRotorPosition().getValueAsDouble() * Math.PI * 0.047498 / 9;

        SmartDashboard.putNumber("Elevator/height", height);

        if (xbox1.getYButton()) {
            runMotor(0.1);
        } else if (xbox1.getAButton()) {
            runMotor(-0.1);
        } else {
            stopMotor();
        }
    }

    public void runMotor(double power) {
        leftMotor.set(power);
    }

    public void stopMotor() {
        leftMotor.set(0);
    }

    //BEFORE TESTING:
    //create elevator ob

    // TODO: Create PID to go to target heights
}
