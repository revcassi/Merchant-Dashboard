package apiMethods;

import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import com.github.wnameless.json.flattener.JsonFlattener;  // Returns path and gives it to framework and it return value for the key
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import wdMethods.ProjectMethods;

public class ApiMethods extends ProjectMethods {

    public static Response response = null;
    public static RequestSpecification request = null;
    
    /**
     * 
     * @author Revathy Cassilingam - This method will set the configuration for Rest Assured
     */
    
    
    private void restAssuredConfiguration() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.useRelaxedHTTPSValidation();
        request = RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when();
    }

    /**
     * @param endPoint - This sets the endpoint of the service Description: This
     *                 method will set the Endpoint for all the scenarios at once
     */
    public void wsISetEndPoint(String endPoint) {
        RestAssured.baseURI = endPoint;
    }

    /**
     * @param key   - the key of the service
     * @param value - the value of the services Description: This method will set
     *              the Headers for all the scenarios at once
     */
    public void wsISetHeaders(String key, String value) {
        request = request.headers(key, value);
    }

    /**
     * @param resourcePath - ResourcePath of service
     * 
     * @param params       - The params of the service ex:/v1/api/conditions/{id} -
     *                     Id is the param Description: This method will GET the
     *                     response from service using (resourcePath+params)  and return
     *                     response as STRING using GetService.
     * @return - Returns a Response
     */

    public Response wsIGetResponse(String resourcePath, String params) {
        restAssuredConfiguration();
        response = request.get(resourcePath + params).then().contentType(ContentType.JSON).extract().response();
        response.then().log().all();
        return response;
    }

    /**
     * @param resourcePath - ResourcePath of service ex:/v1/api/conditions
     * @param params       - The params of the service ex:/v1/api/conditions/{id} -
     *                     Id is the param
     * @param requestBody  - method will POST the request to the service using
     *                     (resourcePath + req body Json) and return response as STRING
     * @return - returns a response
     */

    public Response wsIPostRequest(String resourcePath, String requestBodyJson) {
        restAssuredConfiguration();
        response = request.when().body(requestBodyJson).post(resourcePath).then().contentType(ContentType.JSON)
                .extract().response(); // Getting response from the service using POST method
        response.then().log().all(); //  Printing in the console
        return response;
    }

    /**
     * @param resourcePath    - ResourcePath of service ex:/v1/api/conditions
     * @param params          - The params of the service ex:/v1/api/conditions/{id}
     *                        - Id is the param
     * @param requestBodyJson -json Body as String
     * @return - Response
     */

    public Response wsIPutRequest(String resourcePath, String requestBodyJson) {
        restAssuredConfiguration();
        response = request.when().body(requestBodyJson).put(resourcePath).then().extract().response();
        response.then().log().all(); // All service details, req, response,etc-Printing in the console
        return response;
    }

    /**
     * @param resourcePath            - ResourcePath of service
     *                                ex:/v1/api/conditions
     * @param params                  - The params of the service
     *                                ex:/v1/api/conditions/{id} - Id is the param
     * @param requestBodyJsonFilePath - src/test/resources/Json - Default path where
     *                                we created method to read it Description: This
     *                                method will DELETE the entry from service
     *                                using (resourcePath+params)
     * @return - returns a response
     */
    public Response wsIDeleteRequest(String resourcePath, String params) {
        restAssuredConfiguration();
        response = request.when().delete(resourcePath + params).then().extract().response();
        response.then().log().all();
        return response;
    }

    /**
     * @param jsonPath - This method will help you to GET the particular parameter
     *                 value using Json path and the parameter value is return as
     *                 STRING
     * @return - Returns a string
     */
    
    // This returns String value for the JSONPATH
    public String wsIGetParameterValue(String jsonPath) {
        Object jsonObject = response.jsonPath().get(jsonPath);
        return jsonObject.toString();
    }

    /**
     * @param response - Response which is received in any method
     * @param jsonPath - JsonPath of the particualar param.
     * @return - Returns a String
     */

    public String wsIGetParameterValue(Response response, String jsonPath) {
        Object jsonObject = response.jsonPath().get(jsonPath);  
        return jsonObject.toString();
    }

    /**
     * @param parameterName - Name of the parameter
     * @param index         - It denotes which parameter it is Eg: 2nd Id of the
     *                      response, Here Id is parameterName and 2 is Index value
     *                      Description: This method will help you to GET JSON PATH
     *                      for the particular parameter value using parameterName
     *                      and the jsonPath is return as STRING
     * @return - Returns a string
     */
    
    // This method is used to return the params(JSON PATH) value using Index

    public String wsIGetJsonPathOfParameter(String parameterName, int index) {
        List<String> listPath = new ArrayList<String>();
        String paths = null;
        String flatten = JsonFlattener.flatten(response.asString()); // Split all JsonPath for the given Parameter and given index from the Response.
        String[] split = flatten.split(",");
        for (String string : split) {
            String checkName = "";
            if (string.split(":")[0].contains(".")) {

                String[] split2 = string.split(":");
                String checkSplit = split2[0].toString().replaceAll("\"", "");
                String[] paraName = checkSplit.split("[.]");
                checkName = paraName[paraName.length - 1];
                if (checkName.contains("{")) {
                    checkName = checkName.replace("{", "");
                } else if (checkName.contains("}")) {
                    checkName = checkName.replace("}", "");
                }

            } else {
                String[] split2 = string.split(":");
                String checkSplit = split2[0].toString().replaceAll("\"", "");
                checkName = checkSplit;
                if (checkName.contains("{")) {
                    checkName = checkName.replace("{", "");
                } else if (checkName.contains("}")) {
                    checkName = checkName.replace("}", "");
                }
            }
            if (string.contains(parameterName) && checkName.equals(parameterName)) {
                String[] split2 = string.split(":");
                paths = split2[0].toString().replaceAll("\"", "");
                listPath.add(paths);
            }
        }
        String paramPath = listPath.get(index);
        if (paramPath.contains("{")) {
            paramPath = paramPath.replace("{", "");
        } else if (paramPath.contains("}")) {
            paramPath = paramPath.replace("}", "");
        }
        return paramPath;
    }

    /**
     * @param expectedStatusCode - Status code which needs to be validated ex:200
     *                           Description: This method will validate the Status
     *                           code of the service and User need to pass expected
     *                           status code to this method and It will return
     *                           Boolean value
     * @return - A true or false
     */
   // Validating the status code from the response
    
    public Boolean wsIValidateStatusCode(String expectedStatusCode) {
        if (response.getStatusCode() != parseInt(expectedStatusCode)) { // Get Status code Latest Response 
            iLogErrorMessage(
                    "Expected status code was " + expectedStatusCode + " but received " + response.getStatusCode());
            return false;
        }
        return true;
    }
    
    // Validating the response values and the expected values

    public Boolean wsIValidateExactParameterValue(String parameterName, int indexOfParameter, String expectedValue) {
        String paramValue = wsIGetParameterValueUsingParameterName(parameterName, indexOfParameter); // Got value from the latest response
        if (paramValue.equals(expectedValue)) {
            iLogMessage("Expected status code was " + expectedValue + " but received " + paramValue); // Report get details from here as PASS
        } else {
            iLogErrorMessage("Expected status code was " + expectedValue + " but received " + paramValue); // Report get details from here as FAIL
        }
        return paramValue.equals(expectedValue);
    }
    
    
   // Getting Parameter value from the response
    
    public String wsIGetParameterValueUsingParameterName(String parameterName, int index) {
        String wsIGetJsonPathOfParameter = wsIGetJsonPathOfParameter(parameterName, index); // This will give JSONpath(String) for params - Key
        String wsIGetParameterValue = wsIGetParameterValue(wsIGetJsonPathOfParameter); // This will give value for above JSONpath(String) - Value
        return wsIGetParameterValue; // Return value
    }


}