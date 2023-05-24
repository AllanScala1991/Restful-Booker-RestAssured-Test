package org.restassured.brooker.scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restassured.brooker.helpers.Config;
import org.restassured.brooker.helpers.TokenService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Token {
    @BeforeClass
    public static void setupTest() {
        baseURI = new Config().getBaseUrl();
    }

    @Test
    public void generateTokenSuccessfully() {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", "admin");
        payload.put("password", "password123");

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/auth")
                .then()
                .body("token", is(notNullValue()));
    }

    @Test
    public void tryCreateTokenWithInvalidUsername() {
        Map<String, String> payloadError = new HashMap<>();
        payloadError.put("username", "invalid");
        payloadError.put("password", "password123");

        given()
                .contentType("application/json")
                .body(payloadError)
                .when()
                .post("/auth")
                .then()
                .body("reason", is("Bad credentials"));
    }

    @Test
    public void tryCreateTokenWithInvalidBody() throws IOException {
        Map<String, String> payloadInvalid = new HashMap<>();
        payloadInvalid.put("user", "admin");
        payloadInvalid.put("pass", "password123");

        given()
                .contentType("application/json")
                .body(payloadInvalid)
                .when()
                .post("/auth")
                .then()
                .body("reason", is("Bad credentials"));
    }
}
