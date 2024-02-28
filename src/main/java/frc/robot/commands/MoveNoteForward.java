// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.commands.AmpAddOn.UNScoreAmp;
import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Shooter.ShooterSlowOut;
import frc.robot.subsystems.Barrel;
import frc.robot.testingdashboard.ParallelCommandGroup;

public class MoveNoteForward extends ParallelCommandGroup {
  /** Creates a new MoveNoteForward. */
  public MoveNoteForward() {
    super(Barrel.getInstance(), "ParallelCommands", "MoveNoteForward", new SpinBarrelForward(), new ShooterSlowOut(), new UNScoreAmp());
  }
}
