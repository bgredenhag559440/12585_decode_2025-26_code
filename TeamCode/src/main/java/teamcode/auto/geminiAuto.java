package teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on encoder counts,
 * modified to utilize the structure for a four-wheel strafe-capable robot (Mecanum drive motors).
 * The goal is to drive forward 5 inches using the RUN_TO_POSITION motor control mode.
 *
 * NOTE: The 'encoderDrive' method is modified to take 4 wheel distances (LF, LR, RF, RR)
 * for Mecanum/4-wheel drive.
 *
 * NOTE 2: The hardware map includes declarations for odometry pods for future positional
 * tracking, though the initial move uses the drive motor encoders as required by RUN_TO_POSITION.
 */

@Autonomous(name = "Gemeni Auto", group = "Robot")
//@Disabled
public class geminiAuto extends LinearOpMode { // Renamed class for clarity

    /* Declare OpMode members for Drive Motors. */
    private DcMotor leftFrontDrive = null;
    private DcMotor leftRearDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightRearDrive = null;

    /* Declare OpMode members for Odometry Pods (Dead Wheels).
     * NOTE: Declared as DcMotor to read encoder counts, assuming they are configured as such
     * in the hardware map for easy access to getCurrentPosition().
     * These are based on the names in CrusaderHardware.
     */

    private final ElapsedTime runtime = new ElapsedTime();

