// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
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

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;
  
  CANSparkMax m_SLeftSparkMax;
  CANSparkMax m_SRightSparkMax;
  SparkPIDController m_LeftSparkPIDController;
  SparkPIDController m_RightSparkPIDController;

  NoteProximitySensor m_NoteProximitySensor;

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

      m_P = new TDNumber(this, "ShooterPID", "P", Constants.kShooterP);
      m_I = new TDNumber(this, "ShooterPID", "I", Constants.kShooterI);
      m_D = new TDNumber(this, "ShooterPID", "D", Constants.kShooterD);

      m_LeftSparkPIDController.setP(m_P.get());
      m_LeftSparkPIDController.setI(m_I.get());
      m_LeftSparkPIDController.setD(m_D.get());

      m_RightSparkPIDController.setP(m_P.get());
      m_RightSparkPIDController.setI(m_I.get());
      m_RightSparkPIDController.setD(m_D.get());

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.S_NOTE_SENSOR);
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
      m_LeftSparkPIDController.setReference(LeftRPM, ControlType.kVelocity);
      m_RightSparkPIDController.setReference(RightRPM, ControlType.kVelocity);
    }
    else {
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

  public boolean hasNote() {
    return m_NoteProximitySensor.hasNote();
  }

  @Override
  public void periodic() {
    if (Constants.kEnableShooterPIDTuning && 
        m_LeftSparkPIDController != null &&
        m_RightSparkPIDController != null) {
      m_LeftSparkPIDController.setP(m_P.get());
      m_LeftSparkPIDController.setI(m_I.get());
      m_LeftSparkPIDController.setD(m_D.get());

      m_RightSparkPIDController.setP(m_P.get());
      m_RightSparkPIDController.setI(m_I.get());
      m_RightSparkPIDController.setD(m_D.get());
    }

    super.periodic();
  }
}
