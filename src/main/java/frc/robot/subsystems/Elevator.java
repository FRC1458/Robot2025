package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
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
  private TalonFX mRightMotor; //LEADER



  
  private TrapezoidProfile mProfile;
  private TrapezoidProfile.State mCurState = new TrapezoidProfile.State();
  private TrapezoidProfile.State mGoalState = new TrapezoidProfile.State();
  private PositionVoltage m_request = new PositionVoltage(0).withSlot(0);
  private double prevUpdateTime = Timer.getFPGATimestamp();

  private boolean atTarget = false;

  //private SlewRateLimiter mSpeedLimiter = new SlewRateLimiter(1000);

  private void setUpElevatorMotor(TalonFX motor) {
    motor.getConfigurator().apply(Constants.Elevator.ElevatorConfiguration(),Constants.kLongCANTimeoutMs);
    
    // Set the motor to brake mode (will hold its position when powered off)
}
  private Elevator() {
    //super("Elevator");
	//TODO:figure out what this does

    mPeriodicIO = new PeriodicIO();

    // LEFT ELEVATOR MOTOR
    mLeftMotor = new TalonFX(Constants.Elevator.kElevatorLeftMotorId);
    // RIGHT ELEVATOR MOTOR
    mRightMotor = new TalonFX(Constants.Elevator.kElevatorRightMotorId);
    
    mLeftMotor.setControl(new Follower(mRightMotor.getDeviceID(), true));
    mLeftMotor.setNeutralMode(NeutralModeValue.Coast);
    mRightMotor.setNeutralMode(NeutralModeValue.Brake);

    // mRightMotor.setInverted(true);
    mRightMotor.setControl(new DutyCycleOut(mLeftMotor.getDutyCycle().getValue()));

    //mLeftMotor.burnFlash();
    //mRightMotor.burnFlash();
    //TODO: figure out burnflash equivalent
    mProfile = new TrapezoidProfile(
        new TrapezoidProfile.Constraints(Constants.Elevator.kMaxVelocity, Constants.Elevator.kMaxAcceleration));
  }

  public enum ElevatorState {
    GROUND,
    L2,
    L3,
    L4,
  }

private ElevatorState targetState = ElevatorState.GROUND;

  public ElevatorState currentState = ElevatorState.GROUND;
  //public int targetState = 0; This want from the int type system

  private static class PeriodicIO {
    double elevator_target = 0.0;
    double elevator_power = 0.0;

    boolean is_elevator_pos_control = false;

    ElevatorState state = ElevatorState.GROUND;
  }

  /*-------------------------------- Generic Subsystem Functions --------------------------------*/

  //@Override
  //public void periodic() {
    // TODO: Use this pattern to only drive slowly when we're really high up
    // if(mPivotEncoder.getPosition() > Constants.kPivotScoreCount) {
    // mPeriodicIO.is_pivot_low = true;
    // } else {
    // mPeriodicIO.is_pivot_low = false;
    // }
  //}

  @Override
  public void writePeriodicOutputs() {
    //goToTarget();



    
    // double curTime = Timer.getFPGATimestamp();
    // double dt = curTime - prevUpdateTime;
    // prevUpdateTime = curTime;
    // if (mPeriodicIO.is_elevator_pos_control) {
    //   // Update goal
    //   mGoalState.position = mPeriodicIO.elevator_target;

    //   // Calculate new state
    //   prevUpdateTime = curTime;
    //   mCurState = mProfile.calculate(dt, mCurState, mGoalState);

    //   // Set PID controller to new state
    //   /*mLeftPIDController.setReference(
    //       mCurState.position,
    //       CANSparkMax.ControlType.kPosition,
    //       0,
    //       Constants.Elevator.kG,
    //       ArbFFUnits.kVoltage);*/ //TODO: verify if this patch works
    //   mLeftMotor.setControl(m_request.withPosition(mCurState.position));
    // } else {
    //   //mCurState.position = mLeftEncoder.getPosition().getValueAsDouble();
    //   mCurState.velocity = 0;
    //   mLeftMotor.set(mPeriodicIO.elevator_power);
    
  }

  @Override
  public void stop() {
    mPeriodicIO.is_elevator_pos_control = false;
    mPeriodicIO.elevator_power = 0.0;

    mRightMotor.set(0.0);
  }

  @Override
  public void outputTelemetry() {
    //SmartDashboard.putNumber("Position/Current", mLeftEncoder.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Position/Target", mPeriodicIO.elevator_target);
    //SmartDashboard.putNumber("Velocity/Current", mLeftEncoder.getVelocity().getValueAsDouble());

    SmartDashboard.putNumber("Position/Setpoint", mCurState.position);
    SmartDashboard.putNumber("Velocity/Setpoint", mCurState.velocity);

    SmartDashboard.putNumber("Current/Left", mLeftMotor.getSupplyCurrent().getValueAsDouble());
    SmartDashboard.putNumber("Current/Right", mRightMotor.getSupplyCurrent().getValueAsDouble());

    SmartDashboard.putNumber("Output/Left", mLeftMotor.getMotorOutputStatus().getValueAsDouble());
    SmartDashboard.putNumber("Output/Right", mRightMotor.getMotorOutputStatus().getValueAsDouble());
  
  int currentStateInt = 0;
    if (currentState == ElevatorState.GROUND) {
      currentStateInt = 1;
  } else if (currentState == ElevatorState.L2) {
      currentStateInt = 2;
  } else if (currentState == ElevatorState.L3) {
      currentStateInt = 3;
  } else if (currentState == ElevatorState.L4) {
      currentStateInt = 4;
  }

  SmartDashboard.putNumber("Elevator/State", currentStateInt);

  int targetStateInt = 0;

  if (targetState == ElevatorState.GROUND) {
    targetStateInt = 1;
} else if (currentState == ElevatorState.L2) {
    targetStateInt = 2;
} else if (currentState == ElevatorState.L3) {
    targetStateInt = 3;
} else if (currentState == ElevatorState.L4) {
    targetStateInt = 4;
}

  SmartDashboard.putNumber("Elevator/State", targetStateInt);

  //Integer values Guide: 0 is ground, and numbers after represent the level and their number. (E.g. int 4 = L4)
    
  //SmartDashboard.putNumber("State", mPeriodicIO.state);
}

  /*
  public void reset() {
    //mLeftEncoder.setPosition(0.0);
  }
  */

  /*---------------------------------- Custom Public Functions ----------------------------------*/

  public ElevatorState getState() {
    return mPeriodicIO.state;
  }

  public ElevatorState getLevel() {
    if (DigitalSensor.getSensor(4)) {
      return ElevatorState.L4;
    } else if (DigitalSensor.getSensor(3)) {
      return ElevatorState.L3;
    } else if (DigitalSensor.getSensor(2)) {
      return ElevatorState.L2;
    } else if (DigitalSensor.getSensor(1)) {
      return ElevatorState.GROUND;
    } else {
      return currentState;
    }
  }

  


  //This code is for the incline or decline system version for the elevator which uses two buttons
