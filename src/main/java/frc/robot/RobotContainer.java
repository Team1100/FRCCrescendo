// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.MoveNoteBackward;
import frc.robot.commands.MoveNoteForward;
import frc.robot.commands.MoveNoteToAmp;
import frc.robot.commands.MoveNoteToBarrel;
import frc.robot.commands.GroundIntake;
import frc.robot.commands.PrepareToAmp;
import frc.robot.commands.PrepareToShoot;
import frc.robot.commands.PrepareToShootClose;
import frc.robot.commands.ScoreAmp;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShootSpeaker;
import frc.robot.commands.SourceIntake;
import frc.robot.commands.Shooter.ShooterSlowOut;
import frc.robot.commands.AmpAddOn.AmpPivotRelativeAngleControl;
import frc.robot.commands.AmpAddOn.AmpPivotToIntake;
import frc.robot.commands.AmpAddOn.AmpPivotToScoringPosition;
import frc.robot.commands.AmpAddOn.AmpPivotUp;
import frc.robot.commands.AmpAddOn.AmpResetTargetAngle;
import frc.robot.commands.AmpAddOn.AmpSetZeroAsCurrentPosition;
import frc.robot.commands.AmpAddOn.SpinUpAmpRollers;
import frc.robot.commands.AmpAddOn.UNScoreAmp;
import frc.robot.commands.Barrel.SpinBarrelBackward;
import frc.robot.commands.Barrel.SpinBarrelForward;
import frc.robot.commands.Barrel.SpinBarrelForwardAutoSensorStop;
import frc.robot.commands.BarrelPivot.AlignPivotForMiddleAuto;
import frc.robot.commands.BarrelPivot.AlignPivotToAmp;
import frc.robot.commands.BarrelPivot.AlignPivotToSpeakerClose;
import frc.robot.commands.BarrelPivot.AlignWithSource;
import frc.robot.commands.BarrelPivot.PivotDOWNDOWNDOWN;
import frc.robot.commands.BarrelPivot.PivotDownwards;
import frc.robot.commands.BarrelPivot.PivotRelativeAngleControl;
import frc.robot.commands.BarrelPivot.PivotToSpeaker;
import frc.robot.commands.BarrelPivot.PivotUpwards;
import frc.robot.commands.BarrelPivot.SetZeroAsCurrentPosition;
import frc.robot.commands.BarrelPivot.ResetTargetAngle;
import frc.robot.commands.Drive.DriveToPose;
import frc.robot.commands.Drive.SwerveDrive;
import frc.robot.commands.Drive.TargetDrive;
import frc.robot.commands.Drive.TurnToTarget;
import frc.robot.commands.Elevator.Climb;
import frc.robot.commands.Elevator.UNClimb;
import frc.robot.commands.Intake.Consume;
import frc.robot.commands.Intake.Expel;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.commands.Lights.DisableLights;
import frc.robot.commands.Lights.EnableLights;
import frc.robot.commands.Lights.MakeCool;
import frc.robot.commands.Lights.MakeRainbow;
import frc.robot.commands.Lights.MakeWarm;
import frc.robot.commands.Lights.MoveLightsBlue;
import frc.robot.commands.Lights.MoveLightsColor;
import frc.robot.commands.Lights.MoveLightsGreen;
import frc.robot.commands.Lights.MoveLightsMagenta;
import frc.robot.commands.Lights.MoveLightsPurple;
import frc.robot.commands.Lights.MoveLightsYellow;
import frc.robot.commands.Sensors.ResetAllSensors;
import frc.robot.commands.Sensors.ToggleSensorsOnOff;
import frc.robot.commands.Shooter.IntakeFromShooter;
import frc.robot.commands.Shooter.SpinUpShooter;
import frc.robot.commands.Shooter.SpinUpShooterNoStop;
import frc.robot.commands.Vision.DisablePoseUpdates;
import frc.robot.commands.Vision.EnablePoseUpdates;
import frc.robot.subsystems.AmpAddOn;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.SensorMonitor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.testingdashboard.TDSendable;
import frc.robot.testingdashboard.TestingDashboard;
import frc.robot.utils.FieldUtils;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  private final SensorMonitor m_SensorMonitor;
  private final BarrelPivot m_barrelPivot;
  private final AmpAddOn m_ampAddOn;
  private final SendableChooser<Command> m_autoChooser;
  private PowerDistribution m_pdBoard;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // load configuration
    RobotMap.init();

    m_pdBoard = new PowerDistribution(1, ModuleType.kRev);
    m_pdBoard.setSwitchableChannel(true);

    m_oi = OI.getInstance();

    // Instantiate parameterized commands to register them with the testing dashboard.
    // The first instance of a Command registers itself. No need to store the resulting
    // objects.
    registerCommands();

    // Robot subsystems initialized and configured here
    m_robotDrive = Drive.getInstance();
    m_robotDrive.setDefaultCommand(new SwerveDrive(m_oi.getDriveInputs()));

    m_ampAddOn = AmpAddOn.getInstance();
    if (RobotMap.A_PIVOT_ENABLED) {
      m_ampAddOn.setDefaultCommand(new AmpPivotRelativeAngleControl());
    }

    m_barrelPivot = BarrelPivot.getInstance();
    if (RobotMap.BP_ENABLED) {
      m_barrelPivot.setDefaultCommand(new PivotRelativeAngleControl());
    }

    m_intake = Intake.getInstance();

    m_lights = Lights.getInstance();
    m_lights.setDefaultCommand(new MoveLightsColor());

    m_shooter = Shooter.getInstance();

    m_Vision = Vision.getInstance();

    m_SensorMonitor = SensorMonitor.getInstance();

    // Build the auto commands and add them to the chooser
    m_autoChooser = AutoBuilder.buildAutoChooser("closeAutoTop_startMid");
    new TDSendable(Drive.getInstance(), "Auto Commands", "Chooser", m_autoChooser);
    
    // Configure the trigger/button bindings
    configureBindings();

    // Create Testing Dashboard
    TestingDashboard.getInstance().createTestingDashboard();
    SmartDashboard.putData(m_autoChooser);
  }

  private void registerCommands() {
    // Amp Add-On commands
    new SpinUpAmpRollers();
    new UNScoreAmp();
    new AmpSetZeroAsCurrentPosition();
    new AmpPivotRelativeAngleControl();
    new AmpResetTargetAngle();
    new AmpPivotToIntake();
    new AmpPivotToScoringPosition();
    new AmpPivotUp();

    // Barrel commands
    new SpinBarrelForward();
    new SpinBarrelBackward();
    new SpinBarrelForwardAutoSensorStop();

    // BarrelPivot commands
    new AlignPivotForMiddleAuto();
    new AlignWithSource();
    new AlignPivotToAmp();
    new AlignPivotToSpeakerClose();
    new SetZeroAsCurrentPosition();
    new ResetTargetAngle();
    new PivotRelativeAngleControl();
    new PivotUpwards();
    new PivotDOWNDOWNDOWN();
    new PivotDownwards();
    new PivotToSpeaker();

    // Elevator commands
    new Climb();
    new UNClimb();

    // Intake commands
    new Consume();
    new Expel();

    // Lights commands
    new BlinkLights();
    new DisableLights();
    new EnableLights();
    new MakeRainbow();
    new MoveLightsBlue();
    new MakeCool();
    new MoveLightsGreen();
    new MoveLightsMagenta();
    new MoveLightsYellow();
    new MoveLightsPurple();
    new MoveLightsColor();
    new MakeWarm();

    // Sensor commands
    new ToggleSensorsOnOff();
    new ResetAllSensors();

    // Shooter commands
    new IntakeFromShooter();
    new SpinUpShooter();
    new ShooterSlowOut();
    new SpinUpShooterNoStop();

    // Vision
    new EnablePoseUpdates();
    new DisablePoseUpdates();

    new Shoot();
    new MoveNoteForward();
    new MoveNoteBackward();
    new MoveNoteToAmp();
    new MoveNoteToBarrel();

    new GroundIntake();
    new SourceIntake();
    new PrepareToShoot();
    new ShootSpeaker();
    new PrepareToAmp();
    new ScoreAmp();

    new PrepareToShootClose();


    TDNumber testX = new TDNumber(Drive.getInstance(), "Test Inputs", "TargetPoseX");
    TDNumber testY = new TDNumber(Drive.getInstance(), "Test Inputs", "TargetPoseY");
    new TurnToTarget(()->{
      return new Pose2d(testX.get(), testY.get(), new Rotation2d());
    });

    new TargetDrive(()->{
      return FieldUtils.getInstance().getSpeakerPose().toPose2d();//return new Pose2d(testX.get(), testY.get(), new Rotation2d());//
    }, m_oi.getDriveInputs());

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
    OI.getInstance().bindControls();
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