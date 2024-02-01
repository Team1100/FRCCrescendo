// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.testingdashboard.SubsystemBase;

public class Lights extends SubsystemBase {
  public static Lights m_lights;

  private AddressableLED m_LED;
  private AddressableLEDBuffer m_LEDBuffer;

  private int m_rainbowFirstPixelHue;
  private int m_OrangeFirstPixelValue;
  private Timer m_timer;

  /** Creates a new Lights. */
  public Lights() {
    super("Lights");

    m_LED = new AddressableLED(RobotMap.L_LEDS);

    m_LEDBuffer = new AddressableLEDBuffer(Constants.LED_LENGTH);
    m_LED.setLength(m_LEDBuffer.getLength());

    m_LED.setData(m_LEDBuffer);
    m_LED.start();

    m_rainbowFirstPixelHue = 0;
    m_OrangeFirstPixelValue = 0;
    
    m_timer = new Timer();
    m_timer.start();
    m_timer.reset();
  }

  public static Lights getInstance() {
    if (m_lights == null) {
      m_lights = new Lights();
    }
    return m_lights;
  }

  public AddressableLED getLED() {
    return m_LED;
  }

  public AddressableLEDBuffer getLEDBuffer() {
    return m_LEDBuffer;
  }

  public void enableLights() {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      m_LEDBuffer.setHSV(i, 12, 255, 122);
    }

    m_LED.setData(m_LEDBuffer);
  }

  public void disableLights() {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      m_LEDBuffer.setLED(i, Color.kBlack);
    }

    m_LED.setData(m_LEDBuffer);
  }

  public void makeRainbow() {
    // For every pixel
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_LEDBuffer.getLength())) % 180;
      // Set the value
      m_LEDBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
  }

  public void moveLights() {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      final var value = (m_OrangeFirstPixelValue + (i * 255 / m_LEDBuffer.getLength())) % 255;
      m_LEDBuffer.setHSV(i, 10, 255, value);
    }
    
    // what "moves" the program
    m_OrangeFirstPixelValue += 8;
    m_OrangeFirstPixelValue %= 255;
  }

  public void blinkLights() {
    for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
      m_LEDBuffer.setHSV(i, 10, 255, m_OrangeFirstPixelValue);
    }

    m_OrangeFirstPixelValue = (int)(Math.sin(m_timer.get() * 5) * 128 + 128);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
