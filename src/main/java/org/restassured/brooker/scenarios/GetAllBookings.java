package org.restassured.brooker.scenarios;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restassured.brooker.helpers.Config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetAllBookings {
    private static final Map<String, LocalDate> bookingsDates = new HashMap<>();
    private static final Map<String, Object> payload = new HashMap<>();

    @BeforeClass
    public static void setupTest() {
        baseURI = new Config().getBaseUrl();

        bookingsDates.put("checkin", LocalDate.now());
        bookingsDates.put("checkout", LocalDate.now());

        payload.put("firstname", "Test");
        payload.put("lastname", "Filter");
        payload.put("totalprice", 300);
        payload.put("depositpaid", true);
        payload.put("bookingdates", bookingsDates);
        payload.put("additionalneeds", "Ifood");
    }

    @Test
    public void t01_getAllBookings() {
        given()
                .contentType("application/json")
                .when()
                .get("/booking")
                .then()
                .body("size()", is(greaterThan(0)))
                .body("[0].bookingid", is(notNullValue()));
    }

    @Test
    public void t02_filterBookingByName() {
        String bookingId = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/booking")
                .then()
                .extract().path("bookingid").toString();

        given()
                .contentType("application/json")
                .when()
                .get("/booking?firstname=Test&lastname=Filter")
                .then()
                .body("bookingid", hasItem(Integer.parseInt(bookingId)));
    }
}
