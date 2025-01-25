package frc.robot.autos.actions;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Timer;

public class IntakeAction implements Action {
    private TalonFX motor = new TalonFX(0); // Replace with your motor's ID
    private Ultrasonic sensor = new Ultrasonic(0, 1); // Replace with your sensor's ID
    private Timer timer = new Timer();
    private boolean objectDetected = false;

    @Override
    public boolean isFinished() {
        return objectDetected && timer.get() > 10;
    }

    @Override
    public void update() {
        if (sensor.getRangeInches() < 12) { // TODO: Adjust the range value as needed
            objectDetected = true;
            timer.start();
        }
        if (objectDetected) {
            motor.set(0.5); // 50% speed
        } else {
            motor.stopMotor();
        }
    }

    @Override
    public void done() {
        motor.stopMotor();
    }

    @Override
    public void start() {
        // No setup needed for this action
    }
}