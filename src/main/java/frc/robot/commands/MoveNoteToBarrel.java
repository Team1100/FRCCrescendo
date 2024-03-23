// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.testingdashboard.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Constants;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.Barrel;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.Shooter;

public class MoveNoteToBarrel extends Command {
  private AmpAddOn m_amp;
  private Shooter m_shooter;
  private Barrel m_barrel;
  private Intake m_intake;

  private SensorMonitor m_sensorMonitor;

  private ParallelRaceGroup m_BlinkLights;

  private State m_state;
  private boolean m_finished;
  /** Creates a new MoveNoteToBarrel. */
  public MoveNoteToBarrel() {
    super(Barrel.getInstance(), "NoteMovement", "MoveNoteToBarrel");
    m_amp = AmpAddOn.getInstance();
    m_shooter = Shooter.getInstance();
    m_barrel = Barrel.getInstance();
    m_intake = Intake.getInstance();

    m_sensorMonitor = SensorMonitor.getInstance();

    m_BlinkLights = new BlinkLights().withTimeout(Constants.kConfusedSensorTimeoutSeconds);

    addRequirements(m_intake, m_barrel, m_shooter, m_amp);
  }

  private enum State {
    c_state_prepAmp,
    c_state_inAmp,
    c_state_inShooter,
    c_state_inIntake,
    c_state_Done,
    c_state_NoNote,
    c_state_waitForCancel
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_state = findState();
    m_finished = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    State nextState;
    switch (m_state) {
      case c_state_prepAmp:
        //Ensure note is out of the way, pivot amp down to intake
        if(!m_sensorMonitor.ampNoteCentered()){
          m_amp.setSpeed(Constants.AMP_SPEED_RPM, false);
        } else {
          m_amp.spinStop();
        }
        
        m_amp.setTargetAngle(Constants.kAPivotIntakePositionDegrees);

        if(m_amp.pivotAtIntake()){
          nextState = State.c_state_inAmp;
        } else {
          nextState = State.c_state_prepAmp;
        }
        break;

      case c_state_inAmp:
        m_amp.setSpeed(Constants.AMP_SPEED_RPM, true);
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, true);
        
        if(m_sensorMonitor.shooterHasNote()){
          nextState = State.c_state_inShooter;
        } else {
          nextState = State.c_state_inAmp;
        }
        break;

      case c_state_inShooter:
        m_shooter.setSpeeds(Constants.SHOOTER_INTAKING_SPEED_RPM, Constants.SHOOTER_INTAKING_SPEED_RPM, true);
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, true);
        m_amp.setSpeed(Constants.AMP_SPEED_RPM, true);
        if(m_sensorMonitor.barrelNoteCentered() && !m_sensorMonitor.shooterHasNote()) {
          nextState = State.c_state_Done;
        } else {
          nextState = State.c_state_inShooter;
        }
        break;

      case c_state_inIntake:
        m_intake.setSpeeds(Constants.INTAKE_SPEED_RPM, false);
        m_barrel.setSpeed(Constants.BARREL_SPEED_RPM, false);
        if(m_sensorMonitor.barrelNoteCentered() && !m_sensorMonitor.intakeHasNote()){
          nextState = State.c_state_Done;
        } else {
          nextState = State.c_state_inIntake;
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
        if(!m_BlinkLights.isFinished()) {
          nextState = State.c_state_waitForCancel;
        } else {
          nextState = State.c_state_Done;
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
      case c_Shooter:
      case c_ShooterAndAmp:
      case c_BarrelAndShooter:
        return State.c_state_inShooter;
      
      case c_Amp:
        return State.c_state_prepAmp;

      case c_Barrel:
        return State.c_state_Done;

      case c_Intake:
      case c_IntakeAndBarrel:
        return State.c_state_inIntake;

      case c_NoNote:
      case c_Invalid:
        return State.c_state_NoNote;
    
      default:
        return State.c_state_NoNote;
    }
  }

  private void stopAll() {
    m_amp.spinStop();
    m_shooter.spinStop();
    m_barrel.spinStop();
    m_intake.spinStop();
  }
}
