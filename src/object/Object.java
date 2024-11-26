package object;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Player;
import gamestates.Playing;
import utilz.LoadSave;

public abstract class Object {

    private static final int ANI_SPEED = 25;

    public static final int HEART = 1;
    public static final int DIAMOND = 2;
    public static final int CANON = 3;
    public static final int BOMBBYPIG = 5;
    public static final int DOOR = 6;
    protected float x, y;
    protected Rectangle2D.Float hitBox;
    protected BufferedImage[][] aniDoor;
    protected int aniTick, aniIndex;
    protected int aniType;

    protected boolean active = true;
    protected int state = 0;
    protected Playing playing;

    protected int GetSprite(int aniType) {
        switch (aniType) {
            case HEART:
                return 8;
            case CANON:
                switch (state) {
                    case Canon.IDLE:
                        return 1;
                    case Canon.FIRE:
                        return 8;
                   
                }
            case DIAMOND:
                return 10;

            case BOMBBYPIG:
                switch (state) {
                    case BombByPig.ON:
                        return 4;
                    case BombByPig.BOOM:
                        return 6;
                }
            case DOOR:
                switch (state) {
                    case Door.IDLE:
                        return 1;
                    case Door.CLOSE:
                        return 3;
                    case Door.OPEN:
                        return 5;
                }
        }
        return 0;
    }
    protected BufferedImage[] aniHeart;
    protected BufferedImage[][] aniBombByPig;
    protected BufferedImage[][] aniPigThrowBomb;
    protected BufferedImage[] aniDiamond;
    protected BufferedImage[][] aniCanon;

    public Object(float x, float y, int aniType) {
        this.x = x;
        this.y = y;
        this.aniType = aniType;
        loadAnimation();

    }

    protected abstract void render(Graphics g, int xLvlOffset, int yLvlOffset);

    private void loadAnimation() {
        BufferedImage imgHeart = LoadSave.GetSprite(LoadSave.HEART_COLLECT);
        aniHeart = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            aniHeart[i] = imgHeart.getSubimage(i * 18, 0, 18, 14);

        }
  

        BufferedImage imgDiamond = LoadSave.GetSprite(LoadSave.DIAMOND_COLLECT);
        aniDiamond = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            aniDiamond[i] = imgDiamond.getSubimage(i * 18, 0, 18, 14);

        }
    
        BufferedImage imgCanon = LoadSave.GetSprite(LoadSave.CANON);
        aniCanon = new BufferedImage[2][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
       
                aniCanon[j][i] = imgCanon.getSubimage(i * 44, j * 28, 44, 28);
            }
        }

     
        aniBombByPig = new BufferedImage[2][6];
        BufferedImage imgBomb = LoadSave.GetSprite(LoadSave.BOMBBYPIG);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
            
                aniBombByPig[j][i] = imgBomb.getSubimage(i * 104, j * 112, 104, 112);
            }
        }

        aniPigThrowBomb = new BufferedImage[4][10];
        BufferedImage imgPig = LoadSave.GetSprite(LoadSave.PIGTHROWBOMB);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                aniPigThrowBomb[j][i] = imgPig.getSubimage(i * 52, j * 52, 52, 52);
             
            }
        }

    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSprite(aniType)) {
                aniIndex = 0;

            }
        }
    }


    public boolean CanSeePlayer(Player player) {
        int x = (int) Math.abs(player.getHitBox().x - hitBox.x);
        int y = (int) Math.abs(player.getHitBox().y - hitBox.y);
        if (x < 500 || y < 500) {
            return true;
        }
        return false;
    }

    public void reset() {
        newState(0);
        active = true;
        hitBox.x = x;
        hitBox.y = y;

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getState() {
        return state;
    }

    public void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
}
