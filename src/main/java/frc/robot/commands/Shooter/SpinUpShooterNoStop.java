// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;

public class SpinUpShooterNoStop extends Command {
  Shooter m_shooter;

  /** Creates a new SpinUpShooterNoStop. */
  public SpinUpShooterNoStop() {
    super(Shooter.getInstance(), "Basic", "SpinUpShooterNoStop");
    m_shooter = Shooter.getInstance();
    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.setSpeeds(Constants.LEFT_SHOOTER_SPEED_RPM, Constants.RIGHT_SHOOTER_SPEED_RPM, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
