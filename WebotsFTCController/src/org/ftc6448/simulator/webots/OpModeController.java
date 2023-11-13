package org.ftc6448.simulator.webots;

import java.nio.ByteBuffer;
import java.util.Properties;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.ftc6448.simulator.Controller;
import org.ftc6448.simulator.PlatformSupport;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.studiohartman.jamepad.ControllerManager;

import static org.mujoco.MuJoCoLib.*;
import java.nio.ByteBuffer;

import static org.mujoco.MuJoCoLib.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class OpModeController implements Controller {

	protected final OpMode opMode;
	protected final Properties properties;
	
	public final int timeStep;
	protected GamepadSupport gamepadSupport;
	protected ControllerManager controllerManager;
	protected mjModel model;
	protected mjModel_ model_ref;
	protected mjData data;
	protected mjData_ data_ref;
	protected mjvCamera_ cam_ref;
	protected mjvCamera cam;
	protected mjvOption_ opt_ref;
	protected mjvOption opt;
	protected mjvScene_ scr_ref;
	protected mjvScene scn;
	protected mjrContext_ con_ref;
	protected mjrContext con;
	protected mjvPerturb_ pert_ref;
	protected mjvPerturb pert;
	protected long window;

	public OpModeController(OpMode opMode, Properties properties) {
		this.opMode = opMode;
//		this.supervisor=supervisor;
		this.properties = properties;
		// get the time step of the current world.
//		timeStep = (int) Math.round(supervisor.getBasicTimeStep());
		timeStep = 0;
		System.out.println("timeStep " + timeStep);
	
		opMode.gamepad1 = new Gamepad();
		opMode.gamepad2 = new Gamepad();
		
	}
	
	@Override
	public void initialize() {
//		keyboard = new Keyboard();
//		keyboard.enable(timeStep);

		cam_ref = new mjvCamera_();
		cam = new mjvCamera(cam_ref);
		opt_ref = new mjvOption_();
		opt = new mjvOption(opt_ref);
		scr_ref = new mjvScene_();
		scn = new mjvScene(scr_ref);
		con_ref = new mjrContext_();
		con = new mjrContext(con_ref);
		pert_ref = new mjvPerturb_();
		pert = new mjvPerturb(pert_ref);

		ByteBuffer errstr = null;
		model = mj_loadXML("src/test.xml", null, errstr, 1000);
		model_ref = new mjModel_(model);
		data = mj_makeData(model);
		data_ref = new mjData_(data);

		glfwInit();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		CharSequence str = "demo";
		window = glfwCreateWindow(1000, 1000, str, NULL, NULL);
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);

		mjv_defaultOption(opt);
		mjr_defaultContext(con);
		mjv_defaultCamera(cam);

		cam_ref.distance(10);

		mjv_makeScene(model, scn, 1000);
		mjr_makeContext(model, con, mjFONTSCALE_100);
		
		controllerManager = new ControllerManager();
		controllerManager.initSDLGamepad();
		
		gamepadSupport = new GamepadSupport(properties, controllerManager);

		initializeDevices();
		opMode.internalPreInit();
		System.out.println("Calling OpMode init");
		opMode.init();
		opMode.internalPostInitLoop();
	}
	
	//this method iterates the Robot and the simulation properties file and sets up the hardware map
	private void initializeDevices() {
		final HardwareMap hardwareMap = new HardwareMap();
		opMode.hardwareMap = hardwareMap;
		
		//load all motors into the hardware motor map
//		for (int i = 0; i < this.properties.size(); i++) {
//			System.out.println(this.properties.get(i));
//		}
		for(String key: properties.stringPropertyNames()){
//			System.out.println(properties.getProperty(key) + " " + key);
			hardwareMap.put("frontRight", new WebotsDcMotorImpl("motor0", data_ref));
			hardwareMap.put("frontLeft", new WebotsDcMotorImpl("motor1", data_ref));
			hardwareMap.put("backRight", new WebotsDcMotorImpl("motor2", data_ref));
			hardwareMap.put("backLeft", new WebotsDcMotorImpl("motor3", data_ref));
		}
//			Device device=supervisor.getDeviceByIndex(i);
//
//			System.out.println(device+" "+i);
//
//			if (device instanceof InertialUnit) {
//				//only one imu is supported
//				System.out.println(device+" is IMU");
//				InertialUnit imu=(InertialUnit)device;
//				imu.enable(timeStep);
//				hardwareMap.put("imu", new WebotsBNO055IMU(imu));
//			}
//			else if (device instanceof Motor) {
//				Motor motor=(Motor)device;
//
//				//if there is an associated position sensor, enable it
//				PositionSensor sensor=motor.getPositionSensor();
//				if (sensor!=null) {
//					sensor.enable(timeStep);
//				}
//
//				String mappedName=properties.getProperty(device.getName());
//				if (mappedName!=null) {
//					System.out.println("Loading webots motor " + device.getName()+" as "+mappedName);
//				}
//				else {
//					mappedName=device.getName();
//					System.out.println("Loading webots motor " + mappedName);
//				}
//
//				String type=properties.getProperty(mappedName+".type");
//				if ("servo".equalsIgnoreCase(type)) {
//					String baseRotationProperty=device.getName()+".baseRotation";
//					String baseRotation=properties.getProperty(baseRotationProperty);
//					if (baseRotation==null||baseRotation.trim().length()==0) {
//						System.out.println("No property found for "+baseRotationProperty+", so using 0 as default");
//						baseRotation="0";
//					}
//					System.out.println("Webots motor "+mappedName+" is a servo");
//	    			WebotsServoImpl servo=new WebotsServoImpl(mappedName,motor);
//	    			servo.setBaseRotation(Float.parseFloat(baseRotation));
//	    			hardwareMap.put(mappedName, servo);
//	    		}
//
//				else {
//					WebotsDcMotorImpl webotsMotor=new WebotsDcMotorImpl(mappedName,motor);
//
//					String maxPowerProperty=device.getName()+".maxPower";
//					String maxPower=properties.getProperty(maxPowerProperty);
//					if (maxPower==null||maxPower.trim().length()==0) {
//						System.out.println("No property found for "+maxPowerProperty+", so using 10 as default");
//						maxPower="10";
//					}
//
//					System.out.println("Using "+maxPower+" for property "+maxPowerProperty);
//					webotsMotor.setMaxPower(Float.parseFloat(maxPower));
//
//					hardwareMap.dcMotor.put(mappedName, webotsMotor);
//				}
//			}
//
//
//		}
//
//		//add any missing motors
//	    for (Object key:properties.keySet()) {
//	    	String property=(String)key;
//	    	HardwareDevice webotsDevice=null;
//	    	if (property.endsWith(".type")) {
//	    		String device=property.substring(0,property.length()-5);
//	    		String type=properties.getProperty(property);
//	    		if ("servo".equalsIgnoreCase(type)) {
//	    			webotsDevice=new WebotsServo();
//	    		}
//	    		else if ("continuousServo".equalsIgnoreCase(type)) {
//	    			webotsDevice=new WebotsContinuousServo();
//	    		}
//	    		else if ("motor".equalsIgnoreCase(type)) {
//	    			webotsDevice=new WebotsDcMotor(device);
//	    		}
//	    		else if ("distance".equalsIgnoreCase(type)) {
//	    			webotsDevice=new WebotsDistanceSensor(device);
//	    		}
//	    		else if ("digitalChannel".equalsIgnoreCase(type)) {
//					//TODO: should this do something
//					WebotsDigitalChannel channel=new WebotsDigitalChannel(device);
//
//					System.out.println("Device "+device+" is a digital channel");
//	    			hardwareMap.put(device, channel);
//	    		}
//	    		if (webotsDevice!=null &&  !hardwareMap.hasDevice(device)) {
//	    			System.out.println("Adding empty "+type+" implementation for "+device);
//	    			hardwareMap.put(device, webotsDevice);
//	    		}
//	    	}
//	    }
		
	}

	static void mycontroller(mjModel_ m_, mjData_ d_) {
		if( m_.nu() == m_.nv())
			mju_scl(d_.ctrl(), d_.qvel(), -0.1, m_.nv());
	}

	@Override
	public void run() {
		System.out.println("Starting OpMode");
		opMode.start();
				
		if (opMode instanceof LinearOpMode) {
			LinearOpMode linearOpMode=(LinearOpMode)opMode;
			
			//Autonomous opmodes need special coordination between the OpMode loop and the simulator loop
			System.out.println("Running LinearOpMode");
		

			//if sleep time is 0, then we signal the simulator lock and wait to be signaled back
			//this effectively locks the OpMode frequency to the simulator
			//if sleep time is not 0, then the simulator lock is signaled and then the simulator will wait the associated time
			
			long sleepTime=0;
			String simSleepTimeStr=properties.getProperty("simulatorLoopSleepTime");
			if (simSleepTimeStr != null && simSleepTimeStr.trim().length() > 0) {
				sleepTime=Long.parseLong(simSleepTimeStr);
			}
			
			while (1 != -1) {
				mjrRect viewport = mjr_maxViewport(con);
				mjrRect_ viewport_ref = new mjrRect_(viewport);
				int[] width = new int[1];
				int[] height = new int[1];
				glfwGetFramebufferSize(window, width, height);

				mjv_updateScene(model, data, opt, pert, cam, mjCAT_ALL, scn);
				mjr_render(viewport, scn, con);

				glfwSwapBuffers(window);

				glfwPollEvents();
//				mycontroller(model_ref, data_ref);
				mj_step(model, data);
				System.out.println(data_ref.time());
				linearOpMode.loop();
				linearOpMode.internalPostLoop();
				
				//signal any threads that are waiting for a simulator tick
				PlatformSupport.signalSimulatorLock(sleepTime==0);
				if (linearOpMode.isStopped()) {
					System.out.println("OpMode stopped");
					mj_deleteModel(model);
					mj_deleteData(data);
				}
				if (sleepTime>0) { 
					try {
						//we want to sleep a little bit to allow the other code to keep up with us
						//if this thread is running faster than the OpMode, the OpMode will not be running at the proper frequency
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			//TeleOp opmodes just loop and call the OpMode loop method along with the simulator step
			System.out.println("Running OpMode");
			boolean useKeyboard="true".equalsIgnoreCase(properties.getProperty("emulateGamepadsWithKeyboard"));
			while (0 != -1) {
				handleGamepads(useKeyboard);
				opMode.loop();
				opMode.internalPostLoop();

				//signal any threads that are waiting for a simulator tick (TeleOp opmodes should not, but do it just in case)
				PlatformSupport.signalSimulatorLock(false);				
			}
		}
	}

	private void handleGamepads(boolean useKeyboard) {
		
//		if (!useKeyboard) {
//			gamepadSupport.processJoystick(opMode.gamepad1, opMode.gamepad2);
//		}
//		else {
//
//			//if we are using virtual gamepad, poll the keyboard
//			int key=keyboard.getKey();
//			while (key!=-1) {
//				switch (key) {
//				case Keyboard.UP:
//					opMode.gamepad1.left_stick_y++;
//					break;
//				case Keyboard.DOWN:
//					opMode.gamepad1.left_stick_y--;
//					break;
//				case Keyboard.LEFT:
//					opMode.gamepad1.left_stick_x--;
//					break;
//				case Keyboard.RIGHT:
//					opMode.gamepad1.left_stick_x++;
//					break;
//				}
//				key=keyboard.getKey();
//			}
//		}
		
	}

	@Override
	public void cleanup() {
		opMode.stop();
		controllerManager.quitSDLGamepad();
	}

}
