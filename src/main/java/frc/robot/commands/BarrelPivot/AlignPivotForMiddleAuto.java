// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.Constants;
import frc.robot.subsystems.BarrelPivot;

public class AlignPivotForMiddleAuto extends Command {
  BarrelPivot m_barrelPivot;

  /** Creates a new AlignPivotAuto. */
  public AlignPivotForMiddleAuto() {
    super(BarrelPivot.getInstance(), "Setpoints", "AlignPivotForMiddleAuto");
    m_barrelPivot = BarrelPivot.getInstance();
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_barrelPivot.setTargetAngle(96.25);
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
    return false;
  }
}
