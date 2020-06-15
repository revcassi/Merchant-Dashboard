package testCases.api;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import apiMethods.ApiMethods;
import io.restassured.response.Response;
import wdMethods.ProjectMethods;

public class UsersTest extends ProjectMethods {

    static ApiMethods api = new ApiMethods(); // To maintain object sessions 
    static String id = null;
    static String state = null;
    static Response postResponse = null;

    private void setEndPointAndHeaders() {
        api.wsISetEndPoint("http://qainterview.merchante-solutions.com:3030/");
    }
    
    // Validating the POST response

    public void createUsers() {
        postResponse = api.wsIPostRequest("/users", reqUsers()); // This will get response from POST Method
        id = api.wsIGetParameterValue(postResponse, "id");       // Getting Id from the response
        state = api.wsIGetParameterValue(postResponse, "address.State"); // Getting State from the response
        api.wsIValidateStatusCode("201"); // Checking the response status code as 201
    }
    
 // Validating the PUT response for StausCode and Params

    public void putUsers() {
        api.wsIPutRequest("/users/" + id, putReqUsers());  // Passing resources,id(PARAMS) and Request body for hitting PUT service
        api.wsIValidateStatusCode("200"); // Validating 200 status code
        api.wsIValidateExactParameterValue("name", 0, "UpdatedTest User"); // Validating param values and update in Report(result.html)
        api.wsIValidateExactParameterValue("email", 0, "updatedtest@test.com");
        api.wsIValidateExactParameterValue("username", 0, "UpdatedUserName");
    }
    
    // Validating the GET response for 200 Status Code

    public void getUsers() {
        api.wsIGetResponse("/users/", id); // Get Response using the Latest Id 
        api.wsIValidateStatusCode("200"); // Checking the response status code as 200
    }
    
    // reqBody for user- POST method

    private String reqUsers() {
        return "{\n" + "    \"address\": {\n" + "      \"Zip\": \"33333\",\n" + "      \"State\": \"GA\",\n"
                + "      \"Phone\": \"123-123-1234\",\n" + "      \"Street\": \"123 Williams Rd\",\n"
                + "      \"City\": \"Norcross\"\n" + "    },\n" + "    \"name\": \"Test User\",\n" + "    \"id\": 0,\n"
                + "    \"email\": \"test@test.com\",\n" + "    \"username\": \"Test User\"\n" + "  }";

    }
    
    // reqBody for user- PUT method

    private String putReqUsers() {
        return "{\n" + "    \"address\": {\n" + "      \"Zip\": \"33333\",\n" + "      \"State\": \"GA\",\n"
                + "      \"Phone\": \"123-123-1234\",\n" + "      \"Street\": \"123 Williams Rd\",\n"
                + "      \"City\": \"Norcross\"\n" + "    },\n" + "    \"name\": \"UpdatedTest User\",\n"
                + "    \"id\": 0,\n" + "    \"email\": \"updatedtest@test.com\",\n"
                + "    \"username\": \"UpdatedUserName\"\n" + "  }";

    }

    @BeforeMethod
    public void beforeMethod() {

        test = startTestCase("Users - api", "CRUD operations for Users");
    }

    @AfterMethod
    public void afterMethod() {
        endTestcase();
    }

    @Test
    public void test() {
        setEndPointAndHeaders();
        createUsers(); // Creating a User
        getUsers(); 
        api.wsIValidateExactParameterValue("name", 0, "Test User");       // Validate the user which we created on above step
        api.wsIValidateExactParameterValue("email", 0, "test@test.com");
        api.wsIValidateExactParameterValue("username", 0, "Test User");
        api.wsIValidateExactParameterValue("Zip", 0, "33333");
        putUsers();
        getUsers();
        api.wsIValidateExactParameterValue("name", 0, "UpdatedTest User");     // Validate the updated user which we created on above step
        api.wsIValidateExactParameterValue("email", 0, "updatedtest@test.com");
        api.wsIValidateExactParameterValue("username", 0, "UpdatedUserName");

    }

}
