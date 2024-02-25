// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.subsystems.Barrel;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class ShootSpeaker extends Command {
  enum State {
    INIT,
    SCHEDULE_SPIN_BARREL_FORWARD,
    WAIT_FOR_SHOOTER_NOTE_DETECTION,
    SPIN_BARREL_FORWARD_UNTIL_RELEASE,
    DONE
  }

  SpinBarrelForward m_spinBarrelForward;
  
  Shooter m_shooter;
  Barrel m_barrel;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new ShootSpeaker. */
  public ShootSpeaker() {
    super(Shooter.getInstance(), "", "ShootSpeaker");

    m_state = State.INIT;
    m_isFinished = false;

    m_shooter = Shooter.getInstance();
    m_barrel = Barrel.getInstance();
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
          m_state = State.SCHEDULE_SPIN_BARREL_FORWARD;
        }
        else if (!m_barrel.hasNote()) {
          m_state = State.DONE;
        }
        break;

      case SCHEDULE_SPIN_BARREL_FORWARD:
        m_spinBarrelForward.schedule();
        m_state = State.WAIT_FOR_SHOOTER_NOTE_DETECTION;
        break;

      case WAIT_FOR_SHOOTER_NOTE_DETECTION:
        if (m_shooter.hasNote()) {
          m_state = State.SPIN_BARREL_FORWARD_UNTIL_RELEASE;
        }
        break;

      case SPIN_BARREL_FORWARD_UNTIL_RELEASE:
        if (!m_shooter.hasNote()) {
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
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
