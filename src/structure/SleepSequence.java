package structure;

import org.powbot.api.Random;

public interface SleepSequence {
    default int getLongSleepTime() {
        return Random.nextInt(2200, 3000);
    }

    default int getMediumSleepTime() {
        return Random.nextInt(1400, 2200);
    }

    default int getShortSleepTime() {
        return Random.nextInt(900, 1400);
    }

    default int getVeryShortSleepTime() {
        return Random.nextInt(90, 120);
    }

}
