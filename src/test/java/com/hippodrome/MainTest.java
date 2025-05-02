package com.hippodrome;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

 class MainTest {

    @Test
    @Timeout(value = 22, unit = TimeUnit.SECONDS)
    @Disabled("Disabled to exclude from regular runs")
    void checkMainExecutionWithinTimeout() throws Exception {
        Main.main(new String[]{});
    }
}
