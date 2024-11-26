package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.HelpMethod;
import utilz.LoadSave;

public class Player extends Entity {

    public static final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int ATTACK = 2;
    public static final int JUMP = 3;
    public static final int HIT = 4;
    public static final int FALLING = 5;
    public static final int APPEAR = 6;
    public static final int DISAPPEAR = 7;
    public static final int DEAD = 8;

    private int GetPriteAmount() {
        switch (state) {
            case IDLE:
                return 11;
            case RUNNING:
            case APPEAR:
            case DISAPPEAR:
                return 8;
            case DEAD:
                return 4;
            case ATTACK:
                return 3;
            case HIT:
                return 2;
            case JUMP:
            case FALLING:
            default:
                return 1;
        }
    }
    public BufferedImage imgLiveBar;
    public BufferedImage miniMap;
    private BufferedImage[][] animations;
    private BufferedImage[] aniHeart;
    private BufferedImage[] aniDiamond;
    private BufferedImage[] countNumber;
    private boolean left, right, jump;
    private float jumpSpeed = -3.75f;
    private static final int ANI_HEALTH = 1000;
    private boolean attacking = false, moving = false;
    private boolean attackChecked = false;

    private int expectKey;
    private int count = 0;
    private int aniIndexHealth = 0;
    private float xOffSet = 46;
    private float yOffSet = 34;
    private int flipX = 0;
    private int flipW = 1;
    private Playing playing;
    private int[][] lvlData;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 3;
        this.currHealth = maxHealth;
        this.walkSpeed = 1.5f;
        loadAnimations();

    }

    @Override
    protected void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y - 50, 80, 120);
        resetAttackBox();
    }

    private void resetAttackBox() {
        if (flipW == 1) {
            setAttackBoxOnRightSide();
        } else {
            setAttackBoxOnLeftSide();
        }

    }

    private void setAttackBoxOnLeftSide() {
        attackBox.x = hitBox.x - attackBox.width;
    }

    private void setAttackBoxOnRightSide() {
        attackBox.x = hitBox.x + hitBox.width;
    }

    private void updateAttackBox() {
        if (flipW == 1) {
            setAttackBoxOnRightSide();
        } else {
            setAttackBoxOnLeftSide();
        }
        attackBox.y = hitBox.y - 30;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER);
        animations = new BufferedImage[9][11];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 78, i * 58, 78, 58);
            }
        }
        imgLiveBar = LoadSave.GetSprite(LoadSave.LIVE_BAR);
        loadUI();
    }

    private void loadUI() {
        aniHeart = new BufferedImage[8];
        BufferedImage heartHealth = LoadSave.GetSprite(LoadSave.HEART_HEALTH);
        for (int i = 0; i < 8; i++) {
            aniHeart[i] = heartHealth.getSubimage(i * 18, 0, 18, 14);
        }
        aniDiamond = new BufferedImage[8];
        BufferedImage diamondCount = LoadSave.GetSprite(LoadSave.DIAMOND_COUNT);
        for (int i = 0; i < 8; i++) {
            aniDiamond[i] = diamondCount.getSubimage(i * 18, 0, 18, 14);
        }
        countNumber = new BufferedImage[10];
        BufferedImage count = LoadSave.GetSprite(LoadSave.NUMBER);
        for (int i = 0; i < 10; i++) {
            countNumber[i] = count.getSubimage(i * 6, 0, 6, 8);
        }
    }

    public void update() {

        updateAnimationTick();
        setAnimation();
        if (attacking) {
            checkAttack();
        }
        updatePos();
        updateAttackBox();
        updateHealthBar();
    }

    private void checkAttack() {
        if (attackChecked || aniIndex == 0) {
            return;
        }
        attackChecked = true;
        playing.checkHitEnemy(attackBox, hitBox);
        playing.getGame().getAudioPlayer().playAttackSound();

    }

    public void changeHealth(int value) {
        if (value < 0) {
            if (state == HIT || state == DISAPPEAR) {
                return;
            } else {
                newState(HIT);
            }
        }
        currHealth += value;
        currHealth = Math.max(Math.min(currHealth, maxHealth), 0);
        if (currHealth <= 0) {
            newState(DEAD);
        }
    }

    private void updateHealthBar() {
        if (aniIndexHealth >= ANI_HEALTH) {
            aniIndexHealth = 0;
        } else {
            aniIndexHealth++;
        }
    }

    public void setAnimation() {

        int startAni = state;

        if (state == HIT) {
            return;
        }
        if (state == DEAD || state == DISAPPEAR || state == APPEAR) {
            return;
        }

        if (moving) {
            state = RUNNING;

        } else {
            state = IDLE;
        }
        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }
        if (attacking) {
            state = ATTACK;
            if (startAni == state) {
                return;
            }
        }
        if (startAni != state) {
            resetAniTick();
        }

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public void updatePos() {
        moving = false;
        if (jump) {
            jump();
        }

        if (!inAir) {
            if (!HelpMethod.IsEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }
        float xSpeed = 0;;
        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width + 48;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (inAir) {
            if (airSpeed > 0) {
                if (HelpMethod.CanMoveHereWhenFalling(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                    hitBox.y += airSpeed;
                    airSpeed += gravity;
                } else {
                    resetInAir();
                }
            } else {
                if (HelpMethod.CanMoveHereWhenJumping(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                    hitBox.y += airSpeed;
                    airSpeed += gravity;

                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
            }
        }
        playing.checkHitObject(this);
        updateXPos(xSpeed);
        if (!inAir) {
            if (!left && !right || left && right) {
                return;
            }
        }

        moving = true;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (HelpMethod.CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        }

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (HelpMethod.IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }

    public void render(Graphics g, int xLvOffset, int yLvlOffset) {
        g.drawImage(animations[state][aniIndex], (int) (hitBox.x - xOffSet - xLvOffset + flipX), (int) (hitBox.y - yOffSet - yLvlOffset), width * flipW * 2, height * 2, null);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.drawImage(imgLiveBar, 15, 15, imgLiveBar.getWidth() * 2, imgLiveBar.getHeight() * 2, null);
        for (int i = 1; i <= currHealth; i++) {
            g.drawImage(aniHeart[aniIndexHealth / 140], 15 + i * 22, 34, (int) (aniHeart[0].getWidth() * Game.SCALE), (int) (aniHeart[0].getHeight() * Game.SCALE), null);
        }
        g.drawImage(aniDiamond[aniIndexHealth / 126], 40, 70, (int) (aniDiamond[0].getWidth() * Game.SCALE), (int) (aniDiamond[0].getHeight() * Game.SCALE), null);
        g.drawImage(countNumber[count / 10], 80, 75, countNumber[0].getWidth() * 2, countNumber[0].getHeight() * 2, null);
        g.drawImage(countNumber[count % 10], 90, 75, countNumber[0].getWidth() * 2, countNumber[0].getHeight() * 2, null);
    }

    public void changeDiamondCollect() {
        count += 1;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetPriteAmount()) {
                
                if (state == DEAD) {
                    playing.gameOver();
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
                    return;
                }
                if (state == DISAPPEAR) {
                    playing.levelComplete();
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LVL_COMPLETED);
                    return;
                }
                aniIndex = 0;
                attacking = false;
                attackChecked = false;

                if (state == HIT) {
                    newState(IDLE);
                    if (currHealth <= 0) {
                        newState(DEAD);
                    }
                }
            }
        }
    }

    public void reset() {
        newState(IDLE);
        currHealth = maxHealth;
        hitBox.x = x;
        hitBox.y = y;
        airSpeed = 0f;
        inAir = false;
        moving = false;
        jump = false;
        left = false;
        right = false;
        flipX = 0;
        flipW = 1;
        count = 0;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return jump;
    }

    public void setUp(boolean up) {
        this.jump = up;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void loadMap(BufferedImage imgMap) {
        this.miniMap = imgMap;
    }

    public void setExpectKey(int value) {
        this.expectKey = value;
    }

    public boolean nextLevel() {
        return (count == expectKey);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        initHitBox(32, 52);
        initAttackBox();
    }

}
