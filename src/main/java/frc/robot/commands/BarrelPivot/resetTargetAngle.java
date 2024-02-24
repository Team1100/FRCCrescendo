// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import frc.robot.subsystems.BarrelPivot;
import frc.robot.testingdashboard.Command;

public class resetTargetAngle extends Command {
  BarrelPivot m_barrelPivot;

  /** Creates a new resetTargetAngle. */
  public resetTargetAngle() {
    super(BarrelPivot.getInstance(), "Basic", "resetTargetAngle");
    m_barrelPivot = BarrelPivot.getInstance();
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_barrelPivot.resetTargetAngle();
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
