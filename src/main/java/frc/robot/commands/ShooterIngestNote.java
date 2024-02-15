// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.testingdashboard.ParallelCommandGroup;
import frc.robot.commands.Barrel.SpinBackward;
import frc.robot.commands.Shooter.IntakeFromSource;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShooterIngestNote extends ParallelCommandGroup {
  /** Creates a new ShooterIngestNote. */
  public ShooterIngestNote() {
    super(Shooter.getInstance(), "ParallelCommands", "ShooterIngestNote");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new IntakeFromSource(), new SpinBackward());
  }
}