/* public void incTarget() {
  switch (targetState) {
    case GROUND:
      targetState = ElevatorState.L1;
      break;
    case L1:
      targetState = ElevatorState.L2;
      break;
    case L2:
      targetState = ElevatorState.L3;
      break;
    case L3:
      targetState = ElevatorState.L4;
      break;
    case L4:
      targetState = ElevatorState.GROUND;
      break;
    default:
      targetState = ElevatorState.GROUND;
      break;
  }
}

public void decTarget() {
  switch (targetState) {
    case GROUND:
      targetState = ElevatorState.L4;
      break;
    case L1:
      targetState = ElevatorState.GROUND;
      break;
    case L2:
      targetState = ElevatorState.L1;
      break;
    case L3:
      targetState = ElevatorState.L2;
      break;
    case L4:
      targetState = ElevatorState.L3;
      break;
    default:
      targetState = ElevatorState.GROUND;
      break;
  }
 } */

 // this code is for the single button assignment elevator
public void goToElevatorGround() {
  targetState = ElevatorState.GROUND;
}

 public void goToElevatorL2() {
  targetState = ElevatorState.L2;
}

public void goToElevatorL3() {
  targetState = ElevatorState.L3;
}

public void goToElevatorL4() {
  targetState = ElevatorState.L4;
}


public void runElevator(double speed) {
  mRightMotor.set(speed);
}

public void setTargetLevel (int kLevel) {
  switch (kLevel) {
    case 0:
      targetState = ElevatorState.GROUND;
      break;
    case 1:
     targetState = ElevatorState.L2;
      break;
    case 2:
      targetState = ElevatorState.L3;
      break;
    case 3:
      targetState = ElevatorState.L4;
      break;
    default:
      targetState = ElevatorState.GROUND;
      break;
}
}

public void goToTarget() {
  currentState = getLevel();

  //old int system
 /*  if(targetState > 4 || targetState < 0) {
    targetState = 0;
  } */
 
  if(Laser.inRangeIntake()) {
    atTarget = false;
  }
  
  else if(targetState == currentState) {
    stop();
    atTarget = true;
  }

  else if(targetState.ordinal() > currentState.ordinal()) {
    runElevator(0.05);
    atTarget = false;
  }

  else if(targetState.ordinal() < currentState.ordinal()) {
    runElevator(-0.05);
    atTarget = false;
  }
  atTarget = false;
}

public boolean atTarget() {
  return targetState == currentState;
}

  // public void setElevatorPower(double power) {
  //   SmartDashboard.putNumber("setElevatorPower", power);
  //   mPeriodicIO.is_elevator_pos_control = false;
  //   mPeriodicIO.elevator_power = power;
  // }

  // public void goToElevatorGround() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kGROUNDHeight;
  //   mPeriodicIO.state = ElevatorState.GROUND;
  // }
  
  // public void goToElevatorL1() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL1Height;
  //   mPeriodicIO.state = ElevatorState.L1;
  // }

  // public void goToElevatorL2() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL2Height;
  //   mPeriodicIO.state = ElevatorState.L2;
  // }

  // public void goToElevatorL3() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL3Height;
  //   mPeriodicIO.state = ElevatorState.L3;
  // }

  // public void goToElevatorL4() {
  //   mPeriodicIO.is_elevator_pos_control = true;
  //   mPeriodicIO.elevator_target = Constants.Elevator.kL4Height;
  //   mPeriodicIO.state = ElevatorState.L4;
  // }

  /*---------------------------------- Custom Private Functions ---------------------------------*/
}
