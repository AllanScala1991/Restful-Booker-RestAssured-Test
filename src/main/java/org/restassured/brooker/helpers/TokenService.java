package org.restassured.brooker.helpers;

import com.google.gson.JsonObject;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class TokenService {
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = new Config().getBaseUrl();
    public Object getToken(String username, String password) throws IOException {
        RequestBody payload = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/auth")
                .post(payload)
                .build();

        Response response = client.newCall(request).execute();
        JSONObject body = new JSONObject(response.body().string());
        return  body.get("token");
    }
}
