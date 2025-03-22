package frc.robot.autos.modes;

public class AutoModeLeftSide extends AutoStringAuto {
    public AutoModeLeftSide() {
              super( 
              // Move from S3 to Reef E, 1s into the path Elevator will go to L4 (the path accounts for this and is slower near the end), then Shoot Coral
              "[ ( S 3 R E ) ( Wait 1 Elevator L4 ) ] CShoot " +   

              // Elevator to  Ground, 0.1s after this has begun go to Coral Station (bot is stable even at L3, so 0.1s is enough)
              "[ ( Elevator Ground ) ( Wait 0.1 CS 2 ) ] " +

              // Intake coral, then go to Reef D with the same parallel elevator logic as above, then Shoot Coral
              "CIntake [ ( R D ) ( Wait 1 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.1 CS 2 ) ] " +

              // Intake coral, go to Reef C, Shoot Coral
              "CIntake [ ( R C ) ( Wait 1 Elevator L4 ) ] CShoot " +

              // Elevator to Ground, go to Coral Station
              "[ ( Elevator Ground ) ( Wait 0.1 CS 2 ) ] " +

              // Intake coral, go to Reef B, Shoot Coral
              "CIntake [ ( R B ) ( Wait 2 Elevator L4 ) ] CShoot " +

              // Elevator to Ground
              "Elevator Ground"
              );
    }
}
