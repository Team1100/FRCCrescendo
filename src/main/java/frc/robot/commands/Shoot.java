// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AmpAddOn.AmpPivotUp;
import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.BarrelPivot.AlignPivotToSpeakerClose;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Lights.MoveLightsPurple;
import frc.robot.commands.Shooter.ShootSlower;
import frc.robot.commands.Shooter.SpinUpShooter;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Shoot extends frc.robot.testingdashboard.ParallelRaceGroup {
  /** Creates a new Shoot. */
  public Shoot() {
    super(Shooter.getInstance(), "", "Shoot Command Group");
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ShootSlower(),
      new MoveLightsPurple(),
      new SequentialCommandGroup(
        new WaitCommand(1), 
        new ParallelRaceGroup(
          new SpinBarrelForward(),
          new Consume(),
          new WaitCommand(1)
        )
      )
    );
  }
}
