package ui;

import gamestates.Gamestate;
import static gamestates.Gamestate.MENU;
import static gamestates.Gamestate.PLAYING;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import utilz.LoadSave;

public class Buttons {

    private int x, y, rowIndex, index;
    private int xOffsetCenter = 56 / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed, mouseReleased;
    private Rectangle bounds;

    public Buttons(int x, int y, int rowIndex, Gamestate state) {
        this.x = x;
        this.y = y;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, 84, 84);

    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * 56, rowIndex * 56, 56, 56);
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

    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, 84, 84, null);
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setMouseReleased(boolean mouseReleased) {
        this.mouseReleased = mouseReleased;
    }

    public void applyGamestate(Playing playing) {
        if (Gamestate.getState() == state) {
            if ( state == PLAYING )playing.unPause();
            else {
               try(FileWriter write = new FileWriter("save.in")){
                  write.write(Gamestate.getIndex() + "\n");
                  write.write(Gamestate.getUnlockLevel() + "\n");
               } catch(Exception e){

               }

            }
        } else if (state == Gamestate.NONE) {
            playing.resetAll();
      
        } else {
           
            Gamestate.setState(state);
            if (playing != null) {
                if (playing.getLoading()) {
                    playing.resetAll();
                 
                }
                if(state == MENU ) playing.getGame().getAudioPlayer().playingToMenu(0);
                else if (state == PLAYING && playing.getPause()) playing.getGame().getAudioPlayer().setLevelSongToPause();
                
            }
        }
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
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
}
