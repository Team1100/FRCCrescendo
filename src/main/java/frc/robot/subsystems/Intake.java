// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import frc.robot.RobotMap;
import frc.robot.Constants;

import frc.robot.testingdashboard.SubsystemBase;

public class Intake extends SubsystemBase {
  private static Intake m_intake;
    CANSparkMax m_ILeftSparkMax;
    CANSparkMax m_IRightSparkMax;
  /** Creates a new Intake. */
  private Intake() {
    super("Intake");
  
    if (RobotMap.I_INTAKE_ENABLED)
    {
      m_ILeftSparkMax = new CANSparkMax(RobotMap.I_MOTOR_LEFT, MotorType.kBrushless);
      m_IRightSparkMax = new CANSparkMax(RobotMap.I_MOTOR_RIGHT, MotorType.kBrushless);
      // Motors are set opposite of each other and will spin in different directions on the robot
      m_IRightSparkMax.follow(m_ILeftSparkMax);
    }
  }

  public static Intake getInstance() {
    if (m_intake == null) {
      m_intake = new Intake();
    }
    return m_intake;
  }

  public void spinIn() {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(Constants.kIntakeSpeed);
  }

  public void spinOut() {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(-Constants.kIntakeSpeed);
  }

  public void spinStop() {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(0);
  }

  @Override
  public void periodic() {
    super.periodic();
  }
}
