// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.TestingDashboard;

public class Intake extends SubsystemBase {
  private static Intake m_intake;
    CANSparkMax m_ILeftSparkMax ;
    CANSparkMax m_IRightSparkMax ;
  /** Creates a new Intake. */
  private Intake() {
    m_ILeftSparkMax = new CANSparkMax(RobotMap.I_MOTOR_LEFT, MotorType.kBrushless);
    m_IRightSparkMax = new CANSparkMax(RobotMap.I_MOTOR_RIGHT, MotorType.kBrushless);
    m_IRightSparkMax.follow(m_ILeftSparkMax);
  }

  public static Intake getInstance() {
    if (m_intake == null) {
      m_intake = new Intake();
      TestingDashboard.getInstance().registerSubsystem(m_intake, "Intake");
    }
    return m_intake;
  }

  public void spinIn() {
    m_ILeftSparkMax.set(-RobotMap.I_INTAKE_SPEED);
  }

  public void spinOut() {
    m_ILeftSparkMax.set(RobotMap.I_INTAKE_SPEED);
  }

  public void spinStop() {
    m_ILeftSparkMax.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  
}
