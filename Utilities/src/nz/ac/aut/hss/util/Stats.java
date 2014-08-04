package nz.ac.aut.hss.util;

/**
 * Created by zsb8604 on 30/07/2014.
 */
public class Stats {
    private long time;
    private long expectedValueSum;
            private int count;

    public void startMeasurement() {
        time = System.currentTimeMillis();
    }

    public void stopMeasurementAndAddValue() {
        time = System.currentTimeMillis() - time;
        expectedValueSum += time;
        count++;
    }

    public float getExpectedValue() {
        return (float) expectedValueSum / count;
    }
}
