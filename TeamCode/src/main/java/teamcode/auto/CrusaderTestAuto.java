package teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Test Autonomous", group="Robot")
//    @Disabled
public class CrusaderTestAuto extends LinearOpMode {


    /* Declare OpMode members. */

    CrusaderTestAuto robot = new CrusaderTestAuto(); //alternative to initializing everything separately in the autonomous

    private DcMotor         leftFrontDrive;
    private DcMotor         rightFrontDrive;
    private DcMotor         leftRearDrive;
    private DcMotor         rightRearDrive;

    private ElapsedTime runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as
    // needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 2000 ;    // old 480 eg: TETRIX Motor Encoder //1300
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // old 1.0 No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 1.89 ;     // old 4 For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.4;      //decrese and increse power

    @Override
    public void runOpMode() {

        robot.init(hardwareMap); //alternative to initializing everything separately in the autonomous

        // Initialize the drive system variables.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        leftRearDrive  = hardwareMap.get(DcMotor.class, "leftRearDrive");
        rightRearDrive = hardwareMap.get(DcMotor.class, "rightRearDrive");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips

        // The configuration of forwards/reverse is based on the orientation of the wheels
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);//forward
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);//reverse
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);//forward
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);//reverse

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at",  "%7d :%7d",
                leftFrontDrive.getCurrentPosition(),
                rightFrontDrive.getCurrentPosition(),
                leftRearDrive.getCurrentPosition(),
                rightRearDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // encoderDrive(DRIVE_SPEED,-13,5,"strafe left");
        //robot.bottomCollectorWholeSpin.setPosition(.4);
        //robot.bottomCollectorSpinChain.setPosition(2);
        Drive(DRIVE_SPEED,
                10, 30, "goFar");

        sleep(1000);
        //turn(DRIVE_SPEED, 1, 5, "turnleft");
        // sleep(200);
        //Drive(DRIVE_SPEED, 10, 5, "driveForward");

        // encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
