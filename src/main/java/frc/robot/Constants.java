// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

import frc.robot.utils.vision.VisionConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double END_MATCH_TIME_S = 134.9;

    public static final double DEGREES_PER_REVOLUTION = 360;

    // Defines AmpAddOn constants
    public static final double AMP_SPEED = 0.5;
    public static final double AMP_SPEED_RPM = 0;

    public static final boolean kEnableAmpAddOnPIDTuning = false;
    public static final double kAmpP = 0;
    public static final double kAmpI = 0;
    public static final double kAmpD = 0;
    public static final double kAmpPivotP = 0.0045;
    public static final double kAmpPivotI = 0;
    public static final double kAmpPivotD = 0;

    public static final double kAEncoderZeroOffset = 240;
    public static final double kAEncoderPositionFactorDegrees = DEGREES_PER_REVOLUTION;
    public static final double kAPivotUpperLimitDegrees = 300;
    public static final double kAPivotLowerLimitDegrees = 70;
    public static final double kAPivotToleranceDegrees = 5;
    public static final double kAPivotIntakePositionDegrees = 80;
    public static final double kAPivotUpPositionDegrees = 290;
    public static final double kAPivotDeliverAmpPositionDegrees = 197;
    
    public static final double A_ANGLE_INCREMENT_DEGREES = 2;
    public static final double kADeadband = 0.05;

    // Defines Barrel constants
    public static final boolean kEnableBarrelPIDTuning = false;
    public static final double kBarrelP = 0;
    public static final double kBarrelI = 0;
    public static final double kBarrelD = 0;

    public static final Transform3d kRobotToBarrel =
            new Transform3d(new Translation3d(-0.33, 0.0, 0.2),
                new Rotation3d(0, 0, 0));

    public static final double BARREL_SPEED = 0.7;
    public static final double BARREL_SPEED_RPM = 0;

    // Defines Barrel Pivot constants
    public static final boolean kEnableBarrelPivotPIDTuning = false;
    public static final double kBarrelPivotP = 0.06;
    public static final double kBarrelPivotI = 0.000001;
    public static final double kBarrelPivotD = 0;
    public static final double kBarrelPivotLowerLimitDegrees = 80;
    public static final double kBarrelPivotUpperLimitDegrees = 140;

    public static final double kBPEncoderPositionFactorDegrees = DEGREES_PER_REVOLUTION;

    public static final double BP_ANGLE_INCREMENT_DEGREES = 0.25;
    public static final double kBPDeadband = 0.05;

    public static final double BP_SPEED = 0.1;
    
    public static final double BP_ANGLE_OFFSET_TO_HORIZONTAL_DEGREES = 70;
    public static final double BP_ANGLE_TOLERANCE_DEGREES = 3;
    public static final double BP_SPEAKER_TOLERANCE_DEGREES = 1;
    public static final double BP_SOURCE_ANGLE_DEGREES = 107.5;
    public static final double BP_AMP_SCORING_ANGLE_DEGREES = 100;
    public static final double BP_SHOOTER_SCORING_ANGLE_DEGREES = 120.7;
    public static final double BP_DOWN_ANGLE_DEGREES = 81;
    public static final double BP_FERRY_ANGLE_DEGREES = 110;

    // Defines Drive constants
    public static final double D_ANGLE_TOLERANCE_DEGREES = 2.5;

    public static final double kMaxSpeedMetersPerSecond = 4.8/2;
    public static final double kMaxAngularSpeed = 2 * Math.PI/2; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Defines Elevator constants
    public static final double ELEVATOR_SPEED = 0.2;
    public static final double ELEVATOR_SPEED_RPM = 0;

    public static final boolean kEnableElevatorPivotPIDTuning = false;
    public static final double kElevatorPivotP = 0;
    public static final double kElevatorPivotI = 0;
    public static final double kElevatorPivotD = 0;

    // Chassis configuration
    // Distance between centers of right and left wheels on robot
    public static final double kTrackWidth = Units.inchesToMeters(RobotMap.R_TRACK_WIDTH_INCHES);
    // Distance between front and back wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(RobotMap.R_WHEEL_BASE_INCHES);
    // Distance from robot center to furthest module
    public static final double kBaseRadius = Units.inchesToMeters(RobotMap.R_BASE_RADIUS_INCHES);
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    public static final HolonomicPathFollowerConfig kPathFollowerConfig =
       new HolonomicPathFollowerConfig(
         new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
         new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
         Constants.kMaxSpeedMetersPerSecond, // Max module speed, in m/s
         Constants.kBaseRadius, // Drive base radius in meters
         new ReplanningConfig(true, true)); // Default path replanning config. See the API for the options here
    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    public static final boolean kGyroReversed = false;

    // Defines Neo Motor constant
    public static final double kFreeSpeedRpm = 5676;

    //Sensor Constants
    public static final boolean kSensorThreadingEnabled = true;
    public static final int kSensorDebounceCycles = 3;
    public static final int kThreadedSensorDebounceCycles = 7;
    public static final double kConfusedSensorTimeoutSeconds = 1.5;

    // Defines Swerve Module constants
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.0762;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction; // meters per second

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    public static final double kTurningP = 1;
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps

    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.2;

    // Defines Intake constants
    public static final boolean kEnableIntakePIDTuning = false;
    public static final double kIntakeP = 0.000200;
    public static final double kIntakeI = 0.000001;
    public static final double kIntakeD = 0.003600;

    public static final double kIntakeSpeed = 0.5;
    public static final double INTAKE_SPEED_RPM = 3000;

    public static final int INTAKE_CURRENT_LIMIT_AMPS = 100;

    // Defines Lights constants
    public static final int LED_LENGTH = 42; // number of LEDs

    // Defines Shooter constants
    public static final boolean kEnableShooterPIDTuning = false;
    public static final double kShooterP = 0.000550;
    public static final double kShooterI = 0.000000350;
    public static final double kShooterD = 0.001000;

    public static final double LEFT_SHOOTER_SPEED = 0.40;
    public static final double RIGHT_SHOOTER_SPEED = 0.70;
    public static final double LEFT_SHOOTER_SPEED_RPM = 2000;
    public static final double RIGHT_SHOOTER_SPEED_RPM = 2000;
    public static final double SHOOTER_SPEED_TOLERANCE = 500; // RPM Diff

    public static final double SHOOTER_INTAKING_SPEED = 0.2;
    public static final double SHOOTER_INTAKING_SPEED_RPM = 500;

    // TODO: accurately test & remeasure these constants
    public static final VisionConfig[] kVisionSystems = {
        new VisionConfig("Arducam_OV9281_USB_Camera",
                         new Transform3d(new Translation3d(Units.inchesToMeters(11.5), Units.inchesToMeters(0.5), Units.inchesToMeters(20.5)), 
                                new Rotation3d(0, Units.degreesToRadians(-30), Units.degreesToRadians(180))),
                         PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
                         PoseStrategy.LOWEST_AMBIGUITY),
        new VisionConfig("Forward_Facing_Arducam_OV9281",
                         new Transform3d(new Translation3d(Units.inchesToMeters(15.5), Units.inchesToMeters(0.5), Units.inchesToMeters(21.5)), 
                new Rotation3d(0, Units.degreesToRadians(20), Units.degreesToRadians(0))),
                         PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, 
                         PoseStrategy.LOWEST_AMBIGUITY)
    };

    //Maximum ambiguity accepted as a valid result from the vision systems
    public static final double kMaxValidAmbiguity = 0.2;

    // The layout of the AprilTags on the field
    public static final AprilTagFieldLayout kTagLayout =
            AprilTagFields.kDefaultField.loadAprilTagLayoutField();

    // Height of Speaker mouth above tag pose
    public static final double SPEAKER_HEIGHT_OFFSET = 0.6;
    public static final double SPEAKER_ADJUSTMENT_INCREMENT_M = 0.05;

    // TODO: Determine & test these Poses on a real field
    // Most of these are simulation guesses, aside from kAmpScoreBluePose and kAmpScoreRedPose
    public static final Pose2d kSource1RedPose = new Pose2d(0.71, 1.45, new Rotation2d(Units.degreesToRadians(-120)));
    public static final Pose2d kSource3RedPose = new Pose2d(1.78, 0.87, new Rotation2d(Units.degreesToRadians(-120)));
    public static final Pose2d kAmpScoreRedPose = new Pose2d(14.687441, 7.56, new Rotation2d(-Math.PI / 2));
    public static final Pose2d kSpeakerScoreRedPose = new Pose2d();

    public static final Pose2d kSource1BluePose = new Pose2d(16, 1.47, new Rotation2d(Units.degreesToRadians(-60)));
    public static final Pose2d kSource3BluePose = new Pose2d(14.87, 0.89, new Rotation2d(Units.degreesToRadians(-60)));
    public static final Pose2d kAmpScoreBluePose = new Pose2d(1.82, 7.56, new Rotation2d(-Math.PI / 2));
    public static final Pose2d kSpeakerScoreBluePose = new Pose2d();

    // The standard deviations of our vision estimated poses, which affect correction rate
    // TODO: (Fake values. Experiment and determine estimation noise on an actual robot.)
    public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
    public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);

    //Driver control rate limits
    public static final double kMaxAccelerationMetersPerSecondSquared = 10;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = 4*Math.PI;

    //Autonomous control rate limits(Driver control limits are applied after so should be <=)
    public static final double kAutoMaxAccelerationMpSS = 3;
    public static final double kAutoMaxSpeedMpS = 3.5;
    public static final double kAutoMaxAngularAccelRpSS = 2 * Math.PI;
    public static final double kAutoMaxAngularSpeedRpS = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 5;
    public static final double kIThetaController = 0.1;
    public static final double kDThetaController = 0.05;
    public static final double kThetaTolerance = 0.1;//radians

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
}
