// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;;

public class SpinUpShooter extends Command {
  Shooter m_shooter;

  TDNumber m_LeftRPM;
  TDNumber m_RightRPM;
  TDNumber m_enablePID;

  TDNumber m_LeftShooterSpeed;
  TDNumber m_RightShooterSpeed;

  /** Creates a new SpinUpShooter. */
  public SpinUpShooter() {
    super(Shooter.getInstance(), "Basic", "SpinUpShooter");
    m_shooter = Shooter.getInstance();
    addRequirements(m_shooter);

    m_LeftRPM = new TDNumber(m_shooter, "Shooter Speed (RPM)", "LeftRPM", Constants.LEFT_SHOOTER_SPEED_RPM);
    m_RightRPM = new TDNumber(m_shooter, "Shooter Speed (RPM)", "RightRPM", Constants.RIGHT_SHOOTER_SPEED_RPM);
    m_enablePID = new TDNumber(m_shooter, "Shooter Speed (RPM)", "Enable PID w 1");

    m_LeftShooterSpeed = new TDNumber(m_shooter, "Shooter Speed (Power)", "Left Speed", Constants.LEFT_SHOOTER_SPEED);
    m_RightShooterSpeed = new TDNumber(m_shooter, "Shooter Speed (Power)", "Right Speed", Constants.RIGHT_SHOOTER_SPEED);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_enablePID.get() == 1) {
      m_shooter.setSpeeds(m_LeftRPM.get(), m_RightRPM.get(), false);
    }
    else {
      m_shooter.spinOut(m_LeftShooterSpeed.get(), m_RightShooterSpeed.get());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_enablePID.get() == 1) {
      m_shooter.setSpeeds(0, 0, false);
    }
    else {
      m_shooter.spinStop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