//            encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout
//encoderDrive(DRIVE_SPEED, leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        //rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        //leftRearDrive.setDirection(DcMotor.Direction.FORWARD);
        //  rightRearDrive.setDirection(DcMotor.Direction.REVERSE); );
        telemetry.addData("Path", "Complete");
        telemetry.update();

        sleep(1000);  // pause to display final telemetry message.
    }

    private void init(HardwareMap hardwareMap) {
    }



    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */

    public void Strafe (double speed, double distance, double timeoutS,String message) {

        int newTargetF;

        int newTargetB;

        robot.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int avgFront = (robot.leftFrontDrive.getCurrentPosition() + robot. rightFrontDrive.getCurrentPosition()) / 2;

        int avgBack = (robot.leftRearDrive.getCurrentPosition() + robot.rightRearDrive.getCurrentPosition()) / 2;

        if (opModeIsActive()) {

            newTargetF = avgFront + (int) (distance * COUNTS_PER_INCH);

            newTargetB = avgBack - (int) (distance * COUNTS_PER_INCH);

            robot.leftFrontDrive.setTargetPosition(newTargetF);

            robot.rightFrontDrive.setTargetPosition(newTargetF);

            robot.leftRearDrive.setTargetPosition(newTargetF);

            robot.rightRearDrive.setTargetPosition(newTargetF);


            // Turn On RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.

            runtime.reset();

            robot.leftFrontDrive.setPower(Math.abs(speed));

            robot.rightFrontDrive.setPower(Math.abs(speed));

            robot.leftRearDrive.setPower(Math.abs(speed));

            robot.rightRearDrive.setPower(Math.abs(speed));



            while (opModeIsActive() &&

                    (runtime.seconds() < timeoutS) &&

                    (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy()) && (robot.leftRearDrive.isBusy()) && (robot.rightRearDrive.isBusy())
                //coding is stupid and for some reason the robot is liking crashing into the wall

            ) {

                telemetry.addData(message,timeoutS);

                telemetry.addData("Front Left ",robot.leftFrontDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftFrontDrive.getTargetPosition());

                telemetry.addData("Front Right ",robot.rightFrontDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightFrontDrive.getTargetPosition());

                telemetry.addData("Back Left ",robot.leftRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftRearDrive.getTargetPosition());

                telemetry.addData("Back Right ",robot.rightRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightRearDrive.getTargetPosition());


                telemetry.update();


            }



            // Stop all motion;

            robot.leftFrontDrive.setPower(0);

            robot.rightFrontDrive.setPower(0);

            robot.leftRearDrive.setPower(0);

            robot.rightRearDrive.setPower(0);


            // Turn off RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            sleep(250);

        }

    }



    public void Drive(double speed, double distance, double timeoutS,String message) {

        int newTargetL;

        int newTargetR;

        robot.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int avgLeft = (robot.leftFrontDrive.getCurrentPosition() + robot.leftRearDrive.getCurrentPosition()) / 2;

        int avgRight = (robot.rightFrontDrive.getCurrentPosition() + robot.rightRearDrive.getCurrentPosition()) / 2;

        if (opModeIsActive()) {

            newTargetL = avgLeft - (int) (distance * COUNTS_PER_INCH);

            newTargetR = avgRight + (int) (distance * COUNTS_PER_INCH);

            robot.leftFrontDrive.setTargetPosition(-1*newTargetR);

            robot.rightFrontDrive.setTargetPosition(newTargetR);

            robot.leftRearDrive.setTargetPosition(-1*newTargetR);

            robot.rightRearDrive.setTargetPosition(newTargetR);



            // Turn On RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.

            runtime.reset();

            robot.leftFrontDrive.setPower(-1*Math.abs(speed));

            robot.rightFrontDrive.setPower(Math.abs(speed));

            robot.leftRearDrive.setPower(-1*Math.abs(speed));

            robot.rightRearDrive.setPower(Math.abs(speed));


            while (opModeIsActive() &&

                    (runtime.seconds() < timeoutS) &&

                    (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy())) {

                telemetry.addData(message,timeoutS);

                telemetry.addData("Front Left ",robot.leftFrontDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftFrontDrive.getTargetPosition());

                telemetry.addData("Front Right ",robot.rightFrontDrive. getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightFrontDrive.getTargetPosition());

                telemetry.addData("Back Left ",robot.leftRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftRearDrive.getTargetPosition());

                telemetry.addData("Back Right ",robot.rightRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightRearDrive.getTargetPosition());


                telemetry.update();


            }


            // Stop all motion;

            robot.leftFrontDrive.setPower(0);

            robot.rightFrontDrive.setPower(0);

            robot.leftRearDrive.setPower(0);

            robot. rightRearDrive.setPower(0);


            // Turn off RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            sleep(250);

        }

    }
    public void turn(double speed, double distance, double timeoutS,String message) {

        int newTargetL;

        int newTargetR;

        int avgLeft = (robot.leftFrontDrive.getCurrentPosition() + robot.leftRearDrive.getCurrentPosition()) / 2;

        int avgRight = (robot.rightFrontDrive.getCurrentPosition() + robot.rightRearDrive.getCurrentPosition()) / 2;

        if (opModeIsActive()) {

            newTargetL = avgLeft + (int) (distance * COUNTS_PER_INCH);

            newTargetR = avgRight + (int) (distance * COUNTS_PER_INCH);

            robot.leftFrontDrive.setTargetPosition(newTargetL);

            robot.rightFrontDrive.setTargetPosition(-1*newTargetL);

            robot.leftRearDrive.setTargetPosition(newTargetL);

            robot.rightRearDrive.setTargetPosition(-1*newTargetL);

            // Turn On RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.

            runtime.reset();

            robot.leftFrontDrive.setPower(Math.abs(speed));

            robot.rightFrontDrive.setPower(-1*Math.abs(speed));

            robot.leftRearDrive.setPower(Math.abs(speed));

            robot.rightRearDrive.setPower(-1*Math.abs(speed));


            while (opModeIsActive() &&

                    (runtime.seconds() < timeoutS) &&

                    (robot.leftFrontDrive.isBusy() && robot.rightFrontDrive.isBusy())) {

                telemetry.addData(message,timeoutS);

                telemetry.addData("Front Left ",robot.leftFrontDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftFrontDrive.getTargetPosition());

                telemetry.addData("Front Right ",robot.rightFrontDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightFrontDrive.getTargetPosition());

                telemetry.addData("Back Left ",robot.leftRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.leftRearDrive.getTargetPosition());

                telemetry.addData("Back Right ",robot.rightRearDrive.getCurrentPosition());

                telemetry.addData("TARGET:",robot.rightRearDrive.getTargetPosition());


                telemetry.update();


            }



            // Stop all motion;

            robot.leftFrontDrive.setPower(0);

            robot.rightFrontDrive.setPower(0);

            robot.leftRearDrive.setPower(0);

            robot.rightRearDrive.setPower(0);


            // Turn off RUN_TO_POSITION

            robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            sleep(250);

        }

    }
}



