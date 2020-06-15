package wdMethods;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;




public class ProjectMethods extends SeMethods {

    public String browserName = "chrome";
    public String url = "http://qainterview.merchante-solutions.com/admin" ;
    
    

    @BeforeSuite
    public void beforeSuite() {
        startResult();
    }

    @BeforeTest
    public void beforeTest() {
    }

    @AfterSuite
    public void afterSuite() {
        endResult();
    }

    @AfterTest
    public void afterTest() {
    }

    @AfterMethod
    public void afterMethod() {

    }

    

}
