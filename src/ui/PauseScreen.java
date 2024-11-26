package ui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class PauseScreen {

    private BufferedImage backgroundImg;
    private Buttons[] buttons = new Buttons[3];
    private AudioButton[] audioButtons = new AudioButton[2];

    private Playing playing;
    private BufferedImage imgPaused = LoadSave.GetSprite(LoadSave.PAUSED);
    private int width = (int) (imgPaused.getWidth() * 1.5f), height = (int) (imgPaused.getHeight() * 1.5f);
    private BufferedImage imgDecor = LoadSave.GetSprite(LoadSave.DECOR);
    private int widthD = imgDecor.getWidth(), heightD = imgDecor.getHeight();

    public PauseScreen(Playing playing) {
        this.playing = playing;
        loadBackground();
        loadButton();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
    }

    private void loadButton() {
        buttons[0] = new Buttons((int) (Game.GAME_WIDTH - 402) / 2, 240, 0, Gamestate.PLAYING);
        buttons[1] = new Buttons((int) (Game.GAME_WIDTH + 402) / 2 - 84, 240, 1, Gamestate.MENU); // 2 button nay chuyen doi state
        buttons[2] = new Buttons((int) (Game.GAME_WIDTH - 84) / 2, 240, 4, Gamestate.NONE); // 2 button nay chuyen doi state
        audioButtons[0] = new AudioButton((Game.GAME_WIDTH - width) / 2, 360, 0);
        audioButtons[1] = new AudioButton((Game.GAME_WIDTH + width) / 2 - 84, 360, 1);

    }

    public void update() {
        for (Buttons b : buttons) {
            b.update();
        }
        for (AudioButton ab : audioButtons) {
            ab.update();
        }
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.drawImage(imgDecor, 0, (Game.GAME_HEIGHT - heightD) / 2 - 50, widthD, heightD + 20, null);
        g2d.setComposite(originalComposite);
        g.drawImage(imgPaused, (Game.GAME_WIDTH - width) / 2, 180, width, height, null);
        for (Buttons b : buttons) {
            b.draw(g);
        }
        for (AudioButton ab : audioButtons) {
            ab.draw(g);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (Buttons b : buttons) {
            b.setMouseOver(false);
        }
        for (AudioButton ab : audioButtons) {
            ab.setMouseOver(false);
        }
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMouseOver(true);
                break;
            }
        }
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMouseOver(true);
                break;

            }
        }
    }

    public void mousePressed(MouseEvent e) {
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMousePressed(true);
            }
        }
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMousePressed(true);
            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMouseReleased(true);
                b.applyGamestate(this.playing);
                break;
            }
        }
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMouseReleased(true);
            }
        }
    }

}
