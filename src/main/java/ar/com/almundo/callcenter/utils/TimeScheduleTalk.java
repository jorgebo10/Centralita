package ar.com.almundo.callcenter.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeScheduleTalk implements Talk {
    private final static Logger logger = Logger.getLogger(TimeScheduleTalk.class.getName());
    private final int min;
    private final int max;

    public TimeScheduleTalk(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void talk() {
        randomDelay(min, max);
    }

    private void randomDelay(float min, float max) {
        int random = (int) (max * Math.random() + min);
        try {
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupted exception", e);
        }
    }
}
