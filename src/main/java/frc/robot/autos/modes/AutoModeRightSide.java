package frc.robot.autos.modes;

public class AutoModeRightSide extends AutoStringAuto {
    public AutoModeRightSide() { 
        super( 
              // Move from S1 to Reef J, 1s into the path Elevator will go to L4 (the path accounts for this and is slower near the end), then Shoot Coral
              "[ ( S 1 R J ) ( Wait 1 Elevator L4 ) ] CShoot " +   

              // Elevator to  Ground, 0.2s after this has begun go to Coral Station (bot is stable even at L3, so 0.2s is enough)
              "[ ( Elevator Ground ) ( Wait 0.2 CS 1 ) ] " +              

              // Wait 0.1s at Coral Station to intake coral, then go to Reef K with the same parallel elevator logic as above, then Shoot Coral
              "Wait 0.1 [ ( R K ) ( Wait 1.5 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.2 CS 1 ) ] " +          

              // Wait 0.1s at Coral Station, go to Reef L, Shoot Coral
              "Wait 0.1 [ ( R L ) ( Wait 1.5 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.2 CS 1 ) ] " +      

              // Wait 0.1s at Coral Station, go to Reef A, Shoot Coral
              "Wait 0.1 [ ( R A ) ( Wait 1.5 Elevator L4 ) ] CShoot " +

              // Elevator to Ground
              "Elevator Ground"
              );
    }
}

