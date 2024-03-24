// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.Constants;
import frc.robot.subsystems.BarrelPivot;

public class AlignPivotToAmp extends Command {
  BarrelPivot m_barrelPivot;
  TDNumber m_ampPosition;

  /** Creates a new AlignPivotToAmp. */
  public AlignPivotToAmp() {
    super(BarrelPivot.getInstance(), "Setpoints", "AlignPivotToAmp");
    m_barrelPivot = BarrelPivot.getInstance();
    m_ampPosition = new TDNumber(m_barrelPivot, "Setpoints", "AmpPosition (degrees)", Constants.BP_AMP_SCORING_ANGLE_DEGREES);
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_barrelPivot.setTargetAngle(m_ampPosition.get());
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
