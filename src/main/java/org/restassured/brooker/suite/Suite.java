package org.restassured.brooker.suite;

import org.junit.runner.RunWith;
import org.restassured.brooker.scenarios.*;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        Token.class,
        CreateBooking.class,
        GetAllBookings.class,
        GetBookingById.class,
        PartialUpdate.class,
        UpdateBooking.class
})
public class Suite {
}
