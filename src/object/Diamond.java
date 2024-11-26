package object;

import java.awt.Graphics;
import main.Game;

public class Diamond extends Object {

    public Diamond(float x, float y) {
        super(x, y, 2);
        initHitBox(23, 23);
    }

    public void update() {
        if (isActive()) {
            updateAnimationTick();
        }
    }

    @Override
    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (isActive()) {
            g.drawImage(aniDiamond[aniIndex], (int) hitBox.x - xLvlOffset, (int) hitBox.y - yLvlOffset, (int) (aniDiamond[0].getWidth() * Game.SCALE), (int) (aniDiamond[0].getHeight() * Game.SCALE), null);
        }
    }

}
