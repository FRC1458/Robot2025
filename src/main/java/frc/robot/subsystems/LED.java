package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Random;
import static edu.wpi.first.units.Units.*;

public class LED {
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;
    private int count;
    private boolean even;
    private long timer;
    Random random;
    
    public LED() {
        led = new AddressableLED(8);
        ledBuffer = new AddressableLEDBuffer(Constants.LED.ledLength);
        Random random = new Random();
        count = 100; //arbritrary positive number
        even = true; //can be false, doens't matter
        timer = System.currentTimeMillis();
        led.setLength(ledBuffer.getLength());
        led.setData(ledBuffer);
        led.start();
    }

    public void rainbowPulse() {
        for(int i = Constants.LED.ledStart; i < 53; i++) {
          ledBuffer.setHSV(i, (53 + count - i) % 180 , 255, 255);
        }
        for(int i = 53; i < Constants.LED.ledEnd; i++) {
          ledBuffer.setHSV(i, (count + i - 53) % 180 , 255, 255);
        }
        count++;
        led.setData(ledBuffer);
    }

    public void ChristmasStream() {
        int red = 0, green = 0,blue = 0;
        for(int i = Constants.LED.ledStart; i < Constants.LED.ledEnd; i++) {
            if(i % 53 < 26) {
                red = 255;
                green = 0;
                blue = 0;
            }
            else {
                red = 0;
                green = 255;
                blue = 0;
            }

            if(timer < System.currentTimeMillis()) {
                timer = System.currentTimeMillis() + 100;
                count++;
            }
            ledBuffer.setRGB((i + count) % 108, red, green ,blue);
        }
          led.setData(ledBuffer);
    }

    public void epilepsy() {
        for (int i = Constants.LED.ledStart; i < Constants.LED.ledEnd; i++) {
            ledBuffer.setHSV(i,random.nextInt(256),255,255);
        }
    }

    public void alternatingFlash(int h1, int h2, int delay) { // delay in milliseconds
        for (int i = Constants.LED.ledStart; i < Constants.LED.ledEnd; i++) {
            if ((i % 2 == 0) == even) {
                ledBuffer.setHSV(i,h1,255,255);
            } else {
                ledBuffer.setHSV(i,h2,255,255);
            }
            if(timer < System.currentTimeMillis()) {
                timer = System.currentTimeMillis() + delay;
                even = !even;
            }
        }
    }


    public void random() {
        setSolidColor((int)(Math.random() * 50) + 100, (int)(Math.random() * 50) + 100, (int)(Math.random() * 50) + 100);
    }
    
    public void yellow() {
        setSolidColor(255, 255, 0);
    }

    public void red() {
        setSolidColor(255, 0, 0);
    }

    public void pink() {
        setSolidColor(200, 50, 50);
    }

    public void white() {
        setSolidColor(255, 255, 255);
    }

    public void orange() {
        setSolidColor(255, 50, 0);
    }

    public void green() {
        setSolidColor(0, 255, 0);
    }

    public void lightBlue() {
        setSolidColor(0, 255, 255);
    }

    public void blue() {
        setSolidColor(0, 0, 255);
    }

    public void purple() {
        setSolidColor(255,0, 255);
    }

    public void setSolidColor(int r, int g, int b) {
        for(int i = Constants.LED.ledStart; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, r, g, b);
        }
        led.setData(ledBuffer);
    }

    
}

 