package com.bytehonor.sdk.jdbc.bytehonor.scheduler.plan;

import java.time.LocalDateTime;

import com.bytehonor.sdk.define.bytehonor.task.SafeTask;

public interface SchedulerPlan {

    public boolean accept(LocalDateTime ldt);

    public SafeTask createTask(LocalDateTime ldt);
}
