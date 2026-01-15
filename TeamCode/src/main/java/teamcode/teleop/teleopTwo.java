package teamcode.teleop;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import teamcode.hardware.CrusaderHardware;

@TeleOp(name="TeleopTwo", group="Launchbot") //is you want to duplicate change this name
//@Disabled;
public class teleopTwo<DcMotorAccess> extends OpMode {

    public DcMotor rightFrontDrive;
    public DcMotor leftFrontDrive;
    public DcMotor rightRearDrive;
    public DcMotor leftRearDrive;
    public DcMotor leftShoot;
    public DcMotor rightShoot;
    public Servo gate;
    public Servo spinner;

    public Servo rightArm;
    public Servo leftArm;

    //final double COUNTS_PER_MOTOR_REV = 480;    // eg: TETRIX Motor Encoder
    //final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    //final double WHEEL_DIAMETER_INCHES = 4;     // For figuring circumference
    //final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
    //(WHEEL_DIAMETER_INCHES * 3.1415);
    //final double DRIVE_SPEED = 2;

    CrusaderHardware robot = new CrusaderHardware(); //defines variable "robot"
    com.qualcomm.robotcore.util.ElapsedTime spinnerTimer = new com.qualcomm.robotcore.util.ElapsedTime();

    double countsPerMotorRev = 480;
    double driveGearReduction = 1.0;
    double wheelDiameterInches = 4;
    double countsPerInch = (countsPerMotorRev * driveGearReduction) / (wheelDiameterInches * 3.14159);
    double driveSpeed = 2;

    int height;

    int liftTarget;

    int driveMode= 0;

    double shootingPower = 0.5;
    int shootStep = 0; //0 = idle, 1 = shooting

    //following code runs when driver hits init
    @Override
    public void init() {
        /* Initializes the hardware variables.
           The init() method in the hardware class does all the work here
         */
        //sets the DcMotor variables to be mapped to the robot
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        rightRearDrive  = hardwareMap.get(DcMotor.class, "rightRearDrive");
        leftRearDrive   = hardwareMap.get(DcMotor.class, "leftRearDrive");

        leftShoot       = hardwareMap.get(DcMotor.class, "leftShoot");
        rightShoot      = hardwareMap.get(DcMotor.class, "rightShoot");

        gate = hardwareMap.get(Servo.class, "gate");
        spinner = hardwareMap.get(Servo.class, "spinner");
        rightArm = hardwareMap.get(Servo.class, "rightArm");
        leftArm = hardwareMap.get(Servo.class, "leftArm");

        //Telemetry data sets encoder values to 0, signifies robot waiting
        telemetry.addData("Say", "Hello Mr.Driver");
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {
        double drive = 1 * gamepad1.left_stick_y;
        double strafe = -1 * gamepad1.left_stick_x;
        double twist = 0.7 * gamepad1.right_stick_x;
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

        //opens gate
        if (gamepad1.right_bumper){
            gate.setPosition(1);
        }

        //closes gate
        if(gamepad1.left_bumper){
            gate.setPosition(0.5);
        }

        //turns on shooter
        if(gamepad2.right_bumper){
            leftShoot.setPower(shootingPower);
            rightShoot.setPower(-shootingPower);
        }

        //turns off shooter
        if(gamepad2.left_bumper) {
            leftShoot.setPower(0);
            rightShoot.setPower(0);
        }

        //turns on spinner
        if(gamepad2.x){
            spinner.setPosition(-1);
        }

        //turns spinner wheel off
        if(gamepad2.y){
            spinner.setPosition(0.5);
        }

        //arm push ball into shooter position
        if(gamepad2.dpad_up){
            rightArm.setPosition(0.3);
            leftArm.setPosition(0.7);
        }

        //sets arms down so ball doesn't get into shooting position
        if(gamepad2.dpad_down){
            rightArm.setPosition(0);
            leftArm.setPosition(1);
        }

        // Trigger
        if(gamepad2.right_bumper && shootStep == 0) {
            shootStep = 1;
            spinnerTimer.reset();
        }
        // State 1: Warmup
        if (shootStep == 1) {
            leftShoot.setPower(shootingPower);
            rightShoot.setPower(-shootingPower);
            if (spinnerTimer.milliseconds() >= 350) {
                spinner.setPosition(-1);
            }
        }
        if (spinnerTimer.milliseconds() >= 2510) {
            leftShoot.setPower(0);
            rightShoot.setPower(0);
            spinner.setPosition(0.5);
            shootStep = 0;
        }

        // Trigger
        if(gamepad2.left_bumper && shootStep == 0) {
            shootStep = 1;
            spinnerTimer.reset();
        }
        // State 1: Warmup
        if (shootStep == 1) {
            leftShoot.setPower(0.7);
            rightShoot.setPower(-0.7);
            if (spinnerTimer.milliseconds() >= 350) {
                spinner.setPosition(-1);
            }
        }
        if (spinnerTimer.milliseconds() >= 2510) {
            leftShoot.setPower(0);
            rightShoot.setPower(0);
            spinner.setPosition(0.5);
            shootStep = 0;
        }
    }

    @Override
    public void stop () {
        // Set all drive motor power to zero
        rightFrontDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightRearDrive.setPower(0);
        leftRearDrive.setPower(0);

        // Set the launcher motor power to zero (assuming it's a moving part)
        //if (launcher != null) {
        //    launcher.setPower(0);
        //}
    }
}
