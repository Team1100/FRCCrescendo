/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Lights.EnableLights;
import frc.robot.commands.Lights.MakeRainbow;
import frc.robot.commands.Lights.MoveLights;
import frc.robot.commands.Lights.BlinkLights;
import frc.robot.subsystems.Drive;
import frc.robot.commands.Lights.DisableLights;
import frc.robot.commands.ExcreteNote;
import frc.robot.commands.IngestNote;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShooterIngestNote;
import frc.robot.commands.Drive.TurnToTarget;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  private static OI m_oi;

  private static XboxController m_DriverXboxController;
  private static XboxController m_OperatorXboxController;

  /**
   * Used outside of the OI class to return an instance of the class.
   * @return Returns instance of OI class formed from constructor.
   */
  public static OI getInstance() {
    if (m_oi == null) {
      m_oi = new OI();
    }
    return m_oi;
  }

  public OI() {
    // User Input
    // TODO: Tune deadband
    m_DriverXboxController = new XboxController(RobotMap.U_DRIVER_XBOX_CONTROLLER);
    m_OperatorXboxController = new XboxController(RobotMap.U_OPERATOR_XBOX_CONTROLLER);
    
    ////////////////////////////////////////////////////
    // Now Mapping Commands to XBox
    ////////////////////////////////////////////////////
    new JoystickButton(m_DriverXboxController, Button.kRightBumper.value).whileTrue(new IngestNote());
    new JoystickButton(m_DriverXboxController, Button.kLeftBumper.value).whileTrue(new ExcreteNote());
    new JoystickButton(m_DriverXboxController, Button.kY.value).whileTrue(new ShooterIngestNote());

    new JoystickButton(m_DriverXboxController, Button.kBack.value).onTrue(new InstantCommand(()->Drive.getInstance().zeroHeading()));
    // new JoystickButton(m_DriverXboxController, Button.kRightBumper.value).toggleOnTrue(new MakeRainbow());
    // new JoystickButton(m_DriverXboxController, Button.kLeftBumper.value).toggleOnTrue(new DisableLights());
    // new JoystickButton(m_DriverXboxController, Button.kX.value).toggleOnTrue(new BlinkLights());
    // new JoystickButton(m_DriverXboxController, Button.kY.value).toggleOnTrue(new MoveLights());

    new JoystickButton(m_DriverXboxController, Button.kX.value).whileTrue(new Shoot());
    // new JoystickButton(m_DriverXboxController, Button.kY.value).onTrue(new TurnToTarget(new Pose2d(0,0, new Rotation2d())));
  }

  /**
   * Returns the Xbox Controller
   * @return the Xbox Controller
   */
  public XboxController getDriverXboxController() {
      return m_DriverXboxController;
  }

  public XboxController getOperatorXboxController() {
    return m_OperatorXboxController;
  }  
}
