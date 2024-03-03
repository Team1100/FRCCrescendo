// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.AmpAddOn.AmpPivotToIntake;
import frc.robot.commands.AmpAddOn.UNScoreAmp;
import frc.robot.commands.Barrel.SpinBarrelBackward;
import frc.robot.commands.BarrelPivot.AlignWithSource;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.commands.Shooter.IntakeFromShooter;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.SensorMonitor.NoteLocation;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class SourceIntake extends Command {
  enum State {
    INIT,
    ALIGN_PIVOTS,
    SPIN_SUBSYSTEMS_IN,
    WAIT_FOR_BARREL_NOTE_DETECTION,
    DONE
  }

  BlinkLights m_blinkLights;
  AmpPivotToIntake m_ampPivotToIntake;
  AlignWithSource m_alignWithSource;
  MoveLightsGreen m_moveLightsGreen;
  UNScoreAmp m_unScoreAmp;
  IntakeFromShooter m_intakeFromShooter;
  SpinBarrelBackward m_spinBarrelBackward;

  SensorMonitor m_sensorMonitor;

  private boolean m_isFinished;
  private State m_state;

  /** Creates a new SourceIntake. */
  public SourceIntake() {
    super(Shooter.getInstance(), "", "Source Intake");
    
    m_state = State.INIT;
    m_isFinished = false;

    m_blinkLights = new BlinkLights();
    m_ampPivotToIntake = new AmpPivotToIntake();
    m_alignWithSource = new AlignWithSource();
    m_moveLightsGreen = new MoveLightsGreen();
    m_unScoreAmp = new UNScoreAmp();
    m_intakeFromShooter = new IntakeFromShooter();
    m_spinBarrelBackward = new SpinBarrelBackward();

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
        switch (m_sensorMonitor.determineLocation()) {
          case c_ShooterAndAmp:
          case c_Shooter:
          case c_BarrelAndShooter:
            m_state = State.SPIN_SUBSYSTEMS_IN;
            break;
          case c_Barrel:
          case c_IntakeAndBarrel:
          case c_Intake:
            m_state = State.DONE;
            break;
          default:
            m_blinkLights.schedule();
            m_ampPivotToIntake.schedule();
            m_alignWithSource.schedule();
            m_state = State.ALIGN_PIVOTS;
            break;
        }
        break;

      case ALIGN_PIVOTS:
        if (m_ampPivotToIntake.isFinished() && m_alignWithSource.isFinished()) {
          m_state = State.SPIN_SUBSYSTEMS_IN;
        }
        break;

      case SPIN_SUBSYSTEMS_IN:
        m_moveLightsGreen.schedule();
        m_unScoreAmp.schedule();
        m_intakeFromShooter.schedule();
        m_spinBarrelBackward.schedule();
        m_state = State.WAIT_FOR_BARREL_NOTE_DETECTION;
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
    if (m_blinkLights.isScheduled()) {
      m_blinkLights.cancel();
    }
    if (m_ampPivotToIntake.isScheduled()) {
      m_ampPivotToIntake.cancel();
    }
    if (m_alignWithSource.isScheduled()) {
      m_alignWithSource.cancel();
    }
    if (m_intakeFromShooter.isScheduled()) {
      m_intakeFromShooter.cancel();
    }
    if (m_spinBarrelBackward.isScheduled()) {
      m_spinBarrelBackward.cancel();
    }
    if (m_moveLightsGreen.isScheduled()) {
      m_moveLightsGreen.cancel();
    }
    if (m_unScoreAmp.isScheduled()) {
      m_unScoreAmp.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isFinished;
  }
}
