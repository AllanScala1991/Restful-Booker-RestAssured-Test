package org.restassured.brooker.scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restassured.brooker.helpers.Config;
import org.restassured.brooker.helpers.TokenService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UpdateBooking {
    private static Integer id;
    private static String token;
    private static final Map<String, LocalDate> bookingsDates = new HashMap<>();
    private static final Map<String, Object> payload = new HashMap<>();

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
        bookingsDates.put("checkin", LocalDate.now());
        bookingsDates.put("checkout", LocalDate.now());
        payload.put("firstname", "Update Name");
        payload.put("lastname", "Update Lastname");
        payload.put("totalprice", 789);
        payload.put("depositpaid", false);
        payload.put("bookingdates", bookingsDates);
        payload.put("additionalneeds", "Update additional");
    }

    @Test
    public void updateBookingById() {
        given()
                .contentType("application/json")
                .cookie("token", token)
                .body(payload)
                .when()
                .put("/booking/" + id)
                .then()
                .body("firstname", is("Update Name"))
                .body("lastname", is("Update Lastname"))
                .body("totalprice", is(789))
                .body("depositpaid", is(false));
    }

    @Test
    public void tryUpdateBookingByIdWithNoSendToken() {
        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .put("/booking/" + id)
                .then()
                .statusCode(403)
                .statusLine("HTTP/1.1 403 Forbidden");
    }

    @Test
    public void tryUpdateBookingWithSendInvalidId() {
        given()
                .contentType("application/json")
                .cookie("token", token)
                .body(payload)
                .when()
                .put("/booking/invalid")
                .then()
                .statusCode(405)
                .statusLine("HTTP/1.1 405 Method Not Allowed");
    }

    @Test
    public void tryUpdateBookingWithoutPayload() {
        given()
                .contentType("application/json")
                .cookie("token", token)
                .when()
                .put("/booking/" + id)
                .then()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 Bad Request");
    }
}
