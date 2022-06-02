package com.bytehonor.sdk.server.bytehonor.scheduler.time;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.scheduler.time.AccurateTimeCron;
import com.bytehonor.sdk.jdbc.bytehonor.scheduler.time.AccurateTimeCronBuilder;

public class AccurateTimeCronBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(AccurateTimeCronBuilderTest.class);

    @Test
    public void test() {
        List<AccurateTimeCron> list = AccurateTimeCronBuilder.make().mintueAt(2, 11, 22, 31, 43, 52)
                .hourAt(10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21).build();
        boolean isOk = list != null && list.size() == (6 * 11);

        for (AccurateTimeCron cron : list) {
            LOG.info("m:{}, h:{}, d:{}", cron.getMinute(), cron.getHour(), cron.getDay());
        }

        assertTrue("*testGet*", isOk);
    }

}
