// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import frc.robot.subsystems.BarrelPivot;
import frc.robot.testingdashboard.Command;

public class PivotDownwards extends Command {
  BarrelPivot m_barrelPivot;

  /** Creates a new PivotDownwards. */
  public PivotDownwards() {
    super(BarrelPivot.getInstance(), "Power Testing", "PivotDownwards");
    m_barrelPivot = BarrelPivot.getInstance();
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_barrelPivot.pivotDownwards();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_barrelPivot.stopPivot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
