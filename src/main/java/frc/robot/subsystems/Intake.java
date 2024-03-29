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

  TDNumber m_TDintakeP;
  TDNumber m_TDintakeI;
  TDNumber m_TDintakeD;
  TDNumber m_measuredSpeed;
  double   m_intakeP = Constants.kIntakeP;
  double   m_intakeI = Constants.kIntakeI;
  double   m_intakeD = Constants.kIntakeD;
  private double   m_lastSpeed = 0;
  
  CANSparkMax m_ILeftSparkMax;
  CANSparkMax m_IRightSparkMax;
  SparkPIDController m_SparkPIDController;

  TDNumber m_leftCurrentOutput;
  TDNumber m_rightCurrentOutput;

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
      
      // Motors are set opposite of each other and will spin in different directions on the robot
      m_IRightSparkMax.follow(m_ILeftSparkMax);

      m_SparkPIDController = m_ILeftSparkMax.getPIDController();

      m_TDintakeP = new TDNumber(this, "IntakePID", "P", Constants.kIntakeP);
      m_TDintakeI = new TDNumber(this, "IntakePID", "I", Constants.kIntakeI);
      m_TDintakeD = new TDNumber(this, "IntakePID", "D", Constants.kIntakeD);
      m_measuredSpeed = new TDNumber(this, "Intake Speed (RPM)", "Measured Speed");

      m_SparkPIDController.setP(m_intakeP);
      m_SparkPIDController.setI(m_intakeI);
      m_SparkPIDController.setD(m_intakeD);

      m_leftCurrentOutput = new TDNumber(Drive.getInstance(), "Current", "Intake Left Output", m_ILeftSparkMax.getOutputCurrent());
      m_rightCurrentOutput = new TDNumber(Drive.getInstance(), "Current", "Intake Right Output", m_IRightSparkMax.getOutputCurrent());
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

  @Override
  public void periodic() {
    if (Constants.kEnableIntakePIDTuning && 
        m_SparkPIDController != null) {
          double tmp = m_TDintakeP.get();
          if (tmp != m_intakeP) {
            m_SparkPIDController.setP(tmp);
            m_intakeP = tmp;
          }
          tmp = m_TDintakeI.get();
          if (tmp != m_intakeI) {
            m_SparkPIDController.setI(tmp);
            m_intakeI = tmp;
          }
          tmp = m_TDintakeD.get();
          if (tmp != m_intakeD) {
            m_SparkPIDController.setD(tmp);
            m_intakeD = tmp;
          }
    }
    if (RobotMap.I_INTAKE_ENABLED) {
      m_measuredSpeed.set(m_ILeftSparkMax.getEncoder().getVelocity());
      m_leftCurrentOutput.set(m_ILeftSparkMax.getOutputCurrent());
      m_rightCurrentOutput.set(m_IRightSparkMax.getOutputCurrent());
    }
      
    super.periodic();
  }
}
