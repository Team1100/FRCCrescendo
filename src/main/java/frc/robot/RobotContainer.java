// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Drive.SwerveDrive;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Intake.Expel;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.DisableLights;
import frc.robot.commands.Lights.EnableLights;
import frc.robot.commands.Lights.MakeRainbow;
import frc.robot.commands.Lights.MoveLights;
import frc.robot.commands.Shooter.SpinUpShooter;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.testingdashboard.TDSendable;
import frc.robot.testingdashboard.TestingDashboard;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Handle to Operator Inputs
  private OI m_oi;
  private Vision m_Vision;

  // The robot's subsystems are defined here.
  private final Drive m_robotDrive;
  private final Intake m_intake;
  private final Lights m_lights;
  private final Shooter m_shooter;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // load configuration
    RobotMap.init();
    // Configure the trigger/button bindings
    configureBindings();

    // Instantiate parameterized commands to register them with the testing dashboard.
    // The first instance of a Command registers itself. No need to store the resulting
    // objects.

    // Robot subsystems initialized and configured here
    m_robotDrive = Drive.getInstance();
    m_robotDrive.setDefaultCommand(new SwerveDrive());

    m_intake = Intake.getInstance();

    m_lights = Lights.getInstance();
    m_lights.setDefaultCommand(new MakeRainbow());

    m_shooter = Shooter.getInstance();

    if(Constants.kVisionEnabled){
      m_Vision = Vision.getInstance();
    }

    // Create Testing Dashboard
    registerCommands();
    TestingDashboard.getInstance().createTestingDashboard();
  }

  private static void registerCommands() {
    // Intake commands
    new Consume();
    new Expel();

    // Lights commands
    new BlinkLights();
    new DisableLights();
    new EnableLights();
    new MakeRainbow();
    new MoveLights();

    // Shooter commands
    // new SpinUpShooter();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_oi = OI.getInstance();
    m_oi.getDriverXboxController().getAButton();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return AutoBuilder.buildAuto("autoCommand");
  }
}
