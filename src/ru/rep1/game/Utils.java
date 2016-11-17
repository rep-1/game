package ru.rep1.game;

import java.util.Random;

/**
 * Created by lshi on 17.11.2016.
 */
public class Utils {
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
