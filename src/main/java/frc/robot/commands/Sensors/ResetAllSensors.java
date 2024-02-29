// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Sensors;

import frc.robot.testingdashboard.InstantCommand;
import frc.robot.subsystems.SensorMonitor;


public class ResetAllSensors extends InstantCommand {
  public ResetAllSensors() {
    super(SensorMonitor.getInstance()::resetAllSensors, SensorMonitor.getInstance(), "", "Reset All Sensors");
  }

  public boolean runsWhenDisabled() {
    return true;
  }
}
