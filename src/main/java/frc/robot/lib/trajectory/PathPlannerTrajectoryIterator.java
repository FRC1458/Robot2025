package frc.robot.lib.trajectory;

import java.lang.reflect.*;
import java.util.List;
import edu.wpi.first.math.trajectory.*;

import edu.wpi.first.math.geometry.*;
import com.pathplanner.lib.trajectory.*;

//dc.10.21.2024, rewrite the TrajectoryIterator class based on wpilib Trajectory package, main functions as following
//
//2. rewrite the advance () method which is used often by its callers
//
public class PathPlannerTrajectoryIterator {
    protected double progress_ = 0.0;
    protected PathPlannerTrajectoryState current_PathPlannerSample_;     //corresponding to TrajectorySamplePoint in citrus code
    protected PathPlannerTrajectory mCurrentPathPlannerTrajectory=null;
    protected Trajectory.State current_sample_;
    //construtor code
    public PathPlannerTrajectoryIterator (PathPlannerTrajectory curTrajectory){
        mCurrentPathPlannerTrajectory=curTrajectory;
        current_PathPlannerSample_ = curTrajectory.getStates().get(0);
        //progress_ = view_.first_interpolant();        //dc. is it not zero??
    }

    //advance by additional time on the trajectory
    public Trajectory.State advance (double additional_progress){
        if(additional_progress == Double.POSITIVE_INFINITY){
        }
        progress_ = Math.max(0.0, Math.min(mCurrentPathPlannerTrajectory.getTotalTimeSeconds(), progress_ + additional_progress));
        current_PathPlannerSample_ = mCurrentPathPlannerTrajectory.sample(progress_);
        current_sample_ = fromPathPlannerTrajectoryState(current_PathPlannerSample_);
        return current_sample_;
    }

    //preview the trajectory
    public Trajectory.State preview (double additional_progress){
        final double progress = Math.max(0.0, Math.min(mCurrentPathPlannerTrajectory.getTotalTimeSeconds(), progress_ + additional_progress));
        PathPlannerTrajectoryState future_PathPlannerSample_ = mCurrentPathPlannerTrajectory.sample(progress);
        Trajectory.State future_sample_ = fromPathPlannerTrajectoryState(future_PathPlannerSample_);
        return future_sample_;
    }

    public boolean isDone() {
        return getRemainingProgress() == 0.0;
    }

    public double getProgress() {
        return progress_;
    }

    public double getRemainingProgress() {
        return Math.max(0.0, mCurrentPathPlannerTrajectory.getTotalTimeSeconds() - progress_);
    }

    public Trajectory.State getLastPoint(){
        List<PathPlannerTrajectoryState> stateList = mCurrentPathPlannerTrajectory.getStates();
        PathPlannerTrajectoryState lastPathPlannerSample = stateList.get(stateList.size()-1);
        Trajectory.State lastSample = fromPathPlannerTrajectoryState(lastPathPlannerSample);
        return lastSample;
    }

    //access to the current trajectory properties
    public PathPlannerTrajectory trajectory(){ return mCurrentPathPlannerTrajectory;}

    //get the current state
    public Trajectory.State getState(){
        return fromPathPlannerTrajectoryState(current_PathPlannerSample_);
    }
    public Trajectory.State fromPathPlannerTrajectoryState(PathPlannerTrajectoryState state) {
        Trajectory.State converted = new Trajectory.State(state.timeSeconds,state.linearVelocity,0,state.pose,state.fieldSpeeds.omegaRadiansPerSecond/state.linearVelocity);
        return converted;
    }
}
