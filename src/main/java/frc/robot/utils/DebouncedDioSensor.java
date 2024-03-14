// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Constants;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;

/** Add your docs here. */
public class DebouncedDioSensor {
    private DigitalInput m_sensorDio;
    private TDNumber m_debounceIncrementer;
    private boolean m_output;

    TDNumber m_testingVal;

    public DebouncedDioSensor(int dioPort, SubsystemBase subsystem) {
        m_sensorDio = new DigitalInput(dioPort);
        m_debounceIncrementer = new TDNumber(subsystem, "Sensors", "DebounceIncrementer", 0);
        m_output = false;

        m_testingVal = new TDNumber(subsystem, "Sensors", "SimSensorValue");
    }

    public void update() {
        synchronized(this) {
        boolean val = !m_sensorDio.get();
        if(RobotBase.isSimulation()){
            val = m_testingVal.get() != 0;
        }

        if((val && m_output)
             || (!val && !m_output)) {
            resetDebounce();
        } else {
            incrementDebounce();
        }

        if(debounceComplete()) {
            m_output = val;
            resetDebounce();
        }
        }
    }

    public boolean get() {
        synchronized(this){
            return m_output;
        }
    }

    private void resetDebounce() {
        m_debounceIncrementer.set(0);
    }

    private void incrementDebounce() {
        m_debounceIncrementer.set(m_debounceIncrementer.get()+1);
    }

    private boolean debounceComplete() {
        return m_debounceIncrementer.get() >= (Constants.kSensorThreadingEnabled?Constants.kThreadedSensorDebounceCycles:Constants.kSensorDebounceCycles);
    }

}