    // Constant declarations
    // NOTE: COUNTS_PER_INCH is calculated for the drive wheels in the original file,
    // which is suitable for the RUN_TO_POSITION *drive motor* command.
    static final double COUNTS_PER_MOTOR_REV = 2000;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 1.89; // Odometry pod wheel diameter
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415926535); // Using more precise Pi value
    // counts per inch is now ~336.87
    static final double DRIVE_SPEED = 0.5; // Increased speed for a test drive
    static final double TURN_SPEED = 0.5;
    static final double STRAFE_SPEED = 0.5;

    @Override
    public void runOpMode() {

        // --- Hardware Initialization ---

        // Initialize the drive system motors, using names from newAuto.java and CrusaderHardware.java
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        leftRearDrive = hardwareMap.get(DcMotor.class, "leftRearDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        rightRearDrive = hardwareMap.get(DcMotor.class, "rightRearDrive");

        // Initialize the odometry dead wheel encoders (for positional tracking/future use)
        // These are declared in CrusaderHardware.java, using the same names.
        // If these were configured as a different sensor type in the FTC config, this would need adjustment.

        // --- Drive Motor Direction Configuration (for Mecanum Strafe) ---
        // This direction setting is crucial for the robot to move straight and strafe correctly.
        // Assuming a standard configuration where left motors are reversed relative to right motors for forward movement.
        // (Note: This is different from the directions in the provided CrusaderHardware.java)
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);  // Reverse direction to move forward
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);   // Reverse direction to move forward
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD); // Forward direction
        rightRearDrive.setDirection(DcMotor.Direction.REVERSE);  // Forward direction

        // --- Encoder Configuration ---

        // Stop and Reset drive motor encoders. This ensures a clean start for RUN_TO_POSITION.
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Reset odometry encoders for positional tracking (not used for this specific RUN_TO_POSITION move, but good practice)

        // Set drive motors to RUN_USING_ENCODER mode initially (they will be switched to RUN_TO_POSITION later)
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set odometry encoders to RUN_WITHOUT_ENCODER mode, as they are only used for reading position.

        // Send telemetry message to indicate successful Encoder reset and initial positions
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.addData("Drive Motors Start Pos", "LF:%7d LR:%7d RF:%7d RR:%7d",
                leftFrontDrive.getCurrentPosition(),
                leftRearDrive.getCurrentPosition(),
                rightFrontDrive.getCurrentPosition(),
                rightRearDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // --- Autonomous Path Execution ---

        // STEP 1: Drive Forward 5.0 Inches using the encoderDrive method
        // For Mecanum/4-wheel drive to move straight, all wheel distances must be equal and positive.
        //encoderDrive(DRIVE_SPEED, 5.0, 5.0, 5.0, 5.0, 0.5);  // S1: Forward 5 Inches with .8 Sec timeout
        //encoderDrive(DRIVE_SPEED, -5.0, -5.0, -5.0, -5.0, 0.5); //backwards 5 inches with .8 second timeout

        encoderDrive(STRAFE_SPEED, strafe("right", 0.8));
        encoderDrive(STRAFE_SPEED, strafe("left", 0.8));
        encoderDrive(TURN_SPEED, -5.0, -5.0, 5.0, 5.0, 0.5);//right front is previously negative so its a double negative which is why its put as negative but it acts like its positive

        // The original desired path:
        // encoderDrive(DRIVE_SPEED, 48, 48, 48, 48, 5.0);  // S1: Forward 48 Inches with 5 Sec timeout
        // encoderDrive(TURN_SPEED, 12, 12, -12, -12, 4.0);  // S2: Turn Right 12 Inches (Requires negative distance for right motors)
        // encoderDrive(DRIVE_SPEED, -24, -24, -24, -24, 4.0);  // S3: Reverse 24 Inches (Requires negative distance)

        // Stop and pause
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // Pause to display final telemetry message.
    }

    public double[] strafe(String direction, double timeoutS) {
        double[] values = new double[5];
        if (direction.equalsIgnoreCase("left")) {
            values = new double[]{-1.0, 1.0, 1.0, -1.0, timeoutS};
        } else if (direction.equalsIgnoreCase("right")) {
            values = new double[]{1.0, -1.0, -1.0, 1.0, timeoutS};
        }
        return values;
    }

    /*
     * Method to perform a relative move, based on drive motor encoder counts (RUN_TO_POSITION).
     * This method assumes a Mecanum/4-wheel drive.
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

            // Sets target position for each wheel
            leftFrontDrive.setTargetPosition(leftFrontTarget);
            leftRearDrive.setTargetPosition(leftRearTarget);
            rightFrontDrive.setTargetPosition(rightFrontTarget);
            rightRearDrive.setTargetPosition(rightRearTarget);

            // Turn On RUN_TO_POSITION mode
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion with the absolute value of speed.
            runtime.reset();
            leftFrontDrive.setPower(Math.abs(speed));
            leftRearDrive.setPower(Math.abs(speed));
            rightFrontDrive.setPower(Math.abs(speed));
            rightRearDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, there is time left, and ALL motors are running to position.
            // Using `isBusy()` is the correct way to check if RUN_TO_POSITION has completed.
            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (leftFrontDrive.isBusy() && leftRearDrive.isBusy() &&
                            rightFrontDrive.isBusy() && rightRearDrive.isBusy()))
            {
                // Display current drive motor and odometry pod positions for the driver.
                telemetry.addData("Target Pos", " LF:%7d LR:%7d RF:%7d RR:%7d", leftFrontTarget, leftRearTarget,
                        rightFrontTarget, rightRearTarget);
                telemetry.addData("Current Drive Pos", " LF:%7d LR:%7d RF:%7d RR:%7d",
                        leftFrontDrive.getCurrentPosition(), leftRearDrive.getCurrentPosition(),
                        rightFrontDrive.getCurrentPosition(), rightRearDrive.getCurrentPosition());
//                telemetry.addData("Runtime: ", "%7d", runtime.seconds());
                telemetry.update();
            }

            // Stop all motion;
            leftFrontDrive.setPower(0);
            leftRearDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightRearDrive.setPower(0);

            // Turn off RUN_TO_POSITION and reset encoders for the next move.
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Set motors back to RUN_USING_ENCODER mode for the next potential move.
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(1000);   // Optional pause after each move.
        }
    }
    public void encoderDrive(double speed, double[] strafePatterns) {
        int leftFrontTarget;
        int leftRearTarget;
        int rightFrontTarget;
        int rightRearTarget;



        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            leftFrontTarget = leftFrontDrive.getCurrentPosition() + (int) (strafePatterns[0] * COUNTS_PER_INCH);
            leftRearTarget = leftRearDrive.getCurrentPosition() + (int) (strafePatterns[1] * COUNTS_PER_INCH);
            rightFrontTarget = rightFrontDrive.getCurrentPosition() + (int) (strafePatterns[2] * COUNTS_PER_INCH);
            rightRearTarget = rightRearDrive.getCurrentPosition() + (int) (strafePatterns[3] * COUNTS_PER_INCH);

            // Sets target position for each wheel
            leftFrontDrive.setTargetPosition(leftFrontTarget);
            leftRearDrive.setTargetPosition(leftRearTarget);
            rightFrontDrive.setTargetPosition(rightFrontTarget);
            rightRearDrive.setTargetPosition(rightRearTarget);

            // Turn On RUN_TO_POSITION mode
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion with the absolute value of speed.
            runtime.reset();
            leftFrontDrive.setPower(Math.abs(speed));
            leftRearDrive.setPower(Math.abs(speed));
            rightFrontDrive.setPower(Math.abs(speed));
            rightRearDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, there is time left, and ALL motors are running to position.
            // Using `isBusy()` is the correct way to check if RUN_TO_POSITION has completed.
            while (opModeIsActive() && (runtime.seconds() < strafePatterns[4]) &&
                    (leftFrontDrive.isBusy() && leftRearDrive.isBusy() &&
                            rightFrontDrive.isBusy() && rightRearDrive.isBusy()))
            {
                // Display current drive motor and odometry pod positions for the driver.
                telemetry.addData("Target Pos", " LF:%7d LR:%7d RF:%7d RR:%7d", leftFrontTarget, leftRearTarget,
                        rightFrontTarget, rightRearTarget);
                telemetry.addData("Current Drive Pos", " LF:%7d LR:%7d RF:%7d RR:%7d",
                        leftFrontDrive.getCurrentPosition(), leftRearDrive.getCurrentPosition(),
                        rightFrontDrive.getCurrentPosition(), rightRearDrive.getCurrentPosition());
//                telemetry.addData("Runtime: ", "%7d", runtime.seconds());
                telemetry.update();
            }

            // Stop all motion;
            leftFrontDrive.setPower(0);
            leftRearDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightRearDrive.setPower(0);

            // Turn off RUN_TO_POSITION and reset encoders for the next move.
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Set motors back to RUN_USING_ENCODER mode for the next potential move.
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(1000);   // Optional pause after each move.
        }
    }
}
