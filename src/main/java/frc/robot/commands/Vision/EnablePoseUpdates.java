// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Vision;

import frc.robot.subsystems.Vision;
import frc.robot.testingdashboard.Command;

public class EnablePoseUpdates extends Command {
  Vision m_vision;

  /** Creates a new EnablePoseUpdates. */
  public EnablePoseUpdates() {
    super(Vision.getInstance(), "Pose Updates", "EnablePoseUpdates");
    m_vision = Vision.getInstance();
    addRequirements(m_vision);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_vision.enablePoseUpdates();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
