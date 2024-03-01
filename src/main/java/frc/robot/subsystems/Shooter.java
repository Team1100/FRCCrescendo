// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.SparkPIDController;

import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.NoteProximitySensor;

public class Shooter extends SubsystemBase {
  private static Shooter m_shooter;

  TDNumber m_TDshootP;
  TDNumber m_TDshootI;
  TDNumber m_TDshootD;
  double   m_shootP = Constants.kShooterP;
  double   m_shootI = Constants.kShooterI;
  double   m_shootD = Constants.kShooterD;

  TDNumber m_rightMeasuredSpeed;
  TDNumber m_leftMeasuredSpeed;
  
  CANSparkMax m_SLeftSparkMax;
  CANSparkMax m_SRightSparkMax;
  SparkPIDController m_LeftSparkPIDController;
  SparkPIDController m_RightSparkPIDController;

  NoteProximitySensor m_NoteProximitySensor;

  private double m_leftSpeedSetpoint;
  private double m_rightSpeedSetpoint;

  /** Creates a new Intake. */
  private Shooter() {
    super("Shooter");

    if (RobotMap.S_SHOOTER_ENABLED) {
      m_SLeftSparkMax = new CANSparkMax(RobotMap.S_MOTOR_LEFT, MotorType.kBrushless);
      m_SRightSparkMax = new CANSparkMax(RobotMap.S_MOTOR_RIGHT, MotorType.kBrushless);

      m_SLeftSparkMax.restoreFactoryDefaults();
      m_SRightSparkMax.restoreFactoryDefaults();
      
      m_SLeftSparkMax.setInverted(false);
      m_SRightSparkMax.setInverted(true);

      m_LeftSparkPIDController = m_SLeftSparkMax.getPIDController();
      m_RightSparkPIDController = m_SRightSparkMax.getPIDController();

      m_leftSpeedSetpoint = Constants.LEFT_SHOOTER_SPEED_RPM;
      m_rightSpeedSetpoint = Constants.RIGHT_SHOOTER_SPEED;

      m_TDshootP = new TDNumber(this, "ShooterPID", "P", Constants.kShooterP);
      m_TDshootI = new TDNumber(this, "ShooterPID", "I", Constants.kShooterI);
      m_TDshootD = new TDNumber(this, "ShooterPID", "D", Constants.kShooterD);

      m_leftMeasuredSpeed = new TDNumber(this, "Shooter Speed (RPM)", "Measured Speed Left");
      m_rightMeasuredSpeed = new TDNumber(this, "Shooter Speed (RPM)", "Measured Speed Right");

      m_LeftSparkPIDController.setP(m_shootP);
      m_LeftSparkPIDController.setI(m_shootI);
      m_LeftSparkPIDController.setD(m_shootD);

      m_RightSparkPIDController.setP(m_shootP);
      m_RightSparkPIDController.setI(m_shootI);
      m_RightSparkPIDController.setD(m_shootD);

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.S_NOTE_SENSOR, this);
    }
  }

  public static Shooter getInstance() {
    if (m_shooter == null) {
      m_shooter = new Shooter();
    }
    return m_shooter;
  }

  public void setSpeeds(double LeftRPM, double RightRPM, boolean backwards) {
    if (!backwards) {
      m_leftSpeedSetpoint = LeftRPM;
      m_rightSpeedSetpoint = RightRPM;
      m_LeftSparkPIDController.setReference(LeftRPM, ControlType.kVelocity);
      m_RightSparkPIDController.setReference(RightRPM, ControlType.kVelocity);
    }
    else {
      m_leftSpeedSetpoint = -LeftRPM;
      m_rightSpeedSetpoint = -RightRPM;
      m_LeftSparkPIDController.setReference(-LeftRPM, ControlType.kVelocity);
      m_RightSparkPIDController.setReference(-RightRPM, ControlType.kVelocity);
    }
  }

  public void spinOut(double leftSpeed, double rightSpeed) {
    if (m_SLeftSparkMax != null && m_SRightSparkMax != null) {
      m_SLeftSparkMax.set(leftSpeed);
      m_SRightSparkMax.set(rightSpeed);
    }
  }

  public void spinIn(double speed) {
    if (m_SLeftSparkMax != null && m_SRightSparkMax != null) {
      m_SLeftSparkMax.set(-speed);
      m_SRightSparkMax.set(-speed);
    }
  }

  public void spinStop() {
    if (m_SLeftSparkMax != null && m_SRightSparkMax != null) {
      m_SLeftSparkMax.set(0);
      m_SRightSparkMax.set(0);
    }
  }

  public boolean isAtSetSpeed() {
    if (m_SLeftSparkMax != null && m_SRightSparkMax != null) {
      return 
        MathUtil.isNear(m_leftSpeedSetpoint, m_SLeftSparkMax.getEncoder().getVelocity(), Constants.SHOOTER_SPEED_TOLERANCE) &&
        MathUtil.isNear(m_rightSpeedSetpoint, m_SRightSparkMax.getEncoder().getVelocity(), Constants.SHOOTER_SPEED_TOLERANCE);
    } else {
      return true;
    }
  }

  public boolean hasNote() {
    if(m_NoteProximitySensor != null) {
      return m_NoteProximitySensor.hasNote();
    } else {
      return false;
    }
  }

  public boolean noteCenteredOnSensor() {
    if(m_NoteProximitySensor != null) {
      return m_NoteProximitySensor.noteIsCentered();
    } else {
      return false;
    }
  }

  public void resetSensor() {
    if(m_NoteProximitySensor != null) {
      m_NoteProximitySensor.reset();
    }
  }

  @Override
  public void periodic() {
    if (Constants.kEnableShooterPIDTuning && 
        m_LeftSparkPIDController != null &&
        m_RightSparkPIDController != null) {

      double tmp = m_TDshootP.get();
      if (tmp != m_shootP) {
        m_LeftSparkPIDController.setP(tmp);
        m_RightSparkPIDController.setP(tmp);
        m_shootP = tmp;
      }
      tmp = m_TDshootI.get() / 1000;
      if (tmp != m_shootI) {
        m_LeftSparkPIDController.setI(tmp);
        m_RightSparkPIDController.setI(tmp);
        m_shootI = tmp;
      }
      tmp = m_TDshootD.get();
      if (tmp != m_shootD) {
        m_LeftSparkPIDController.setD(tmp);
        m_RightSparkPIDController.setD(tmp);
        m_shootD = tmp;
      }      
    }

    m_leftMeasuredSpeed.set(m_SLeftSparkMax.getEncoder().getVelocity());
    m_rightMeasuredSpeed.set(m_SRightSparkMax.getEncoder().getVelocity());

    super.periodic();
    if(m_NoteProximitySensor != null) {
      m_NoteProximitySensor.update();
    }
  }
}
