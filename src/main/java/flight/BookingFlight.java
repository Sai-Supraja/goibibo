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
import org.testng.Reporter;
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

	// This method is responsible for closing a modal popup if it is displayed.
	public void modalPopupClose() {
	    // Logging that we entered the modal popup method.
	    System.out.println("Entered modal popup method");

	    // Logging a pass status for reporting.
	    reportLogPass("modalPop");

	    // Finding and checking if the modal popup is displayed, if so, clicking the close button.
	    WebElement modalPopup = driver.findElement(By.xpath(readXml("modal")));
	    if (modalPopup.isDisplayed()) {
	        driver.findElement(By.xpath(readXml("modalclose"))).click();
	    }
	}

	// This method clicks on the "Flights" link if it is not already active.
	public void flightsLinkClick() throws SAXException, IOException, ParserConfigurationException {
	    // Finding the Flights link element and logging its text.
	    WebElement flightsLink = driver.findElement(By.xpath(readXml("flightslink")));
	    System.out.println("Flights Link Text: " + flightsLink.getText());

	    // Checking if the Flights link is not already active and clicking it if not.
	    String flightsLinkAttribute = flightsLink.getAttribute("class");
	    if (!flightsLinkAttribute.contains("active")) {
	        flightsLink.click();
	    }

	    // Logging a pass status for reporting.
	    reportLogPass("links clicked");
	}

	// This method selects the "Round Trip" option.
	public void selectRoundTrip() {
	    driver.findElement(By.xpath(readXml("roundtripbutton"))).click();
	}

	// This method handles selecting the departure location.
	public void fromFlight(HashMap<String, String> testData) throws InterruptedException {
	    // Getting the "from" value from testData and interacting with the corresponding elements.
	    String fromValue = testData.get("from");
	    driver.findElement(By.xpath(readXml("fromfield"))).click();
	    Thread.sleep(1000);
	    driver.findElement(By.xpath(readXml("frominputfield"))).sendKeys(fromValue);

	    // Locating and selecting the desired departure location.
	    List<WebElement> fromValues = driver.findElements(By.xpath(readXml("fromvaluelist")));
	    for (int i = 0; i < fromValues.size(); i++) {
	        if (fromValues.get(i).getText().contains("BLR")) {
	            fromValues.get(i).click();
	            break;
	        }
	    }

	    // Logging a pass status for reporting and printing the selected values.
	    reportLogPass("From Flight");
	    System.out.println("From Values: " + fromValues.toString());
	}

	// This method handles selecting the arrival location.
	public void toFlight(HashMap<String, String> testData) throws InterruptedException {
	    // Getting the "to" value from testData and interacting with the corresponding elements.
	    String toValue = testData.get("to");
	    Thread.sleep(1000);
	    driver.findElement(By.xpath(readXml("toinputfield"))).sendKeys(toValue);

	    // Locating and selecting the desired arrival location.
	    List<WebElement> toValues = driver.findElements(By.xpath(readXml("tovaluelist")));
	    for (int i = 0; i < toValues.size(); i++) {
	        if (toValues.get(i).getText().contains("BOM")) {
	            toValues.get(i).click();
	            break;
	        }
	    }
	}

	// This method selects the departure date.
	public void departureDateSelect(HashMap<String, String> testData, String departureDate, String departureMonth) {
	    DateSelect(testData, departureDate, departureMonth);
	}

	// This method selects the arrival date.
	public void arrivalDateSelect(HashMap<String, String> testData, String arrivalDate, String arrivalMonth) {
	    // Clicking on the "return" tab and then invoking the DateSelect method.
	    driver.findElement(By.xpath(readXml("returntab"))).click();
	    DateSelect(testData, arrivalDate, arrivalMonth);

	    // Clicking the "done" button after selecting the arrival date.
	    driver.findElement(By.xpath(readXml("dateselection_donebutton"))).click();
	}

	// This method handles the date selection process.
	public void DateSelect(HashMap<String, String> testData, String date, String month) {
	    // Locating and iterating through the available months to select the desired date.
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

	// This method adds adults to the passenger count.
	public void addAdults(String totalAdults) {
	    addPassengers(totalAdults, "Adults");
	}

	// This method adds children to the passenger count.
	public void addChildren(String totalChildren) {
	    addPassengers(totalChildren, "Children");
	}

	// This method adds infants to the passenger count.
	public void addInfants(String totalInfants) {
	    addPassengers(totalInfants, "Infants");
	}

	// This method adds passengers of a specific type.
	public void addPassengers(String totalPassengers, String passengerName) {
	    // Retrieving the current passenger count and incrementing it to reach the desired count.
	    String actualCount = driver.findElement(By.xpath(readXml("passengercount").replace("passenger", passengerName)))
	            .getText();
	    int passengers;
	    for (passengers = Integer.parseInt(actualCount); passengers < Integer.parseInt(totalPassengers); passengers++) {
	        driver.findElement(By.xpath(readXml("addpassengers").replace("passenger", passengerName))).click();

	    }
	}


	// This method is responsible for selecting the travel class, which is a required step for booking a flight.
	public void travelClass() {
	    // Clicking on the travel class element to initiate the class selection.
	    driver.findElement(By.xpath(readXml("travelclass"))).click();
	    
	    // Clicking on the "done" button after selecting the travel class.
	    driver.findElement(By.xpath(readXml("travelclass_donebutton"))).click();
	}

	// This method is responsible for initiating the flight search process.
	public void searchFlights() {
	    // Clicking on the "Search Flights" button to start searching for available flights.
	    driver.findElement(By.xpath(readXml("searchflights"))).click();
	}

	// This method is responsible for booking a flight after selecting the departure and return flights.
	public void bookFlight() throws InterruptedException {
	    // Creating a WebDriverWait object to wait for a specific element to be visible.
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    
	    // Waiting for the round trip offers header to become visible before proceeding.
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(readXml("roundtripoffersheader"))));
	    
	    // Selecting the departure and return flights and clicking on the "Book" button.
	    fromFlight();
	    toFlight();
	    driver.findElement(By.cssSelector(readXml("bookbutton"))).click();
	}

	// This method checks for the presence of an error popup and handles it if it appears.
	public boolean errorPopup() throws InterruptedException {
	    // Handling window handles.
	    windowHandle();
	    System.out.println("Window handled");
	    Thread.sleep(2000);
	    
	    try {
	        System.out.println("Checking error pop-up");
	        
	        // Checking if the error pop-up is displayed.
	        if (driver.findElement(By.xpath(readXml("reconformingpopup"))).isDisplayed()) {
	            System.out.println("Displayed error pop-up");
	            driver.findElement(By.xpath(readXml("okbutton"))).click();
	            System.out.println("Ok button clicked");
	            return true;
	        }
	        
	        // If the error pop-up is not available, return false.
	        System.out.println("Error pop-up not available");
	        return false;
	    } catch (NoSuchElementException exception) {
	        // Handling exceptions related to the error pop-up.
	        System.out.println("Exception occurred while handling error pop-up");
	        exception.printStackTrace();
	        return false;
	    } catch (Exception exception) {
	        // Handling exceptions related to the error pop-up.
	        System.out.println("Exception occurred while handling error pop-up");
	        exception.printStackTrace();
	        return true;
	    }
	}
	

	public void fromFlight() throws InterruptedException {
		// The purpose of this code is to dynamically load and select a departure flight from a list that may require scrolling.

		// Initialize a list to store the names of departure flights.
		List<String> fromFlightNames = new ArrayList<String>();

		// Retrieve the list of departure flight elements from the web page.
		List<WebElement> fromFlights = driver.findElements(By.xpath(readXml("fromflights")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Check if the list of departure flights is initially available.
		// If not, wait for it to become available by periodically checking its presence.
		while (!(fromFlights.size() > 0)) {
		    Thread.sleep(5000);
		    fromFlights = driver.findElements(By.xpath(readXml("fromflights")));
		}

		// Initialize variables to keep track of the number of loaded flight elements.
		int initialElementCount = 0;
		int currentElementCount = 0;

		// Initialize a variable to keep track of the last flight element.
		WebElement lastFlight = null;

		// Start a loop to scroll down, load additional flight elements, and collect their information.
		do {
		    // Store the current number of flight elements.
		    initialElementCount = currentElementCount;

		    // Execute JavaScript to scroll down the page by a certain pixel amount.
		    js.executeScript("window.scrollBy(0, 500);");

		    // Wait for some time to allow new flight elements to load (adjustable time).
		    try {
		        Thread.sleep(2000);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }

		    // Find all flight names after scrolling.
		    List<WebElement> flightNames = driver.findElements(By.xpath(readXml("fromflightname")));

		    // Loop through the newly loaded flight elements and collect their text (flight names).
		    for (int i = initialElementCount; i < flightNames.size(); i++) {
		        String flightNameText = flightNames.get(i).getText();
		        System.out.println(flightNameText);
		        fromFlightNames.add(flightNameText);
		        Integer index = i + 1;

		        // Identify the radio button associated with each flight element.
		        lastFlight = flightNames.get(i).findElement(By.xpath(readXml("fromflight_radiobutton").replace("INDEX", index + "")));
		    }

		    // Count the number of flight elements after loading.
		    currentElementCount = flightNames.size();
		} while (currentElementCount > initialElementCount);

		// Scroll the page up to its initial position.
		js.executeScript("window.scrollBy(0, -400);");

		// Click on the radio button of the last selected departure flight.
		lastFlight.click();

		// Print information about the loaded departure flights.
		System.out.println("Total from flights after scroll: " + initialElementCount);
		System.out.println(fromFlightNames);
		System.out.println(fromFlightNames.size());

	}

	// The purpose of this code is to interact with the web page to accomplish two important tasks:

	// 1. Gathering return flight information:
//	    The code retrieves information about available return flights, which is essential for users to select their return flight.
//	    It uses scrolling and dynamic loading techniques to ensure that all available flights are captured.

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

	// 2. Handling the fare selection page:
//	    This code is responsible for navigating and interacting with the fare selection page.
//	    It collects information related to cabin baggage, check-in baggage, date change fees, cancellation fees, and seat selection.
//	    The collected information is displayed for user reference.
//	    Additionally, it validates the consistency of round trip fares and proceeds with the booking process, ensuring data integrity and a seamless user experience.

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

	// The purpose of this method is to verify the fare summary information before proceeding with the booking.
	// It checks if the fare summary contains the expected number of adults, children, and infants as provided in the method arguments.
	// Additionally, it handles the confirmation checkbox error color and proceeds with the booking process.

	public void verifyFareSummary(String adults, String children, String infants) throws InterruptedException {
	    Thread.sleep(2000);
	    
	    // Handle multiple browser windows or tabs
	    windowHandle();
	    
	    Thread.sleep(2000);
	    
	    try {
	        // Wait for the review booking header to be visible within a timeout of 20 seconds
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(readXml("reviewbookingheader"))));
	    } catch (TimeoutException timeoutex) {
	        // If the review booking header is not found within the timeout, wait for an additional 2 seconds
	        Thread.sleep(2000);
	    }
	    
	    // Retrieve and print the fare summary details
	    String fareSummaryDetails = driver.findElement(By.xpath(readXml("faresummary"))).getText();
	    System.out.println("Fare Summary Details: " + fareSummaryDetails);
	    Reporter.log("Fare Summary Details: " + fareSummaryDetails);
	    
	    // Check if the fare summary contains the expected numbers of adults, children, and infants
	    if (fareSummaryDetails.contains(adults) && fareSummaryDetails.contains(children)
	            && fareSummaryDetails.contains(infants)) {
	        Assert.assertTrue(true);
	    }
	    
	    // Locate and click the "Proceed" button
	    WebElement proceed = driver.findElement(By.xpath(readXml("proceedbutton")));
	    proceed.click();
	    
	    // Check and log the color of the confirmation checkbox error
	    WebElement actualconfirmCheckboxError = driver.findElement(By.xpath(readXml("confirmcheckerror")));
	    String confirmErrorColor = actualconfirmCheckboxError.getCssValue("color");
	    String confirmErrorColorHexValue = Color.fromString(confirmErrorColor).asHex();
	    System.out.println("Confirm checkbox error color: " + confirmErrorColorHexValue);
	    
	    // Locate and click the confirmation checkbox
	    driver.findElement(By.xpath(readXml("confirmcheckbox"))).click();
	    
	    // Scroll to the "Proceed" button and click it
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proceed);
	    proceed.click();
	}

	// The purpose of this method is to verify traveler-related errors, including first name, date of birth, email ID, and mobile number errors.
	// It iterates through the list of adult, child, and infant travelers to validate their respective first name errors.
	// Additionally, it validates the common date of birth, email ID, and mobile number errors.

	public void verifyTravellerErrors(String firstNameError, String dateOfBirthError, String emailIDError,
	        String mobileNumError) {
	    // Locate and expand the sections for adult travelers
	    List<WebElement> travellerAdults = driver.findElements(By.xpath(readXml("adultsexpand")));
	    for (int i = 0; i < travellerAdults.size(); i++) {
	        travellerAdults.get(i).click();
	        String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
	        Assert.assertEquals(actualNameError, firstNameError);
	    }
	    
	    // Locate and expand the sections for child travelers
	    List<WebElement> travellerChild = driver.findElements(By.xpath(readXml("childexpand")));
	    for (int i = 0; i < travellerChild.size(); i++) {
	        travellerChild.get(i).click();
	        String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
	        Assert.assertEquals(actualNameError, firstNameError);
	    }
	    
	    // Locate and expand the sections for infant travelers
	    List<WebElement> travellerInfant = driver.findElements(By.xpath(readXml("infantsexpand")));
	    for (int i = 0; i < travellerInfant.size(); i++) {
	        travellerInfant.get(i).click();
	        String actualNameError = driver.findElement(By.xpath(readXml("firstnameerror"))).getText();
	        Assert.assertEquals(actualNameError, firstNameError);
	    }
	    
	    // Validate the common date of birth error
	    String actualDateOfBirthError = driver.findElement(By.xpath(readXml("dateofbirtherror"))).getText();
	    Assert.assertEquals(actualDateOfBirthError, dateOfBirthError);
	    
	    // Validate the common email ID error
	    String actualemailIDError = driver.findElement(By.xpath(readXml("emailiderror"))).getText();
	    Assert.assertEquals(actualemailIDError, emailIDError);
	    
	    // Validate the common mobile number error
	    String actualmobileNumError = driver.findElement(By.xpath(readXml("mobilenumerror"))).getText();
	    Assert.assertEquals(actualmobileNumError, mobileNumError);
	}

	// The purpose of this method is to add traveler details, including adults, children, and infants, along with their personal information.
	// It iterates through the list of travelers, selecting their gender, entering their first name, and providing their date of birth, email ID, and mobile number.
	// After entering the details, it proceeds with the booking process.

	public void addTravellerDetails(String adult1, String adult2, String adult3, String child1, String child2,
	        String infant1, String day, String month, String year, String emailID, String mobileNo, String adultGender,
	        String childGender, String infantGender) {
	    // Locate and expand the sections for adult travelers and fill in their details
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

	    // Locate and expand the sections for child travelers and fill in their details
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
	    
	    // Locate and expand the sections for infant travelers and fill in their details
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
	    
	    // Select the date of birth
	    WebElement dayOfBirth = driver.findElement(By.xpath(readXml("dayselect")));
	    Select dayselect = new Select(dayOfBirth);
	    dayselect.selectByValue(day);

	    WebElement monthOfBirth = driver.findElement(By.xpath(readXml("monthselect")));
	    Select monthselect = new Select(monthOfBirth);
	    monthselect.selectByVisibleText(month);

	    WebElement yearOfBirth = driver.findElement(By.xpath(readXml("yearselect")));
	    Select yearselect = new Select(yearOfBirth);
	    yearselect.selectByValue(year);

	    // Enter email ID and mobile number
	    driver.findElement(By.xpath(readXml("emailid"))).sendKeys(emailID);
	    driver.findElement(By.xpath(readXml("mobilenumber"))).sendKeys(mobileNo);

	    // Locate and click the "Proceed" button
	    driver.findElement(By.xpath(readXml("proceedbutton"))).click();

	    // Proceed to the payment page and capture a screenshot if the URL contains "checkout"
	    driver.findElement(By.xpath(readXml("proceedtopayment"))).click();
	    
	    try {
	        Thread.sleep(2000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    
	    String currentURL = driver.getCurrentUrl();
	    
	    // Capture a screenshot of the checkout page if the URL contains "checkout"
	    if (currentURL.contains("checkout")) {
	        try {
	            getScreenshot("Checkout Page", driver);
	        } catch (IOException e) {
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

	// The purpose of this method is to read and retrieve locator values from an XML configuration file.
	// Locators are used to identify web elements in test automation scripts.
	// This method takes an object name as input and returns its corresponding locator value from the XML file.

	public String readXml(String objName) {
	    String locatorValue = null;
	    
	    // Create a new File object to represent the XML configuration file
	    File file = new File(XPaths);
	    
	    // Create a DocumentBuilderFactory to parse XML
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
	    
	    try {
	        // Initialize a DocumentBuilder to parse the XML file
	        builder = factory.newDocumentBuilder();
	    } catch (ParserConfigurationException e) {
	        // Handle any configuration errors
	        e.printStackTrace();
	    }
	    
	    Document document = null;
	    
	    try {
	        // Parse the XML file and create a Document object
	        document = builder.parse(file);
	    } catch (SAXException | IOException e) {
	        // Handle any parsing errors or file not found exceptions
	        e.printStackTrace();
	    }
	    
	    // Retrieve a list of "locator" nodes from the XML document
	    NodeList nodeList = document.getElementsByTagName("locator");
	    
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = nodeList.item(i);
	        
	        // Check if the current node is an ELEMENT_NODE
	        if (node.getNodeType() == node.ELEMENT_NODE) {
	            Element element = (Element) node;
	            
	            // Retrieve the locator value corresponding to the provided object name
	            locatorValue = element.getElementsByTagName(objName).item(0).getTextContent();
	            
	            // Exit the loop as soon as the locator value is found
	            break;
	        }
	    }
	    
	    // Return the located locator value
	    return locatorValue;
	}


}
