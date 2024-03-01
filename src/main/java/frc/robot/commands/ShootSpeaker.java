// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Lights.MoveLightsPurple;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class ShootSpeaker extends Command {
  enum State {
    INIT,
    WAIT_FOR_RELEASE,
    DONE
  }

  MoveLightsPurple m_MoveLightsPurple;
  SpinBarrelForward m_spinBarrelForward;
  
  Shooter m_shooter;
  SensorMonitor m_sensorMonitor;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new ShootSpeaker. */
  public ShootSpeaker() {
    super(Shooter.getInstance(), "", "ShootSpeaker");

    m_state = State.INIT;
    m_isFinished = false;

    m_MoveLightsPurple = new MoveLightsPurple();
    m_spinBarrelForward = new SpinBarrelForward();

    m_shooter = Shooter.getInstance();
    m_sensorMonitor = SensorMonitor.getInstance();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_state = State.INIT;
    m_isFinished = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_state) {
      case INIT:
        if (m_shooter.isAtSetSpeed()) {
          m_MoveLightsPurple.schedule();
          m_spinBarrelForward.schedule();
          m_state = State.WAIT_FOR_RELEASE;
        }
        break;
        
      case WAIT_FOR_RELEASE:
        if (m_sensorMonitor.determineLocation() == NoteLocation.c_NoNote) {
          m_state = State.DONE;
        }
        break;

      case DONE:
        m_isFinished = true;
        break;
      
      default:
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_spinBarrelForward.isScheduled()) {
      m_spinBarrelForward.cancel();
    }
    if (m_MoveLightsPurple.isScheduled()) {
      m_MoveLightsPurple.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
