package frc.robot.autos.modes;

public class AutoModeLeftSide extends AutoStringAuto {
    public AutoModeLeftSide() {
        super( 
            // Move from S3 to Reef E, 1s into the path Elevator will go to L4 (the path accounts for this and is slower near the end), then Shoot Coral
            "[ ( S 3 R E ) ( Wait 1.4 Elevator L4 ) ] CShoot Wait 0.2 " +   

            // Elevator to  Ground, 0.2s after this has begun go to Coral Station (bot is stable even at L3, so 0.2s is enough)
            "[ ( Elevator Ground ) ( CS 2 ) ] CIntake " +              

            // Wait 0.1s at Coral Station to intake coral, then go to Reef D with the same parallel elevator logic as above, then Shoot Coral
            "[ ( R D ) ( Wait 1.35 Elevator L4 ) ] CShoot Wait 0.2 " +

            // Elevator to Ground, go to Coral Station
            "[ ( Elevator Ground ) ( CS 2 ) ] CIntake " +          

            // Wait 0.1s at Coral Station, go to Reef C, Shoot Coral
            "[ ( R C ) ( Wait 1.35 Elevator L4 ) ] CShoot Wait 0.2 " +

            // Elevator to Ground, go to Coral Station
            //"[ ( Elevator Ground ) ( Wait 0.2 CS 2 ) ] " +      

            // Wait 0.1s at Coral Station, go to Reef B, Shoot Coral
            //"Wait 0.1 [ ( R B ) ( Wait 1.5 Elevator L4 ) ] CShoot " +

            // Elevator to Ground
            "Elevator Ground"
        );
    }
}
