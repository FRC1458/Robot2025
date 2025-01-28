package frc.robot.subsystems;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Loops.ILooper;
import frc.robot.Loops.Loop;
import frc.robot.lib.util.Conversions;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.DigitalSensor;
public class Elevator extends Subsystem {
	/*-------------------------------- Private instance variables ---------------------------------*/
	private static Elevator mInstance;
	private PeriodicIO mPeriodicIO;
	public static Elevator getInstance() {
		if (mInstance == null) {
		mInstance = new Elevator();
		}
		return mInstance;
	}

	private TalonFX mLeftMotor;
	private TalonFX mRightMotor; //LEADER

	private Elevator() {
		//super("Elevator");
		mPeriodicIO = new PeriodicIO();

		// LEFT ELEVATOR MOTOR
		mLeftMotor = new TalonFX(Constants.Elevator.kElevatorLeftMotorId);
		// RIGHT ELEVATOR MOTOR
		mRightMotor = new TalonFX(Constants.Elevator.kElevatorRightMotorId);
		// in init function
		mRightMotor.getConfigurator().apply(Constants.Elevator.motorConfigs());
		mLeftMotor.getConfigurator().apply(Constants.Elevator.motorConfigs());
		mLeftMotor.setControl(new Follower(mRightMotor.getDeviceID(), true));
		mLeftMotor.setNeutralMode(NeutralModeValue.Brake);
		mRightMotor.setNeutralMode(NeutralModeValue.Brake);
	}


	private static class PeriodicIO {
		public int currentState = 0;
		public int targetState = 0;
		public MotionMagicVoltage demand;
	}

	/*-------------------------------- Generic Subsystem Functions --------------------------------*/
	@Override
	public void readPeriodicInputs() {}

	@Override
	public void registerEnabledLoops(ILooper enabledLooper) {
		enabledLooper.register(new Loop() {
			@Override
			public void onStart(double timestamp) {}

			@Override
			public void onLoop(double timestamp) {
				mRightMotor.setControl(mPeriodicIO.demand);
			}

			@Override
			public void onStop(double timestamp) {}
		});
	}
	
	@Override
	public void writePeriodicOutputs() {}

	@Override
	public void stop() {
		mRightMotor.set(0.0);
	}

	@Override
	public void outputTelemetry() {
		SmartDashboard.putNumber("Current/Left", mLeftMotor.getSupplyCurrent().getValueAsDouble());
		SmartDashboard.putNumber("Current/Right", mRightMotor.getSupplyCurrent().getValueAsDouble());

		SmartDashboard.putNumber("Output/Left", mLeftMotor.getMotorOutputStatus().getValueAsDouble());
		SmartDashboard.putNumber("Output/Right", mRightMotor.getMotorOutputStatus().getValueAsDouble());
		//TODO: figure out how to put a state in smart dashbaord
	}

	/*
	public void reset() {
		//mLeftEncoder.setPosition(0.0);
	}
	*/

	/*---------------------------------- Custom Public Functions ----------------------------------*/

	public void updateLocation() {
		for (int i = 0; i < 5; i++) {
			if (DigitalSensor.getSensor(i)) {
				mPeriodicIO.currentState = i;
				break;
			}
		}
	}

	public void setTargetLevel(int target) {
		mPeriodicIO.targetState = target;
		if (mPeriodicIO.targetState > 4) {
			mPeriodicIO.targetState = 0;
		}
		if (mPeriodicIO.targetState < 0) {
			mPeriodicIO.targetState = 4;
		}
	}

	public void incTarget() {
		setTargetLevel(mPeriodicIO.targetState + 1);
	}

	public void decTarget() {
		setTargetLevel(mPeriodicIO.targetState - 1);
	}

	public void runElevator(double speed) {
		mRightMotor.set(speed);
	}

	public boolean goToTarget() {
		updateLocation();

		if (mPeriodicIO.targetState > 4 || mPeriodicIO.targetState < 0) {
			mPeriodicIO.targetState = 0;
		}

		if (Laser.inRangeIntake()) {
			return false;
		} else if (mPeriodicIO.targetState == mPeriodicIO.currentState) {
			stop();
			return true;
		} else if (mPeriodicIO.targetState > mPeriodicIO.currentState) {
			runElevator(Constants.Elevator.kElevatorSpeed);
			return false;
		} else if (mPeriodicIO.targetState < mPeriodicIO.currentState) {
			runElevator(-Constants.Elevator.kElevatorSpeed);
			return false;
		}
		return false;
	}

	/*---------------------------------- Custom Private Functions ---------------------------------*/
	/**
	 * Sets position of the elevator, in rotations.
	 * @param position
	 */
	public void setPosition(double position) {
		double rotorSpeed = mRightMotor.getVelocity().getValueAsDouble();

		if (Math.abs(rotorSpeed) < 0.002) {
			mPeriodicIO.demand = new MotionMagicVoltage(
				((Laser.inRangeShooter() || Laser.inRangeIntake()) ? Constants.Elevator.kElevatorHoldCoralVoltage : 0.0) +
				(Laser.inRangeAlgaeShooter() ? Constants.Elevator.kElevatorHoldAlgaeVoltage : 0.0)
			);
		} else {
			mPeriodicIO.demand = new MotionMagicVoltage(rotorSpeed);
		}
	}
}
