// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Elevator;

import frc.robot.Constants;
import frc.robot.subsystems.Elevator;
import frc.robot.testingdashboard.Command;


public class Climb extends Command {
  Elevator m_Elevator;

  /** Creates a new Up. */
  public Climb() {
    super(Elevator.getInstance(), "Basic", "Climb");
    m_Elevator = Elevator.getInstance();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_Elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Elevator.climb(Constants.ELEVATOR_SPEED);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Elevator.doNotMove();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
