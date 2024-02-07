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

public class Intake extends SubsystemBase {
  private static Intake m_intake;

  TDNumber m_P;
  TDNumber m_I;
  TDNumber m_D;
  
  CANSparkMax m_ILeftSparkMax;
  CANSparkMax m_IRightSparkMax;
  SparkPIDController m_LeftSparkPIDController;
  SparkPIDController m_RightSparkPIDController;

  /** Creates a new Intake. */
  private Intake() {
    super("Intake");
  
    if (RobotMap.I_INTAKE_ENABLED)
    {
      m_ILeftSparkMax = new CANSparkMax(RobotMap.I_MOTOR_LEFT, MotorType.kBrushless);
      m_IRightSparkMax = new CANSparkMax(RobotMap.I_MOTOR_RIGHT, MotorType.kBrushless);

      m_ILeftSparkMax.restoreFactoryDefaults();
      m_IRightSparkMax.restoreFactoryDefaults();

      m_ILeftSparkMax.setInverted(false);
      m_IRightSparkMax.setInverted(false);
      
      // Motors are set opposite of each other and will spin in different directions on the robot
      m_IRightSparkMax.follow(m_ILeftSparkMax);

      m_LeftSparkPIDController = m_ILeftSparkMax.getPIDController();
      m_RightSparkPIDController = m_IRightSparkMax.getPIDController();

      m_P = new TDNumber(this, "IntakePID", "P", Constants.kIntakeP);
      m_I = new TDNumber(this, "IntakePID", "I", Constants.kIntakeI);
      m_D = new TDNumber(this, "IntakePID", "D", Constants.kIntakeD);

      m_LeftSparkPIDController.setP(m_P.get());
      m_LeftSparkPIDController.setI(m_I.get());
      m_LeftSparkPIDController.setD(m_D.get());

      m_RightSparkPIDController.setP(m_P.get());
      m_RightSparkPIDController.setI(m_I.get());
      m_RightSparkPIDController.setD(m_D.get());
    }
  }

  public static Intake getInstance() {
    if (m_intake == null) {
      m_intake = new Intake();
    }
    return m_intake;
  }

  public void setSpeeds(double RPM) {
    m_LeftSparkPIDController.setReference(RPM, ControlType.kVelocity);
    m_RightSparkPIDController.setReference(RPM, ControlType.kVelocity);
  }

  public void spinIn(double speed) {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(speed);
  }

  public void spinOut(double speed) {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(speed);
  }

  public void spinStop() {
    if (m_ILeftSparkMax != null)
      m_ILeftSparkMax.set(0);
  }

  @Override
  public void periodic() {
    if (Constants.kEnableIntakePID && 
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
