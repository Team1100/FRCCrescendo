// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AmpAddOn.AmpPivotUp;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.commands.Drive.TargetDrive;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.utils.FieldUtils;

public class PrepareToFerry extends Command {
  enum State {
    INIT,
    MOVE_TO_BARREL,
    PREPARING_TO_SHOOT,
    WAIT_FOR_PREPARING_TO_SHOOT,
    READY_TO_SHOOT,
    DONE
  }

  OI m_oi;
  
  BlinkLights m_blinkLights;
  MoveNoteToBarrel m_moveNoteToBarrel;
  AmpPivotUp m_ampPivotUp;
  TargetDrive m_trackAmp;
  MoveLightsGreen m_moveLightsGreen;

  Shooter m_shooter;
  SensorMonitor m_sensorMonitor;
  BarrelPivot m_barrelPivot;

  TDNumber m_ferryAngle;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new PrepareToFerry. */
  public PrepareToFerry() {
    super(Shooter.getInstance(), "", "PrepareToFerry");

    m_oi = OI.getInstance();

    m_state = State.INIT;
    m_isFinished = false;

    m_blinkLights = new BlinkLights();
    m_moveNoteToBarrel = new MoveNoteToBarrel();
    m_ampPivotUp = new AmpPivotUp();
    m_trackAmp = new TargetDrive(()->{
        return FieldUtils.getInstance().getAmpPose().toPose2d();
      }, m_oi.getDriveInputs());
    m_moveLightsGreen = new MoveLightsGreen();

    m_shooter = Shooter.getInstance();
    m_sensorMonitor = SensorMonitor.getInstance();
    m_barrelPivot = BarrelPivot.getInstance();

    m_ferryAngle = new TDNumber(m_barrelPivot, "Setpoints", "FerryPosition (degrees)", Constants.BP_FERRY_ANGLE_DEGREES);
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
        m_shooter.setSpeeds(3500, 2500, false);
        m_barrelPivot.setTargetAngle(m_ferryAngle.get());
        m_trackAmp.schedule();
        m_state = State.WAIT_FOR_PREPARING_TO_SHOOT;
        break;

      case WAIT_FOR_PREPARING_TO_SHOOT:
        if (m_ampPivotUp.isFinished() && m_shooter.isAtSetSpeed() && m_barrelPivot.atGoal() && m_trackAmp.atGoal()) {
          m_blinkLights.cancel();
          m_moveLightsGreen.schedule();
          m_state = State.READY_TO_SHOOT;
        }
        break;

      case READY_TO_SHOOT:
        if (m_sensorMonitor.determineLocation() == NoteLocation.c_NoNote) {
          m_state = State.DONE;
        }
        if ((!m_barrelPivot.atGoal() || !m_trackAmp.atGoal()) && m_moveLightsGreen.isScheduled()) {
          m_moveLightsGreen.cancel();
          m_blinkLights.schedule();
        }
        else if (m_barrelPivot.atGoal() && m_trackAmp.atGoal() && m_blinkLights.isScheduled()) {
          m_blinkLights.cancel();
          m_moveLightsGreen.schedule();
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
    m_shooter.spinStop();
    if (m_blinkLights.isScheduled()) {
      m_blinkLights.cancel();
    }
    if (m_moveNoteToBarrel.isScheduled()) {
      m_moveNoteToBarrel.cancel();
    }
    if (m_ampPivotUp.isScheduled()) {
      m_ampPivotUp.cancel();
    }
    if (m_trackAmp.isScheduled()) {
      m_trackAmp.cancel();
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
