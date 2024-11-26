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

public class GameCompleteScreen {

    private BufferedImage backgroundImg;
    private Buttons[] buttons = new Buttons[2];
    private Playing playing;
    private BufferedImage imgGameComplete = LoadSave.GetSprite(LoadSave.GAMECOMPLETE);
    private BufferedImage imgDecor = LoadSave.GetSprite(LoadSave.DECOR);
    private int widthD = imgDecor.getWidth(), heightD = imgDecor.getHeight();
    private int width = (int) (imgGameComplete.getWidth() * 1.5f), height = (int) (imgGameComplete.getHeight() * 1.5f);

    public GameCompleteScreen(Playing playing) {
        this.playing = playing;
        loadBackground();
        loadButton();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
    }

    private void loadButton() {
        buttons[0] = new Buttons(Game.GAME_WIDTH / 2 - 126, 290, 1, Gamestate.MENU);
        buttons[1] = new Buttons(Game.GAME_WIDTH / 2 + 42, 290, 6, Gamestate.QUIT);
    }

    public void update() {
        for (Buttons button : buttons) {
            button.update();
        }
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));;
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.drawImage(imgDecor, 0, (Game.GAME_HEIGHT - heightD) / 2, widthD, heightD + 20, null);
        g2d.setComposite(originalComposite);

        g.drawImage(imgGameComplete, (int) (Game.GAME_WIDTH - width) / 2, 230, width, height, null);
        for (Buttons button : buttons) {
            button.draw(g);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (Buttons button : buttons) {
            button.setMouseOver(false);
        }

        for (Buttons button : buttons) {
            if (button.isIn(e)) {
                button.setMouseOver(true);
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        for (Buttons button : buttons) {
            if (button.isIn(e)) {
                button.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (Buttons button : buttons) {
            if (button.isIn(e)) {
                button.setMouseReleased(true);
                playing.reset();
                playing.setLoading(true);
                button.applyGamestate(playing);
            }
        }
    }
}
