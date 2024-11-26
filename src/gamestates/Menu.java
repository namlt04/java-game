package gamestates;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.Buttons;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

    private Buttons[] buttons = new Buttons[5];
    private BufferedImage backgroundImg;
    private BufferedImage nameImg;
    private int aniTick = 0;
    private BufferedImage[] backgroundGif;
    private Playing playing;

    public Menu(Game game) {
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
        int width = (int) (nameImg.getWidth() * 1.5f);
        buttons[0] = new Buttons((Game.GAME_WIDTH - width) / 2 - 42, 290, 0, Gamestate.PLAYING);
        buttons[1] = new Buttons((Game.GAME_WIDTH) / 2 - 42, 290, 5, Gamestate.OPTIONS);
        buttons[2] = new Buttons(((Game.GAME_WIDTH + width) / 2) - 42, 290, 6, Gamestate.QUIT);
        buttons[3] = new Buttons(((Game.GAME_WIDTH - width) / 2) + (width / 4) - 42, 290, 7, Gamestate.CHOOSE);
        buttons[4] = new Buttons(((Game.GAME_WIDTH - width) / 2) + (width / 4)*3 - 42, 290, 8, Gamestate.MENU);
  

    }

    @Override

    public void update() {
        for (Buttons b : buttons) {
            b.update();
        }
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
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));;
        g2d.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.setComposite(originalComposite);

        for (Buttons b : buttons) {
            b.draw(g);
        }
        g.drawImage(nameImg, (Game.GAME_WIDTH - ((int) (nameImg.getWidth() * 1.5f))) / 2, 230, (int) (nameImg.getWidth() * 1.5f), (int) (nameImg.getHeight() * 1.5f), null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                b.setMousePressed(true);
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Buttons b : buttons) {
            if (b.isIn(e)) {
                if (b.isMousePressed()) {
                    b.applyGamestate(game.getPlaying());
                }
                break;
            }
        }

        resetButtons();

    }

    private void resetButtons() {
        for (Buttons b : buttons) {
            b.resetBools();
        }

    }

    @Override
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

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.setState(Gamestate.PLAYING);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
