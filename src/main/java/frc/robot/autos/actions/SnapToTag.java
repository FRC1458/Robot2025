package frc.robot.autos.actions;

import frc.robot.Constants;
import frc.robot.FieldLayout;
import frc.robot.RobotState;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.print.event.PrintJobListener;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveDrive;
public class SnapToTag implements Action {
	private SwerveDrive mDrive = SwerveDrive.getInstance();

    private Pose2d initialPose = new Pose2d();
    private Pose2d curPose = new Pose2d();
	private Transform2d deltaPose = new Transform2d();
	private Translation2d normalizedDelta = new Translation2d();
    private Pose2d finalPose = new Pose2d();
	private boolean shouldFlip = false;

    private int tag = 0;
    private int mNum = 0;
    protected static boolean isRunning = false;

	private Pose2d delta = new Pose2d();

	private Transform2d pid = new Transform2d(
		1.0,
		1.0,
		new Rotation2d(1.0)
	);

	private Transform2d prevError  = new Transform2d();
	private Transform2d intError  = new Transform2d();
    /**
     * @param isLeft - if the robot is on the left side of the field
     */
    public SnapToTag(int num) {
        mNum = num;
    }

    @Override
    public void start() {
		System.out.println("starting snap");
		curPose = RobotState.getInstance().getLatestFieldToVehicle();
		deltaPose = finalPose.minus(curPose);
        getTagPosition();
    }

    @Override
    public void update() {
		curPose = RobotState.getInstance().getLatestFieldToVehicle();
		deltaPose = finalPose.minus(curPose);
		normalizedDelta = deltaPose.getTranslation().times(1/deltaPose.getTranslation().getNorm());
		delta = new Pose2d(normalizedDelta.times(
			Math.min(
				deltaPose.getTranslation().getNorm(),
				Constants.Swerve.maxSpeed
			)),
			deltaPose.getRotation());
		System.out.println(delta.toString());
		mDrive.feedTeleopSetpoint(ChassisSpeeds.fromFieldRelativeSpeeds(
			delta.getX() * pid.getY(),
			delta.getY() * pid.getY(),
			delta.getRotation().getRadians() * pid.getRotation().getRadians(),
			curPose.getRotation()
		));
    }

    @Override
    public boolean isFinished() {
		System.out.println("Checking snap " + deltaPose.getTranslation().getNorm() + " " + deltaPose.getRotation().getDegrees());
		return deltaPose.getTranslation().getNorm() < 0.02 && Math.abs(deltaPose.getRotation().getDegrees()) < 3;
    }

    @Override
    public void done() {
		mDrive.stop();
		System.out.println("finished snap " + deltaPose.getTranslation().getNorm() + " " + deltaPose.getRotation().getDegrees());
    }

    private void getTagPosition() {
        tag = FieldLayout.getClosestTag(initialPose.getTranslation()).ID;
        for (int num : new int[] {1, 2, 12, 13}) {
            if (num == tag) {
                shouldFlip = true;
                break;
            }
        }
        Rotation2d aprilTagRotation = FieldLayout.getClosestTag(initialPose.getTranslation()).pose.getRotation().toRotation2d();//TODO: check if flipped 180 deg
        finalPose = new Pose2d(FieldLayout.getClosestTag(initialPose.getTranslation()).pose
								.getTranslation().toTranslation2d().
								plus(FieldLayout.offsets[mNum].rotateBy(aprilTagRotation)),
								aprilTagRotation.minus(new Rotation2d(shouldFlip ? 0.0 : Math.PI)));
    }
}