// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.subsystems.AmpAddOn;
import frc.robot.testingdashboard.Command;

public class AmpResetTargetAngle extends Command {
  AmpAddOn m_AmpAddOn;

  /** Creates a new AmpResetTargetAngle. */
  public AmpResetTargetAngle() {
    super(AmpAddOn.getInstance(), "Basic", "AmpResetTargetAngle");
    m_AmpAddOn = AmpAddOn.getInstance();
    addRequirements(m_AmpAddOn);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_AmpAddOn.resetTargetAngle();
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
