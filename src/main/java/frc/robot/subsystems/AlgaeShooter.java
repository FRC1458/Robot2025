package frc.robot.subsystems;

import java.util.zip.Checksum;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.units.measure.Per;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Loops.ILooper;
import frc.robot.Loops.Loop;
import frc.robot.subsystems.SwerveDrive.PeriodicIO;



public class AlgaeShooter extends Subsystem {
    private static AlgaeShooter mInstance;
    private PeriodicIO mPeriodicIO = new PeriodicIO();

	public static AlgaeShooter getInstance() {
		if (mInstance == null) {
			mInstance = new AlgaeShooter();
		}
		return mInstance;
	}

    private class PeriodicIO {
		double speed = 0.0;
		double position = 0.0;
		AlgaeShooterState state = AlgaeShooterState.RETRACTED;
	}


	private double extendedPosition;
    private TalonFX mAlgaeMotor;
	private TalonFX mAlgaePivotMotor;

	private MotionMagicVoltage m_request;


	private DigitalInput sensor;


    public AlgaeShooter() {
        mAlgaeMotor = new TalonFX(Constants.AlgaeSmth.kAlgaeMotorId);
        mAlgaePivotMotor = new TalonFX(Constants.AlgaeSmth.kAlgaePivotMotorId);


		var talonFXConfigs = new TalonFXConfiguration();

		var slot0Configs = talonFXConfigs.Slot0;
		slot0Configs.kS = Constants.AlgaeSmth.kS; // Add 0.0 V output to overcome static friction
		slot0Configs.kV = Constants.AlgaeSmth.kV; // A velocity target of 1 rps results in 0.0 V output
		slot0Configs.kP = Constants.AlgaeSmth.kP; // An error of 1 rotation results in 0.4 V output
		slot0Configs.kI = Constants.AlgaeSmth.kI; // no output for integrated error
		slot0Configs.kD = Constants.AlgaeSmth.kD; // A velocity of 1 rps results in 0.0 V output
	
		var motionMagicConfigs = talonFXConfigs.MotionMagic;
		motionMagicConfigs.MotionMagicCruiseVelocity = Constants.Elevator.kCruiseVelocity; // Target cruise velocity of 80 rps
		motionMagicConfigs.MotionMagicAcceleration = Constants.Elevator.kAcceleration; // Target acceleration of 240 rps/s (0.5 seconds)
		motionMagicConfigs.MotionMagicJerk = Constants.Elevator.kJerk;
	
		mAlgaePivotMotor.getConfigurator().apply(talonFXConfigs);
		m_request = new MotionMagicVoltage(0);
		sensor = new DigitalInput(4);

		extendedPosition = Constants.AlgaeSmth.kExtendedPosition;

        //mHangMotor.setNeutralMode(NeutralModeValue.Brake);
    }

	public enum AlgaeShooterState {
		RETRACTED,
		EXTENDED
	}

    public void registerEnabledLoops(ILooper enabledLooper) {
        enabledLooper.register(new Loop() {
            @Override
            public void onStart(double timestamp) {}

            @Override
            public void onLoop(double timestamp) {
				if(sensor.get()){
					resetPos();
				}
				switch (mPeriodicIO.state) {
					case RETRACTED:
						stop();
						retracted();
					case EXTENDED:
						extended();
					}
			}
            
            @Override
            public void onStop(double timestamp) {
                //stop();
            }

        });
    }

    public void writePeriodicOutputs() {
		mAlgaeMotor.set(mPeriodicIO.speed);
		goToPos();
    }

    @Override
    public void outputTelemetry() {

    }

	public void intake(){
		mPeriodicIO.speed = Constants.AlgaeSmth.kAlgaeSpeed;
	}

	public void shoot(){
		mPeriodicIO.speed = -Constants.AlgaeSmth.kAlgaeSpeed;
	}

	public void stop(){
		mPeriodicIO.speed = 0;
	}

	public void goToPos(){
		mAlgaePivotMotor.setControl(m_request.withPosition(mPeriodicIO.position));
	}

	public void retracted(){
		mPeriodicIO.position = 0.0;
	}

	public void extended(){
		mPeriodicIO.position = extendedPosition;
	}

	public void resetPos(){
		mAlgaePivotMotor.setPosition(0);

	}

	public void setExtendedTarget(double newPosition) {
		extendedPosition = newPosition;
	}
}
