package frc.robot.testingdashboard;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class TestingDashboardTab {
  public String tabName;
  public ShuffleboardTab tab;
  public TestingDashboardCommandTable commandTable;
  public TestingDashboardDataTable dataTable;

  public TestingDashboardTab(String name)
  {
    tabName = name;
    commandTable = new TestingDashboardCommandTable();
    dataTable = new TestingDashboardDataTable();
  }
}
