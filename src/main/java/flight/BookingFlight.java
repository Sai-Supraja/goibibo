package flight;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BookingFlight {
	public static final String XPaths = System.getProperty("user.dir") + "//src//main//java//resources//Objectrepo.xml";
	public WebDriver driver;
	public String messages;
	ExtentTest test;

	public BookingFlight(WebDriver driver) {
		System.out.println(driver.getCurrentUrl());
		this.driver = driver;
		this.test = getReportObject().createTest("Booking");
	}

	public void modalPopupClose() {
	    System.out.println("Entered modal popup method");
	    reportLogPass("modalPop"); 
	    WebElement modalPopup = driver.findElement(By.xpath(readXml("modal")));
	    if (modalPopup.isDisplayed()) {
	        driver.findElement(By.xpath(readXml("modalclose"))).click();
	    }
	}

	public void flightsLinkClick() throws SAXException, IOException, ParserConfigurationException {
	    WebElement flightsLink = driver.findElement(By.xpath(readXml("flightslink")));
	    System.out.println("Flights Link Text: " + flightsLink.getText());
	    String flightsLinkAttribute = flightsLink.getAttribute("class");
	    if (!flightsLinkAttribute.contains("active")) {
	        flightsLink.click();
	    }
	    reportLogPass("links clicked"); 
	}

	public void selectRoundTrip() {
	    driver.findElement(By.xpath(readXml("roundtripbutton"))).click();
	}

	public void fromFlight(HashMap<String, String> testData) throws InterruptedException {
	    String fromValue = testData.get("from");
	    driver.findElement(By.xpath(readXml("fromfield"))).click();
	    Thread.sleep(1000);
	    driver.findElement(By.xpath(readXml("frominputfield"))).sendKeys(fromValue);
	    List<WebElement> fromValues = driver.findElements(By.xpath(readXml("fromvaluelist")));
	    for (int i = 0; i < fromValues.size(); i++) {
	        if (fromValues.get(i).getText().contains("BLR")) {
	            fromValues.get(i).click();
	            break;
	        }
	    }
	    reportLogPass("From Flight"); 
	    System.out.println("From Values: " + fromValues.toString());
	}

	public void toFlight(HashMap<String, String> testData) throws InterruptedException {
	    String toValue = testData.get("to");
	    Thread.sleep(1000);
	    driver.findElement(By.xpath(readXml("toinputfield"))).sendKeys(toValue);
	    List<WebElement> toValues = driver.findElements(By.xpath(readXml("tovaluelist")));
	    for (int i = 0; i < toValues.size(); i++) {
	        if (toValues.get(i).getText().contains("BOM")) {
	            toValues.get(i).click();
	            break;
	        }
	    }
	}

	public void departureDateSelect(HashMap<String, String> testData, String departureDate, String departureMonth) {
	    DateSelect(testData, departureDate, departureMonth);
	}

	public void arrivalDateSelect(HashMap<String, String> testData, String arrivalDate, String arrivalMonth) {
	    driver.findElement(By.xpath(readXml("returntab"))).click();
	    DateSelect(testData, arrivalDate, arrivalMonth);

	    driver.findElement(By.xpath(readXml("dateselection_donebutton"))).click();
	}

	public void DateSelect(HashMap<String, String> testData, String date, String month) {
	    // driver.findElement(By.xpath(readXml("departure"))).click();
	    List<WebElement> daypickerMonth = driver.findElements(By.xpath(readXml("daypickermonth")));

	    for (int monthCount = 0; monthCount < daypickerMonth.size(); monthCount++) {
	        System.out.println("Month: " + daypickerMonth.get(monthCount).getText());
	        if (daypickerMonth.get(monthCount).getText().contains(month)) {

	            String MonthValue = daypickerMonth.get(monthCount).getText();
	            List<WebElement> daypickerDate = driver
	                    .findElements(By.xpath(readXml("daypickerdate").replace("MONTH", MonthValue)));
	            for (int dateCount = 0; dateCount < daypickerDate.size(); dateCount++) {
	                if (daypickerDate.get(dateCount).getText().contains(date)) {
	                    System.out.println("date: " + daypickerDate.get(dateCount).getText());
	                    daypickerDate.get(dateCount).click();
	                    break;
	                }
	            }

	            break;
	        }

	        else {
	            driver.findElement(By.xpath(readXml("nextmontharrow"))).click();
	        }
	    }
	}


	public void addAdults(String totalAdults) {
		addPassengers(totalAdults, "Adults");
	}

	public void addChildren(String totalChildren) {
		addPassengers(totalChildren, "Children");
	}

	public void addInfants(String totalInfants) {
		addPassengers(totalInfants, "Infants");
	}

	public void addPassengers(String totalPassengers, String passengerName) {
		String actualCount = driver.findElement(By.xpath(readXml("passengercount").replace("passenger", passengerName)))
				.getText();
		int passengers;
		for (passengers = Integer.parseInt(actualCount); passengers < Integer.parseInt(totalPassengers); passengers++) {
			driver.findElement(By.xpath(readXml("addpassengers").replace("passenger", passengerName))).click();

		}

	}

	public void travelClass() {
		driver.findElement(By.xpath(readXml("travelclass"))).click();
		driver.findElement(By.xpath(readXml("travelclass_donebutton"))).click();

	}

	public void searchFlights() {
		driver.findElement(By.xpath(readXml("searchflights"))).click();

	}

	public void bookFlight() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(readXml("roundtripoffersheader"))));
		fromFlight();
		toFlight();
		driver.findElement(By.cssSelector(readXml("bookbutton"))).click();
	}

	public boolean errorPopup() throws InterruptedException {

		windowHandle();
		System.out.println("Window handled");
		Thread.sleep(2000);
		try {
			System.out.println("Checking error pop-up");

			if (driver.findElement(By.xpath(readXml("reconformingpopup"))).isDisplayed()) {
				System.out.println("Displayed error pop-up");
				driver.findElement(By.xpath(readXml("okbutton"))).click();
				System.out.println("Ok button clicked");
				return true;
			}
			System.out.println("error pop-up not available");
			return false;
		} catch (NoSuchElementException exception) {
			System.out.println("Exception occured while handling error pop-up");
			exception.printStackTrace();
			return false;
		} catch (Exception exception) {
			System.out.println("Exception occured while handling error pop-up");
			exception.printStackTrace();
			return true;
		}
	}

	public void fromFlight() throws InterruptedException {
		List<String> fromFlightNames = new ArrayList<String>();
		List<WebElement> fromFlights = driver.findElements(By.xpath(readXml("fromflights")));
		System.out.println("Flights list available");
		JavascriptExecutor js = (JavascriptExecutor) driver;

		while (!(fromFlights.size() > 0)) {
			Thread.sleep(5000);
			fromFlights = driver.findElements(By.xpath(readXml("fromflights")));
		}

		int initialElementCount = 0;
		int currentElementCount = 0;
		WebElement lastFlight = null;

		do {
			// Store the current number of elements
			initialElementCount = currentElementCount;

			// Execute JavaScript to scroll down by a certain pixel

			js.executeScript("window.scrollBy(0, 500);");

			// Wait for some time to allow new elements to load (you can adjust this)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Find all flight names after scrolling
			List<WebElement> flightNames = driver.findElements(By.xpath(readXml("fromflightname")));

			// Loop through the newly loaded elements and collect their text
			for (int i = initialElementCount; i < flightNames.size(); i++) {
				String flightNameText = flightNames.get(i).getText();
				System.out.println(flightNameText);
				fromFlightNames.add(flightNameText);
				Integer index = i + 1;
				lastFlight = flightNames.get(i)
						.findElement(By.xpath(readXml("fromflight_radiobutton").replace("INDEX", index + "")));
			}

			// Count the number of elements
			currentElementCount = flightNames.size();
		} while (currentElementCount > initialElementCount);

		js.executeScript("window.scrollBy(0, -400);");
		lastFlight.click();
		System.out.println("Total from flights after scroll: " + initialElementCount);
		System.out.println(fromFlightNames);
		System.out.println(fromFlightNames.size());
	}

	public void toFlight() throws InterruptedException {

		List<String> returnFlightNames = new ArrayList<String>();
		List<WebElement> toFlights = driver.findElements(By.xpath(readXml("returnflights")));
		System.out.println("Flights list available");
		JavascriptExecutor js = (JavascriptExecutor) driver;

		while (!(toFlights.size() > 0)) {
			Thread.sleep(5000);
			toFlights = driver.findElements(By.xpath(readXml("returnflights")));
		}

		int initialElementCount = 0;
		int currentElementCount = 0;
		WebElement lastreturnFlight = null;

		do {
			// Store the current number of elements
			initialElementCount = currentElementCount;

			// Execute JavaScript to scroll down by a certain pixel

			js.executeScript("window.scrollBy(0, 500);");

			// Wait for some time to allow new elements to load (you can adjust this)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Find all flight names after scrolling
			List<WebElement> flightNames = driver.findElements(By.xpath(readXml("returnflightname")));

			// Loop through the newly loaded elements and collect their text
			for (int i = initialElementCount; i < flightNames.size(); i++) {
				String flightNameText = flightNames.get(i).getText();
				System.out.println(flightNameText);
				returnFlightNames.add(flightNameText);
				Integer index = i + 1;
				lastreturnFlight = flightNames.get(i)
						.findElement(By.xpath(readXml("returnflight_radiobutton").replace("INDEX", index + "")));
			}

			// Count the number of elements
			currentElementCount = flightNames.size();
		} while (currentElementCount > initialElementCount);

		js.executeScript("window.scrollBy(0, -3000);");
		lastreturnFlight.click();
		System.out.println("Total return flights after scroll: " + initialElementCount);
		System.out.println(returnFlightNames);
		System.out.println(returnFlightNames.size());

	}

	public void fareSelectionPage() {
		String cabinBaggage = driver.findElement(By.xpath(readXml("cabinbaggage"))).getText();
		String checkInBaggage = driver.findElement(By.xpath(readXml("checkinbaggage"))).getText();
		String dateChangeFee = driver.findElement(By.xpath(readXml("datechangefee"))).getText();
		String cancellationFee = driver.findElement(By.xpath(readXml("cancellationfee"))).getText();
		String seat = driver.findElement(By.xpath(readXml("seat"))).getText();
		System.out.println("Cabin Baggage: " + cabinBaggage + " , " + "Check-in Baggage: " + checkInBaggage + " , "
				+ "datechangefee: " + dateChangeFee + " , " + "cancellationfee: " + cancellationFee + " ," + "seat: "
				+ seat);
		String roundTripFare = driver.findElement(By.xpath(readXml("roundtripfare"))).getText();
		System.out.println("Round Trip Fare: " + roundTripFare);
		driver.findElement(By.xpath(readXml("nextbutton"))).click();
		String roundTripFareConfirmation = driver.findElement(By.xpath(readXml("roundtripfare"))).getText();
		System.out.println("Round Trip Fare: " + roundTripFareConfirmation);
		Assert.assertEquals(roundTripFare, roundTripFareConfirmation);
		driver.findElement(By.xpath(readXml("proceedbutton"))).click();

	}

	public void verifyFareSummary(String adults, String children, String infants) throws InterruptedException {

		Thread.sleep(2000);
		windowHandle();
		Thread.sleep(2000);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(readXml("reviewbookingheader"))));
		} catch (TimeoutException timeoutex) {
			Thread.sleep(2000);
		}
		String fareSummaryDetails = driver.findElement(By.xpath(readXml("faresummary"))).getText();
		System.out.println("Fare Summary Details: " + fareSummaryDetails);
		if (fareSummaryDetails.contains(adults) && fareSummaryDetails.contains(children)
				&& fareSummaryDetails.contains(infants)) {
			Assert.assertTrue(true);
		}
		WebElement proceed = driver.findElement(By.xpath(readXml("proceedbutton")));
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proceed);
		proceed.click();

		WebElement actualconfirmCheckboxError = driver.findElement(By.xpath(readXml("confirmcheckerror")));
		String confirmErrorColor = actualconfirmCheckboxError.getCssValue("color");
		String confirmErrorColorHexValue = Color.fromString(confirmErrorColor).asHex();
		System.out.println("Confirm checkbox error color: " + confirmErrorColorHexValue);
		driver.findElement(By.xpath(readXml("confirmcheckbox"))).click();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proceed);
		proceed.click();
	}

	public void verifyTravellerErrors(String firstNameError, String dateOfBirthError, String emailIDError,
			String mobileNumError) {
		List<WebElement> travellerAdults = driver.findElements(By.xpath(readXml("adultsexpand")));
		for (int i = 0; i < travellerAdults.size(); i++) {
			travellerAdults.get(i).click();

			String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
			Assert.assertEquals(actualNameError, firstNameError);
		}
		List<WebElement> travellerChild = driver.findElements(By.xpath(readXml("childexpand")));
		for (int i = 0; i < travellerChild.size(); i++) {
			travellerChild.get(i).click();
			String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
			Assert.assertEquals(actualNameError, firstNameError);
		}

		List<WebElement> travellerInfant = driver.findElements(By.xpath(readXml("infantsexpand")));
		for (int i = 0; i < travellerInfant.size(); i++) {
			travellerInfant.get(i).click();
			String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
			Assert.assertEquals(actualNameError, firstNameError);
		}
		String actualDateOfBirthError = driver.findElement(By.xpath(readXml("dateofbirtherror"))).getText();
		Assert.assertEquals(actualDateOfBirthError, dateOfBirthError);

		String actualemailIDError = driver.findElement(By.xpath(readXml("emailiderror"))).getText();
		Assert.assertEquals(actualemailIDError, emailIDError);

		String actualmobileNumError = driver.findElement(By.xpath(readXml("mobilenumerror"))).getText();
		Assert.assertEquals(actualmobileNumError, mobileNumError);

	}

	public void addTravellerDetails(String adult1, String adult2, String adult3, String child1, String child2,
			String infant1, String day, String month, String year, String emailID, String mobileNo, String adultGender,
			String childGender, String infantGender) {
		List<WebElement> travellerAdults = driver.findElements(By.xpath(readXml("adultsexpand")));
		for (int i = 0; i < travellerAdults.size(); i++) {
			travellerAdults.get(i).click();
			switch (i) {
			case 0:
				WebElement adultgender = driver.findElement(By.xpath(readXml("adultgender")));
				Select adultGenderSelect = new Select(adultgender);
				adultGenderSelect.selectByVisibleText(adultGender);
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(adult1);
			case 1:
				WebElement adultgender1 = driver.findElement(By.xpath(readXml("adultgender")));
				Select adultGenderSelect1 = new Select(adultgender1);
				adultGenderSelect1.selectByVisibleText(adultGender);
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(adult2);
			case 2:
				WebElement adultgender2 = driver.findElement(By.xpath(readXml("adultgender")));
				Select adultGenderSelect2 = new Select(adultgender2);
				adultGenderSelect2.selectByVisibleText(adultGender);
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(adult3);
			}

		}

		List<WebElement> travellerChildren = driver.findElements(By.xpath(readXml("childexpand")));
		for (int i = 0; i < travellerChildren.size(); i++) {
			travellerChildren.get(i).click();
			switch (i) {
			case 0:
				WebElement childgender = driver.findElement(By.xpath(readXml("childgender")));
				Select childGenderSelect = new Select(childgender);
				childGenderSelect.selectByVisibleText(childGender);
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(child1);
			case 1:
				WebElement childgender1 = driver.findElement(By.xpath(readXml("childgender")));
				Select childGenderSelect1 = new Select(childgender1);
				childGenderSelect1.selectByVisibleText(childGender);
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(child2);
			}
		}
		List<WebElement> travellerInfant = driver.findElements(By.xpath(readXml("infantsexpand")));
		for (int i = 0; i < travellerInfant.size(); i++) {
			travellerInfant.get(i).click();
			WebElement infantgender = driver.findElement(By.xpath(readXml("infantgender")));
			Select infantGenderSelect = new Select(infantgender);
			infantGenderSelect.selectByVisibleText(infantGender);
			switch (i) {
			case 0:
				driver.findElement(By.xpath(readXml("firstname"))).sendKeys(infant1);
			}
		}

		WebElement dayOfBirth = driver.findElement(By.xpath(readXml("dayselect")));
		Select dayselect = new Select(dayOfBirth);
		dayselect.selectByValue(day);

		WebElement monthOfBirth = driver.findElement(By.xpath(readXml("monthselect")));
		Select monthselect = new Select(monthOfBirth);
		monthselect.selectByVisibleText(month);

		WebElement yearOfBirth = driver.findElement(By.xpath(readXml("yearselect")));
		Select yearselect = new Select(yearOfBirth);
		yearselect.selectByValue(year);

		driver.findElement(By.xpath(readXml("emailid"))).sendKeys(emailID);
		driver.findElement(By.xpath(readXml("mobilenumber"))).sendKeys(mobileNo);

		driver.findElement(By.xpath(readXml("proceedbutton"))).click();

		driver.findElement(By.xpath(readXml("proceedtopayment"))).click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentURL = driver.getCurrentUrl();
		if (currentURL.contains("checkout")) {
			try {
				getScreenshot("Checkout Page", driver);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getScreenshot(String Name, WebDriver driver) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "//reports//" + Name + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "//reports//" + Name + ".png";
	}

	public void windowHandle() {
		String mainWindow = driver.getWindowHandle();

		// Perform the click operation that opens new window

		// Switch to new window opened
		for (String newWindow : driver.getWindowHandles()) {
			driver.switchTo().window(newWindow);
		}
	}

	public void dayPickerDate() {
		List<WebElement> daypickerDate = driver.findElements(By.xpath(readXml("daypickerdate")));
		for (int i = 0; i < daypickerDate.size(); i++) {
			daypickerDate.get(i).getText();
		}
	}

	public static ExtentReports getReportObject() {
		String path = System.getProperty("user.dir") + "//reports//index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Automation Test Results");
		reporter.config().setDocumentTitle("Test Automation Results");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Supraja");
		return extent;
	}

	public void reportLogPass(String description) {
		test.log(Status.PASS, description);
	}

	public String readXml(String objName) {

		String locatorValue = null;
		File file = new File(XPaths);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(file);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//         Element rootElement = document.getDocumentElement();
		NodeList nodeList = document.getElementsByTagName("locator");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			// System.out.println("\nCurrent element: " + node.getNodeName());
			if (node.getNodeType() == node.ELEMENT_NODE) {
				Element element = (Element) node;
				locatorValue = element.getElementsByTagName(objName).item(0).getTextContent();
				break;
			}
		}
		return locatorValue;

	}

}
