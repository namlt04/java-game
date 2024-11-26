package input;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import main.GamePanel;
import gamestates.Gamestate;
public class KeyBoardInput implements KeyListener {
	private GamePanel gamePanel;
	public KeyBoardInput(GamePanel gamePanel) {
		this.gamePanel = gamePanel; 
	}
	@SuppressWarnings("incomplete-switch")
	@Override
	public void keyPressed(KeyEvent e) {
		switch(Gamestate.getState()){
		case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);      
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void keyReleased(KeyEvent e) {
		switch (Gamestate.getState()){ 
		case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
	}
	}



	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
