// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

/** Add your docs here. */
public class TDNumber implements TDValue {

    private double m_val;
    private String m_tabName;
    private String m_groupName;
    private String m_dataName;
    private boolean m_needsPost = false;
    
    public TDNumber(SubsystemBase subsystem, String groupName, String dataName) {
      m_val = 0.0;
      m_tabName = subsystem.getName();
      m_groupName = groupName;
      m_dataName = dataName;
      TestingDashboard.getInstance().registerNumber(m_tabName, groupName, dataName, m_val);
      subsystem.registerValue(this);
      get();
    }

    public TDNumber(SubsystemBase subsystem, String groupName, String dataName, double val) {
      m_val = val;
      m_tabName = subsystem.getName();
      m_groupName = groupName;
      m_dataName = dataName;
      TestingDashboard.getInstance().registerNumber(m_tabName, m_groupName, m_dataName, m_val);
      subsystem.registerValue(this);
      set(val);
    }

    public void set(double val)
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
    public double get() {
      if (!m_needsPost) {
        m_val = TestingDashboard.getInstance().getNumber(m_tabName, m_dataName);
      }
      return m_val;
    }

    public void post()
    {
      if (m_needsPost)
      {
        TestingDashboard.getInstance().updateNumber(m_tabName, m_dataName, m_val);
        m_needsPost = false;
      }
    }
}
