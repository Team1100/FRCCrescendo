// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;;

public class SpinUpShooter extends Command {
  Shooter m_shooter;

  TDNumber m_LeftRPM;
  TDNumber m_RightRPM;

  /** Creates a new SpinUpShooter. */
  public SpinUpShooter() {
    super(Shooter.getInstance(), "Basic", "SpinUpShooter");
    m_shooter = Shooter.getInstance();
    addRequirements(m_shooter);

    m_LeftRPM = new TDNumber(m_shooter, "Shooter Speed", "LeftRPM");
    m_RightRPM = new TDNumber(m_shooter, "Shooter Speed", "RightRPM");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // m_shooter.setSpeeds(0, 0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // m_shooter.setSpeeds(m_LeftRPM.get(), m_RightRPM.get());
    m_shooter.spinOut();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // m_shooter.setSpeeds(0, 0);
    m_shooter.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
