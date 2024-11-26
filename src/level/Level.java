package level;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.Game;

public class Level {

    private Game game;
    private BufferedImage imgLevel;
    private BufferedImage imgBackground;
    private int[][] lvlData;

    public Level(BufferedImage imgLevel, BufferedImage imgBackground) {
        this.imgLevel = imgLevel;
        this.imgBackground = imgBackground;
        lvlData = new int[40][40];
        loadLevel();
    }

    private void loadLevel() {
        for (int i = 0; i * 16 < imgLevel.getHeight(); i++) {
            for (int j = 0; j * 16 < imgLevel.getWidth(); j++) {
                Color c = new Color(imgLevel.getRGB(j * 16, i * 16));
                int value = c.getRed();
                lvlData[i][j] = value;
            }
        }
    }

    public BufferedImage getBackground() {
        return imgBackground;
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public BufferedImage getMap() {
        return imgLevel;
    }

}
