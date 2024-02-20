// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.NoteProximitySensor;

public class Barrel extends SubsystemBase {
  private static Barrel m_barrel;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;

  CANSparkMax m_CanSparkMax;
  SparkPIDController m_SparkPIDController;

  NoteProximitySensor m_NoteProximitySensor;

  /** Creates a new Barrel. */
  private Barrel() {
    super("Barrel");
    if(RobotMap.B_ENABLED){
      m_CanSparkMax = new CANSparkMax(RobotMap.B_MOTOR, MotorType.kBrushless);

      m_CanSparkMax.restoreFactoryDefaults();
      m_CanSparkMax.setInverted(true);

      m_SparkPIDController = m_CanSparkMax.getPIDController();

      m_P = new TDNumber(this, "BarrelPID", "P", Constants.kBarrelP);
      m_I = new TDNumber(this, "BarrelPID", "I", Constants.kBarrelI);
      m_D = new TDNumber(this, "BarrelPID", "D", Constants.kBarrelD);

      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.B_NOTE_SENSOR);
    }
  }

  public static Barrel getInstance() {
    if (m_barrel == null) {
      m_barrel = new Barrel();
    }
    return m_barrel;
  }

  public void setSpeed(double RPM, boolean backwards) {
    if (!backwards) {
      m_SparkPIDController.setReference(RPM, ControlType.kVelocity);
    }
    else {
      m_SparkPIDController.setReference(-RPM, ControlType.kVelocity);
    }
  }

  public void spinForward(double speed) {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(speed);
    }
  }

  public void spinBackward(double speed) {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(-speed);
    }
  }

  public void spinStop() {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(0);
    }
  }

  public boolean hasNote() {
    return m_NoteProximitySensor.hasNote();
  }

  public boolean noteCenteredOnSensor() {
    return m_NoteProximitySensor.noteIsCentered();
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (Constants.kEnableBarrelPIDTuning && 
        m_CanSparkMax != null) {
      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());
    }

    super.periodic();
  }
}
