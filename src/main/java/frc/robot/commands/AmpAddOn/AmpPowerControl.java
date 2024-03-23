// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.testingdashboard.Command;
import frc.robot.testingdashboard.TDNumber;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.OI;
import frc.robot.subsystems.AmpAddOn;

public class AmpPowerControl extends Command {
  AmpAddOn m_ampAddOn;
  XboxController m_operatorController;
  TDNumber powerIncrement;

  /** Creates a new AmpPowerControl. */
  public AmpPowerControl() {
    super(AmpAddOn.getInstance(), "Power Control", "AmpPowerControl");
    m_ampAddOn = AmpAddOn.getInstance();
    m_operatorController = OI.getInstance().getOperatorXboxController();

    powerIncrement = new TDNumber(m_ampAddOn, "Power Control", "Power Increment", 0.01);

    addRequirements(m_ampAddOn);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_ampAddOn.setPivotMotorPower(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double m_power = m_operatorController.getRightY();
    m_ampAddOn.setPivotMotorPower(m_power * powerIncrement.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_ampAddOn.setPivotMotorPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
