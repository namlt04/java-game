package gamestates;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import level.LevelManager;

import main.Game;
import ui.Buttons;
import ui.ButtonsChooseMap;
import utilz.LoadSave;

public class Choose extends State implements Statemethods {

    private ButtonsChooseMap[] buttons = new ButtonsChooseMap[4];
    private Buttons back;
    private BufferedImage backgroundImg;
    private BufferedImage nameImg;
    private int aniTick = 0;
    private BufferedImage[] backgroundGif;
    private Playing playing;
    private LevelManager levelManager;
    private BufferedImage imgOptions = LoadSave.GetSprite(LoadSave.GAMEOPTIONS);
    private int width = (int) (imgOptions.getWidth() * 1.5f), height = (int) (imgOptions.getHeight() * 1.5f);

    public Choose(Game game) {
        super(game);
        loadBackground();
        loadButtons();

    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.OPACITY);
        nameImg = LoadSave.GetSprite(LoadSave.NAME);
        backgroundGif = LoadSave.GetArrayRes(LoadSave.BACKGROUNDGIF);
    }

    private void loadButtons() {
        for (int i = 0; i < 4; i++) {
            buttons[i] = new ButtonsChooseMap((Game.GAME_WIDTH) / 2 - 200 + (i * 100), 230, i);
        }
        back = new Buttons((Game.GAME_WIDTH - 84) / 2, 350, 4, Gamestate.MENU);

    }

    public void update() {

        for (ButtonsChooseMap button : buttons) {
            button.update();
        }
        back.update();
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

        for (ButtonsChooseMap button : buttons) {
            button.draw(g);
        }
        back.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        for (ButtonsChooseMap button : buttons) {
            if (button.isIn(e)) {
                button.setMousePressed(true);
            }
        }
        if (back.isIn(e)) {
            back.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (ButtonsChooseMap button : buttons) {
            if (button.isIn(e)) {
                button.setMouseReleased(true);
                button.apply();
                game.getPlaying().setLoading(true);
                break;
            }
        }
        if (back.isIn(e)) {
            back.setMouseReleased(true);
            back.applyGamestate(playing);
        }
    }

    private void resetButtons() {
        for (ButtonsChooseMap button : buttons) {
            button.resetBools();
        }
        back.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        for (ButtonsChooseMap button : buttons) {
            button.setMouseOver(false);
        }
        back.setMouseOver(false);
        for (ButtonsChooseMap button : buttons) {
            if (button.isIn(e)) {
                button.setMouseOver(true);
                break;
            }
        }
        if (back.isIn(e)) {
            back.setMouseOver(true);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

}
