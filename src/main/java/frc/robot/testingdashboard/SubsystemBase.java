// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

import java.util.ArrayList;

public class SubsystemBase extends edu.wpi.first.wpilibj2.command.SubsystemBase {

  ArrayList<TDValue> m_values;
  TDBoolean m_enabled;
  /** Creates a new Subsystem and registers it with the TestingDashboard. */
  public SubsystemBase(String name)
  {
    m_values = new ArrayList<TDValue>();
    setName(name);
    TestingDashboard.getInstance().registerTab(name);
    // must do this after the subsystem is registered
    m_enabled = new TDBoolean(this, "TestingDashboard", "Enabled", true);
  }

  @Override
  public void periodic() {

    if (!m_enabled.get()) return;

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
