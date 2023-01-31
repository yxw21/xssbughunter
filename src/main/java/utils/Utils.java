package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Login;
import okhttp3.*;

public class Utils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient();

    public static Login login(String username, String password) throws Exception {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://xss.cl/api/login")
                .post(body)
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody.string(), Login.class);
    }

    public static models.Request request(String req, String token) throws Exception {
        String json = String.format("{\"request\":\"%s\"}", req);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://xss.cl/api/requests")
                .header("authorization", "Bearer " + token)
                .post(body)
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody.string(), models.Request.class);
    }
}
