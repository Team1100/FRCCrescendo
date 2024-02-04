// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.testingdashboard;

import edu.wpi.first.util.sendable.Sendable;

/** Add your docs here. */
public class TDSendable implements TDValue {

    private Sendable m_val;
    private String m_tabName;
    private String m_groupName;
    private String m_dataName;
    
    public TDSendable(SubsystemBase subsystem, String groupName, String dataName, Sendable val) {
        m_tabName = subsystem.getName();
        m_groupName = groupName;
        m_dataName = dataName;
        m_val = val;
        TestingDashboard.getInstance().registerSendable(m_tabName, m_groupName, m_dataName, m_val);
    }

    public void post()
    {
        // sendables post themselves
    }
}

