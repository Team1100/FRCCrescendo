// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDBoolean;
import frc.robot.testingdashboard.TDString;

public class SensorMonitor extends SubsystemBase {
  private static SensorMonitor m_instance;

  private Intake m_intake;
  private Barrel m_barrel;
  private Shooter m_shooter;
  private AmpAddOn m_amp;

  public enum NoteLocation {
    c_Intake,
    c_IntakeAndBarrel,
    c_Barrel,
    c_BarrelAndShooter,
    c_Shooter,
    c_ShooterAndAmp,
    c_Amp,
    c_NoNote,
    c_Invalid,
    c_SensorsDisabled
  }

  private TDString m_noteLocation;
  private TDBoolean m_sensorsEnabled;

  public static SensorMonitor getInstance(){
    if(m_instance == null){
      m_instance = new SensorMonitor();
    }
    return m_instance;
  }
  /** Creates a new SensorMonitor. */
  private SensorMonitor() {
    super("SensorMonitor");

    m_intake = Intake.getInstance();
    m_barrel = Barrel.getInstance();
    m_shooter = Shooter.getInstance();
    m_amp = AmpAddOn.getInstance();

    m_noteLocation = new TDString(this, "", "Note Location", "No Note In Robot");
    m_sensorsEnabled = new TDBoolean(this, "Toggle Sensors", "Sensors Enabled", true);
  }

  /** Flips the value of sensorsEnabled boolean */
  public void toggleSensorsOnOff() {
    m_sensorsEnabled.set(!m_sensorsEnabled.get());
  }

  public boolean intakeHasNote() {
    if (determineLocation() == NoteLocation.c_Intake || 
          determineLocation() == NoteLocation.c_IntakeAndBarrel) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean shooterHasNote() {
    if (determineLocation() == NoteLocation.c_Shooter || 
          determineLocation() == NoteLocation.c_ShooterAndAmp || 
          determineLocation() == NoteLocation.c_BarrelAndShooter) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean ampHasNote() {
    if (determineLocation() == NoteLocation.c_Amp ||
          determineLocation() == NoteLocation.c_ShooterAndAmp) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean barrelHasNote() {
    if (determineLocation() == NoteLocation.c_Barrel || 
          determineLocation() == NoteLocation.c_IntakeAndBarrel ||
          determineLocation() == NoteLocation.c_BarrelAndShooter) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean isValidState() {
    if(m_intake.hasNote()){
      return !(m_shooter.hasNote() || m_amp.hasNote());
    } else if(m_barrel.hasNote()){
      return (!m_amp.hasNote() && !(m_shooter.hasNote() && m_intake.hasNote()));
    } else if(m_shooter.hasNote()){
      return (!m_intake.hasNote() && !(m_barrel.hasNote() && m_amp.hasNote()));
    } else if(!m_sensorsEnabled.get()) {
      return false;
    }
    return true;
  }

  public void resetAllSensors() {
    m_intake.resetSensor();
    m_barrel.resetSensor();
    m_shooter.resetSensor();
    m_amp.resetSensor();
  }

  @Override
  public void periodic() {
    NoteLocation currentLocation = determineLocation();
    m_noteLocation.set(locationToString(currentLocation));

    super.periodic();
  }

  private String locationToString(NoteLocation location){
    switch (location) {
      case c_Intake:
        return "Intake";
      case c_IntakeAndBarrel:
        return "Intake & Barrel";
      case c_Barrel:
        return "Barrel";
      case c_BarrelAndShooter:
        return "Barrel & Shooter";
      case c_Shooter:
        return "Shooter";
      case c_ShooterAndAmp:
        return "Shooter & Amp";
      case c_Amp:
        return "Amp";
      case c_NoNote:
        return "No Note In Robot";
      case c_Invalid:
        return "Invalid Sensor State";
      case c_SensorsDisabled:
        return "Sensors Disabled";
    
      default:
        return "Invalid Sensor State";
    }
  }

  public NoteLocation determineLocation() {
    if(!isValidState()){
      if (!m_sensorsEnabled.get()) {
        return NoteLocation.c_SensorsDisabled;
      }
      else {
        return NoteLocation.c_Invalid;
      }
    }

    if(m_intake.hasNote()){
      if(m_barrel.hasNote()){
        return NoteLocation.c_IntakeAndBarrel;
      } else {
        return NoteLocation.c_Intake;
      }
    }

    if(m_barrel.hasNote()){
      if(m_shooter.hasNote()){
        return NoteLocation.c_BarrelAndShooter;
      } else {
        return NoteLocation.c_Barrel;
      }
    }

    if(m_shooter.hasNote()){
      if(m_amp.hasNote()){
        return NoteLocation.c_ShooterAndAmp;
      } else {
        return NoteLocation.c_Shooter;
      }
    }

    if(m_amp.hasNote()){
      return NoteLocation.c_Amp;
    }

    return NoteLocation.c_NoNote;
  }
}
