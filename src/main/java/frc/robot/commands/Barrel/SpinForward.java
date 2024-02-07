// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Barrel;

import frc.robot.Constants;
import frc.robot.subsystems.Barrel;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;

public class SpinForward extends Command {
  Barrel m_barrel;

  TDNumber m_RPM;

  TDNumber m_BarrelSpeed;

  /** Creates a new SpinForward. */
  public SpinForward() {
    super(Barrel.getInstance(), "Basic", "SpinForward");
    m_barrel = Barrel.getInstance();

    m_RPM = new TDNumber(m_barrel, "Barrel Speed (RPM)", "RPM");

    m_BarrelSpeed = new TDNumber(m_barrel, "Barrel Speed (Power)", "Speed", Constants.BARREL_SPEED);

    addRequirements(m_barrel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // m_barrel.setSpeed(m_RPM.get());
    m_barrel.spinForward(m_BarrelSpeed.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // m_barrel.setSpeed(0);
    m_barrel.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
