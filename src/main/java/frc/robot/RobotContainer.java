// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.ExcreteNote;
import frc.robot.commands.IngestNote;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShooterIngestNote;
import frc.robot.commands.Barrel.SpinBackward;
import frc.robot.commands.Barrel.SpinForward;
import frc.robot.commands.BarrelPivot.PivotRelativeAngleControl;
import frc.robot.commands.BarrelPivot.SetZeroAsCurrentPosition;
import frc.robot.commands.Drive.DriveToPose;
import frc.robot.commands.Drive.SwerveDrive;
import frc.robot.commands.Drive.TargetDrive;
import frc.robot.commands.Drive.TurnToRotation;
import frc.robot.commands.Drive.TurnToTarget;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Intake.Expel;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.DisableLights;
import frc.robot.commands.Lights.EnableLights;
import frc.robot.commands.Lights.MakeRainbow;
import frc.robot.commands.Lights.MoveLights;
import frc.robot.commands.Shooter.IntakeFromShooter;
import frc.robot.commands.Shooter.SpinUpShooter;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.testingdashboard.TDSendable;
import frc.robot.testingdashboard.TestingDashboard;
import frc.robot.utils.FieldUtils;
import frc.robot.utils.SwerveDriveInputs;

import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
  private final BarrelPivot m_barrelPivot;
  private final SendableChooser<Command> m_autoChooser;

  private SwerveDriveInputs m_driveInputs;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // load configuration
    RobotMap.init();

    //Set up drive translation and rotation inputs
    XboxController driveController = OI.getInstance().getDriverXboxController();
    Supplier<Double> xInput;
    Supplier<Double> yInput;
    if(RobotBase.isReal()){
      xInput = ()->driveController.getLeftY();
      yInput = ()->driveController.getLeftX();
    } else {
      xInput = ()->-driveController.getLeftX();
      yInput = ()->driveController.getLeftY();
    }
    m_driveInputs = new SwerveDriveInputs(xInput, yInput, ()->driveController.getRightX());

    // Instantiate parameterized commands to register them with the testing dashboard.
    // The first instance of a Command registers itself. No need to store the resulting
    // objects.
    registerCommands();

    // Robot subsystems initialized and configured here
    m_robotDrive = Drive.getInstance();
    m_robotDrive.setDefaultCommand(new SwerveDrive(m_driveInputs));

    m_barrelPivot = BarrelPivot.getInstance();
    if (RobotMap.BP_ENABLED) {
      m_barrelPivot.setDefaultCommand(new PivotRelativeAngleControl());
    }

    m_intake = Intake.getInstance();

    m_lights = Lights.getInstance();
    m_lights.setDefaultCommand(new MoveLights());

    m_shooter = Shooter.getInstance();

    m_Vision = Vision.getInstance();

    // Build the auto commands and add them to the chooser
    m_autoChooser = AutoBuilder.buildAutoChooser("3NotePaths");
    new TDSendable(Drive.getInstance(), "Auto Commands", "Chooser", m_autoChooser);
    
    // Configure the trigger/button bindings
    configureBindings();

    // Create Testing Dashboard
    TestingDashboard.getInstance().createTestingDashboard();
  }

  private void registerCommands() {
    // Barrel commands
    new SpinForward();
    new SpinBackward();

    // BarrelPivot commands
    new SetZeroAsCurrentPosition();

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
    new IntakeFromShooter();
    new SpinUpShooter();

    new Shoot();
    new ShooterIngestNote();

    new IngestNote();
    new ExcreteNote();

    // TDNumber turnTestAngle = new TDNumber(Drive.getInstance(), "Test Inputs", "Turn Angle");
    // new TurnToRotation(()->new Rotation2d(turnTestAngle.get()));
    TDNumber testX = new TDNumber(Drive.getInstance(), "Test Inputs", "TargetPoseX");
    TDNumber testY = new TDNumber(Drive.getInstance(), "Test Inputs", "TargetPoseY");
    Pose2d targetPose = new Pose2d(testX.get(), testY.get(), new Rotation2d());
    new TurnToTarget(targetPose);

    new TargetDrive(()->{
      return FieldUtils.getInstance().getSpeakerPose().toPose2d();//return new Pose2d(testX.get(), testY.get(), new Rotation2d());//
    }, m_driveInputs);

    new DriveToPose(()->{
      return FieldUtils.getInstance().getSpeakerPose().toPose2d();//return new Pose2d(testX.get(), testY.get(), new Rotation2d());//
    });
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
    return m_autoChooser.getSelected();
  }
}
