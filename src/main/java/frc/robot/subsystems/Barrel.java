// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.NoteProximitySensor;

public class Barrel extends SubsystemBase {
  private static Barrel m_barrel;

  TDNumber m_TDrollerP;
  TDNumber m_TDrollerI;
  TDNumber m_TDrollerD;
  double m_rollerP = Constants.kBarrelP;
  double m_rollerI = Constants.kBarrelI;
  double m_rollerD = Constants.kBarrelD;

  TDNumber m_barrelSpeedRPM;
  private double m_lastSpeed = 0;

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
      m_CanSparkMax.setIdleMode(IdleMode.kBrake);

      m_SparkPIDController = m_CanSparkMax.getPIDController();

      m_TDrollerP = new TDNumber(this, "BarrelPID", "P", Constants.kBarrelP);
      m_TDrollerI = new TDNumber(this, "BarrelPID", "I", Constants.kBarrelI);
      m_TDrollerD = new TDNumber(this, "BarrelPID", "D", Constants.kBarrelD);

      m_barrelSpeedRPM = new TDNumber(this, "", "Measured Barrel Speed RPM");

      m_SparkPIDController.setP(m_rollerP);
      m_SparkPIDController.setI(m_rollerI);
      m_SparkPIDController.setD(m_rollerD);

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.B_NOTE_SENSOR, this);
    }
  }

  public static Barrel getInstance() {
    if (m_barrel == null) {
      m_barrel = new Barrel();
    }
    return m_barrel;
  }

  public void setSpeed(double RPM, boolean backwards) {
    double setPoint = backwards? -RPM : RPM;
    if (m_lastSpeed != setPoint) {
      m_lastSpeed = setPoint;
      m_SparkPIDController.setReference(setPoint, ControlType.kVelocity);
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

  public void setCoastMode() {
    m_CanSparkMax.setIdleMode(IdleMode.kCoast);
  }

  public void setBreakMode() {
    m_CanSparkMax.setIdleMode(IdleMode.kBrake);
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
    // This method will be called once per scheduler run
    if (Constants.kEnableBarrelPIDTuning && 
        m_CanSparkMax != null) {

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
    }

    m_barrelSpeedRPM.set(m_CanSparkMax.getEncoder().getVelocity());

    super.periodic();
    if(m_NoteProximitySensor != null){
      m_NoteProximitySensor.update();
    }
  }
}
