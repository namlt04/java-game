package ui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class FirstScreen {

    private BufferedImage backgroundImg;
    private BufferedImage imgGuide;
    private int aniTick = 0;
    private BufferedImage[] backgroundGif;
    private Playing playing;

    public FirstScreen(Playing playing) {
        this.playing = playing;
        loadBackground();

    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
        imgGuide = LoadSave.GetSprite(LoadSave.GUIDE);
        backgroundGif = LoadSave.GetArrayRes(LoadSave.BACKGROUNDGIF);
    }

    public void update() {

        if (aniTick >= 95) {
            aniTick = 0;
        } else {
            aniTick++;
        }

    }

    public void draw(Graphics g) {

        g.drawImage(backgroundGif[aniTick / 8], 0, 0, (int) ((backgroundGif[aniTick / 8]).getWidth() * 1.75f), (int) ((backgroundGif[aniTick / 8]).getHeight() * 1.75f), null);
        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.setComposite(originalComposite);

        g.drawImage(imgGuide, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            playing.unFirstUpdate();
        }

    }

    public void mouseMoved(MouseEvent e) {
    }

}
