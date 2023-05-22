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
public class GetBookingById {
    private static final Map<String, LocalDate> bookingsDates = new HashMap<>();
    private static final Map<String, Object> payload = new HashMap<>();

    @BeforeClass
    public static void setupTest() {
        baseURI = new Config().getBaseUrl();
        bookingsDates.put("checkin", LocalDate.now());
        bookingsDates.put("checkout", LocalDate.now());

        payload.put("firstname", "Test");
        payload.put("lastname", "ID");
        payload.put("totalprice", 380);
        payload.put("depositpaid", false);
        payload.put("bookingdates", bookingsDates);
        payload.put("additionalneeds", "Hamburguer");
    }

    @Test
    public void t01_getBookingById() {
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
                .get("/booking/" + bookingId)
                .then()
                .body("firstname", is("Test"))
                .body("lastname", is("ID"))
                .body("totalprice", is(380));
    }

    @Test
    public void t02_tryGetBookingWithInvalidId() {
        given()
                .contentType("application/json")
                .when()
                .get("/booking/101010")
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found");
    }
}
