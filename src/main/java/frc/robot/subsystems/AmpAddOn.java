// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkPIDController;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.NoteProximitySensor;

public class AmpAddOn extends SubsystemBase {
  private static AmpAddOn m_AmpAddOn;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;

  double m_targetAngle;

  TDNumber m_PivotP;
  TDNumber m_PivotI;
  TDNumber m_PivotD;
  TDNumber m_encoderValueRotations;
  TDNumber m_encoderValueAngleDegrees;

  CANSparkMax m_CanSparkMax;
  CANSparkMax m_PivotCanSparkMax;
  SparkPIDController m_SparkPIDController;
  SparkPIDController m_PivotSparkPIDController;
  AbsoluteEncoder m_absoluteEncoder;

  NoteProximitySensor m_NoteProximitySensor;
  
  /** Creates a new AmpAddOn. */
  private AmpAddOn() {
    super("AmpAddOn");
    if(RobotMap.A_ENABLED){
      m_CanSparkMax = new CANSparkMax(RobotMap.A_MOTOR, MotorType.kBrushless);
      m_PivotCanSparkMax = new CANSparkMax(RobotMap.A_PIVOT_MOTOR, MotorType.kBrushless);

      m_CanSparkMax.restoreFactoryDefaults();
      m_PivotCanSparkMax.restoreFactoryDefaults();

      m_PivotCanSparkMax.setIdleMode(IdleMode.kBrake);

      m_CanSparkMax.setInverted(false);
      m_PivotCanSparkMax.setInverted(false);
      
      m_SparkPIDController = m_CanSparkMax.getPIDController();

      m_P = new TDNumber(this, "Amp Roller PID", "P", Constants.kAmpP);
      m_I = new TDNumber(this, "Amp Roller PID", "I", Constants.kAmpI);
      m_D = new TDNumber(this, "Amp Roller PID", "D", Constants.kAmpD);

      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());

      m_PivotSparkPIDController = m_PivotCanSparkMax.getPIDController();

      m_PivotP = new TDNumber(this, "Amp Pivot PID", "P", Constants.kAmpPivotP);
      m_PivotI = new TDNumber(this, "Amp Pivot PID", "I", Constants.kAmpPivotI);
      m_PivotD = new TDNumber(this, "Amp Pivot PID", "D", Constants.kAmpPivotD);

      m_PivotSparkPIDController.setP(m_PivotP.get());
      m_PivotSparkPIDController.setI(m_PivotI.get());
      m_PivotSparkPIDController.setD(m_PivotD.get());

      m_absoluteEncoder = m_PivotCanSparkMax.getAbsoluteEncoder(Type.kDutyCycle);
      m_PivotSparkPIDController.setFeedbackDevice(m_absoluteEncoder);

      m_absoluteEncoder.setInverted(false);
      m_absoluteEncoder.setPositionConversionFactor(Constants.kAEncoderPositionFactorDegrees);
      m_targetAngle = getAngle();

      m_encoderValueRotations = new TDNumber(this, "Encoder Values", "Rotations", getAngle() / Constants.kAEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees = new TDNumber(this, "Encoder Values", "Angle (degrees)", getAngle());

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.A_NOTE_SENSOR, this);
    }
  }

  public static AmpAddOn getInstance() {
    if (m_AmpAddOn == null) {
      m_AmpAddOn = new AmpAddOn();
    }
    return m_AmpAddOn;
  }

  public void setSpeed(double RPM, boolean backwards) {
    if (!backwards) {
      m_SparkPIDController.setReference(RPM, ControlType.kVelocity);
    }
    else {
      m_SparkPIDController.setReference(-RPM, ControlType.kVelocity);
    }
  }

  public void spinIn(double speed) {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(-speed);
    }
  }

  public void spinOut(double speed) {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(speed);
    }
  }

  public void spinStop() {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(0);
    }
  }

  public double getAngle() {
    return m_absoluteEncoder.getPosition();
  }

  public void setTargetAngle(double angle) {
    m_targetAngle = angle;
    m_PivotSparkPIDController.setReference(m_targetAngle, ControlType.kVelocity);
  }

  public double getTargetAngle() {
    return m_targetAngle;
  }

  public void setZeroAsCurrentPosition() {
    m_absoluteEncoder.setZeroOffset(getAngle());
    m_targetAngle = 0;
  }

  public boolean hasNote() {
    return m_NoteProximitySensor.hasNote();
  }

  public boolean noteCenteredOnSensor() {
    return m_NoteProximitySensor.noteIsCentered();
  }

  @Override
  public void periodic() {
    if (RobotMap.A_ENABLED) {
      if (Constants.kEnableAmpAddOnPIDTuning) {
        m_SparkPIDController.setP(m_P.get());
        m_SparkPIDController.setI(m_I.get());
        m_SparkPIDController.setD(m_D.get());

        m_PivotSparkPIDController.setP(m_PivotP.get());
        m_PivotSparkPIDController.setI(m_PivotI.get());
        m_PivotSparkPIDController.setD(m_PivotD.get());
      }

      m_encoderValueRotations.set(getAngle() / Constants.kBPEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees.set(getAngle());
    }
    super.periodic();
    if(m_NoteProximitySensor != null) {
      m_NoteProximitySensor.update();
    }
  }
}
