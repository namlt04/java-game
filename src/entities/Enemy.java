package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.HelpMethod;
import utilz.LoadSave;

public abstract class Enemy extends Entity {

    public static final int PIGPATROL = 1;
    public static final int PIGGUARD = 2;
    public static final int KINGPIG = 3;
    public static final int BOX = 4;

    public static final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int ATTACK = 2;
    public static final int HIT = 3;
    public static final int DEAD = 4;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    protected int enemyState = 0, enemyType = 1;
    protected boolean active = true;

    protected boolean attacked = true;
    protected boolean attackChecked;
    protected int dir = RIGHT;
    protected BufferedImage[][] aniPig, aniKingPig;
    protected float attackDistance = 24.0f;
    protected int tileY;
    protected boolean firstUpdate = true;
    protected int xOffset;
    protected int yOffset;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        walkSpeed = 0.35f;
        loadAnimation();
    }
    
        private int GetSpriteAmount(int enemyType) {
        switch (enemyType) {
            case PIGPATROL:
            case PIGGUARD:
                switch (state) {
                    case IDLE:
                        return 11;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        return 5;
                    case DEAD:
                        return 4;
                    case HIT:
                        return 2;
                }
            case KINGPIG:
                switch (state) {
                    case IDLE:
                        return 12;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        return 5;
                    case HIT:
                        return 2;
                    case DEAD:
                        return 4;
                }

        }
        return 0;
    }

    protected abstract void render(Graphics g, int xLvlOffset, int yLvlOffset);

    private void loadAnimation() {
        BufferedImage imgPig = LoadSave.GetSprite(LoadSave.PIG);
        aniPig = new BufferedImage[5][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                aniPig[j][i] = imgPig.getSubimage(i * 34, j * 28, 34, 28);
            }
        }
        BufferedImage imgKingPig = LoadSave.GetSprite(LoadSave.KINGPIG);
        aniKingPig = new BufferedImage[5][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                aniKingPig[j][i] = imgKingPig.getSubimage(i * 38, j * 28, 38, 28);
            }
        }
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType)) {
                aniIndex = 0;
                if (state == ATTACK || state == HIT) {
                    newState(IDLE);
                }
                if (state == DEAD) {
                    active = false;
                }

            }
        }
    }

    protected void checkPlayerHit(Rectangle2D attackBox, Playing playing) {
        if (attackBox.intersects(playing.getPlayer().getHitBox())) {
            playing.getPlayer().changeHealth(-1);
        }
        attackChecked = true;
    }

    protected void changeDirec() {
        if (dir == LEFT) {
            dir = RIGHT;
        } else {
            dir = LEFT;
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;
        if (dir == LEFT) {
            xSpeed += 0.7f;
        } else {
            xSpeed -= 0.7f;
        }

        if (HelpMethod.CanMoveHere((float) hitBox.x + xSpeed, (float) hitBox.y, (float) hitBox.width, (float) hitBox.height, lvlData)) {
            if (HelpMethod.IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }
        }
        changeDirec();
    }

    protected void moveStatic(int[][] lvlData) {
        float xSpeed = 0;
        if (dir == LEFT) {
            xSpeed += 0.7f;
        } else {
            xSpeed -= 0.7f;
        }

        if (HelpMethod.CanMoveHereStatic((float) hitBox.x + xSpeed, (float) hitBox.y, (float) hitBox.width, (float) hitBox.height, lvlData)) {
            if (HelpMethod.IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }
        }
        changeDirec();
    }

    public boolean canSeePlayerStatic(int[][] lvlData, Rectangle2D.Float rec) {
        int tileX1 = (int) rec.x / Game.TILES_SIZE;
        int tileY1 = (int) rec.y / Game.TILES_SIZE;
        int tileX2 = (int) (rec.x + rec.width) / Game.TILES_SIZE;
        int tileY2 = (int) rec.y / Game.TILES_SIZE;
        int tileX3 = (int) rec.x / Game.TILES_SIZE;
        int tileY3 = (int) (rec.y + rec.height) / Game.TILES_SIZE;
        int tileX4 = (int) (rec.x + rec.width) / Game.TILES_SIZE;
        int tileY4 = (int) (rec.y + rec.height) / Game.TILES_SIZE;
        if (lvlData[tileY1][tileX1] == 249 || lvlData[tileY2][tileX2] == 249 || lvlData[tileY3][tileX3] == 249 || lvlData[tileY4][tileX4] == 249) {
            if (isPlayerInRange(rec)) {
                return true;
            }
        }
        return false;

    }

    protected void inAirCheck(int[][] lvlData, Playing playing) {
        updateInAir(lvlData);
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!HelpMethod.IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        switch (enemyType) {
            case PIGPATROL -> {
                return absValue <= attackDistance;
            }
            case PIGGUARD -> {
                return absValue <= attackDistance;
            }
            case KINGPIG -> {
                return absValue <= attackDistance;
            }
        }
        return false;
    }

    protected void updateInAir(int[][] lvlData) {
        if (HelpMethod.CanMoveHereWhenFalling(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += airSpeed;
            airSpeed += gravity;
        }
    }

    public void hurt() {
        if (state == HIT || state == DEAD) {
            return;
        } else {
            newState(HIT);
        }

        currHealth -= 1;
        currHealth = Math.max(0, Math.min(currHealth, maxHealth));
        System.out.println(currHealth);
        if (currHealth <= 0) {
            newState(DEAD);
        }
    }

    public boolean canSeePlayer(int[][] lvlData, Rectangle2D.Float rec) {
        if (rec.x >= hitBox.x && rec.x <= (hitBox.x + hitBox.height)) {
            if (isPlayerInRange(rec)) {
                return true;
            }
        }
        if ((rec.x + rec.height) >= hitBox.x && (rec.x + rec.width) <= (hitBox.x + hitBox.height)) {
            if (isPlayerInRange(rec)) {
                return true;
            }
        }
        if (rec.x < hitBox.x && (rec.x + rec.height) > (hitBox.x + hitBox.height)) {
            if (isPlayerInRange(rec)) {
                return true;
            }
        }
        return false;
    }

    protected void towardPlayer(Player player) {
        if (player.hitBox.x > hitBox.x) {
            dir = LEFT;
        } else {
            dir = RIGHT;
        }

    }

    protected boolean isPlayerInRange(Rectangle2D.Float rec) {

        int absValue = (int) Math.abs(rec.x - hitBox.x);
        switch (enemyType) {
            case PIGPATROL:
                return absValue <= attackDistance;
            case PIGGUARD:
                return absValue <= attackDistance * 5;
            case KINGPIG:
                return absValue <= attackDistance * 10;
        }
        return false;

    }

    public void reset() {
        firstUpdate = false;
        currHealth = maxHealth;
        newState(IDLE);
        active = true;
        aniTick = 0;
        aniIndex = 0;
        hitBox.x = x;
        hitBox.y = y;
        airSpeed = 0;
    }

    public int flipX() {
        if (dir == LEFT) {
            return -(width * 2 + 10);
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (dir == LEFT) {
            return -1;
        } else {
            return 1;
        }
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }

    public boolean canAttack() {
        return attacked;
    }

    public void setAttacked(boolean value) {
        this.attacked = value;
    }

}
