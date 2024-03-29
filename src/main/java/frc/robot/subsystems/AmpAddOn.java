// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.math.MathUtil;

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

  TDNumber m_TDrollerP;
  TDNumber m_TDrollerI;
  TDNumber m_TDrollerD;
  double m_rollerP = Constants.kAmpP;
  double m_rollerI = Constants.kAmpI;
  double m_rollerD = Constants.kAmpD;

  TDNumber m_targetAngle;

  TDNumber m_TDpivotP;
  TDNumber m_TDpivotI;
  TDNumber m_TDpivotD;
  double m_pivotP = Constants.kAmpPivotP;
  double m_pivotI = Constants.kAmpPivotI;
  double m_pivotD = Constants.kAmpPivotD;

  TDNumber m_encoderValueRotations;
  TDNumber m_encoderValueAngleDegrees;
  TDNumber m_rollerSpeedRPM;

  CANSparkMax m_CanSparkMax;
  CANSparkMax m_PivotCanSparkMax;
  SparkPIDController m_SparkPIDController;
  SparkPIDController m_PivotSparkPIDController;
  AbsoluteEncoder m_absoluteEncoder;

  TDNumber m_currentOutput;
  TDNumber m_pivotCurrentOutput;

  // initially set motor to "Don't move"
  private double m_lastAngle = 0;
  private double m_lastSpeed = 0;
  
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

      m_TDrollerP = new TDNumber(this, "Amp Roller PID", "P", Constants.kAmpP);
      m_TDrollerI = new TDNumber(this, "Amp Roller PID", "I", Constants.kAmpI);
      m_TDrollerD = new TDNumber(this, "Amp Roller PID", "D", Constants.kAmpD);

      m_SparkPIDController.setP(m_rollerP);
      m_SparkPIDController.setI(m_rollerI);
      m_SparkPIDController.setD(m_rollerD);

      m_PivotSparkPIDController = m_PivotCanSparkMax.getPIDController();

      m_TDpivotP = new TDNumber(this, "Amp Pivot PID", "P", Constants.kAmpPivotP);
      m_TDpivotI = new TDNumber(this, "Amp Pivot PID", "I", Constants.kAmpPivotI);
      m_TDpivotD = new TDNumber(this, "Amp Pivot PID", "D", Constants.kAmpPivotD);

      m_PivotSparkPIDController.setP(m_pivotP);
      m_PivotSparkPIDController.setI(m_pivotI);
      m_PivotSparkPIDController.setD(m_pivotD);

      m_absoluteEncoder = m_PivotCanSparkMax.getAbsoluteEncoder(Type.kDutyCycle);
      m_PivotSparkPIDController.setFeedbackDevice(m_absoluteEncoder);

      m_absoluteEncoder.setInverted(false);
      m_absoluteEncoder.setPositionConversionFactor(Constants.kAEncoderPositionFactorDegrees);
      m_absoluteEncoder.setZeroOffset(Constants.kAEncoderZeroOffset);
      m_targetAngle = new TDNumber(this, "Encoder Values", "Target Angle", getAngle());

      m_encoderValueRotations = new TDNumber(this, "Encoder Values", "Rotations", getAngle() / Constants.kAEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees = new TDNumber(this, "Encoder Values", "Angle (degrees)", getAngle());
      m_rollerSpeedRPM = new TDNumber(this, "Encoder Values", "Measured Roller Speed RPM");

      m_currentOutput = new TDNumber(Drive.getInstance(), "Current", "Amp Roller Output", m_CanSparkMax.getOutputCurrent());
      m_pivotCurrentOutput = new TDNumber(Drive.getInstance(), "Current", "Amp Pivot Output", m_PivotCanSparkMax.getOutputCurrent());
      
      m_CanSparkMax.burnFlash();
      m_PivotCanSparkMax.burnFlash();
    }
  }

  public static AmpAddOn getInstance() {
    if (m_AmpAddOn == null) {
      m_AmpAddOn = new AmpAddOn();
    }
    return m_AmpAddOn;
  }

  public void setSpeed(double RPM, boolean backwards) {

    if (m_SparkPIDController == null) return;

    double setPoint = backwards? -RPM : RPM;
    if (m_lastSpeed != setPoint) {
      m_lastSpeed = setPoint;
      m_SparkPIDController.setReference(setPoint, ControlType.kVelocity);
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

  public void setPivotMotorPower(double speed) {
    if (m_PivotCanSparkMax != null) {
      m_PivotCanSparkMax.set(speed);
    }
  }

  public double getAngle() {
    if (m_absoluteEncoder != null) {
      return m_absoluteEncoder.getPosition();
    }
    return 45;
  }

  public void setTargetAngle(double angle) {
    double setPoint = angle % Constants.DEGREES_PER_REVOLUTION;
    setPoint  = MathUtil.clamp(setPoint,
                               Constants.kAPivotLowerLimitDegrees, 
                               Constants.kAPivotUpperLimitDegrees);
    m_targetAngle.set(setPoint);
    if (m_PivotSparkPIDController != null) {
      if (m_lastAngle != setPoint) {
        m_lastAngle = setPoint;
        m_PivotSparkPIDController.setReference(setPoint, ControlType.kPosition);
      }
    }
  }

  public void resetTargetAngle() {
    setTargetAngle(getAngle());
  }

  public double getTargetAngle() {
    return m_targetAngle.get();
  }

  public void setZeroOffset(double angle) {
    if (m_absoluteEncoder != null) {
      m_absoluteEncoder.setZeroOffset(angle);
    }
    resetTargetAngle();
  }

  public boolean pivotAtTargetAngle() {
    return MathUtil.isNear(m_targetAngle.get(), getAngle(), Constants.kAPivotToleranceDegrees);
  }

  public boolean pivotAtIntake() {
    return MathUtil.isNear(Constants.kAPivotIntakePositionDegrees, getAngle(), Constants.kAPivotToleranceDegrees);
  }

  public boolean pivotUp() {
    return MathUtil.isNear(Constants.kAPivotUpPositionDegrees, getAngle(), Constants.kAPivotToleranceDegrees);
  }

  public boolean pivotAtDeliverPosition() {
    return MathUtil.isNear(Constants.kAPivotDeliverAmpPositionDegrees, getAngle(), Constants.kAPivotToleranceDegrees);
  }


  @Override
  public void periodic() {
    if (RobotMap.A_ENABLED) {
      if (Constants.kEnableAmpAddOnPIDTuning) {
        double tmp = m_TDrollerP.get();
        if (tmp != m_rollerP) {
          m_SparkPIDController.setP(tmp);
          m_rollerP = tmp;
        }
        tmp = m_TDrollerI.get();
        if (tmp != m_rollerI) {
          m_SparkPIDController.setI(tmp);
          m_rollerI = tmp;
        }
        tmp = m_TDrollerD.get();
        if (tmp != m_rollerD) {
          m_SparkPIDController.setD(tmp);
          m_rollerD = tmp;
        }
      
        tmp = m_TDpivotP.get();
        if (tmp != m_pivotP) {
          m_PivotSparkPIDController.setP(tmp);
          m_pivotP = tmp;
        }
        tmp = m_TDpivotI.get();
        if (tmp != m_pivotI) {
          m_PivotSparkPIDController.setI(tmp);
          m_pivotI = tmp;
        }
        tmp = m_TDpivotD.get();
        if (tmp != m_pivotD) {
          m_PivotSparkPIDController.setD(tmp);
          m_pivotD = tmp;
        }
      }

      m_encoderValueRotations.set(getAngle() / Constants.kBPEncoderPositionFactorDegrees);
      m_encoderValueAngleDegrees.set(getAngle());
      m_rollerSpeedRPM.set(m_CanSparkMax.getEncoder().getVelocity());

      m_currentOutput.set(m_CanSparkMax.getOutputCurrent());
      m_pivotCurrentOutput.set(m_PivotCanSparkMax.getOutputCurrent());
    }
    super.periodic();
  }
}
