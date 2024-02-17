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

public class Elevator extends SubsystemBase {
  private static Elevator m_Elevator;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;

  CANSparkMax m_LeftCanSparkMax;
  CANSparkMax m_RightCanSparkMax;
  SparkPIDController m_SparkPIDController;

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

      m_SparkPIDController = m_LeftCanSparkMax.getPIDController();

      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());

    }
  }

  public static Elevator getInstance() {
    if (m_Elevator == null) {
      m_Elevator = new Elevator();
    }
    return m_Elevator;
  }

  public void setSpeed(double RPM, boolean backwards) {
    if (!backwards) {
      m_SparkPIDController.setReference(RPM, ControlType.kVelocity);
    }
    else {
      m_SparkPIDController.setReference(-RPM, ControlType.kVelocity);
    }
  }

  public void climb(double speed) {
    if (m_LeftCanSparkMax != null) {
      m_LeftCanSparkMax.set(speed);
    }
  }

  public void unClimb(double speed) {
    if (m_LeftCanSparkMax != null) {
      m_LeftCanSparkMax.set(-speed);
    }
  }

  public void doNotMove() {
    if (m_LeftCanSparkMax != null) {
    m_LeftCanSparkMax.set(0);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (Constants.kEnableElevatorPivotPIDTuning && m_LeftCanSparkMax != null) {
      m_SparkPIDController.setP(m_P.get());
      m_SparkPIDController.setI(m_I.get());
      m_SparkPIDController.setD(m_D.get());
    }
    
    super.periodic();
  }
}
