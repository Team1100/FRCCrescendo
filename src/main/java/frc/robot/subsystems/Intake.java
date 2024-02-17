// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;

import frc.robot.RobotMap;
import frc.robot.Constants;

import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.NoteProximitySensor;

public class Intake extends SubsystemBase {
  private static Intake m_intake;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;
  
  CANSparkMax m_ILeftSparkMax;
  CANSparkMax m_IRightSparkMax;
  SparkPIDController m_SparkPIDController;

  NoteProximitySensor m_NoteProximitySensor;

  /** Creates a new Intake. */
  private Intake() {
    super("Intake");
  
    if (RobotMap.I_INTAKE_ENABLED)
    {
      m_ILeftSparkMax = new CANSparkMax(RobotMap.I_MOTOR_LEFT, MotorType.kBrushless);
      m_IRightSparkMax = new CANSparkMax(RobotMap.I_MOTOR_RIGHT, MotorType.kBrushless);

      m_ILeftSparkMax.restoreFactoryDefaults();
      m_IRightSparkMax.restoreFactoryDefaults();

      m_ILeftSparkMax.setInverted(true);
      
      // Motors are set opposite of each other and will spin in different directions on the robot
      m_IRightSparkMax.follow(m_ILeftSparkMax);

      m_SparkPIDController = m_ILeftSparkMax.getPIDController();

      m_P = new TDNumber(this, "IntakePID", "P", Constants.kIntakeP);
      m_I = new TDNumber(this, "IntakePID", "I", Constants.kIntakeI);
      m_D = new TDNumber(this, "IntakePID", "D", Constants.kIntakeD);

      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.I_NOTE_SENSOR);
    }
  }

  public static Intake getInstance() {
    if (m_intake == null) {
      m_intake = new Intake();
    }
    return m_intake;
  }

  public void setSpeeds(double RPM, boolean backwards) {
    if (!backwards) {
      m_SparkPIDController.setReference(RPM, ControlType.kVelocity);
    }
    else {
      m_SparkPIDController.setReference(-RPM, ControlType.kVelocity);
    }
  }

  public void spinIn(double speed) {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(speed);
  }

  public void spinOut(double speed) {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(-speed);
  }

  public void spinStop() {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(0);
  }

  public boolean hasNote() {
    return m_NoteProximitySensor.hasNote();
  }

  @Override
  public void periodic() {
    if (Constants.kEnableIntakePIDTuning && 
        m_SparkPIDController != null) {
      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());
    }
    
    super.periodic();
  }
}
