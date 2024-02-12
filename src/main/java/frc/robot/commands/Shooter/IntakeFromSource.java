// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;

public class IntakeFromSource extends Command {
  Shooter m_shooter;

  TDNumber m_RPM;
  TDNumber m_enablePID;

  TDNumber m_shooterSpeed;

  /** Creates a new IntakeFromSource. */
  public IntakeFromSource() {
    super(Shooter.getInstance(), "Basic", "IntakeFromSource");
    m_shooter = Shooter.getInstance();

    m_RPM = new TDNumber(m_shooter, "Intaking Speed (RPM)", "RPM", Constants.SHOOTER_INTAKING_SPEED_RPM);
    m_enablePID = new TDNumber(m_shooter, "Intaking Speed (RPM)", "Enable PID w 1");

    m_shooterSpeed = new TDNumber(m_shooter, "Intaking Speed (Power)", "Speed", Constants.SHOOTER_INTAKING_SPEED);

    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_enablePID.get() == 1) {
      m_shooter.setSpeeds(m_RPM.get(), m_RPM.get(), true);
    }
    else {
      m_shooter.spinIn(m_shooterSpeed.get());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_enablePID.get() == 1) {
      m_shooter.setSpeeds(0, 0, true);
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
