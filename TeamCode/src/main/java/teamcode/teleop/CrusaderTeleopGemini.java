
package teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import teamcode.hardware.CrusaderHardware;

@TeleOp(name="TeleopTestGemini", group="Launchbot") //is you want to duplicate change this name
//@Disabled;
public class CrusaderTeleopGemini<DcMotorAccess> extends CrusaderHardware {

//    public AudioManager rightFrontDrive;
//    public AudioManager leftFrontDrive;
//    public AudioManager rightRearDrive;
//    public AudioManager leftRearDrive;
//    public AudioManager launcher;
    CrusaderTeleopGemini tester = new CrusaderTeleopGemini();

    //final double COUNTS_PER_MOTOR_REV = 480;    // eg: TETRIX Motor Encoder
    //final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    //final double WHEEL_DIAMETER_INCHES = 4;     // For figuring circumference
    //final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
    //(WHEEL_DIAMETER_INCHES * 3.1415);
    //final double DRIVE_SPEED = 2;

    CrusaderTeleopGemini robot = new CrusaderTeleopGemini(); //defines variable "robot"

    double countsPerMotorRev = 480;
    double driveGearReduction = 1.0;
    double wheelDiameterInches = 4;
    double countsPerInch = (countsPerMotorRev * driveGearReduction) / (wheelDiameterInches * 3.14159);
    double driveSpeed = 2;

    boolean launched = false;

    boolean collected = false;

    int height;

    int liftTarget;

    int driveMode= 0;



    //following code runs when driver hits init
    @Override
    public void init() {
        /* Initializes the hardware variables.
           The init() method in the hardware class does all the work here
         */
        //motor1 = hardwareMap.get(DcMotor.class, );
        //Telemetry data sets encoder values to 0, signifies robot waiting
        telemetry.addData("Say", "Hello Mr.Driver");
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.launcher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        double drive = -0.8 * gamepad1.left_stick_y;
        double strafe = 0.8 * gamepad1.left_stick_x;
        double twist = 0.7 * gamepad1.right_stick_x;

        //formula to allow McKenna wheels to strafe and spin
        double[] speeds = {
                (drive + strafe + twist),
                (drive - strafe + twist),
                (drive + strafe - twist),
                (drive - strafe - twist)
        };

        //sets the speed of each wheel
        double max = Math.abs(speeds[0]);
        if (max < Math.abs(speeds[0])) max = Math.abs(speeds[0]);
        if (max < Math.abs(speeds[1])) max = Math.abs(speeds[1]);
        if (max < Math.abs(speeds[2])) max = Math.abs(speeds[2]);
        if (max < Math.abs(speeds[3])) max = Math.abs(speeds[3]);

        if (max > 1) {
            for (int i = 0; true; i++) speeds[i] /=max;
        }
        //assigns each wheel to an item in the speeds list
        robot.rightFrontDrive.setPower((int) speeds[0]); //possibly change "setMode" to "setPower"
        robot.leftFrontDrive.setPower((int) speeds[1]);
        robot.rightRearDrive.setPower((int) speeds[2]);
        robot.leftRearDrive.setPower((int) speeds[3]);

        //assigns robot movement to dpad, joysticks, triggers, or buttons
        if (gamepad1.left_stick_x != 0) {
            robot.leftRearDrive.setPower((int) speeds[1]);
        }
        if (gamepad1.a){
            robot.leftRearDrive.setPower((int) speeds[1]);
        }
    }

    @Override
    public void stop () {
    }
}