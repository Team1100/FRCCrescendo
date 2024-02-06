// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;

public class Barrel extends SubsystemBase {
  private static Barrel m_barrel;

  CANSparkMax m_CanSparkMax;

  /** Creates a new Barrel. */
  public Barrel() {
    super("Barrel");
    if(RobotMap.B_ENABLED){
      m_CanSparkMax = new CANSparkMax(RobotMap.B_MOTOR, MotorType.kBrushless);

      m_CanSparkMax.restoreFactoryDefaults();
      m_CanSparkMax.setInverted(false);
    }
  }

  public static Barrel getInstance() {
    if (m_barrel == null) {
      m_barrel = new Barrel();
    }
    return m_barrel;
  }

  public void spinForward() {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(Constants.BARREL_SPEED);
    }
  }

  public void spinStop() {
    if (m_CanSparkMax != null) {
      m_CanSparkMax.set(0);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
