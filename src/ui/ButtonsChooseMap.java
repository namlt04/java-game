package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;

public class ButtonsChooseMap {

    private int x, y, rowIndex, index;
    private int xOffsetCenter = 56 / 2;
    private BufferedImage[][] imgs;
    private boolean mouseOver, mousePressed, mouseReleased;
    private int state = 0;
    private Rectangle bounds;
    private int levelIndex;

    public ButtonsChooseMap(int x, int y, int levelIndex) {
        this.x = x;
        this.y = y;
        this.levelIndex = levelIndex;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, 84, 84);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3][3];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.BUTTONSCHOOSE);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                imgs[i][j] = temp.getSubimage(j * 56, levelIndex * 3 * 56 + i * 56, 56, 56);
            }

        }
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
        if (mouseReleased) {
            resetBools();
        }
        state = 0;
        if (levelIndex < Gamestate.getUnlockLevel()) {
            state = 0;
        } else if (levelIndex > Gamestate.getUnlockLevel()) {
            state = 2;
        }
        if (levelIndex == Gamestate.getIndex()) {
            state = 1;
        }
     

    }

    public void draw(Graphics g) {
        g.drawImage(imgs[state][index], x, y, 84, 84, null);
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setMouseReleased(boolean mouseReleased) {
        this.mouseReleased = mouseReleased;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void apply() {
        if (state == 0 || state == 1) {
            Gamestate.setIndex(levelIndex);
        }
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
        mouseReleased = false;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isIn(MouseEvent e) {
        if (bounds.contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }

    public void setState(int value) {
        this.state = value;
    }
}
