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

public class GameoverScreen {

    private BufferedImage backgroundImg;
    private Buttons[] buttons = new Buttons[2];
    private int x, y;
    private Playing playing;
    private BufferedImage imgGameover = LoadSave.GetSprite(LoadSave.GAMEOVER);
    private BufferedImage imgDecor = LoadSave.GetSprite(LoadSave.DECOR);
    private int widthD = imgDecor.getWidth(), heightD = imgDecor.getHeight();
    private int width = (int) (imgGameover.getWidth() * 1.5f), height = (int) (imgGameover.getHeight() * 1.5f);

    public GameoverScreen(Playing playing) {
        this.playing = playing;
        loadBackground();
        loadButton();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
    }

    private void loadButton() {
        buttons[0] = new Buttons((int) ((Game.GAME_WIDTH) / 2 - 126), 290, 1, Gamestate.MENU);
        buttons[1] = new Buttons((int) (Game.GAME_WIDTH / 2) + 42, 290, 4, Gamestate.NONE);
    }

    public void update() {
        for (Buttons b : buttons) {
            b.update();
        }

    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));;
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g2d.drawImage(imgDecor, 0, (Game.GAME_HEIGHT - heightD) / 2, widthD, heightD + 20, null);
        g2d.setComposite(originalComposite);
        g.drawImage(imgGameover, (int) (Game.GAME_WIDTH - width) / 2, 230, width, height, null);
        for (Buttons b : buttons) {
            b.draw(g);
        }

    }

    public void mouseMoved(MouseEvent e) {
        for (Buttons b : buttons) {
            b.setMouseOver(false);
        }
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMouseOver(true);
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

    }

    public void mouseReleased(MouseEvent e) {
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMouseReleased(true);
                b.applyGamestate(playing);
            }
        }

    }
}
