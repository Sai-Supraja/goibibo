# Goibibo Flight Booking Automation Test

This repository contains automation test scripts for booking flights on the Goibibo website using Selenium WebDriver and TestNG.

## Prerequisites

Before you can run the automation tests, make sure you have the following prerequisites installed on your system:

- [Eclipse](https://www.eclipse.org/downloads/)
- [TestNG plugin for Eclipse](http://testng.org/doc/download.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

## Getting Started

Follow these steps to set up and run the Goibibo Flight Booking Automation Test:

1. Clone this repository to your local machine:

   ```bash
   git clone [repo] (https://github.com/Sai-Supraja/goibibo.git)

## Setup and Configuration

### Eclipse IDE

1. **Open Eclipse IDE.**

### Import the Project

2. **Import the project into Eclipse:**

   - Go to `File` -> `Import`.
   - Select `Maven` -> `Existing Maven Projects`.
   - Browse to the cloned repository folder and select it.
   - Click `Finish`.

### TestNG Environment Setup

3. **Set up the TestNG environment:**

   - Make sure you have TestNG plugin installed in Eclipse.
   - Right-click on the project in the Eclipse Project Explorer.
   - Go to `Build Path` -> `Configure Build Path`.
   - Under the `Libraries` tab, ensure that the JRE System Library is set to Java 11.

### TestNG Suite Configuration

4. **Create a TestNG suite XML file for your test cases (e.g., `testng.xml`) if not already provided:**

   - Open the `testng.xml` file.
   - Configure the test suite according to your requirements, specifying the classes and methods to be executed.

### Running Tests

5. **Execute the automation tests:**

   - Right-click on the `testng.xml` file.
   - Select `Run As` -> `TestNG Suite` to execute the automation tests.

## Project Structure

The project structure is organized as follows:

- `src/test/java`: Contains the Java source code for the automation test cases.
- `src/main/resources`: Contains configuration files, if needed.
- `testng.xml`: TestNG suite configuration file.

## Test Cases

The automation test cases are defined in the `src/test/java` directory. You can add, modify, or extend test cases as needed to cover different scenarios for booking flights on the Goibibo website.

## Dependencies

The project's dependencies are managed using Maven. You can view and update the dependencies in the `pom.xml` file.

## Reporting

TestNG generates test reports that you can view in the Eclipse IDE. After running the tests, look for the `test-output` folder in your project directory. Open the HTML report to view the test results.
