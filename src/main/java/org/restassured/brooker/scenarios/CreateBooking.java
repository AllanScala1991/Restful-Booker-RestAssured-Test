package org.restassured.brooker.scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restassured.brooker.helpers.Config;
import static org.hamcrest.Matchers.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateBooking {
    private static final Map<String, LocalDate> bookingsDates = new HashMap<>();
    private static final Map<String, Object> payload = new HashMap<>();

    @BeforeClass
    public static void setupTest() {
        baseURI = new Config().getBaseUrl();
        bookingsDates.put("checkin", LocalDate.now());
        bookingsDates.put("checkout", LocalDate.now());

        payload.put("firstname", "James");
        payload.put("lastname", "Rodrigues");
        payload.put("totalprice", 300);
        payload.put("depositpaid", true);
        payload.put("bookingdates", bookingsDates);
        payload.put("additionalneeds", "Ifood");
    }

    @Test
    public void t01_createNewBookingSuccessfully() {
        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", is(notNullValue()))
                .body("booking.firstname", is("James"));
    }

    @Test
    public void t02_tryCreateBookingWithInvalidPayloadKeys() {
        Map<String, Object> invalidPayloadKeys = payload;
        invalidPayloadKeys.remove("firstname");
        invalidPayloadKeys.replace("lastname", "");

        given()
                .contentType("application/json")
                .body(invalidPayloadKeys)
                .when()
                .post("/booking")
                .then()
                .statusCode(500)
                .statusLine("HTTP/1.1 500 Internal Server Error");
    }
}
