// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;

public class BarrelPivot extends SubsystemBase {
  private static BarrelPivot m_barrelPivot;

  double m_targetAngle;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;
  TDNumber m_encoderValueRotations;
  TDNumber m_encoderValueAngleDegrees;
  
  CANSparkMax m_BPLeftSparkMax;
  CANSparkMax m_BPRightSparkMax;
  SparkPIDController m_SparkPIDController;
  AbsoluteEncoder m_absoluteEncoder;

  /** Creates a new BarrelPivot. */
  private BarrelPivot() {
    super("BarrelPivot");

    if (RobotMap.BP_ENABLED) {
      m_BPLeftSparkMax = new CANSparkMax(RobotMap.BP_MOTOR_LEFT, MotorType.kBrushless);
      m_BPRightSparkMax = new CANSparkMax(RobotMap.BP_MOTOR_RIGHT, MotorType.kBrushless);

      m_BPLeftSparkMax.restoreFactoryDefaults();
      m_BPRightSparkMax.restoreFactoryDefaults();

      m_BPLeftSparkMax.setIdleMode(IdleMode.kBrake);
      m_BPRightSparkMax.setIdleMode(IdleMode.kBrake);

      m_BPLeftSparkMax.setInverted(false);
      // Motors are set opposite of each other, but they want to spin in the same direction
      m_BPRightSparkMax.follow(m_BPLeftSparkMax, true);

      m_SparkPIDController = m_BPLeftSparkMax.getPIDController();

      m_P = new TDNumber(this, "Barrel Pivot PID", "P", Constants.kBarrelPivotP);
      m_I = new TDNumber(this, "Barrel Pivot PID", "I", Constants.kBarrelPivotI);
      m_D = new TDNumber(this, "Barrel Pivot PID", "D", Constants.kBarrelPivotD);

      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());

      m_absoluteEncoder = m_BPLeftSparkMax.getAbsoluteEncoder(Type.kDutyCycle);
      m_SparkPIDController.setFeedbackDevice(m_absoluteEncoder);

      m_absoluteEncoder.setInverted(false);
      m_absoluteEncoder.setPositionConversionFactor(Constants.kBPEncoderPositionFactorDegrees);
      m_targetAngle = getAngle();

      m_encoderValueRotations = new TDNumber(this, "Encoder Values", "Rotations", getAngle() / Constants.kBPEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees = new TDNumber(this, "Encoder Values", "Angle (degrees)", getAngle());
    }
  }

  public static BarrelPivot getInstance() {
    if (m_barrelPivot == null) {
      m_barrelPivot = new BarrelPivot();
    }
    return m_barrelPivot;
  }

  public double getAngle() {
    return m_absoluteEncoder.getPosition();
  }

  public void setTargetAngle(double angle) {
    m_targetAngle = angle;
    m_SparkPIDController.setReference(m_targetAngle, ControlType.kVelocity);
  }

  public double getTargetAngle() {
    return m_targetAngle;
  }

  public void setZeroAsCurrentPosition() {
    m_absoluteEncoder.setZeroOffset(getAngle());
    m_targetAngle = 0;
  }

  @Override
  public void periodic() {
    if (RobotMap.BP_ENABLED) {
      if (Constants.kEnableBarrelPivotPIDTuning) {
        m_SparkPIDController.setP(m_P.get());
        m_SparkPIDController.setI(m_I.get());
        m_SparkPIDController.setD(m_D.get());
      }

      m_encoderValueRotations.set(getAngle() / Constants.kBPEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees.set(getAngle());
    }
    super.periodic();
  }
}
