package ru.rep1.game;

/**
 * Created by lshi on 17.11.2016.
 */
public class Constant {
    public static final int GAME_WIDTH = 860;
    public static final int GAME_HEIGHT = 624;

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
        ON_GAME_FINISH
    }

}
