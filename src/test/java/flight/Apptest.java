package flight;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * This class contains test methods for flight booking automation.
 */
public class Apptest extends Basetest {

	/**
	 * Test method for flight booking.
	 *
	 * @param testData A HashMap containing test data.
	 * @throws IOException                  if there is an I/O error.
	 * @throws SAXException                 if there is an XML parsing error.
	 * @throws ParserConfigurationException if there is a configuration error.
	 * @throws InterruptedException         if the thread is interrupted while
	 *                                      sleeping.
	 */
	@Test(dataProvider = "getData")
	// retryAnalyzer= TestComponents.Retrytests.class
	public void flightbooking(HashMap<String, String> testData)
			throws IOException, SAXException, ParserConfigurationException, InterruptedException {

		// Print the current URL
		System.out.println(driver.getCurrentUrl());

		// Create an instance of the BookingFlight class
		BookingFlight app = new BookingFlight(driver);

		// Sleep for 3 seconds
		Thread.sleep(3000);

		// Close the modal popup
		app.modalPopupClose();

		app.flightsLinkClick();
		app.selectRoundTrip();
		app.fromFlight(testData);
		app.toFlight(testData);
		app.departureDateSelect(testData, testData.get("departuredate"), testData.get("departuremonth"));
		app.arrivalDateSelect(testData, testData.get("arrivaldate"), testData.get("arrivalmonth"));
		app.addAdults(testData.get("adults"));
		app.addChildren(testData.get("children"));
		app.addInfants(testData.get("infants"));
		app.travelClass();
		app.searchFlights();
		app.bookFlight();
		app.fareSelectionPage();
		if (app.errorPopup()) {
			System.out.println("error pop-up retry logic");
			app.bookFlight();
			app.fareSelectionPage();
			app.verifyFareSummary(testData.get("adults"), testData.get("children"), testData.get("infants"));
			app.verifyTravellerErrors(testData.get("firstnameerror"), testData.get("dateofbirtherror"),
					testData.get("emailiderror"), testData.get("mobilenumbererror"));
			app.addTravellerDetails(testData.get("adult1"), testData.get("adult2"), testData.get("adult3"),
					testData.get("child1"), testData.get("child2"), testData.get("infant"), testData.get("dayofbirth"),
					testData.get("monthofbirth"), testData.get("yearofbirth"), testData.get("emailid"),
					testData.get("mobilenumber"), testData.get("adultgender"), testData.get("childgender"),
					testData.get("infantgender"));
		} else {
			System.out.println("No pop-up logic- Normal flow");
			app.verifyFareSummary(testData.get("adults"), testData.get("children"), testData.get("infants"));
			app.verifyTravellerErrors(testData.get("firstnameerror"), testData.get("dateofbirtherror"),
					testData.get("emailiderror"), testData.get("mobilenumbererror"));
			app.addTravellerDetails(testData.get("adult1"), testData.get("adult2"), testData.get("adult3"),
					testData.get("child1"), testData.get("child2"), testData.get("infant"), testData.get("dayofbirth"),
					testData.get("monthofbirth"), testData.get("yearofbirth"), testData.get("emailid"),
					testData.get("mobilenumber"), testData.get("adultgender"), testData.get("childgender"),
					testData.get("infantgender"));
		}

	}

	/**
	 * Data provider method to supply test data.
	 *
	 * @return An array of test data in the form of HashMaps.
	 * @throws IOException if there is an I/O error.
	 */
	@DataProvider
	public Object[][] getData() throws IOException {

		// Get test data from a JSON file
		List<HashMap<String, String>> data = getJsonData(
				System.getProperty("user.dir") + "//src//test//java//DataReader//testData.json");

		// Return test data as a 2D array
		return new Object[][] { { data.get(0) } };
	}
}