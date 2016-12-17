package ru.rep1.game.black;

import ru.rep1.game.Constant;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lshi on 17.12.2016.
 */
public class TempController {
    private Temperature temperature;
    private Timer coolTimer;

    public TempController() {
        init();
    }

    public void init() {
        coolTimer = new Timer();
    }

    public void start() {
        cool();
    }

    public void stop() {
        coolTimer.cancel();
        coolTimer.purge();
    }

    public boolean isOverheat() {
        return temperature.getState() >= Temperature.MAX_STATE;
    }

    private void cool() {
        coolTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                temperature.dec();
            }
        }, 0, Constant.COOLING_SPEED);
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
}
