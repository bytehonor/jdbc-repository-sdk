package com.bytehonor.sdk.server.bytehonor.scheduler.time;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import com.bytehonor.sdk.jdbc.bytehonor.scheduler.time.AccurateTimeCron;
import com.bytehonor.sdk.jdbc.bytehonor.scheduler.time.TimeCron;

public class AccurateTimeCronTest {

    @Test
    public void testMatch() {
        int minute = 2;
        int hour = 3;
        AccurateTimeCron cron = new AccurateTimeCron(minute, hour, TimeCron.ANY);
        LocalTime lt = LocalTime.of(hour, minute, 0);
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), lt);

        boolean isOk = cron.match(ldt);
        assertTrue("*testMatch*", isOk);
    }

}
