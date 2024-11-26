package object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

public class Door extends Object {

    public static final int IDLE = 0;
    public static final int CLOSE = 1;
    public static final int OPEN = 2;

    public Door(float x, float y) {
        super(x, y, DOOR);
        state = IDLE;
        initHitBox(90, 115);
        loadAnimation();
    }

    private void loadAnimation() {
        BufferedImage imgDoor = LoadSave.GetSprite(LoadSave.DOOR);
        aniDoor = new BufferedImage[3][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                aniDoor[j][i] = imgDoor.getSubimage(i * 46, j * 56, 46, 56);
            }
        }
    }

    public void update() {
        updateAnimationTick();
    }

    @Override
    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage(aniDoor[state][aniIndex], (int) (hitBox.x - xLvlOffset), (int) (hitBox.y - yLvlOffset), (int) aniDoor[0][0].getWidth() * 2, (int) aniDoor[0][0].getHeight() * 2, null);
    }

}
