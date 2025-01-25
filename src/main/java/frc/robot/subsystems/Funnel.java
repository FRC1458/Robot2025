package frc.robot.subsystems;
import frc.robot.Loops.ILooper;

import java.util.Queue;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 * The Subsystem abstract class, which serves as a basic framework for all robot subsystems. Each subsystem outputs
 * commands to SmartDashboard, has a stop routine (for after each match), and a routine to zero all sensors, which helps
 * with calibration.
 * <p>
 * All Subsystems only have one instance (after all, one robot does not have two drivetrains), and functions get the
 * instance of the drivetrain and act accordingly. Subsystems are also a state machine with a desired state and actual
 * state; the robot code will try to match the two states with actions. Each Subsystem also is responsible for
 * instantializing all member components at the start of the match.
 */

public class Funnel extends Subsystem {

	private static Funnel mInstance;

	private Boolean isExtended;

	public static Funnel getInstance() {
		if (mInstance == null) {
		  mInstance = new Funnel();
		}
		return mInstance;
	  }

	private TalonFX mFunnelPivotMotor;

	private Funnel() {
		
        mFunnelPivotMotor = new TalonFX(Constants.Elevator.kElevatorLeftMotorId);
		mFunnelPivotMotor.setNeutralMode(NeutralModeValue.Brake);
    }

	public void retractFunnel(){
		mFunnelPivotMotor.set(0.05);
	}

	public void extendFunnel(){
		mFunnelPivotMotor.set(-0.05);
	}

	private void pivotFunnel() {
		
	}


	public void writeToLog() {}

	// Optional design pattern for caching periodic reads to avoid hammering the HAL/CAN.
	public void readPeriodicInputs() {}

	// Optional design pattern for caching periodic writes to avoid hammering the HAL/CAN.
	public void writePeriodicOutputs() {}

	public void stop() {}

	public void zeroSensors() {}

	public void outputTelemetry() {}

	public void registerEnabledLoops(ILooper enabledLooper) {}

	public boolean checkSystem() {
		return false;
	}

	public boolean hasEmergency = false;

	// Optional pattern for checking if attached devices have healthy configurations
	public boolean checkDeviceConfiguration() {
		return true;
	}

	// Optional pattern for checking if attached devices have healthy configurations
	public void rewriteDeviceConfiguration() {}
}
