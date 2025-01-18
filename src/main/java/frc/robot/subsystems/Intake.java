package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
//import frc.robot.Helpers;
//import frc.robot.subsystems.leds.LEDs;
import frc.robot.Loops.ILooper;
import frc.robot.Loops.Loop;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem {
  private final NetworkTable intakeTable;
  private final DigitalInput m_IntakeLimitSwitch = new DigitalInput(Constants.Intake.k_intakeLimitSwitchId);

  //public final LEDs m_leds = LEDs.getInstance();

  /*-------------------------------- Private instance variables ---------------------------------*/
  private static Intake mInstance;
  private PeriodicIO m_periodicIO;

  public static Intake getInstance() {
    if (mInstance == null) {
      mInstance = new Intake();
    }
    return mInstance;
  }

  private TalonFX mIntakeMotor;

  private Intake() {
    //super("Intake");
    intakeTable = NetworkTableInstance.getDefault().getTable("intake");
    mIntakeMotor = new TalonFX(Constants.Intake.kIntakeMotorId/* , MotorType.kBrushless*/);
    //mIntakeMotor.restoreFactoryDefaults();
    mIntakeMotor.setNeutralMode(NeutralModeValue.Coast);
    m_periodicIO = new PeriodicIO();
  }

  private static class PeriodicIO {
    // Input: Desired state
    IntakeState intake_state = IntakeState.NONE;

    // Output: Motor set values
    double intake_speed = 0.0;
  }

  public enum IntakeState {
    NONE,
    INTAKE,
    EJECT,
    PULSE,
    FEED_SHOOTER,
  }

  /*-------------------------------- Generic Subsystem Functions --------------------------------*/

  @Override
  public void registerEnabledLoops(ILooper enabledLooper) {
    enabledLooper.register(new Loop() {
      @Override
      public void onStart(double timestamp) {
        //System.out.println("DummySubsystem loop has started");
      }

      @Override
      public void onLoop(double timestamp) {
        checkAutoTasks();

        // Intake control
        m_periodicIO.intake_speed = intakeStateToSpeed(m_periodicIO.intake_state);
        SmartDashboard.putString("State", m_periodicIO.intake_state.toString());
      }

      @Override
      public void onStop(double timestamp) {
        //System.out.println("DummySubsystem loop has stopped");
      }
    });
  }

  @Override
  public void writePeriodicOutputs() {
    mIntakeMotor.set(m_periodicIO.intake_speed);
  }

  @Override
  public void stop() {
    m_periodicIO.intake_speed = 0.0;
  }

  @Override
  public void outputTelemetry() {
    SmartDashboard.putNumber("Speed", intakeStateToSpeed(m_periodicIO.intake_state));
    SmartDashboard.putNumber("Pivot/Current (amps)", mIntakeMotor.getTorqueCurrent().getValue().abs(Amps));
    SmartDashboard.putBoolean("Limit Switch", getIntakeHasNote());
  }

  /*
  @Override
  public void reset() {
  }
  */

  public double intakeStateToSpeed(IntakeState state) {
    switch (state) {
      case INTAKE:
        return Constants.Intake.k_intakeSpeed;
      case EJECT:
        return Constants.Intake.k_ejectSpeed;
      case PULSE:
        // Use the timer to pulse the intake on for a 1/16 second,
        // then off for a 15/16 second
        if (Timer.getFPGATimestamp() % 1.0 < (1.0 / 45.0)) {
          return Constants.Intake.k_intakeSpeed;
        }
        return 0.0;
      case FEED_SHOOTER:
        return Constants.Intake.k_feedShooterSpeed;
      default:
        // "Safe" default
        return 0.0;
    }
  }

  /*---------------------------------- Custom Public Functions ----------------------------------*/

  public IntakeState getIntakeState() {
    return m_periodicIO.intake_state;
  }

  public boolean getIntakeHasNote() {
    // NOTE: this is intentionally inverted, because the limit switch is normally
    // closed
    return !m_IntakeLimitSwitch.get();
  }

  // Intake helper functions
  public void intake() {
    m_periodicIO.intake_state = IntakeState.INTAKE;
  }

  public void eject() {
    m_periodicIO.intake_state = IntakeState.EJECT;
  }

  public void pulse() {
    m_periodicIO.intake_state = IntakeState.PULSE;
  }

  public void feedShooter() {
    m_periodicIO.intake_state = IntakeState.FEED_SHOOTER;
  }

  public void stopIntake() {
    m_periodicIO.intake_state = IntakeState.NONE;
    m_periodicIO.intake_speed = 0.0;
  }

  public void setState(IntakeState state) {
    m_periodicIO.intake_state = state;
  }

  /*---------------------------------- Custom Private Functions ---------------------------------*/
  private void checkAutoTasks() {
    // If the intake is set to GROUND, and the intake has a note,
    // Stop the intake and go to the SOURCE position
    if (getIntakeHasNote() && m_periodicIO.intake_state == IntakeState.INTAKE) {
      m_periodicIO.intake_state = IntakeState.NONE;
      //m_leds.setColor(Color.kGreen);
    }
  }

  public static double modRotations(double input) {
    input %= 1.0;
    if (input < 0.0) {
      input += 1.0;
    }
    return input;
  }

  public static double modDegrees(double input) {
    input %= 360.0;
    if (input < 0.0) {
      input += 360.0;
    }
    return input;
  }

  public static int clamp(int val, int min, int max) {
    return Math.max(min, Math.min(max, val));
  }
}
