// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.subsystems.AmpAddOn;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.Constants;


public class UNScoreAmp extends Command {
  AmpAddOn m_AmpAddOn;

  TDNumber m_RPM;
  TDNumber m_enablePID;
  TDNumber m_ampSpeed;
  
  /** Creates a new UNScoreAmp. */
  public UNScoreAmp() {
    super(AmpAddOn.getInstance(), "Basic", "UNScore");
    m_AmpAddOn = AmpAddOn.getInstance();

    m_RPM = new TDNumber(m_AmpAddOn, "Amp Speed (RPM)", "RPM", Constants.AMP_SPEED_RPM);
    m_enablePID = new TDNumber(m_AmpAddOn, "Amp Speed (RPM)", "Enable PID w 1");

    m_ampSpeed = new TDNumber(m_AmpAddOn, "Amp Speed (Power)", "Speed", Constants.AMP_SPEED);

    addRequirements(m_AmpAddOn);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_enablePID.get() == 1) {
      m_AmpAddOn.setSpeed(m_RPM.get(), true);
    }
    else {
      m_AmpAddOn.spinIn(m_ampSpeed.get());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_enablePID.get() == 1) {
      m_AmpAddOn.setSpeed(0, false);
    }
    else {
      m_AmpAddOn.spinStop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
