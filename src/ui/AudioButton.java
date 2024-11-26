package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;

public class AudioButton {

    private int x, y, state, rowIndex, index;
    private BufferedImage[][] imgs;
    private boolean mouseOver, mousePressed, mouseReleased;
    private Rectangle bounds;

    public AudioButton(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
        if (state == 0) {
            this.rowIndex = Gamestate.getSfx();
        } else {
            this.rowIndex = Gamestate.getMusic();
        }
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, 84, 84);
    }

    private void loadImgs() {
        imgs = new BufferedImage[2][3];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.BUTTONS);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                imgs[j][i] = temp.getSubimage(i * 56, 56 * 2 + j * 56, 56, 56);
            }
        }

    }

    public void draw(Graphics g) {
        g.drawImage(imgs[rowIndex][index], x, y, 84, 84, null);
    }

    public void update() {
        if (state == 0) {
            this.rowIndex = Gamestate.getSfx();
        } else {
            this.rowIndex = Gamestate.getMusic();
        }
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
        if (mouseReleased) {
            if (rowIndex == 0) {
                rowIndex = 1;
            } else {
                rowIndex = 0;
            }
            if (state == 0) {
                Gamestate.setSfx(rowIndex);
            } else {
                Gamestate.setMusic(rowIndex);
            }
            resetBools();
        }

    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setMouseReleased(boolean mouseReleased) {
        this.mouseReleased = mouseReleased;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isIn(MouseEvent e) {
        return bounds.contains(e.getX(), e.getY());
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
        mouseReleased = false;
    }

}
