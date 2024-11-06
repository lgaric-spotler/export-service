package com.spotler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressLoggerUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressLoggerUtil.class);

    public static Integer logProgress(String progressName, int current, int total, Integer lastLoggedProgress) {
        int progress = (current + 1) * 100 / total;
        if (progress - lastLoggedProgress >= 20) {
            LOGGER.info("[{}] Progress: {}%", progressName, progress);
            lastLoggedProgress = progress;
        }

        return lastLoggedProgress;
    }
}
