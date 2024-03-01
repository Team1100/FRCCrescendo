// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Lights.MoveLightsYellow;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.testingdashboard.Command;

public class GroundIntake extends Command {
  enum State {
    INIT,
    WAIT_FOR_BARREL_NOTE_DETECTION,
    DONE
  }

  MoveLightsYellow m_moveLightsYellow;
  Consume m_consume;
  SpinBarrelForward m_spinBarrelForward;

  SensorMonitor m_sensorMonitor;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new GroundIntake. */
  public GroundIntake() {
    super(Intake.getInstance(), "", "GroundIntake");

    m_state = State.INIT;
    m_isFinished = false;
    
    m_moveLightsYellow = new MoveLightsYellow();
    m_consume = new Consume();
    m_spinBarrelForward = new SpinBarrelForward();

    m_sensorMonitor = SensorMonitor.getInstance();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_state = State.INIT;
    m_isFinished = false;

    m_moveLightsYellow.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_state) {
      case INIT:
        switch (m_sensorMonitor.determineLocation()) {
          case c_NoNote:
          case c_SensorsDisabled:
          case c_Intake:
          case c_IntakeAndBarrel:
            m_consume.schedule();
            m_spinBarrelForward.schedule();
            m_state = State.WAIT_FOR_BARREL_NOTE_DETECTION;
            break;
          default:
            m_state = State.DONE;
            break;
        }
        break;
      
      case WAIT_FOR_BARREL_NOTE_DETECTION:
        if (m_sensorMonitor.determineLocation() == NoteLocation.c_Barrel) {
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
    if (m_moveLightsYellow.isScheduled()) {
      m_moveLightsYellow.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
