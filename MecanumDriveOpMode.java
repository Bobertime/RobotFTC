package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MecanumDriveOpMode extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor frontLeftSlide;
    private DcMotor frontRightSlide;
    private DcMotor backLeftSlide;
    private DcMotor backRightSlide;

    private boolean pressABtn = false;
    private boolean pressBBtn = false;

    @Override
    public void runOpMode() {
        // Вызываем метод для инициализации всех двигателей и датчиков
        initializeHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            // Вызываем метод для движения робота, включая поворот
            mecanumDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            viperSlideMoveBottom(gamepad1.a);
            viperSlideMoveUp(gamepad1.b);
        }
    }

    // Метод для инициализации всех двигателей и датчиков
    private void initializeHardware() {
        // Инициализация моторов
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeftSlide = hardwareMap.get(DcMotor.class, "frontLeftSlide");
        frontRightSlide = hardwareMap.get(DcMotor.class, "frontRightSlide");
        backLeftSlide = hardwareMap.get(DcMotor.class, "backLeftSlide");
        backRightSlide = hardwareMap.get(DcMotor.class, "backRightSlide");

        // Инвертируем правые моторы, чтобы согласовать их вращение
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontRightSlide.setDirection(DcMotor.Direction.REVERSE);
        backRightSlide.setDirection(DcMotor.Direction.REVERSE);

        frontLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Сброс энкодера
        frontLeftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Режим работы по позиции
        frontRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Включаем телеметрию для отладки
        telemetry.addData("Status", "Hardware Initialized");
    }

    // Метод для движения робота на колесах Mecanum с вращением
    private void mecanumDrive(double drive, double strafe, double rotate) {
        // Вычисляем мощность для каждого мотора для движения в четырех направлениях и вращения
        double frontLeftPower = drive + strafe + rotate;
        double frontRightPower = drive - strafe - rotate;
        double backLeftPower = drive - strafe + rotate;
        double backRightPower = drive + strafe - rotate;

        // Устанавливаем мощность для каждого мотора
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        // Телеметрия для отладки
        telemetry.addData("Motors", "frontLeft (%.2f), frontRight (%.2f), backLeft (%.2f), backRight (%.2f)",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.update();
    }

    private void viperSlideMoveBottom(boolean aBtn) {
        int targetPosition = (int)(34.8 / 2 * 537.7);
        if (aBtn && !pressABtn) {
            backLeftSlide.setTargetPosition(targetPosition);
            backRightSlide.setTargetPosition(targetPosition);
            pressABtn = true;
        } else {
            backLeftSlide.setTargetPosition(0);
            backRightSlide.setTargetPosition(0);
            pressABtn = false;
        }

        backLeftSlide.setPower(1.0);
        backRightSlide.setPower(1.0);

        if (!backLeftSlide.isBusy() || !backRightSlide.isBusy()) {
            backLeftSlide.setPower(0.0);
            backRightSlide.setPower(0.0);// Останавливаем мотор, если достигли целевой позиции
        }
    }

    private  void viperSlideMoveUp(boolean bBtn) {
        int targetPosition = (int)(69.0 / 2 * 537.7);
        if (bBtn && !pressBBtn) {
            frontLeftSlide.setTargetPosition(targetPosition);
            frontRightSlide.setTargetPosition(targetPosition);
            pressBBtn = true;
        } else {
            frontLeftSlide.setTargetPosition(0);
            frontRightSlide.setTargetPosition(0);
            pressBBtn = false;
        }

        frontLeftSlide.setPower(1.0);
        frontRightSlide.setPower(1.0);

        if (!frontRightSlide.isBusy() || !frontLeftSlide.isBusy()) {
            frontLeftSlide.setPower(0.0);
            frontRightSlide.setPower(0.0);
        }
    }
}

