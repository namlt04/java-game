package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInput implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.getState()) {
            case MENU ->
                gamePanel.getGame().getMenu().mouseMoved(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseMoved(e);
            case OPTIONS ->
                gamePanel.getGame().getOptions().mouseMoved(e);
            case CHOOSE ->
                gamePanel.getGame().getChoose().mouseMoved(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.getState()) {
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseClicked(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.getState()) {
            case MENU ->
                gamePanel.getGame().getMenu().mousePressed(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mousePressed(e);
            case OPTIONS ->
                gamePanel.getGame().getOptions().mousePressed(e);
            case CHOOSE ->
                gamePanel.getGame().getChoose().mousePressed(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.getState()) {
            case MENU ->
                gamePanel.getGame().getMenu().mouseReleased(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseReleased(e);
            case OPTIONS ->
                gamePanel.getGame().getOptions().mouseReleased(e);
            case CHOOSE ->
                gamePanel.getGame().getChoose().mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
