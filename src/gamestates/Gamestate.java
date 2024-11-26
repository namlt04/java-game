package gamestates;

public enum Gamestate {
    PLAYING, MENU, OPTIONS, QUIT, NONE, CHOOSE;
    private static int sfx = 0;
    private static int music = 0;
    private static int index = 0;
    private static int unlockLevel = 0;
    private static Gamestate state = MENU;

    public static void setSfx(int value) {
        sfx = value;
    }

    public static int getSfx() {
        return sfx;
    }

    public static void setMusic(int value) {
        music = value;
    }

    public static int getMusic() {
        return music;
    }

    public static void setIndex(int value) {
        index = value;
    }

    public static int getIndex() {
        return index;
    }

    public static void setUnlockLevel(int value) {
        unlockLevel = value;
    }

    public static int getUnlockLevel() {
        return unlockLevel;
    }

    public static void setState(Gamestate value) {
        state = value;
    }

    public static Gamestate getState() {
        return state;
    }

}
