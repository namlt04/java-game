package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {

    public static final String PLAYER = "king.png";
    // CONSTANT LOAD OBJECT ------------------------------------
    public static final String HEART_COLLECT = "heart_collect.png";
    public static final String HEART_HEALTH = "heart_health.png";
    public static final String DIAMOND_COLLECT = "diamond_collect.png";
    public static final String DIAMOND_COUNT = "diamond_count.png";
    public static final String BOMBBYPIG = "bombbypig.png";
    public static final String PIGTHROWBOMB = "pigthrowbomb.png";
    public static final String BOX = "box.png";
    public static final String DOOR = "door.png";
    // CONSTANT LOAD UI ---------------------------------------
    public static final String LIVE_BAR = "live_bar.png";
    public static final String BUTTONS = "buttons.png";
    public static final String NUMBER = "number.png";
    public static final String OPACITY = "opacity.png";
    public static final String NAME = "game_name.png";
    public static final String BUTTON = "sound_button.png";
    public static final String GAMEOVER = "gameover.png";
    public static final String GAMECOMPLETE = "gamecomplete.png";
    public static final String GAMEOPTIONS = "options.png";
    public static final String PAUSED = "paused.png";
    public static final String DECOR = "decor.png";
    public static final String GUIDE = "guide.png";
    public static final String BUTTONSCHOOSE = "buttonschoose.png";
    // CONSTANT LOAD ENEMY -----------------------------------
    public static final String PIG = "pig.png";
    public static final String BALL = "ball.png";
    public static final String CANON = "canon.png";
    public static final String KINGPIG = "kingpig.png";
    //CONSTANT LOAD FOLDER -----------------------------------
    public static final String BACKGROUNDGIF = "/backgroundgif";
    public static final String BACKGROUNDLEVELS = "/backgroundlevels";
    public static final String DATALEVELS = "/datalevels";

    public static BufferedImage[] GetArrayRes(String path) {
        URL url = LoadSave.class.getResource(path);
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }

            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
			try {
            imgs[i] = ImageIO.read(filesSorted[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgs;
    }

    public static BufferedImage GetSprite(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

}
