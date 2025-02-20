package frc.robot.teleop;

import frc.robot.Loops.CrashTrackingRunnable;
import frc.robot.autos.AutoModeEndedException;
import frc.robot.autos.actions.Action;
import frc.robot.subsystems.SwerveDrive.PeriodicIO;
import edu.wpi.first.math.proto.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to run auto actions in teleop mode.
 */
public class TeleopActionExecutor {
	protected double m_update_rate = 1.0 / 50.0;
	protected boolean m_active = false;
	protected double startTime = 0.0;
	public List<Action> m_runningActions = new ArrayList<Action>();
	private static TeleopActionExecutor mInstance;

	private Thread m_thread;

	public TeleopActionExecutor() {
		m_thread = new Thread(new CrashTrackingRunnable() {
			@Override
			public void runCrashTracked() {
				run();
				System.out.println("Teleop Action Executor Running!");
			}
			
		});
	
		m_thread.start();
	}



	public void run() {
		for (Action action : m_runningActions) {
			if (!action.isFinished()) {
				action.update();
			} else {
				action.done();
			}
		}
		long waitTime = (long) (m_update_rate * 1000.0);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static TeleopActionExecutor getInstance() {
		if (mInstance == null) {
			mInstance = new TeleopActionExecutor();
		}
		return mInstance;
	}

	protected double currentTime() {
		return Timer.getFPGATimestamp() - startTime;
	}

	public void runAction(Action action) {
		action.start();
		m_runningActions.add(action);
	}

	public void abort() {
		if (m_runningActions.isEmpty()) {
			System.out.println("No running actions to abort");
			return;
		}
		for (Action action : m_runningActions) {
			action.done();
		}
		String actionNames = "";
		for (Action action : m_runningActions) {
			actionNames += action.getClass().toString() + " ";
		}
		m_runningActions.clear();
		System.out.println("Aborted "+actionNames+"at time "+Timer.getFPGATimestamp());
	}
}
