package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Loops.ILooper;
import frc.robot.Loops.Loop;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.DigitalSensor;

public class Elevator extends Subsystem {

  /*-------------------------------- Private instance variables ---------------------------------*/
  private static Elevator mInstance;
  private PeriodicIO mPeriodicIO;

  // private static final double kPivotCLRampRate = 0.5;
  // private static final double kCLRampRate = 0.5;

  public static Elevator getInstance() {
    if (mInstance == null) {
      mInstance = new Elevator();
    }
    return mInstance;
  }

  private TalonFX mLeftMotor;
  private TalonFX mRightMotor; // LEADER

  private MotionMagicVoltage m_request;
  



  private Elevator() {

    mPeriodicIO = new PeriodicIO();

    // LEFT ELEVATOR MOTOR
    mLeftMotor = new TalonFX(Constants.Elevator.kElevatorLeftMotorId); // MASTER
    // RIGHT ELEVATOR MOTOR
    mRightMotor = new TalonFX(Constants.Elevator.kElevatorRightMotorId);

    var talonFXConfigs = new TalonFXConfiguration();

    var slot0Configs = talonFXConfigs.Slot0;
    slot0Configs.kS = Constants.Elevator.kS; // Add 0.0 V output to overcome static friction
    slot0Configs.kV = Constants.Elevator.kV; // A velocity target of 1 rps results in 0.0 V output
    slot0Configs.kP = Constants.Elevator.kP; // An error of 1 rotation results in 0.4 V output
    slot0Configs.kI = Constants.Elevator.kI; // no output for integrated error
    slot0Configs.kD = Constants.Elevator.kD; // A velocity of 1 rps results in 0.0 V output

    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = Constants.Elevator.kCruiseVelocity; // Target cruise velocity of 80 rps
    motionMagicConfigs.MotionMagicAcceleration = Constants.Elevator.kAcceleration; // Target acceleration of 240 rps/s (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = Constants.Elevator.kJerk;

    mRightMotor.getConfigurator().apply(talonFXConfigs);
    mLeftMotor.getConfigurator().apply(talonFXConfigs);

    mRightMotor.setControl(new Follower(mLeftMotor.getDeviceID(), true));
    mLeftMotor.setNeutralMode(NeutralModeValue.Coast);
    mLeftMotor.setNeutralMode(NeutralModeValue.Brake);

    mLeftMotor.setControl(new DutyCycleOut(mLeftMotor.getDutyCycle().getValue()));

    m_request = new MotionMagicVoltage(0);
  }

  public enum ElevatorState {
    GROUND,
    L2,
    L3,
    L4,
    AP,
    A1,
    A2,
  }

  private static class PeriodicIO {
    double elevator_target = 0.0;
    String state = "Ground";
  }

  /*-------------------------------- Generic Subsystem Functions --------------------------------*/

  // @Override
  // public void periodic() {
  // TODO: Use this pattern to only drive slowly when we're really high up
  // if(mPivotEncoder.getPosition() > Constants.kPivotScoreCount) {
  // mPeriodicIO.is_pivot_low = true;
  // } else {
  // mPeriodicIO.is_pivot_low = false;
  // }
  // }

  	/*-------------------------------- Generic Subsystem Functions --------------------------------*/

	@Override
	public void registerEnabledLoops(ILooper enabledLooper) {
		enabledLooper.register(new Loop() {
			@Override
			public void onStart(double timestamp) {}

			@Override
			public void onLoop(double timestamp) {
        
			}

			@Override
			public void onStop(double timestamp) {
				stop();
			}
		});
	}


  @Override
  public void writePeriodicOutputs() {
    goToTarget();
  }

  @Override
  public void stop() {
    mPeriodicIO.elevator_target = mLeftMotor.getPosition().getValueAsDouble();
  }

  @Override
  public void outputTelemetry() {
    SmartDashboard.putNumber("Position/Target", mPeriodicIO.elevator_target);
    SmartDashboard.putString("State", mPeriodicIO.state);
  }

  public void resetRot(double pos) {
    mRightMotor.setPosition(pos);
  }


  public void runElevatorRaw(double speed) {
    mLeftMotor.set(speed);
  }

  public void setTarget(String targ) {
    switch(targ) {
      case "Ground":
        mPeriodicIO.elevator_target = Constants.Elevator.kGroundHeight;
        mPeriodicIO.state = "Ground";
        break;
      case "L2":
        mPeriodicIO.elevator_target = Constants.Elevator.kL2Height;
        mPeriodicIO.state = "L2";
        break;
      case "L3":
        mPeriodicIO.elevator_target = Constants.Elevator.kL3Height;
        mPeriodicIO.state = "L3";
        break;
      case "L4":
        mPeriodicIO.elevator_target = Constants.Elevator.kL4Height;
        mPeriodicIO.state = "L4";
        break;
      case "AP":
        mPeriodicIO.elevator_target = Constants.Elevator.kAPHeight;
        mPeriodicIO.state = "AP";
        break;
      case "A1":
        mPeriodicIO.elevator_target = Constants.Elevator.kA1Height;
        mPeriodicIO.state = "A1";
      case "A2":
        mPeriodicIO.elevator_target = Constants.Elevator.kA2Height;
        mPeriodicIO.state = "A2";

    }
  }


  public void goToTarget() {

    if (Laser.inRangeIntake()) {
//      System.out.println("Break Laser Check");
      return;
    }
//    System.out.println("Going to Target: " + mPeriodicIO.elevator_target);
    mLeftMotor.setControl(m_request.withPosition(mPeriodicIO.elevator_target));

    
  }


  public boolean isAtTarget() {
    System.out.println("reading");
    return Math.abs(mLeftMotor.getPosition().getValueAsDouble() - mPeriodicIO.elevator_target) < 0.5;
  }

}
