package entities;

import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected static final int ANI_SPEED = 25;
    protected float gravity = 0.05f;
    protected float fallSpeedAfterCollision = 0.4f;

    protected int state;
    protected boolean active = true;
    protected int maxHealth, currHealth;
    protected int aniTick, aniIndex;

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected Rectangle2D.Float attackBox;

    protected float airSpeed;
    protected float walkSpeed;
    protected boolean inAir = false;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected abstract void initAttackBox();

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public int getState() {
        return state;
    }

    public boolean isActive() {
        return active;
    }

}
