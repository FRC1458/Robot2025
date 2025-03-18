package frc.robot.autos.modes;

public class AutoModeLeftSide extends AutoStringAuto {
    public AutoModeLeftSide() {
        /*super("S 4 " + 
                "[ ( R 5r ) ( Wait 1.2 Elevator L4 ) ] Wait 0.3 CShoot Wait 0.5 Elevator Ground " + 
                "CS 2 Wait 1 " + 
                "[ ( R 4l ) ( Wait 1 Elevator L4 ) ] Wait 0.3 CShoot Wait 0.5 Elevator Ground " +
                "CS 2 Wait 1 " + 
                "[ ( R 4r ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground ");*/
                super("[ Wait 15 ( " +      //timer to test if path is within 15s
              "S 3 R E " +          //Go to Reef E
              "Wait 0.2 CS 2 " +   //Retract Elevator, Go to Coral Station
              "Wait 0.1 R D " +     //Intake coral, Go to Reef D
              "Wait 0.2 CS 2 " +    //Retract Elevator, Go to Coral Station
              "Wait 0.1 R C " +     //Intake coral, Go to Reef C
              "Wait 0.2 CS 2 " +    //Retract Elevator, Go to Coral Station
              "Wait 0.1 R B " +     //Intake coral, Go to Reef B
              ") ]");
    }
}
