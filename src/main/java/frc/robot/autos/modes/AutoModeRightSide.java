package frc.robot.autos.modes;

public class AutoModeRightSide extends AutoStringAuto {
    public AutoModeRightSide() { 
        //Old Right Side Path
        /*super("S 1 " + 
                "[ ( R 1l ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground " + 
                "CS 1 Wait 1 " + 
                "[ ( R 2r ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground " +
                "CS 1 Wait 1 " + 
                "[ ( R 2l ) ( Wait 1 Elevator L4 ) ] Wait 1 CShoot Wait 1 Elevator Ground ");
    */
        //Currently allocating 0.75s to extend elevator (built into path file), 0.2s to retract elevator after scoring, and 0.1s to intake coral, values pobably need to change after testing
        //Currently takes 15.4s to run (in simulation)
        //Need to test paths on actual robot, then add elevator and shooter, then test if those values work

        super("[ Wait 15 ( " +      //timer to test if path is within 15s
              "S 1 R J " +          //Go to Reef J
              "Wait 0.2 CS 1 " +   //Retract Elevator, Go to Coral Station
              "Wait 0.1 R K " +     //Intake coral, Go to Reef K
              "Wait 0.2 CS 1 " +    //Retract Elevator, Go to Coral Station
              "Wait 0.1 R L " +     //Intake coral, Go to Reef L
              "Wait 0.2 CS 1 " +    //Retract Elevator, Go to Coral Station
              "Wait 0.1 R A " +     //Intake coral, Go to Reef A
              ") ]");

        //want to have shooter shoot at 14.95 seconds (or around that) to "hail mary" it.
    }
}

