package object;

import java.awt.Graphics;
import gamestates.Playing;

public class Canon extends Object {

    public static final int IDLE = 0;
    public static final int FIRE = 1;
    private int dir;

    public Canon(float x, float y, int dir, Playing playing) {
        super(x, y, Object.CANON);
        this.dir = dir;
        initHitBox(30, 30);
        this.playing = playing;

    }

    public void update(BombByPig bomb) {
        updateAnimationTick();
        updateBehavior(playing, bomb);

    }

    public void updateBehavior(Playing playing, BombByPig bomb) {
        switch (state) {
            case IDLE:
                if (CanSeePlayer(playing.getPlayer()) && !bomb.isActive()) {
                    newState(FIRE);
                }
                break;
            case FIRE:
                if (aniIndex == 3) {
                    bomb.setActive(true);
                    bomb.setMove(true);
                    newState(IDLE);
                }
                break;
        }
    }

    @Override
    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage(aniCanon[state][aniIndex / 2], (int) hitBox.x - xLvlOffset,
                (int) hitBox.y - yLvlOffset, 44 * 3 * dir, 28 * 3, null);

    }

}
