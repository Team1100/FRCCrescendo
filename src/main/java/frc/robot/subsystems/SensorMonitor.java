// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDBoolean;
import frc.robot.testingdashboard.TDString;
import frc.robot.utils.NoteProximitySensor;
import frc.robot.utils.SensorThread;

public class SensorMonitor extends SubsystemBase {
  private static SensorMonitor m_instance;

  private NoteProximitySensor m_intake;
  private NoteProximitySensor m_barrel;
  private NoteProximitySensor m_shooter;
  private NoteProximitySensor m_amp;

  private SensorThread m_sensorUpdater;

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

    m_intake = new NoteProximitySensor(RobotMap.I_NOTE_SENSOR, Intake.getInstance());
    m_barrel = new NoteProximitySensor(RobotMap.B_NOTE_SENSOR, Barrel.getInstance());
    m_shooter = new NoteProximitySensor(RobotMap.S_NOTE_SENSOR, Shooter.getInstance());
    m_amp = new NoteProximitySensor(RobotMap.A_NOTE_SENSOR, AmpAddOn.getInstance());

    m_noteLocation = new TDString(this, "", "Note Location", "No Note In Robot");
    m_sensorsEnabled = new TDBoolean(this, "Toggle Sensors", "Sensors Enabled", false);
    if(Constants.kSensorThreadingEnabled){
      startSensorThread();
    }
  }

  private void startSensorThread() {
    if(m_sensorUpdater == null || !m_sensorUpdater.isAlive()) {
      m_sensorUpdater = new SensorThread(m_intake, m_barrel, m_shooter, m_amp);
      m_sensorUpdater.start();
    } else {
      System.out.println("Sensor Update Thread is already running");
    }
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

  public boolean intakeNoteCentered() {
    return m_intake.noteIsCentered();
  }

  public void intakeResetSensor() {
    m_intake.reset();
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

  public boolean shooterNoteCentered() {
    return m_shooter.noteIsCentered();
  }

  public void shooterResetSensor() {
    m_shooter.reset();
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

  public boolean ampNoteCentered() {
    return m_amp.noteIsCentered();
  }

  public void ampResetSensor() {
    m_amp.reset();
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

  public boolean barrelNoteCentered() {
    return m_barrel.noteIsCentered();
  }

  public boolean barrelSeesNote() {
    return m_barrel.seesNote();
  }

  public void barrelResetSensor() {
    m_barrel.reset();
  }

  public boolean isValidState() {
    if(!m_sensorsEnabled.get()) {
      return false;
    }else if(m_intake.hasNote()){
      return !(m_shooter.hasNote() || m_amp.hasNote());
    } else if(m_barrel.hasNote()){
      return (!m_amp.hasNote() && !(m_shooter.hasNote() && m_intake.hasNote()));
    } else if(m_shooter.hasNote()){
      return (!m_intake.hasNote() && !(m_barrel.hasNote() && m_amp.hasNote()));
    }
    return true;
  }

  public void resetAllSensors() {
    m_intake.reset();
    m_barrel.reset();
    m_shooter.reset();
    m_amp.reset();
  }

  @Override
  public void periodic() {
    if(!Constants.kSensorThreadingEnabled){
      m_intake.update();
      m_barrel.update();
      m_shooter.update();
      m_amp.update();
    }

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
    if (!m_sensorsEnabled.get()) {
        return NoteLocation.c_SensorsDisabled;
      }
    if(!isValidState()){
      if (!m_sensorsEnabled.get()) {
        return NoteLocation.c_SensorsDisabled;
      }
      else {
        if(!attemptToCorrectInvalid()) { //If we can't correct the invalid state, return invalid
          return NoteLocation.c_Invalid;
        }
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

  /*
   * Tries to reset sensors based on the actual sensor inputs rather than stored state
   * 
   * @returns true if it succeeded at correcting the invalid state
   */
  boolean attemptToCorrectInvalid() {
    if(m_intake.seesNote()) {
        if(m_amp.hasNote() && !m_amp.seesNote()){
          m_amp.reset();
        }
        if(m_shooter.hasNote() && !m_shooter.seesNote()) {
          m_shooter.reset();
        }
    }

    if(m_barrel.seesNote()) {
      if(m_amp.hasNote() && !m_amp.seesNote()) {
        m_amp.reset();
      }
    }

    if(m_shooter.seesNote()) {
      if(m_intake.hasNote() && !m_intake.seesNote()) {
        m_intake.reset();
      }
    }

    if(m_amp.seesNote()) {
      if(m_intake.hasNote() && !m_intake.seesNote()){
        m_intake.reset();
      }

      if(m_barrel.hasNote() && !m_barrel.seesNote()){
        m_barrel.reset();
      }
    }

    return isValidState();
  }
}
