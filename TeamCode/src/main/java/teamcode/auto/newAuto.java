package teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

    /*
     * This OpMode illustrates the concept of driving a path based on encoder counts.
     * The code is structured as a LinearOpMode
     *
     * The code REQUIRES that you DO have encoders on the wheels,
     *   otherwise you would use: RobotAutoDriveByTime;
     *
     *  This code ALSO requires that the drive Motors have been configured such that a positive
     *  power command moves them forward, and causes the encoders to count UP.
     *
     *   The desired path in this example is:
     *   - Drive forward for 48 inches
     *   - Spin right for 12 Inches
     *   - Drive Backward for 24 inches
     *   - Stop and close the claw.
     *
     *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
     *  that performs the actual movement.
     *  This method assumes that each movement is relative to the last stopping place.
     *  There are other ways to perform encoder based moves, but this method is probably the simplest.
     *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
     *
     * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
     * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
     */

    @Autonomous(name = "New Auto", group = "Robot")
    //@Disabled
    public class newAuto extends LinearOpMode {

        /* Declare OpMode members. */
        private DcMotor leftFrontDrive = null;
        private DcMotor leftRearDrive = null;
        private DcMotor rightFrontDrive = null;
        private DcMotor rightRearDrive = null;

        private final ElapsedTime runtime = new ElapsedTime();

        // Calculate the COUNTS_PER_INCH for your specific drive train.
        // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
        // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
        // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
        // This is gearing DOWN for less speed and more torque.
        // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
        static final double COUNTS_PER_MOTOR_REV = 2000;    // for odometry pod eg: TETRIX Motor Encoder
        static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
        static final double WHEEL_DIAMETER_INCHES = 1.89;     // for odometry pod For figuring circumference
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        //counts per inch is 336.8704733
        static final double DRIVE_SPEED = 0.1;
        static final double TURN_SPEED = 0.5;

        @Override
        public void runOpMode() {

            // Initialize the drive system variables.
            leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
            leftRearDrive = hardwareMap.get(DcMotor.class, "leftRearDrive");
            rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
            rightRearDrive = hardwareMap.get(DcMotor.class, "rightRearDrive");

            // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
            // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
            // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
            leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
            leftRearDrive.setDirection(DcMotor.Direction.REVERSE);
            rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
            rightRearDrive.setDirection(DcMotor.Direction.FORWARD);

            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Send telemetry message to indicate successful Encoder reset
            telemetry.addData("Starting at", "%7d :%7d :%7d :%7d%n",
                    leftFrontDrive.getCurrentPosition(),
                    leftRearDrive.getCurrentPosition(),
                    rightFrontDrive.getCurrentPosition(),
                    rightRearDrive.getCurrentPosition());
            telemetry.update();

            // Wait for the game to start (driver presses START)
            waitForStart();

            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)

            //makes the robot go forward
            encoderDrive(DRIVE_SPEED, 10, 10, 10, 10, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
//            encoderDrive(TURN_SPEED, 12, 12, -12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
//            encoderDrive(DRIVE_SPEED, -24, -24, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout

//            if ((leftFrontDrive.getCurrentPosition() == leftFrontTarget))

            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);  // pause to display final telemetry message.
        }

        /*
         *  Method to perform a relative move, based on encoder counts.
         *  Encoders are not reset as the move is based on the current position.
         *  Move will stop if any of three conditions occur:
         *  1) Move gets to the desired position
         *  2) Move runs out of time
         *  3) Driver stops the OpMode running.
         */
        public void encoderDrive(double speed, double leftFrontInches, double leftRearInches,
                                 double rightFrontInches, double rightRearInches, double timeoutS) {
            int leftFrontTarget;
            int leftRearTarget;
            int rightFrontTarget;
            int rightRearTarget;

            // Ensure that the OpMode is still active
            if (opModeIsActive()) {

                // Determine new target position, and pass to motor controller
                leftFrontTarget = leftFrontDrive.getCurrentPosition() + (int) (leftFrontInches * COUNTS_PER_INCH);
                leftRearTarget = leftRearDrive.getCurrentPosition() + (int) (leftRearInches * COUNTS_PER_INCH);
                rightFrontTarget = rightFrontDrive.getCurrentPosition() + (int) (rightFrontInches * COUNTS_PER_INCH);
                rightRearTarget = rightRearDrive.getCurrentPosition() + (int) (rightRearInches * COUNTS_PER_INCH);

                //Sets target position for each wheel
                leftFrontDrive.setTargetPosition(leftFrontTarget);
                leftRearDrive.setTargetPosition(leftRearTarget);
                rightFrontDrive.setTargetPosition(rightFrontTarget);
                rightRearDrive.setTargetPosition(rightRearTarget);

                // Turn On RUN_TO_POSITION
                leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                // reset the timeout time and start motion.
                runtime.reset();
                leftFrontDrive.setPower(Math.abs(speed));
                leftRearDrive.setPower(Math.abs(speed));
                rightFrontDrive.setPower(-Math.abs(speed));
                rightRearDrive.setPower(Math.abs(speed));

                // keep looping while we are still active, and there is time left, and both motors are running.
                // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
                // its target position, the motion will stop.  This is "safer" in the event that the robot will
                // always end the motion as soon as possible.
                // However, if you require that BOTH motors have finished their moves before the robot continues
                // onto the next step, use (isBusy() || isBusy()) in the loop test.

                //might need to add && (runtime.seconds() > timeoutS)
                //to the while

                //another thing that could need ti be changed is seperate the || for the front and rear drives
                //could change back to &&
                //could use ! operator
                //could change back to drive.isBusy()

                //rightFrontDrive is >= instead of <= because it is switched to negative due to funky motor
                while (opModeIsActive()  &&
                        (leftFrontDrive.getCurrentPosition() <= leftFrontTarget && leftRearDrive.getCurrentPosition() <= leftRearTarget &&
                                rightFrontDrive.getCurrentPosition() <= rightFrontTarget && rightRearDrive.getCurrentPosition() <= rightRearTarget) && timeoutS <= runtime.seconds())
                         {
                    // Display it for the driver.
                    telemetry.addData("Running to", " %7d :%7d :%7d :%7d%n", leftFrontTarget, leftRearTarget,
                            rightFrontTarget, rightRearTarget);

                    //rightFrontDrive is the only one running
                    telemetry.addData("Currently at", " %7d :%7d :%7d :%7d%n",
                            leftFrontDrive.getCurrentPosition(), leftRearDrive.getCurrentPosition(),
                            rightFrontDrive.getCurrentPosition(), rightRearDrive.getCurrentPosition());

                    //prints current positions
                    telemetry.addData("Left Front Drive: ", leftFrontDrive.getCurrentPosition());
                    telemetry.addData("Left Rear Drive: ", leftRearDrive.getCurrentPosition());
                    telemetry.addData("right Front Drive: ", rightFrontDrive.getCurrentPosition());
                    telemetry.addData("right Rear Drive: ", rightRearDrive.getCurrentPosition());

                    telemetry.update();
                }

                // Stop all motion;
                leftFrontDrive.setPower(0);
                leftRearDrive.setPower(0);
                rightFrontDrive.setPower(0);
                rightRearDrive.setPower(0);

                // Turn off RUN_TO_POSITION
                //reset encoders
                leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                //runs using encoders again
                leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                sleep(250);   // optional pause after each move.
            }

        }

