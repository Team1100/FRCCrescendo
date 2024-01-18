// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

// Defines Neo Motor constant
  public static final double kFreeSpeedRpm = 5676;

  // Defines Drive constants
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
      / kDrivingMotorReduction;

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
}
