// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Barrel;

import frc.robot.Constants;
import frc.robot.subsystems.Barrel;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.testingdashboard.Command;

public class SpinBarrelForwardAutoSensorStop extends Command {
  Barrel m_barrel;
  SensorMonitor m_sensorMonitor;

  boolean m_isFinished;

  /** Creates a new SpinBarrelForwardAutoSensorStop. */
  public SpinBarrelForwardAutoSensorStop() {
    super(Barrel.getInstance(), "Basic", "SpinBarrelForwardAutoSensorStop");
    m_barrel = Barrel.getInstance();
    m_sensorMonitor = SensorMonitor.getInstance();

    m_isFinished = false;

    addRequirements(m_barrel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_isFinished = false;
    m_barrel.spinForward(Constants.BARREL_SPEED);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_sensorMonitor.barrelSeesNote()) {
      m_isFinished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_barrel.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
