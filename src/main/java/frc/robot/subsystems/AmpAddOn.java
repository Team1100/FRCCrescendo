// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.utils.NoteProximitySensor;

public class AmpAddOn extends SubsystemBase {
  private static AmpAddOn m_AmpAddOn;
  CANSparkMax m_CanSparkMax;
  CANSparkMax m_PivotCanSparkMax;

  NoteProximitySensor m_NoteProximitySensor;
  
  /** Creates a new AmpAddOn. */
  private AmpAddOn() {
    super("AmpAddOn");
    if(RobotMap.A_ENABLED){
      m_CanSparkMax = new CANSparkMax(RobotMap.A_MOTOR, MotorType.kBrushless);
      m_PivotCanSparkMax = new CANSparkMax(RobotMap.A_PIVOT_MOTOR, MotorType.kBrushless);

      m_CanSparkMax.restoreFactoryDefaults();
      m_PivotCanSparkMax.restoreFactoryDefaults();

      m_CanSparkMax.setInverted(false);
      m_PivotCanSparkMax.setInverted(false);

      m_NoteProximitySensor = new NoteProximitySensor(RobotMap.A_NOTE_SENSOR, this);
    }
  }

  public static AmpAddOn getInstance() {
    if (m_AmpAddOn == null) {
      m_AmpAddOn = new AmpAddOn();
    }
    return m_AmpAddOn;
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

  public boolean hasNote() {
    return m_NoteProximitySensor.hasNote();
  }

  public boolean noteCenteredOnSensor() {
    return m_NoteProximitySensor.noteIsCentered();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    super.periodic();
    m_NoteProximitySensor.update();
  }
}