/*        public void loop() {

            double drive = 0.1;
            double strafe = 1;
            double twist = 1;
            //formula to allow McKenna wheels to strafe and spin
            double[] speeds = {
                    (-drive + strafe + twist), //right front drive
                    (-drive - strafe - twist), //left front drive
                    (drive + strafe - twist), //right rear drive
                    (-drive + strafe - twist) //left rear drive
            };

            //sets the speed of each wheel
            double max = Math.abs(speeds[0]);
            if (max < Math.abs(speeds[0])) max = Math.abs(speeds[0]);
            if (max < Math.abs(speeds[1])) max = Math.abs(speeds[1]);
            if (max < Math.abs(speeds[2])) max = Math.abs(speeds[2]);
            if (max < Math.abs(speeds[3])) max = Math.abs(speeds[3]);

            if (max > 1) {
                for (int i = 0; i < speeds.length; i++) { // Correct loop condition
                    speeds[i] /= max;
                }
            }
            //assigns each wheel to an item in the speeds list
            //keep setPower because it is a DcMotor, not an audioManager
            rightFrontDrive.setPower(speeds[0]);
            leftFrontDrive.setPower(speeds[1]);
            rightRearDrive.setPower(speeds[2]);
            leftRearDrive.setPower(speeds[3]);
        }
 */
    }
