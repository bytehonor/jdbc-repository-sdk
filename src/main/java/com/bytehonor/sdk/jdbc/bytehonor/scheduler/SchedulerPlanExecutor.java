package com.bytehonor.sdk.jdbc.bytehonor.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bytehonor.sdk.define.bytehonor.task.SafeTask;
import com.bytehonor.sdk.jdbc.bytehonor.scheduler.plan.SchedulerPlan;

public class SchedulerPlanExecutor {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(1);

    public static void run(final LocalDateTime ldt) {
        Objects.requireNonNull(ldt, "ldt");

        List<SchedulerPlan> plans = SchedulerPlanFactory.plans();
        for (SchedulerPlan plan : plans) {
            if (plan.accept(ldt)) {
                doRun(plan.createTask(ldt));
            }
        }
    }

    public static void runParallel(final LocalDateTime ldt) {
        Objects.requireNonNull(ldt, "ldt");

        List<SchedulerPlan> plans = SchedulerPlanFactory.plans();
        plans.parallelStream().forEach(plan -> {
            if (plan.accept(ldt)) {
                doRun(plan.createTask(ldt));
            }
        });
    }

    public static void doRun(SafeTask task) {
        if (task == null) {
            return;
        }
        SERVICE.execute(task);
    }

}
