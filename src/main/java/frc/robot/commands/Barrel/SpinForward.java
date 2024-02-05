// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Barrel;

import frc.robot.subsystems.Barrel;
import frc.robot.testingdashboard.Command;

public class SpinForward extends Command {
  Barrel m_barrel;

  /** Creates a new SpinForward. */
  public SpinForward() {
    super(Barrel.getInstance(), "Basic", "SpinForward");
    m_barrel = Barrel.getInstance();
    addRequirements(m_barrel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_barrel.spinForward();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_barrel.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
