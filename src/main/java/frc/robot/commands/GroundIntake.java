// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Intake.Consume;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.Barrel;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class GroundIntake extends Command {
  enum State {
    INIT,
    CHECK_NOTE_LOCATION,
    SPIN_SUBSYSTEMS_IN,
    WAIT_FOR_BARREL_NOTE_DETECTION,
    DONE
  }

  Consume m_consume;
  SpinBarrelForward m_spinBarrelForward;

  Intake m_intake;
  Barrel m_barrel;
  Shooter m_shooter;
  AmpAddOn m_ampAddOn;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new GroundIntake. */
  public GroundIntake() {
    super(Intake.getInstance(), "", "GroundIntake");

    m_state = State.INIT;
    m_isFinished = false;
    
    m_consume = new Consume();
    m_spinBarrelForward = new SpinBarrelForward();

    m_intake = Intake.getInstance();
    m_barrel = Barrel.getInstance();
    m_shooter = Shooter.getInstance();
    m_ampAddOn = AmpAddOn.getInstance();
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
        m_state = State.CHECK_NOTE_LOCATION;
        break;

      case CHECK_NOTE_LOCATION:
        if (!m_intake.hasNote() && !m_barrel.hasNote() && !m_shooter.hasNote() && !m_ampAddOn.hasNote()) {
          m_state = State.SPIN_SUBSYSTEMS_IN;
        }
        else if (m_intake.hasNote()) {
          m_state = State.SPIN_SUBSYSTEMS_IN;
        }
        else {
          m_state = State.DONE;
        }
        break;

      case SPIN_SUBSYSTEMS_IN:
        m_consume.schedule();
        m_spinBarrelForward.schedule();
        m_state = State.WAIT_FOR_BARREL_NOTE_DETECTION;
        break;
      
      case WAIT_FOR_BARREL_NOTE_DETECTION:
        if (m_barrel.hasNote() && !m_intake.hasNote()) {
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
    if (m_consume.isScheduled()) {
      m_consume.cancel();
    }
    if (m_spinBarrelForward.isScheduled()) {
      m_spinBarrelForward.cancel();
    }

    m_isFinished = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
