// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Barrel;

import frc.robot.Constants;
import frc.robot.subsystems.Barrel;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;

public class SpinBackward extends Command {
  Barrel m_barrel;

  TDNumber m_RPM;
  TDNumber m_enablePID;

  TDNumber m_BarrelSpeed;

  /** Creates a new SpinBackward. */
  public SpinBackward() {
    super(Barrel.getInstance(), "Basic", "SpinBackward");
    m_barrel = Barrel.getInstance();

    m_RPM = new TDNumber(m_barrel, "Barrel Speed (RPM)", "RPM", Constants.BARREL_SPEED_RPM);
    m_enablePID = new TDNumber(m_barrel, "Barrel Speed (RPM)", "Enable PID w 1");

    m_BarrelSpeed = new TDNumber(m_barrel, "Barrel Speed (Power)", "Speed", Constants.BARREL_SPEED);

    addRequirements(m_barrel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_enablePID.get() == 1) {
      m_barrel.setSpeed(m_RPM.get(), true);
    }
    else {
      m_barrel.spinBackward(m_BarrelSpeed.get());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_enablePID.get() == 1) {
      m_barrel.setSpeed(0, true);
    }
    else {
      m_barrel.spinStop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

