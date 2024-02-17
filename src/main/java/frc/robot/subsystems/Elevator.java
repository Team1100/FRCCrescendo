// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;;

public class Elevator extends SubsystemBase {
  private static Elevator m_Elevator;
  CANSparkMax m_LeftCanSparkMax;
  CANSparkMax m_RightCanSparkMax;

  /** Creates a new Elevator. */
  public Elevator() {
    super("Elevator");
    if(RobotMap.E_ELEVATOR_ENABLED) {
      m_LeftCanSparkMax = new CANSparkMax(RobotMap.E_MOTOR_LEFT, MotorType.kBrushless);
      m_RightCanSparkMax = new CANSparkMax(RobotMap.E_MOTOR_RIGHT, MotorType.kBrushless);

      m_LeftCanSparkMax.restoreFactoryDefaults();
      m_RightCanSparkMax.restoreFactoryDefaults();

      m_LeftCanSparkMax.setInverted(false);
      m_RightCanSparkMax.follow(m_LeftCanSparkMax, true);
    }
  }

  public static Elevator getInstance() {
    if (m_Elevator == null) {
      m_Elevator = new Elevator();
    }
    return m_Elevator;
  }

  public void climb(double speed) {
    m_LeftCanSparkMax.set(speed);
    m_RightCanSparkMax.set(speed);
  }

  public void unClimb(double speed) {
    m_LeftCanSparkMax.set(-speed);
    m_RightCanSparkMax.set(-speed);
  }

  public void doNotMove() {
    m_LeftCanSparkMax.set(0);
    m_RightCanSparkMax.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    super.periodic();
  }
}
