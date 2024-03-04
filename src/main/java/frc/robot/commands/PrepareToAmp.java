// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.testingdashboard.Command;
import frc.robot.commands.AmpAddOn.AmpPivotToScoringPosition;
import frc.robot.commands.BarrelPivot.AlignPivotToAmp;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;

public class PrepareToAmp extends Command {
  enum State {
    INIT,
    MOVE_TO_AMP,
    SCHEDULE_ALIGN_PIVOTS,
    WAIT_FOR_ALIGN_PIVOTS,
    READY_TO_AMP_SCORE,
    DONE
  }

  BlinkLights m_blinkLights;
  MoveNoteToAmp m_moveNoteToAmp;
  AmpPivotToScoringPosition m_ampPivotToScoringPosition;
  AlignPivotToAmp m_alignPivotToAmp;
  MoveLightsGreen m_MoveLightsGreen;

  BarrelPivot m_barrelPivot;
  SensorMonitor m_sensorMonitor;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new PrepareToAmp. */
  public PrepareToAmp() {
    super(AmpAddOn.getInstance(), "", "PrepareToAmp");

    m_state = State.INIT;
    m_isFinished = false;

    m_blinkLights = new BlinkLights();
    m_moveNoteToAmp = new MoveNoteToAmp();
    m_ampPivotToScoringPosition = new AmpPivotToScoringPosition();
    m_alignPivotToAmp = new AlignPivotToAmp();
    m_MoveLightsGreen = new MoveLightsGreen();

    m_barrelPivot = BarrelPivot.getInstance();
    m_sensorMonitor = SensorMonitor.getInstance();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_state = State.INIT;
    m_isFinished = false;

    m_blinkLights.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_state) {
      case INIT:
        switch (m_sensorMonitor.determineLocation()) {
          case c_Amp:
          case c_SensorsDisabled:
            m_state = State.SCHEDULE_ALIGN_PIVOTS;
            break;
          case c_NoNote:
            m_state = State.DONE;
          default:
            m_moveNoteToAmp.schedule();
            m_state = State.MOVE_TO_AMP;
            break;
        }
        break;

      case MOVE_TO_AMP:
        if (m_moveNoteToAmp.isFinished()) {
          m_state = State.SCHEDULE_ALIGN_PIVOTS;
        }
        break;

      case SCHEDULE_ALIGN_PIVOTS:
        m_ampPivotToScoringPosition.schedule();
        m_alignPivotToAmp.schedule();
        m_state = State.WAIT_FOR_ALIGN_PIVOTS;
        break;

      case WAIT_FOR_ALIGN_PIVOTS:
        if (m_ampPivotToScoringPosition.isFinished() && m_barrelPivot.alignedToAmp()) {
          m_blinkLights.cancel();
          m_MoveLightsGreen.schedule();
          m_state = State.READY_TO_AMP_SCORE;
        }
        break;

      case READY_TO_AMP_SCORE:
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
    if (m_blinkLights.isScheduled()) {
      m_blinkLights.cancel();
    }
    if (m_moveNoteToAmp.isScheduled()) {
      m_moveNoteToAmp.cancel();
    }
    if (m_ampPivotToScoringPosition.isScheduled()) {
      m_ampPivotToScoringPosition.cancel();
    }
    if (m_alignPivotToAmp.isScheduled()) {
      m_alignPivotToAmp.cancel();
    }
    if (m_MoveLightsGreen.isScheduled()) {
      m_MoveLightsGreen.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
