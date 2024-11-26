package object;

import java.awt.Graphics;
import main.Game;

public class Heart extends Object {

    public Heart(float x, float y) {
        super(x, y, 1);
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
            g.drawImage(aniHeart[aniIndex], (int) hitBox.x - xLvlOffset, 
           (int) hitBox.y - yLvlOffset, (int) (18 * Game.SCALE), 
           (int) (14 * Game.SCALE), null);
        }
    }

}
