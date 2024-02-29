// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.subsystems.AmpAddOn;

public class AmpSetZeroAsCurrentPosition extends Command {
  AmpAddOn m_ampAddOn;

  TDNumber m_inputOffset;
  /** Creates a new AmpSetZeroAsCurrentPosition. */
  public AmpSetZeroAsCurrentPosition() {
    super(AmpAddOn.getInstance(), "Basic", "AmpSetZeroAsCurrentPosition");
    m_ampAddOn = AmpAddOn.getInstance();

    m_inputOffset = new TDNumber(m_ampAddOn, "Basic", "ZeroOffset");
    
    addRequirements(m_ampAddOn);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_ampAddOn.setZeroOffset(m_inputOffset.get());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
