package flight;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base class for test automation with comments.
 */
public class Basetest {

	public static WebDriver driver;
	Properties prop;
	ExtentTest test;

	/**
	 * Initializes and configures the WebDriver.
	 *
	 * @return The configured WebDriver instance.
	 * @throws IOException if there is an I/O error.
	 */
	public WebDriver initializeDriver() throws IOException {

		// Load properties from GlobalData.properties file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//resources//GlobalData.properties");
		prop.load(fis);
		String browserName = prop.getProperty("browser");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");

		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		} else if (browserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browserName.equalsIgnoreCase("safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	}

	/**
	 * BeforeSuite method to initialize the WebDriver and launch the web
	 * application.
	 *
	 * @throws IOException if there is an I/O error.
	 */
	@BeforeSuite(alwaysRun = true)
	public void launchApplication() throws IOException {
		driver = initializeDriver();
		String url = prop.getProperty("url");
		driver.get(url);
	}

	/**
	 * AfterSuite method to close all the browser instances opened during the
	 * automation.
	 */
	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		// Close the WebDriver instance
		// driver.quit();
	}

	/**
	 * Reads JSON data from a file and converts it into a list of HashMaps.
	 *
	 * @param filepath The path to the JSON file.
	 * @return A list of HashMaps containing the data.
	 * @throws IOException if there is an I/O error.
	 */
	public List<HashMap<String, String>> getJsonData(String filepath) throws IOException {
		String jsonContent = FileUtils.readFileToString(new File(filepath), StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}

	/**
	 * Captures a screenshot and saves it to the specified location.
	 *
	 * @param Name   The name of the screenshot.
	 * @param driver The WebDriver instance to capture the screenshot from.
	 * @return The file path of the saved screenshot.
	 * @throws IOException if there is an I/O error.
	 */
	public String getScreenshot(String Name, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "//reports//" + Name + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "//reports//" + Name + ".png";
	}
}
