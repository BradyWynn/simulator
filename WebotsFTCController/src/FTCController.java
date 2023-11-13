import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ftc6448.simulator.PlatformSupport;
import org.ftc6448.simulator.webots.OpModeController;

import org.mujoco.MuJoCoLib;
import org.mujoco.MuJoCoLib.*;

// import com.cyberbotics.webots.controller.Supervisor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

//import static org.mujoco.MuJoCoLib.*;
// java -classpath "C:/Program Files/Webots/lib/controller/java/Controller.jar;C:/PROGRA~3/FTCSIM~1/SAMPLE~2/CONTRO~1//FTCCON~1/FTCCON~1.JAR;C:/ProgramData/FTCSimulator/SampleAndroidStudioProject/TeamCode/build/intermediates/javac/debug/classes" "-Djava.library.path=C:/Program Files/Webots/lib/controller/java" FTCController

/**
 * This controller is a bootstrap to load properties and required JAR files and
 * then call into the correct Webots controller class. This is needed because
 * Webots expects the controller to not have a package name and be named the
 * same as the jar file.
 * 
 */
public class FTCController {
	public static void main(String[] args) {
//		System.out.println("Working!");
		FTCController controller = new FTCController();
		try {
			controller.launch();
		} catch (Exception e) {
			System.out.println("Uncaught exception when starting controller "+e);
			System.out.flush();
			throw new RuntimeException("Failed to load opmode", e);
		}
	}

	public void launch() throws Exception {
		System.out.println("1!");
//		Supervisor supervisor = new Supervisor();
		
//		PlatformSupport.supervisor = supervisor;
		
		Properties properties = loadProperties();
		System.out.println(properties);

		//create a classloader to load all classpath entries specified in the properties file
		URLClassLoader classLoader = createClassLoader(properties);
		System.out.println("2!");

		//load the specified OpMode
		OpMode opMode = createOpMode(properties, classLoader);
		System.out.println("3!");
		
		OpModeController controller = new OpModeController(opMode, properties);
		PlatformSupport.timeStep=controller.timeStep;
		
		// init and run the controller
		System.out.println("Initializing controller " + controller.getClass().getName());
		try {
			controller.initialize();
		}
		catch (Throwable e) {
			System.out.println("Error during controller initialize: "+e);
			e.printStackTrace();
		}
		System.out.println("Running controller " + controller.getClass().getName());
		System.out.flush();
		try {
			controller.run();
		}
		catch (Throwable e) {
			System.out.println("Error during controller run: "+e);
			e.printStackTrace();
		}
		System.out.println("Cleaning up controller " + controller.getClass().getName());
		controller.cleanup();
	}

	private OpMode createOpMode(Properties properties, URLClassLoader classLoader) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		System.out.println("4!");
		Class<?> opModeClass = classLoader.loadClass(properties.getProperty("opMode"));
		System.out.println("Starting controller for OpMode "+ opModeClass);

		Object newInstance = opModeClass.getConstructor().newInstance();
		if (!(newInstance instanceof OpMode)) {
			throw new RuntimeException("Expected an OpMode class, but opMode was "+newInstance.getClass());
		}
		return (OpMode)newInstance;
	}

	private URLClassLoader createClassLoader(Properties properties) throws MalformedURLException {
		List<URL> files = new ArrayList<>();
		for (int i = 1; i < 100; i++) {
			String codeSource = properties.getProperty("classpath." + i);
			if (codeSource == null || codeSource.trim().length() == 0) {
				break;
			}
			File codeFile = new File(codeSource);
			System.out.println("Adding " + codeFile.getAbsolutePath() + " to classpath");
			files.add(codeFile.toURI().toURL());
		}				
		URLClassLoader classLoader = new URLClassLoader(files.toArray(new URL[files.size()]),
				getClass().getClassLoader());
		return classLoader;
	}

	protected static final Properties loadProperties() {
		Properties mapping = new Properties();
		File mappingsFile = new File("simulation.properties");
		try {
			mapping.load(new FileInputStream(mappingsFile));
		} catch (IOException e) {
			System.err.println("Failed to load simulation properties from " + mappingsFile.getAbsolutePath());
			e.printStackTrace();
		}
		return mapping;
	}
}
