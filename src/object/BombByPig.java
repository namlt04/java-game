package object;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import utilz.HelpMethod;

public class BombByPig extends Object {
    public static final int ON = 0;
    public static final int BOOM = 1;
    private int dir;
    private int x, y;
    private boolean move;
    private float Speed = 1.5f;
    private int xOffset = 35;
    private int yOffset = 50;
    private int[][] lvlData;

    public BombByPig(int x, int y, int dir, int[][] lvlData) {
        super(x, y, Object.BOMBBYPIG);
        initHitBox(30, 30);
        this.active = false;
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.lvlData = lvlData;
    }

    public void drawHitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.draw3DRect((int) hitBox.x - xLvlOffset, (int) hitBox.y - yLvlOffset,
                (int) hitBox.width, (int) hitBox.height, true);
    }

    public void update() {
        updateAnimationTick();
        updateAnimationState();
    }

    private void updateAnimationState() {
        if (isActive()) {
            if (HelpMethod.CanMoveHere(hitBox.x - (dir * Speed), hitBox.y + (2 * (float) (Math.sqrt(Speed) / 50)), hitBox.width, hitBox.height, lvlData) && getMove()) {
                hitBox.x -= (dir * Speed);
                hitBox.y += (2 * (float) (Math.sqrt(Speed / 50)));
            } else {
                updateBoom();
            }
        }
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (active) {
            g.drawImage(aniBombByPig[state][aniIndex],
                    (int) hitBox.x - xLvlOffset - xOffset,
                    (int) hitBox.y - yOffset - yLvlOffset,
                    aniBombByPig[0][0].getWidth(), aniBombByPig[0][0].getHeight(),
                    null);
        }
    }

    public void updateBoom() {
        state = BOOM;
        if (state == BOOM && aniIndex == 4) {
            newState(ON);
            updatePos();
        }
    }

    public void updatePos() {
        setActive(false);
        setMove(false);
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public boolean getMove() {
        return move;
    }
}
