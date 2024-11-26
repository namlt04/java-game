package gamestates;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioButton;
import ui.Buttons;
import utilz.LoadSave;

public class Options extends State implements Statemethods {

    private AudioButton[] audioButtons = new AudioButton[2];
    private Buttons buttons;
    private BufferedImage backgroundImg;
    private BufferedImage nameImg;
    private int aniTick = 0;
    private BufferedImage[] backgroundGif;
    private Playing playing;
    private BufferedImage imgOptions = LoadSave.GetSprite(LoadSave.GAMEOPTIONS);
    private int width = (int) (imgOptions.getWidth() * 1.5f), height = (int) (imgOptions.getHeight() * 1.5f);

    public Options(Game game) {
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

        audioButtons[0] = new AudioButton((Game.GAME_WIDTH - width) / 2, 290, 0);
        audioButtons[1] = new AudioButton((int) ((Game.GAME_WIDTH - 84) / 2), 290, 1);
        buttons = new Buttons(((Game.GAME_WIDTH + width) / 2) - 84, 290, 4, Gamestate.MENU);
    }

    @Override

    public void update() {
        for (AudioButton ab : audioButtons) {
            ab.update();
        }
        buttons.update();
        if (aniTick >= 95) {
            aniTick = 0;
        } else {
            aniTick++;
        }

    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundGif[aniTick / 8], 0, 0, (int) ((backgroundGif[aniTick / 8]).getWidth() * 1.75f), (int) ((backgroundGif[aniTick / 8]).getHeight() * 1.75f), null);
        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.setComposite(originalComposite);

        for (AudioButton audioButton : audioButtons) {
            audioButton.draw(g);
        }
        buttons.draw(g);
        g.drawImage(imgOptions, (Game.GAME_WIDTH - width) / 2, 230, width, height, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMousePressed(true);
            }
        }
        if (buttons.isIn(e)) {
            buttons.setMousePressed(true);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMouseReleased(true);
            }
        }
        if (buttons.isIn(e)) {
            buttons.setMouseReleased(true);
            buttons.applyGamestate(playing);
        }


    }

    private void resetButtons() {
        for (AudioButton ab : audioButtons) {
            ab.resetBools();
        }
        buttons.resetBools();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (AudioButton ab : audioButtons) {
            ab.setMouseOver(false);
        }
        buttons.setMouseOver(false);
        for (AudioButton ab : audioButtons) {
            if (ab.isIn(e)) {
                ab.setMouseOver(true);
                break;
            }
        }
        if (buttons.isIn(e)) {
            buttons.setMouseOver(true);
        }

    }

    public AudioButton[] getButtons() {
        return audioButtons;
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {


    }

}
