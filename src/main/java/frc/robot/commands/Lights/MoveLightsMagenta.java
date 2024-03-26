// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Lights;

import frc.robot.testingdashboard.Command;
import frc.robot.subsystems.Lights;

public class MoveLightsMagenta extends Command {
  Lights m_lights;
  int hue;
  /** Creates a new MoveLightsMagenta. */
  public MoveLightsMagenta() {
    super(Lights.getInstance(), "Basic", "MoveLightsMagenta");
    m_lights = Lights.getInstance();

    hue = 160; // Magenta

    addRequirements(m_lights);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_lights.moveLights(hue);
    m_lights.setData();
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
