// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.testingdashboard.TestingDashboard;

public class Intake extends SubsystemBase {
  private static Intake m_intake;

  /** Creates a new Intake. */
  private Intake() {}

  public static Intake getInstance() {
    if (m_intake == null) {
      m_intake = new Intake();
      TestingDashboard.getInstance().registerSubsystem(m_intake, "Intake");
    }
    return m_intake;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  
}
