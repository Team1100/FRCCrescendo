// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BarrelPivot;

import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;
import frc.robot.subsystems.BarrelPivot;

public class AlignPivotToSpeaker extends Command {
  BarrelPivot m_barrelPivot;
  TDNumber m_SpeakerPosition;

  /** Creates a new AlignPivotToSpeaker. */
  public AlignPivotToSpeaker() {
    super(BarrelPivot.getInstance(), "Setpoints", "AlignPivotToSpeaker");
    m_barrelPivot = BarrelPivot.getInstance();
    m_SpeakerPosition = new TDNumber(m_barrelPivot, "Setpoints", "SpeakerPosition (degrees)", Constants.BP_SHOOTER_SCORING_ANGLE_DEGREES);
    addRequirements(m_barrelPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_barrelPivot.setTargetAngle(m_SpeakerPosition.get());
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
    return MathUtil.isNear(Constants.BP_SHOOTER_SCORING_ANGLE_DEGREES, m_barrelPivot.getAngle(), 3);
  }
}
