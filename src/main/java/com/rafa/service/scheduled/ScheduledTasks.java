package com.rafa.service.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    private static final int SECOND_TO_MILLIS = 1000 /* milis */;

    private static final int MINUTE_TO_MILLIS = 60 * SECOND_TO_MILLIS;

    private static final int HOUR_TO_MILIS = 60 * MINUTE_TO_MILLIS;

    public ScheduledTasks() {

    }
}
