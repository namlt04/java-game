package level;

import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import main.Game;
import utilz.LoadSave;

public class LevelManager {

    private Game game;
    private ArrayList<Level> level;

    public LevelManager(Game game) {
        this.game = game;
        level = new ArrayList<>();
        setLevel();
        buildAllLevels();
    }
    private void setLevel(){
        try (Scanner sc = new Scanner(new File("save.in"))) { 
            Gamestate.setIndex(Integer.parseInt(sc.next())); 
            Gamestate.setUnlockLevel(Integer.parseInt(sc.next())); 
        } catch (NumberFormatException | IOException e) {
  
        }
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetArrayRes(LoadSave.DATALEVELS);
        BufferedImage[] allBackgrounds = LoadSave.GetArrayRes(LoadSave.BACKGROUNDLEVELS);
        for (int i = 0; i < allLevels.length; i++) {
            level.add(new Level(allLevels[i], allBackgrounds[i]));
        }
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        BufferedImage background = level.get(Gamestate.getIndex()).getBackground();
        int newWidth = background.getWidth() * 2;
        int newHeight = background.getHeight() * 2;
        g.drawImage(background, -xLvlOffset, -yLvlOffset, newWidth, newHeight, null);

    }

    public void loadNextLevel() {

        if (Gamestate.getIndex() + 1 == level.size()) {
            game.getPlaying().endGame();
            return;
        }
        Gamestate.setIndex(Gamestate.getIndex() + 1);
        Gamestate.setUnlockLevel(Math.max(Gamestate.getIndex(), Gamestate.getUnlockLevel()));
    }

    public void setLevelIndex(int value) {
        Gamestate.setIndex(value);
       
    }

    public int[][] getLvlData() {
        return (level.get(Gamestate.getIndex())).getLvlData();
    }

    public void reset() {
        Gamestate.setIndex(0);
    }

    public BufferedImage getMap() {
        return (level.get(Gamestate.getIndex())).getMap();
    }

}
