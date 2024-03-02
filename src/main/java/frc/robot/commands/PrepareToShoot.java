// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.commands.AmpAddOn.AmpPivotUp;
import frc.robot.commands.BarrelPivot.PivotToSpeaker;
import frc.robot.commands.Drive.TargetDrive;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.commands.Shooter.SpinUpShooter;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.testingdashboard.Command;
import frc.robot.utils.FieldUtils;

public class PrepareToShoot extends Command {
  enum State {
    INIT,
    MOVE_TO_BARREL,
    PREPARING_TO_SHOOT,
    WAIT_FOR_PREPARING_TO_SHOOT,
    READY_TO_SHOOT,
    DONE
  }

  OI m_oi;
  
  // TODO: implement commands
  BlinkLights m_blinkLights;
  MoveNoteToBarrel m_moveNoteToBarrel;
  AmpPivotUp m_ampPivotUp;
  SpinUpShooter m_spinUpShooter;
  PivotToSpeaker m_pivotToSpeaker;
  TargetDrive m_trackSpeaker;
  MoveLightsGreen m_moveLightsGreen;

  Shooter m_shooter;
  SensorMonitor m_sensorMonitor;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new PrepareToShoot. */
  public PrepareToShoot() {
    super(Shooter.getInstance(), "", "PrepareToShoot");

    m_oi = OI.getInstance();

    m_state = State.INIT;
    m_isFinished = false;

    m_blinkLights = new BlinkLights();
    m_moveNoteToBarrel = new MoveNoteToBarrel();
    m_ampPivotUp = new AmpPivotUp();
    m_spinUpShooter = new SpinUpShooter();
    m_pivotToSpeaker = new PivotToSpeaker();
    m_trackSpeaker = new TargetDrive(()->{
        return FieldUtils.getInstance().getSpeakerPose().toPose2d();
      }, m_oi.getDriveInputs());
    m_moveLightsGreen = new MoveLightsGreen();

    m_shooter = Shooter.getInstance();
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
          case c_Barrel:
          case c_SensorsDisabled:
            m_state = State.PREPARING_TO_SHOOT;
            break;
          case c_NoNote:
            m_state = State.DONE;
          default:
            m_moveNoteToBarrel.schedule();
            m_state = State.MOVE_TO_BARREL;
            break;
        }
        break;

      case MOVE_TO_BARREL:
        if (m_moveNoteToBarrel.isFinished()) {
          m_state = State.PREPARING_TO_SHOOT;
        }
        break;

      case PREPARING_TO_SHOOT:
        m_ampPivotUp.schedule();
        m_spinUpShooter.schedule();
        m_pivotToSpeaker.schedule();
        m_trackSpeaker.schedule();
        break;

      case WAIT_FOR_PREPARING_TO_SHOOT:
        if (m_ampPivotUp.isFinished() && m_shooter.isAtSetSpeed()) { // && tracking speaker && BP aligned
          m_moveLightsGreen.schedule();
          m_state = State.READY_TO_SHOOT;
        }
        break;

      case READY_TO_SHOOT:
        if (m_sensorMonitor.determineLocation() == NoteLocation.c_NoNote) {
          m_state = State.DONE;
        }
        // TODO: 
        // if ((speaker not tracked || BP not aligned) && red not scheduled)
        //   schedule red lights
        // else if (green not scheduled)
        //   schedule green lights
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
    if (m_spinUpShooter.isScheduled()) {
      m_spinUpShooter.cancel();
    }
    if (m_blinkLights.isScheduled()) {
      m_blinkLights.cancel();
    }
    if (m_moveNoteToBarrel.isScheduled()) {
      m_moveNoteToBarrel.cancel();
    }
    if (m_ampPivotUp.isScheduled()) {
      m_ampPivotUp.cancel();
    }
    // Cancel all scheduled commands
    if (m_pivotToSpeaker.isScheduled()) {
      m_pivotToSpeaker.cancel();
    }
    if (m_trackSpeaker.isScheduled()) {
      m_trackSpeaker.cancel();
    }
    if (m_moveLightsGreen.isScheduled()) {
      m_moveLightsGreen.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
