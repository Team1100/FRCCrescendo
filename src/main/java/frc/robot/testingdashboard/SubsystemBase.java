// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

import java.util.ArrayList;

public class SubsystemBase extends edu.wpi.first.wpilibj2.command.SubsystemBase {

  ArrayList<TDValue> m_values;
  /** Creates a new Subsystem and registers it with the TestingDashboard. */
  public SubsystemBase(String name)
  {
    m_values = new ArrayList<TDValue>();
    setName(name);
    TestingDashboard.getInstance().registerTab(name);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    for(TDValue value : m_values)
    {
      value.post();
    }
  }

  public void registerValue(TDValue value)
  {
    m_values.add(value);
  }
}
