package com.bytehonor.sdk.jdbc.bytehonor.scheduler.plan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.bytehonor.sdk.jdbc.bytehonor.scheduler.time.TimeCron;

public abstract class AccurateTimeJobPlan implements SchedulerPlan {

    @Override
    public final boolean accept(LocalDateTime ldt) {
        List<TimeCron> timeCrons = timeCrons();
        // 没有配置的则false
        if (timeCrons == null || timeCrons.isEmpty()) {
            return false;
        }
        AtomicInteger ai = new AtomicInteger(0);
        timeCrons.parallelStream().forEach(item -> {
            if (item.match(ldt)) {
                ai.incrementAndGet();
            }
        });
        return ai.get() > 0;
    }

    public abstract List<TimeCron> timeCrons();
}
