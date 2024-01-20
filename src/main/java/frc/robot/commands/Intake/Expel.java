
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;


import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.OI;
import frc.robot.subsystems.Intake;
import frc.robot.testingdashboard.TestingDashboard;

public class Expel extends Command {
  private static XboxController m_driverController;
  Intake m_intake;

  /** Creates a new ArcadeDrive. */
  public Expel() {
    m_intake = Intake.getInstance();
    m_driverController = OI.getInstance().getDriverXboxController();

    addRequirements(m_intake);
  }

  public static void registerWithTestingDashboard() {
    Intake intake = Intake.getInstance();
    Expel cmd = new Expel();
    TestingDashboard.getInstance().registerCommand(intake, "Basic", cmd);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intake.spinOut();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

