package frc.robot.autos.modes;

public class AutoModeRightSide extends AutoStringAuto {
    public AutoModeRightSide() { 
        /*super("S 1 " + 
                "[ ( R 1l ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground " + 
                "CS 1 Wait 1 " + 
                "[ ( R 2r ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground " +
                "CS 1 Wait 1 " + 
                "[ ( R 2l ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground ");
    */

        super("S 1 R J WAIT 0.2 CS 1 WAIT 0.1 R K WAIT 0.2 CS 1 WAIT 0.1 R L WAIT 0.2 CS 1 WAIT 0.1 R A");
    }
}

