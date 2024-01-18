package frc.robot.testingdashboard;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestingDashboardTab {
  public SubsystemBase subsystem;
  public String subsystemName;
  public ShuffleboardTab tab;
  public TestingDashboardCommandTable commandTable;
  public TestingDashboardDataTable dataTable;
}
