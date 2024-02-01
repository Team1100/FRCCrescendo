// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Lights;

import frc.robot.testingdashboard.Command;
import frc.robot.subsystems.Lights;

public class BlinkLights extends Command {
  Lights m_lights;

  /** Creates a new BlinkLights. */
  public BlinkLights() {
    super(Lights.getInstance(), "Basic", "BlinkLights");
    m_lights = Lights.getInstance();
    addRequirements(m_lights);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_lights.blinkLights();
    m_lights.getLED().setData(m_lights.getLEDBuffer());
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
