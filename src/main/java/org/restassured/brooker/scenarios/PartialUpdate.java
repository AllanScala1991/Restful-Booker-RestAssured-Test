package org.restassured.brooker.scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restassured.brooker.helpers.Config;
import org.restassured.brooker.helpers.TokenService;

import java.io.IOException;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PartialUpdate {
    private static Integer id;
    private static String token;

    @BeforeClass
    public static void setupTest() throws IOException {
        baseURI = new Config().getBaseUrl();
        id = given()
                .contentType("application/json")
                .when()
                .get("/booking")
                .then()
                .extract().path("[0].bookingid");

        token = new TokenService().getToken("admin", "password123").toString();
    }

    @Test
    public void t01_partialUpdateBookingByID() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("totalprice", 299);
        payload.put("additionalneeds", "food");

        given()
                .contentType("application/json")
                .cookie("token", token)
                .body(payload)
                .when()
                .patch("/booking/" + id)
                .then()
                .body("totalprice", is(299))
                .body("additionalneeds", is("food"));
    }

    @Test
    public void t02_tryPartialUpdateBookingWithEmptyToken() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("totalprice", 599);
        payload.put("additionalneeds", "pizza");

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .patch("/booking/" + id)
                .then()
                .statusCode(403)
                .statusLine("HTTP/1.1 403 Forbidden");
    }

    @Test
    public void t03_tryPartialUpdateBookingWithSendInvalidId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("totalprice", 125);
        payload.put("additionalneeds", "hamburguer");

        given()
                .contentType("application/json")
                .cookie("token", token)
                .body(payload)
                .when()
                .patch("/booking/" + "invalid")
                .then()
                .statusCode(405)
                .statusLine("HTTP/1.1 405 Method Not Allowed");
    }
}
