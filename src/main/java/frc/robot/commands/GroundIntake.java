// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.commands.Lights.MoveLightsYellow;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.testingdashboard.Command;

public class GroundIntake extends Command {
  enum State {
    INIT,
    WAIT_FOR_BARREL_NOTE_DETECTION,
    NOTE_DETECTED,
    DONE
  }

  MoveLightsYellow m_moveLightsYellow;
  Consume m_consume;
  SpinBarrelForward m_spinBarrelForward;
  MoveLightsGreen m_moveLightsGreen;
  WaitCommand m_wait;

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

    m_moveLightsGreen = new MoveLightsGreen();
    m_wait = new WaitCommand(1.5);

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
        m_consume.schedule();
        m_spinBarrelForward.schedule();
        m_state = State.WAIT_FOR_BARREL_NOTE_DETECTION;
        break;
      
      case WAIT_FOR_BARREL_NOTE_DETECTION:
        if (m_sensorMonitor.barrelSeesNote()) {
          m_state = State.NOTE_DETECTED;
        }
        break;

      case NOTE_DETECTED:
        m_spinBarrelForward.cancel();
        m_consume.cancel();
        m_moveLightsGreen.schedule();
        m_wait.schedule();
        m_state = State.DONE;

      case DONE:
        if(m_wait.isFinished()) {
          m_isFinished = true;
        }
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
    if(m_moveLightsGreen.isScheduled()) {
      m_moveLightsGreen.cancel();
    }
    if(m_wait.isScheduled()) {
      m_wait.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
