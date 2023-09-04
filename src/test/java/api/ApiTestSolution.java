package api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;

public class ApiTestSolution {

    // This method performs a GET request to a given API URL and validates its schema.
    // It checks if the response status code is 200, the content type is JSON, and specific JSON fields have expected values.
    public static void performGetRequestAndValidateSchema(String apiUrl) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet);
        
        // Validate that the response status code is 200 (OK).
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        
        HttpEntity entity = response.getEntity();
        
        // Check that the content type of the response is JSON.
        Assert.assertTrue(entity.getContentType().getValue().contains("application/json"));
        
        String responseBody = EntityUtils.toString(entity);
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        // Validate specific fields in the JSON response.
        Assert.assertEquals(jsonResponse.getInt("userId"), 1);
        Assert.assertEquals(jsonResponse.getInt("id"), 1);
        Assert.assertEquals(jsonResponse.getString("title"), "delectus aut autem");
        Assert.assertFalse(jsonResponse.getBoolean("completed"));
    }

    // This method performs a POST request to a given API URL with a request body and validates the response.
    // It checks if the response status code is 201 (Created) and validates the JSON response.
    // If the response status code is not as expected, it handles negative responses.
    @SuppressWarnings("deprecation")
	public static void performPostRequest(String apiUrl, String requestBody) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);
        
        // Set the request body and content type.
        httpPost.setEntity(new StringEntity(requestBody, "application/json", "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        
        // Validate that the response status code is 201 (Created).
        Assert.assertEquals(statusCode, 201);

        if (statusCode == 201) {
            // If the status code is 201, validate the response.
            validatePostResponse(response);
        } else {
            // Handle negative responses for other status codes.
            handleNegativeResponse(statusCode);
        }
    }

    // This method validates the response of a successful POST request.
    public static void validatePostResponse(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        
        // Check that the content type of the response is JSON.
        Assert.assertTrue(entity.getContentType().getValue().contains("application/json"));
        
        String responseBody = EntityUtils.toString(entity);
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        // Validate specific fields in the JSON response.
        int id = jsonResponse.getInt("id");
        Assert.assertEquals(id, 101);
        Assert.assertNotEquals(id, 103);
    }

    // This method handles negative responses based on different status codes.
    public static void handleNegativeResponse(int statusCode) {
        switch (statusCode) {
            case 400:
                // Handle a 400 status code (Bad Request).
                Assert.assertEquals(statusCode, 400);
                break;
            case 409:
                // Handle a 409 status code (Conflict).
                Assert.assertEquals(statusCode, 409);
                break;
            case 415:
                // Handle a 415 status code (Unsupported Media Type).
                Assert.assertEquals(statusCode, 415);
                break;
            default:
                // Handle unexpected status codes by failing the test.
                Assert.fail("Unexpected status code: " + statusCode);
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        String getApiUrl = "https://jsonplaceholder.typicode.com/todos/1";
        String postApiUrl = "https://jsonplaceholder.typicode.com/posts";
        String postRequestBody = "{ \"id\": \"103\" }";

        // Perform a GET request and validate its schema.
        performGetRequestAndValidateSchema(getApiUrl);
        
        // Perform a POST request with a request body and validate the response.
        performPostRequest(postApiUrl, postRequestBody);
    }
}
