// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.Constants;
import frc.robot.testingdashboard.SubsystemBase;
import frc.robot.testingdashboard.TDNumber;
import frc.robot.testingdashboard.TDString;

/** Add your docs here. */
public class NoteProximitySensor {
    private enum State {
        c_State_No_Note,
        c_State_First_Leg,
        c_State_Middle_Hole,
        c_State_Second_Leg
    };
    private DigitalInput m_sensorDIO;
    private State m_state;

    private TDString m_tdState;
    private TDNumber m_testingState;
    private TDNumber m_debounceIncrementer;

    public NoteProximitySensor(int dioPort, SubsystemBase subsystem) {
        m_sensorDIO = new DigitalInput(dioPort);
        m_state = State.c_State_No_Note;
        String sensorName = subsystem.getName() + "NoteSensor";
        m_tdState = new TDString(subsystem, "Sensors", sensorName);
        m_testingState = new TDNumber(subsystem, "Sensors", "SensorValue", 0);
        m_debounceIncrementer = new TDNumber(subsystem, "Sensors", sensorName+"Debounce");
    }

    public void update(){
        boolean val = !m_sensorDIO.get();
        if(RobotBase.isSimulation()){
            val = m_testingState.get()!=0;
        }
        State nextState = State.c_State_No_Note;

        switch (m_state) {
            case c_State_No_Note:
                nextState = State.c_State_No_Note;
                if(val){
                    incrementDebounce();
                } else {
                    resetDebounce();
                }
                if(debounceComplete()) {
                    resetDebounce();
                    nextState = State.c_State_First_Leg;
                }
                break;

            case c_State_First_Leg:
                nextState = State.c_State_First_Leg;
                if(val){
                    resetDebounce();
                } else {
                    incrementDebounce();
                }
                if(debounceComplete()) {
                    resetDebounce();
                    nextState = State.c_State_Middle_Hole;
                }
                break;

            case c_State_Middle_Hole:
                nextState = State.c_State_Middle_Hole;
                if(val){
                    incrementDebounce();
                } else {
                    resetDebounce();
                }
                if(debounceComplete()) {
                    nextState = State.c_State_Second_Leg;
                    resetDebounce();
                }
                break;

            case c_State_Second_Leg:
                nextState = State.c_State_Second_Leg;
                if(val){
                    resetDebounce();
                } else {
                    incrementDebounce();
                }
                if(debounceComplete()) {
                    nextState = State.c_State_No_Note;
                    resetDebounce();
                }
                break;

            default:
                System.out.println("Note Proximity Sensor has invalid state");
                nextState = State.c_State_No_Note;
                break;
        }

        m_state = nextState;
        updateTD();
    }

    public boolean hasNote() {
        return m_state != State.c_State_No_Note;
    }

    public boolean noteIsCentered() { return m_state == State.c_State_Middle_Hole; }

    public void updateTD(){
        switch (m_state) {
            case c_State_No_Note:
                m_tdState.set("No Note Detected");
                break;
            case c_State_First_Leg:
                m_tdState.set("HasNote:FirstLeg");
                break;
            case c_State_Middle_Hole:
                m_tdState.set("HasNote:CenterHole");
                break;
            case c_State_Second_Leg:
                m_tdState.set("HasNote:SecondLeg");
                break;
            default:
                m_tdState.set("Unknown State");
                break;
        }
    }

    private void incrementDebounce(){
        m_debounceIncrementer.set(m_debounceIncrementer.get() + 1);
    }

    private boolean debounceComplete(){
        return m_debounceIncrementer.get() >= Constants.kSensorDebounceCycles;
    }

    private void resetDebounce(){
        m_debounceIncrementer.set(0);
    }
}
