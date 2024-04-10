// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;;

public class ShootSlower extends Command {
  Shooter m_shooter;

  double m_LeftRPM;
  double m_RightRPM;

  /** Creates a new ShootSlower. */
  public ShootSlower() {
    super(Shooter.getInstance(), "Basic", "ShootSlower");
    m_shooter = Shooter.getInstance();
    addRequirements(m_shooter);
    m_LeftRPM = 1000;
    m_RightRPM = 1700;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.setSpeeds(m_LeftRPM, m_RightRPM, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooter.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
