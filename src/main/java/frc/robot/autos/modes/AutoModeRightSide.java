package frc.robot.autos.modes;

public class AutoModeRightSide extends AutoStringAuto {
    public AutoModeRightSide() { 
        super( 
              // Move from S1 to Reef J, 1s into the path Elevator will go to L4 (the path accounts for this and is slower near the end), then Shoot Coral
              "[ ( S 1 R J ) ( Wait 1 Elevator L4 ) ] CShoot " +   

              // Elevator to  Ground, 0.1s after this has begun go to Coral Station (bot is stable even at L3, so 0.1s is enough)
              "[ ( Elevator Ground ) ( Wait 0.1 CS 1 ) ] " +              

              // Intake coral, then go to Reef K with the same parallel elevator logic as above, then Shoot Coral
              "CIntake [ ( R K ) ( Wait 1 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.1 CS 1 ) ] " +          

              // Intake coral, go to Reef L, Shoot Coral
              "CIntake [ ( R L ) ( Wait 1 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.1 CS 1 ) ] " +      

              // Intake coral, go to Reef A, Shoot Coral
              "CIntake [ ( R A ) ( Wait 2 Elevator L4 ) ] CShoot " +

              // Elevator to Ground
              "Elevator Ground"
              );
    }
}

