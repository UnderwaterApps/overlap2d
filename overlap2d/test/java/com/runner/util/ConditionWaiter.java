package com.runner.util;

import com.runner.exception.TimeoutException;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class ConditionWaiter {
    public static void wait(Condition condition, String message, final int maxCount, final Runnable runnable) {
        int current = 0;
        while (!condition.check()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {

            }
            current++;
            if (current > maxCount) {
                if (runnable != null) {
                    runnable.run();
                }
                throw new TimeoutException(message);
            }
        }
    }

    public static void wait(Condition condition, String message, final int maxCount) {
        wait(condition, message, maxCount, null);
    }

    public static void wait(Condition condition, Description description, final int maxCount, final Runnable runnable) {
        wait(condition, "timeout! " + description.toString(), maxCount, runnable);
    }
}
