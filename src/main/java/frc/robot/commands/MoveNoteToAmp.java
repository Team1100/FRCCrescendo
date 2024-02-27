// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Constants;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.Barrel;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class MoveNoteToAmp extends Command {
  private AmpAddOn m_amp;
  private Shooter m_shooter;
  private Barrel m_barrel;
  private Intake m_intake;

  private ParallelRaceGroup m_BlinkLights;

  private boolean m_finished;

  private enum State {
    c_state_startsInShooter,
    c_state_moveAmpToIntake,
    c_state_movingAmp,
    c_state_inIntake,
    c_state_inBarrel,
    c_state_inShooter,
    c_state_inAmp,
    c_state_Done,
    c_state_NoNote,
    c_state_waitForCancel
  }

  private State m_state;
  /** Creates a new MoveNoteToAmp. */
  public MoveNoteToAmp() {
    super(AmpAddOn.getInstance(), "Note Movement", "MoveNoteToAmp");

    m_amp = AmpAddOn.getInstance();
    m_shooter = Shooter.getInstance();
    m_barrel = Barrel.getInstance();
    m_intake = Intake.getInstance();

    m_BlinkLights = new BlinkLights().withTimeout(1.5);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_amp, m_shooter, m_barrel, m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_finished = false;
    if(m_amp.noteCenteredOnSensor()){
      m_state = State.c_state_Done;
    } else if(m_shooter.hasNote() && !m_amp.pivotAtIntake() ){
      m_state = State.c_state_startsInShooter;
    } else if (!m_amp.pivotAtIntake()) {
      m_state = State.c_state_moveAmpToIntake;
    } else {
      m_state = findState();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    State nextState = State.c_state_NoNote;

    switch (m_state) {
      case c_state_startsInShooter:
        //Back it into the barrel to ensure it doesn't get stuck
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, true);
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, true);

        if(!m_shooter.hasNote() && m_barrel.noteCenteredOnSensor()) {
          m_barrel.spinStop();
          m_shooter.spinStop();
          nextState = State.c_state_moveAmpToIntake;
        } else {
          nextState = State.c_state_startsInShooter;
        }
        break;

      case c_state_moveAmpToIntake:
        m_amp.setTargetAngle(Constants.kAPivotIntakePositionDegrees);

        if(m_amp.pivotAtIntake()){
          nextState = findState();
        } else {
          nextState = State.c_state_movingAmp;
        }
        break;

      case c_state_inIntake:
        m_intake.setSpeeds(Constants.INTAKE_SPEED_RPM, false);
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, false);

        if(m_barrel.noteCenteredOnSensor() && !m_intake.hasNote()){
          m_intake.spinStop();
          nextState = State.c_state_inBarrel;
        } else {
          nextState = State.c_state_inIntake;
        }
        break;

      case c_state_inBarrel:
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, false);
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, false);
        m_amp.setSpeed(Constants.AMP_SPEED_RPM, false);

        if(!m_barrel.hasNote()){
          m_intake.spinStop();
          nextState = State.c_state_inShooter;
        } else {
          nextState = State.c_state_inBarrel;
        }
        break;

      case c_state_inShooter:
        m_amp.setSpeed(Constants.AMP_SPEED_RPM, false);
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, false);
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, false);

        if(!m_shooter.hasNote() && m_amp.hasNote()){
          m_barrel.spinStop();
          nextState = State.c_state_inAmp;
        } else {
          nextState = State.c_state_inShooter;
        }
        break;

      case c_state_inAmp:
        m_amp.setSpeed(Constants.AMP_SPEED_RPM, false);
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, false);

        if(m_amp.noteCenteredOnSensor()) {
          m_amp.spinStop();
          m_shooter.spinStop();
          nextState = State.c_state_Done;
        } else {
          nextState = State.c_state_inAmp;
        }
        break;

      case c_state_Done:
        stopAll();
        m_finished = true;

        nextState = State.c_state_Done;
        break;

      case c_state_NoNote:
        m_BlinkLights.schedule();
        nextState = State.c_state_waitForCancel;
        break;

      case c_state_waitForCancel:
        stopAll();
        if(m_BlinkLights.isFinished()) {
          nextState = State.c_state_Done;
        } else {
          nextState = State.c_state_waitForCancel;
        }
        break;
      
      default:
        nextState = findState();
        break;
    }

    m_state = nextState;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(m_BlinkLights.isScheduled()){
      m_BlinkLights.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finished;
  }

  private State findState() {
    SensorMonitor.NoteLocation loc = SensorMonitor.getInstance().determineLocation();
    switch (loc) {
      case c_IntakeAndBarrel:
      case c_Intake:
        return State.c_state_inIntake;
      
      case c_Barrel:
      case c_BarrelAndShooter:
        return State.c_state_inBarrel;
      
      case c_Shooter:
      case c_ShooterAndAmp:
        return State.c_state_inShooter;

      case c_Amp:
        if(m_amp.noteCenteredOnSensor()) {
          return State.c_state_Done;
        } else {
          return State.c_state_inAmp;
        }

      case c_NoNote:
      case c_Invalid:
        return State.c_state_NoNote;
    
      default:
        return State.c_state_NoNote;
    }
  }

  private void stopAll() {
    m_intake.spinStop();
    m_barrel.spinStop();
    m_shooter.spinStop();
    m_amp.spinStop();
  }
}
