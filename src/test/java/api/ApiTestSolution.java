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

    public static void performGetRequestAndValidateSchema(String apiUrl) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet);
        
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        HttpEntity entity = response.getEntity();
        Assert.assertTrue(entity.getContentType().getValue().contains("application/json"));
        
        String responseBody = EntityUtils.toString(entity);
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        Assert.assertEquals(jsonResponse.getInt("userId"), 1);
        Assert.assertEquals(jsonResponse.getInt("id"), 1);
        Assert.assertEquals(jsonResponse.getString("title"), "delectus aut autem");
        Assert.assertFalse(jsonResponse.getBoolean("completed"));
    }

    @SuppressWarnings("deprecation")
	public static void performPostRequest(String apiUrl, String requestBody) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);
        
        httpPost.setEntity(new StringEntity(requestBody, "application/json", "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 201);

        if (statusCode == 201) {
            validatePostResponse(response);
        } else {
            handleNegativeResponse(statusCode);
        }
    }

    public static void validatePostResponse(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        Assert.assertTrue(entity.getContentType().getValue().contains("application/json"));
        
        String responseBody = EntityUtils.toString(entity);
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        int id = jsonResponse.getInt("id");
        Assert.assertEquals(id, 101);
        Assert.assertNotEquals(id, 103);
    }

    public static void handleNegativeResponse(int statusCode) {
        switch (statusCode) {
            case 400:
                Assert.assertEquals(statusCode, 400); // Bad Request
                break;
            case 409:
                Assert.assertEquals(statusCode, 409); // Conflict
                break;
            case 415:
                Assert.assertEquals(statusCode, 415); // Unsupported Media Type
                break;
            default:
                Assert.fail("Unexpected status code: " + statusCode);
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        String getApiUrl = "https://jsonplaceholder.typicode.com/todos/1";
        String postApiUrl = "https://jsonplaceholder.typicode.com/posts";
        String postRequestBody = "{ \"id\": \"103\" }";

        performGetRequestAndValidateSchema(getApiUrl);
        performPostRequest(postApiUrl, postRequestBody);
    }
}
