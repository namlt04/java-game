package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import gamestates.Playing;
import main.Game;

public class PigGuard extends Enemy {

    public PigGuard(float x, float y, int width, int height) {
        super(x, y, width, height, PIGGUARD);
        initHitBox(28, 28);
        initAttackBox();
        initValue();
    }

    private void initValue() {
        this.state = IDLE;
        this.xOffset = 25;
        this.yOffset = 25;
        this.maxHealth = 2;
        this.currHealth = maxHealth;
    }

    @Override
    protected void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 24, 40);
    }

    private void updateAttackBox() {
        if (dir == LEFT) {
            attackBox.x = hitBox.x + 30;
        } else {
            attackBox.x = hitBox.x - 25;
        }
        attackBox.y = hitBox.y - 20;
    }

    private void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            inAirCheck(lvlData, playing);
        }
        switch (state) {
            case IDLE:
                newState(IDLE);
                if (canSeePlayerStatic(lvlData, playing.getPlayer().getHitBox())) {
                    towardPlayer(playing.getPlayer());
                    newState(RUNNING);
                }
                break;
            case RUNNING:
                moveStatic(lvlData);
                if (!canSeePlayerStatic(lvlData, playing.getPlayer().getHitBox())) {
                    newState(IDLE);
                } else if (isPlayerCloseForAttack(playing.getPlayer())) {
                    newState(ATTACK);
                }
                break;
            case ATTACK:
                if (aniIndex == 0) {
                    attackChecked = false;
                }
                if (aniIndex == 3 && !attackChecked) {
                    checkPlayerHit(attackBox, playing);
                }
                break;
            case HIT:
                break;
            case DEAD:
                break;
        }
    }

    public void update(int[][] lvlData, Playing playing) {
        updateAnimationTick();
        updateBehavior(lvlData, playing);
        updateAttackBox();
    }

    @Override
    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (active) {
            g.drawImage(aniPig[state][aniIndex], (int) (hitBox.x - xLvlOffset - xOffset - flipX()), (int) (hitBox.y - yLvlOffset - yOffset), (int) (width * Game.SCALE * flipW()), (int) (height * Game.SCALE), null);
        }
    }
}
