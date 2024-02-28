// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.Constants;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;

public class AmpPivotToIntake extends Command {
  private AmpAddOn m_amp;
  private TDNumber m_intakePosition;
  /** Creates a new AmpPivotToIntake. */
  public AmpPivotToIntake() {
    super(AmpAddOn.getInstance(), "Pivot Commands", "PivotToIntake");
    m_amp = AmpAddOn.getInstance();
    m_intakePosition = new TDNumber(m_amp, "Pivot Commands", "Pivot Intake Position Degrees", Constants.kAPivotIntakePositionDegrees);
    addRequirements(m_amp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_amp.setTargetAngle(m_intakePosition.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_amp.pivotAtIntake();
  }
}
