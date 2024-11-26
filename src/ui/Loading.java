
package ui;

import gamestates.Playing;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.LoadSave;

public class Loading {

    private BufferedImage[] imgLoading;
    private BufferedImage backgroundImg;
    private int aniIndex = 0;

    private final int loadingTime = 1200;
    private long loadingStartTime;
    private long time;

    public Loading() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
        BufferedImage player = LoadSave.GetSprite(LoadSave.PLAYER);
        imgLoading = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            imgLoading[i] = player.getSubimage(i * 78, 58, 78, 58);

        }
    }

    public void update(Playing playing) {

        time = System.currentTimeMillis() - loadingStartTime;
        if (time > loadingTime) {
            playing.setLoading(false);
        }
        aniIndex = (int) (time / 200);
    }

    public void draw(Graphics g) {
        int width = 78 * 3;
        int height = 58 * 3;
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(imgLoading[aniIndex], (int) ((Game.GAME_WIDTH - width) / 2), (int) ((Game.GAME_HEIGHT - height) / 2), width, height, null);
    }

    public void setStartTime() {
        this.loadingStartTime = System.currentTimeMillis();
    }
}
