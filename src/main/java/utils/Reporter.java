package utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class Reporter implements ExtentReporter {
    public static ExtentTest test;   // Extent Report
    public static ExtentReports extent; 
    public String testCaseName, testDescription, category, authors;
    
    // To update TC description and status to the report

    public void reportStep(String desc, String status) {  

        long snapNumber = 100000l;  // Append this number to the .jpg 

        try {
            if (!desc.contains("-api")) {  // This snapshot for UI
                snapNumber = takeSnap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        if (!desc.contains("-api")) {        // UI Logging
            // Write if it is successful or failure or information
            if (status.toUpperCase().equals("PASS")) {
                test.log(LogStatus.PASS, desc + test.addScreenCapture("./../reports/images/" + snapNumber + ".jpg")); // Pass + screenshot
            } else if (status.toUpperCase().equals("FAIL")) {
                test.log(LogStatus.FAIL, desc + test.addScreenCapture("./../reports/images/" + snapNumber + ".jpg")); // Fail + screenshot
                throw new RuntimeException("FAILED");
            } else if (status.toUpperCase().equals("INFO")) {
                test.log(LogStatus.INFO, desc);
            } else if (status.toUpperCase().equals("WARN")) {
                test.log(LogStatus.WARNING, desc + test.addScreenCapture("./../reports/images/" + snapNumber + ".jpg"));
            }
        } else {                                           
            if (status.toUpperCase().equals("PASS")) {           // API Logging
                test.log(LogStatus.PASS, desc);
            } else if (status.toUpperCase().equals("FAIL")) {
                test.log(LogStatus.FAIL, desc);
                throw new RuntimeException("FAILED");
            } else if (status.toUpperCase().equals("INFO")) {
                test.log(LogStatus.INFO, desc);
            } else if (status.toUpperCase().equals("WARN")) {
                test.log(LogStatus.WARNING, desc);
            }
        }
    }

    public abstract long takeSnap(); 

    public ExtentReports startResult() {
        try {
            FileUtils.deleteDirectory(new File("./reports"));  // Delete existing Report dir
        } catch (IOException e) {
            e.printStackTrace();
        }
        extent = new ExtentReports("./reports/result.html", false);  // Creating new report
        extent.loadConfig(new File("./extent.xml")); 
        System.out.println("Started results");
        return extent;
    }

    public ExtentTest startTestCase(String testCaseName, String testDescription) {
        test = extent.startTest(testCaseName, testDescription);  // Start reporting
        return test;
    }

    public void endResult() {
        extent.flush();          // Report is ready to view
    }

    public void endTestcase() {
        extent.endTest(test);     // Ending of Report
    }

}