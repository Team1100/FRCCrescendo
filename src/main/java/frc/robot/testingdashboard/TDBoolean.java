// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

  /** Add your docs here. */
public class TDBoolean implements TDValue {

  private boolean m_val;
  private String m_tabName;
  private String m_groupName;
  private String m_dataName;
  private boolean m_needsPost = false;
  
  public TDBoolean(SubsystemBase subsystem, String groupName, String dataName) {
    m_val = false;
    m_tabName = subsystem.getName();
    m_groupName = groupName;
    m_dataName = dataName;
    TestingDashboard.getInstance().registerBoolean(m_tabName, groupName, dataName, m_val);
    subsystem.registerValue(this);
    get();
  }

  public TDBoolean(SubsystemBase subsystem, String groupName, String dataName, boolean val) {
    m_val = val;
    m_tabName = subsystem.getName();
    m_groupName = groupName;
    m_dataName = dataName;
    TestingDashboard.getInstance().registerBoolean(m_tabName, m_groupName, m_dataName, m_val);
    subsystem.registerValue(this);
    set(val);
  }

  public void set(boolean val)
  {
    if (m_val != val)
    {
      m_needsPost = true;
      m_val = val;
    }
  }

  /**
   * @return current value
   */
  public boolean get() {
    if (!m_needsPost) {
      m_val = TestingDashboard.getInstance().getBoolean(m_tabName, m_dataName);
    }
    return m_val;
  }

  public void post()
  {
    if (m_needsPost)
    {
      TestingDashboard.getInstance().updateBoolean(m_tabName, m_dataName, m_val);
      m_needsPost = false;
    }
  }
}
