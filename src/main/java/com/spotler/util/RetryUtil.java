package com.spotler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RetryUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryUtil.class);

    public static <T> T retry(String taskName, Callable<T> task, int maxRetries) {
        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                return task.call();
            } catch (Exception e) {
                LOGGER.error("[{}] Error occurred!", taskName, e);
                attempt++;
                if (attempt > maxRetries) {
                    throw new RuntimeException(String.format("[%s] Max retries reached!", taskName));
                }
                LOGGER.warn("[{}] Retrying... attempt {}/{}", taskName, attempt, maxRetries);
            }
        }
        throw new RuntimeException("Max retries reached!");
    }

    public static void retry(String taskName, Runnable task, int maxRetries) {
        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                task.run();
                return;
            } catch (Exception e) {
                LOGGER.error("[{}] Error occurred!", taskName, e);
                attempt++;
                if (attempt > maxRetries) {
                    throw new RuntimeException(String.format("[%s] Max retries reached!", taskName));
                }
                LOGGER.warn("[{}] Retrying... attempt {}/{}", taskName, attempt, maxRetries);
            }
        }
        throw new RuntimeException("Max retries reached!");
    }
}
