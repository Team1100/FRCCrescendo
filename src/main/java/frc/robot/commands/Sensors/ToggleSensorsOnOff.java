// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Sensors;

import frc.robot.subsystems.SensorMonitor;
import frc.robot.testingdashboard.Command;

public class ToggleSensorsOnOff extends Command {
  SensorMonitor m_sensorMonitor;

  /** Creates a new ToggleSensorsOnOff. */
  public ToggleSensorsOnOff() {
    super(SensorMonitor.getInstance(), "Toggle Sensors", "ToggleSensorsOnOff");
    m_sensorMonitor = SensorMonitor.getInstance();
    addRequirements(m_sensorMonitor);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_sensorMonitor.toggleSensorsOnOff();
    m_sensorMonitor.resetAllSensors();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
