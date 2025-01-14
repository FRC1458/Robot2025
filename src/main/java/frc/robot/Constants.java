package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

//dc.10.21.2024 classes used in SwerveConstants
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.robot.lib.util.COTSTalonFXSwerveConstants;
import frc.robot.lib.util.SwerveModuleConstants;
import frc.robot.subsystems.SwerveDrive.KinematicLimits;
import frc.robot.subsystems.limelight.GoalTracker;

public final class Constants {
    public static final double stickDeadband = 0.07;
    public static boolean isEpsilon;

    // robot loop time
	public static final double kLooperDt = 0.02;
    // Disables extra smart dashboard outputs that slow down the robot
	public static final boolean disableExtraTelemetry = false;

    public static final class Swerve {
        public static final int pigeonID = 20;

        public static final COTSTalonFXSwerveConstants chosenModule =  //TODO: This must be tuned to specific robot
        COTSTalonFXSwerveConstants.SDS.MK4i.KrakenX60(COTSTalonFXSwerveConstants.SDS.MK4i.driveRatios.L3);

        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(23.5); //TODO: This must be tuned to specific robot
        public static final double wheelBase = Units.inchesToMeters(23.5); //TODO: This must be tuned to specific robot
        public static final double wheelCircumference = chosenModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final Translation2d[] swerveModuleLocations = {  //dc.10.28.2024, need for WheelTracker.java
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0)
         };
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            swerveModuleLocations[0],
            swerveModuleLocations[1],
            swerveModuleLocations[2],
            swerveModuleLocations[3]);//DC.11.14.24 shall be the 4th wheel 

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenModule.driveGearRatio;
        public static final double angleGearRatio = chosenModule.angleGearRatio;

        /* Motor Inverts */
        public static final InvertedValue angleMotorInvert = chosenModule.angleMotorInvert;
        public static final InvertedValue driveMotorInvert = chosenModule.driveMotorInvert;

        /* Angle Encoder Invert */
        public static final SensorDirectionValue cancoderInvert = chosenModule.cancoderInvert;

        /* Swerve Current Limiting */
        public static final int angleCurrentLimit = 20;
        public static final int angleCurrentThreshold = 30;
        public static final double angleCurrentThresholdTime = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveCurrentLimit = 30; //dc.11.9.24 reduce max current per motor, total current needs to time motor-count(8)
        public static final int driveCurrentThreshold = 45; 
        public static final double driveCurrentThresholdTime = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = chosenModule.angleKP;
        public static final double angleKI = chosenModule.angleKI;
        public static final double angleKD = chosenModule.angleKD;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.12; //TODO: This must be tuned to specific robot
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0004;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values From SYSID */
        public static final double driveKS = 0.32; //TODO: This must be tuned to specific robot
        public static final double driveKV = 1.51;
        public static final double driveKA = 0.27;

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 4; //TODO: dc 11.9.24, increase max speed so that we can observe amplified drivetrain bahavior 
        /** Radians per Second */
        public static final double maxAngularVelocity = 3.14; //TODO: This must be tuned to specific robot

        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Coast;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;

        /* Module Specific Constants */
        /* Front Left Module - Module 0 */
        public static final class FrontLeftMod { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 8;
            public static final int angleMotorID = 10;
            public static final int canCoderID = 7;
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.1521);
            public static final boolean isDriveInverted = true;
            public static final boolean isAngleInverted = false;
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, isDriveInverted, isAngleInverted);
        }

        /* Front Right Module - Module 1 */
        public static final class FrontRightMod { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 9;
            public static final int angleMotorID = 11;
            public static final int canCoderID = 6;
            public static final boolean isDriveInverted = true;
            public static final boolean isAngleInverted = false;
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.4243);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, isDriveInverted, isAngleInverted);
        }
        
        /* Back Left Module - Module 2 */
        public static final class BackLeftMod { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 3;
            public static final int angleMotorID = 5;
            public static final int canCoderID = 0;
            public static final boolean isDriveInverted = true;
            public static final boolean isAngleInverted = false;
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.1643);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, isDriveInverted, isAngleInverted);
        }

        /* Back Right Module - Module 3 */
        public static final class BackRightMod { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 4;
            public static final int angleMotorID = 2;
            public static final int canCoderID = 1;
            public static final boolean isDriveInverted = false;
            public static final boolean isAngleInverted = false;
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.4148);
            public static final SwerveModuleConstants constants =
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset, isDriveInverted, isAngleInverted);
        }
    }

    public static final class AutoConstants { //TODO: The below constants are used in the example auto, and must be tuned to specific robot
        public static final double kMaxSpeedMetersPerSecond = 1;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = 1;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }

    public static final class LimelightConstants {

		public static final double kNoteHeight = 0.0508;
		public static final double kNoteTargetOffset = 0.2;
		public static final double kMaxNoteTrackingDistance = 6.75;
		public static final double kNoteTrackEpsilon = 1.0;

		public static final String kName = "limelight";
		public static final Translation2d kRobotToCameraTranslation = new Translation2d(0.0, 0.0);
		public static final double kCameraHeightMeters = isEpsilon ? 0.59 : 0.65;
		public static final Rotation2d kCameraPitch = Rotation2d.fromDegrees(-18.0);
		public static final Rotation2d kCameraYaw = Rotation2d.fromDegrees(0.0);

		public static final GoalTracker.Configuration kNoteTrackerConstants = new GoalTracker.Configuration();

		static {
			kNoteTrackerConstants.kMaxTrackerDistance = 0.46;
			kNoteTrackerConstants.kMaxGoalTrackAge = 0.5;
			kNoteTrackerConstants.kCameraFrameRate = 30.0;
			kNoteTrackerConstants.kStabilityWeight = 1.0;
			kNoteTrackerConstants.kAgeWeight = 0.2;
			kNoteTrackerConstants.kSwitchingWeight = 0.2;
		}
	}

    //dc.10.21.2024, citrus code constants
    public static final class SwerveConstants {
        public static final boolean invertGyro = false; // Always ensure Gyro is CCW+ CW-

   		/* Heading Controller */

		// Stabilize Heading PID Values
		public static final double kStabilizeSwerveHeadingKp = 10.0;
		public static final double kStabilizeSwerveHeadingKi = 0.0;
		public static final double kStabilizeSwerveHeadingKd = 0.3;
		public static final double kStabilizeSwerveHeadingKf = 2.0;

		// Snap Heading PID Values
		public static final double kSnapSwerveHeadingKp = 10.0;
		public static final double kSnapSwerveHeadingKi = 0.0;
		public static final double kSnapSwerveHeadingKd = 0.6;
		public static final double kSnapSwerveHeadingKf = 1.0;

        /*dc.10.21.2024 mapping existing constants so that ported citrus code only needs minimal changes */
        public static final SwerveDriveKinematics kKinematics = Swerve.swerveKinematics;
        //public static final boolean driveMotorInvert = false;   //TODO: need to verify with the actual Robot setting
        //public static final boolean angleMotorInvert = true;    //TODO: need to verify with the actual Robot setting
        public static final double wheelDiameter = Swerve.chosenModule.wheelDiameter; //??4.0inch
        public static final double wheelCircumference = Swerve.chosenModule.wheelCircumference;
        public static final double driveGearRatio = Swerve.chosenModule.driveGearRatio;//?? Constants.isEpsilon ? 5.82 : 5.82; 
        public static final double angleGearRatio = Swerve.chosenModule.angleGearRatio;
        public static final double maxSpeed = Swerve.maxSpeed; 
        public static final double maxAngularVelocity = Swerve.maxAngularVelocity;
        public static final double kV = 12 * Math.PI * wheelDiameter / (driveGearRatio * maxSpeed); //TODO: need to finetune with the actual robot
        public static final double maxAutoSpeed = maxSpeed * 0.85;  // Max out at 85% to ensure attainable speeds, 
                                                                    // This max_speed needs to be consistent with the max_velocity used by pathweaver 
                                                                    // It will be used to normalize velocity on trajectory point
        public static final double kCancoderBootAllowanceSeconds = 10.0;

        public static final KinematicLimits kUncappedLimits = new KinematicLimits();

		static {
			kUncappedLimits.kMaxDriveVelocity = maxSpeed;
			kUncappedLimits.kMaxAccel = Double.MAX_VALUE;
			kUncappedLimits.kMaxAngularVelocity = Swerve.maxAngularVelocity;
			kUncappedLimits.kMaxAngularAccel = Double.MAX_VALUE;
		}

        public static final boolean invertYAxis = false;
		public static final boolean invertRAxis = false;
		public static final boolean invertXAxis = false;

        /* TalonFx module constants*/
        
        public static TalonFXConfiguration AzimuthFXConfig() {
            TalonFXConfiguration config = new TalonFXConfiguration();

            config.Slot0.kP = .3;
            config.Slot0.kI = 0.0;
            config.Slot0.kD = 0.0008;
            config.Slot0.kS = 0.0;
            config.Slot0.kV = 0.0;

            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.CurrentLimits.StatorCurrentLimit = Swerve.angleCurrentLimit;//80;

            config.CurrentLimits.SupplyCurrentLimitEnable = true;
            config.CurrentLimits.SupplyCurrentLimit = Swerve.angleCurrentLimit;//60;
            config.CurrentLimits.SupplyCurrentLowerLimit = Swerve.angleCurrentThreshold;//dc:  ctre updates 2025 
            config.CurrentLimits.SupplyCurrentLowerTime = Swerve.angleCurrentThresholdTime; ;//dc:  ctre updates 2025

            config.Voltage.PeakForwardVoltage = 12.0;
            config.Voltage.PeakReverseVoltage = -12.0;

            config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

            return config;
        }

        public static TalonFXConfiguration DriveFXConfig() {
			TalonFXConfiguration config = new TalonFXConfiguration();

			config.Slot0.kP = 0.030 * 12.0;
			config.Slot0.kI = 0.0;
			config.Slot0.kD = 0.000000 * 12.0;
			config.Slot0.kS = 0.1;
			config.Slot0.kV = 12 * Math.PI * wheelDiameter / (driveGearRatio * maxSpeed);

			config.CurrentLimits.StatorCurrentLimitEnable = true;
			config.CurrentLimits.StatorCurrentLimit = Swerve.driveCurrentLimit;//citrus code value = 110;

			config.CurrentLimits.SupplyCurrentLimitEnable = true;
			config.CurrentLimits.SupplyCurrentLimit = Swerve.driveCurrentLimit;//citrus value = 90;
            config.CurrentLimits.SupplyCurrentLowerLimit = Swerve.driveCurrentThreshold;//add this to limit current spiking 
			config.CurrentLimits.SupplyCurrentLowerTime = 0.5;

			config.Voltage.PeakForwardVoltage = 12.0;
			config.Voltage.PeakReverseVoltage = -12.0;

			config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

			config.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = Swerve.openLoopRamp;
			config.OpenLoopRamps.VoltageOpenLoopRampPeriod = Swerve.openLoopRamp;
			return config;
		}

    }
    
    /* dc.10.21.2024 extra constants needed during porting of citrus SwerveModule.java code */

    // Timeout constants
	public static final int kLongCANTimeoutMs = 100;
}
