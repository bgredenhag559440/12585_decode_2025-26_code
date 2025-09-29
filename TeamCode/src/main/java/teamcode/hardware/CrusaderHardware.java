package teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */





/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class CrusaderHardware extends OpMode {
    /* Public OpMode members. */
    public DcMotor leftFrontDrive   = null;
    public DcMotor  leftRearDrive    = null;
    public DcMotor  rightFrontDrive   = null;
    public DcMotor  rightRearDrive    = null;
    public DcMotor launcher = null;
    //public DcMotor  armExtension   = null;
    //public DcMotor  armRotate   = null;
    // public DcMotor  craneRotateChain = null;
    // public Servo    clawLeft   = null;
    // public Servo    clawRight   = null;
    //public Servo claw   = null;
    //public Servo    trayRotate   = null;
    //public Servo    bottomCollectorSpinChain   = null;
    // public Servo    bottomCollectorWholeSpin  = null;
    // public Servo    collectorArmRotate = null;
    // public Servo    collectorArmRotateTwo = null;
    //public Servo  tempServo = null;
    //public Servo    planeLaunch = null;






    /* local OpMode members. */
    HardwareMap hwMap =  null;
    private final ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    //public CrusaderHardware(){}

    public HardwareMap ahwMap;
    /* Initialize standard Hardware interfaces */
    @Override
    public void init() {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontDrive  = hwMap.get(DcMotor.class, "leftFrontDrive");
        leftRearDrive  = hwMap.get(DcMotor.class, "leftRearDrive");
        rightFrontDrive  = hwMap.get(DcMotor.class, "rightFrontDrive");
        rightRearDrive  = hwMap.get(DcMotor.class, "rightRearDrive");
        launcher = hwMap.get(DcMotor.class, "launcher");
//        armExtension = hwMap.get(DcMotor.class, "armExtension");
//        armRotate = hwMap.get(DcMotor.class, "armRotate");
//        claw = hwMap.get(Servo.class, "claw");
        //clawRight = hwMap.get(Servo.class, "clawRight");
        //clawRotate = hwMap.get(Servo.class, "clawRotate");
        //trayRotate = hwMap.get(Servo.class, "trayRotate");
        //bottomCollectorSpinChain = hwMap.get(Servo.class, "bottomCollectorSpinChain");
        //bottomCollectorWholeSpin = hwMap.get(Servo.class, "bottomCollectorWholeSpin");
        //collectorArmRotate = hwMap.get(Servo.class, "collectorArmRotate");
        //collectorArmRotateTwo = hwMap.get(Servo.class, "collectorArmRotateTwo");
        //planeLaunch = hwMap.get(Servo.class, "planeLaunch");
        //craneRotateChain = hwMap.get(DcMotor.class, "craneRotateChain");
        //tempServo = hwMap.get(Servo.class, "tempServo");




        // Assigns the rotation to the individual wheels
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE); // reverse on ROTC, Reverse on Comp. Bot
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE); // Forward on ROTC, Reverse on Comp Bot
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);// Forward on ROTC, Forward on Comp Bot
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);// Reverse on ROTC, Forward on Comp Bot
        launcher.setDirection(DcMotor.Direction.FORWARD);
//        armExtension.setDirection(DcMotorSimple.Direction.FORWARD);
//        armRotate.setDirection(DcMotorSimple.Direction.REVERSE);//opposite so reverse
        //craneRotateChain.setDirection(DcMotorSimple.Direction.FORWARD);


        // Set all motors to zero power
        leftFrontDrive.setPower(0);
        leftRearDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightRearDrive.setPower(0);
        launcher.setPower(0);
//        armExtension.setPower(0);
//        armRotate.setPower(0);
        //craneRotateChain.setPower(0);



        // Set all motors to run without encoders.
        // *IMPORTANT---May want to use RUN_USING_ENCODERS if encoders are installed.
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        armExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //craneRotateChain.setMode(DcMotor.Runde.RUN_USMode.RUN_USING_ENCODER);


    }
    @Override
    public void loop() {

    }
}

