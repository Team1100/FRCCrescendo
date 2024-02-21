// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.testingdashboard.Command;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.AmpAddOn;

public class AmpPivotRelativeAngleControl extends Command {
  AmpAddOn m_ampAddOn;
  XboxController m_operatorController;

  /** Creates a new AmpPivotRelativeAngleControl. */
  public AmpPivotRelativeAngleControl() {
    super(AmpAddOn.getInstance(), "Basic", "AmpPivotRelativeAngleControl");
    m_ampAddOn = AmpAddOn.getInstance();
    m_operatorController = OI.getInstance().getOperatorXboxController();
    
    addRequirements(m_ampAddOn);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double angle = m_ampAddOn.getTargetAngle();
    double input = MathUtil.applyDeadband(m_operatorController.getRightY(), Constants.kADeadband);

    angle += input * Constants.A_ANGLE_INCREMENT_DEGREES;

    m_ampAddOn.setTargetAngle(angle);
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
