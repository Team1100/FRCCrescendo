// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.testingdashboard.ParallelCommandGroup;
import frc.robot.commands.AmpAddOn.SpinUpAmpRollers;
import frc.robot.commands.Barrel.SpinBarrelBackward;
import frc.robot.commands.Intake.Expel;
import frc.robot.commands.Shooter.IntakeFromShooter;
import frc.robot.subsystems.Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MoveNoteBackward extends ParallelCommandGroup {
  /** Creates a new Excrete. */
  public MoveNoteBackward() {
    super(Intake.getInstance(), "ParallelCommands", "MoveNoteBackward");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new Expel(), new SpinBarrelBackward(), new IntakeFromShooter(), new SpinUpAmpRollers());
  }
}