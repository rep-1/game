package ru.rep1.game;

/**
 * Created by lshi on 17.11.2016.
 */
public class Constant {
    public static final int ORIGINAL_WIDTH = 1920;
    public static final int ORIGINAL_HEIGHT = 1080;
    public static final int GAME_WIDTH = 1600;
    public static final int GAME_HEIGHT = 900;
    public static final double GLOBAL_SCALE = ORIGINAL_WIDTH / (double)GAME_WIDTH;
    public static final double GLOBAL_SCALE_REV = 1.0D/ GLOBAL_SCALE;

    public static final long COOLING_SPEED = 2000;
    public static final double TARGET_SPEED = 3D;

    public static final int BULLETHOLDER_MAX = 10;
    public static final int BULLETHOLDER_WAIT = 500;
    public static final int BULLETHOLDER_RELOAD = 2000;

    public enum State {
        INTRO,
        PLAY,
        RELOAD,
        OUTRO,
        FINISH
    }

    public enum Event {
        ON_GAME_START,
        ON_GAME_PLAY,
        ON_RELOAD_START,
        ON_RELOAD_END,
        ON_TARGET_IN_PLACE,
        ON_GAME_OUTRO,
        ON_GAME_FINISH,

        ON_FIRE,
        TARGET_SHOT,
        TARGET_KILLED
    }

}
