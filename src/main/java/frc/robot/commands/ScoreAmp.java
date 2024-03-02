// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.AmpAddOn.SpinUpAmpRollers;
import frc.robot.commands.Lights.MoveLightsPurple;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.testingdashboard.Command;

public class ScoreAmp extends Command {
  MoveLightsPurple m_MoveLightsPurple;
  SpinUpAmpRollers m_SpinUpAmpRollers;
  
  SensorMonitor m_sensorMonitor;
  
  /** Creates a new ScoreAmp. */
  public ScoreAmp() {
    super (AmpAddOn.getInstance(), "", "ScoreAmp");

    m_MoveLightsPurple = new MoveLightsPurple();
    m_SpinUpAmpRollers = new SpinUpAmpRollers();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_MoveLightsPurple.schedule();
    m_SpinUpAmpRollers.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_MoveLightsPurple.isScheduled()) {
      m_MoveLightsPurple.cancel();
    }
    if (m_SpinUpAmpRollers.isScheduled()) {
      m_SpinUpAmpRollers.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_sensorMonitor.determineLocation() == NoteLocation.c_NoNote;
  }
}
