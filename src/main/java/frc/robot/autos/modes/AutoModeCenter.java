package frc.robot.autos.modes;

public class AutoModeCenter extends AutoStringAuto {
    public AutoModeCenter() { 
        super( 
            // Move to R 6l (4.65s path), Elevator has to start in parralell to not hit reef bars, but path accel is MUCH lower than even snap to tag so its fine  
            "[ ( S 2 R 6l ) ( Wait 4 Elevator L4 ) ] CShoot Wait 0.2 " +   

            // Elevator to  Ground, Wait (so we don't hit a robot in auto), then go to location thats near-ish the coral station but also wont inturrupt a robot doing an auto)
            "Elevator Ground Wait 1 ");
}}
