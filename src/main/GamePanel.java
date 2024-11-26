package main;

import input.KeyBoardInput;
import input.MouseInput;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private MouseInput mouseInput;
    private Game game;

    public GamePanel(Game game) {
        mouseInput = new MouseInput(this);
        this.game = game;
        setPanelSize();
        setFocusable(true);
        addKeyListener(new KeyBoardInput(this));
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);

    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void updateGame() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
