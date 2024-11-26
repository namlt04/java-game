package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import entities.EnemyManager;
import entities.Player;
import main.Game;
import object.ObjectManager;
import ui.GameCompleteScreen;
import ui.GameoverScreen;
import ui.PauseScreen;
import ui.FirstScreen;
import level.LevelManager;
import ui.Loading;

public class Playing extends State implements Statemethods {

    private boolean gameOver = false;
    private boolean gamePause = false;
    private boolean gameComplete = false;
    private boolean lvlComplete = false;

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private GameoverScreen gameoverScreen;
    private GameCompleteScreen gameCompleteScreen;
    private Loading loadingScreen;
    private int xLvlOffset = 0;
    private int yLvlOffset = 0;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int topBorder = (int) (0.6 * Game.GAME_HEIGHT);
    private int bottomBorder = (int) (0.4 * Game.GAME_HEIGHT);

    private int lvlTilesWide = 40;
    private int maxTilesOffSet1 = lvlTilesWide - Game.TILES_WIDTH;
    private int maxLvlOffsetX = maxTilesOffSet1 * Game.TILES_SIZE;

    private int lvlTilesHeight = 40;
    private int maxTilesOffSet2 = lvlTilesHeight - Game.TILES_HEIGHT;
    private int maxLvlOffsetY = maxTilesOffSet2 * Game.TILES_SIZE;
    private PauseScreen pauseScreen;
    private FirstScreen firstScreen;
    private boolean firstUpdate = true;
    private BufferedImage[] imgLoading;
    private BufferedImage backgroundImg;

    private boolean loading = false;

    private void cameraLR() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }
        xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
    }

    private void cameraUD() {
        int playerY = (int) player.getHitBox().y;
        int diff = playerY - yLvlOffset;
        if (diff > topBorder) {
            yLvlOffset += diff - topBorder;
        } else if (diff < bottomBorder) {
            yLvlOffset += diff - bottomBorder;
        }
        yLvlOffset = Math.max(Math.min(yLvlOffset, maxLvlOffsetY), 0);
    }

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    public void resetAll() {
        if (gameComplete) {
            levelManager.reset();
        }
        enemyManager.resetAll();
        objectManager.resetAll();
        player.reset();
        gamePause = false;
        gameOver = false;
        setStartTime();
        if (lvlComplete){
            if (!gameComplete) levelManager.loadNextLevel();
        }
        
        loadEntity();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(0, 0, 78, 58, this);
        setPosition();
        enemyManager = new EnemyManager(levelManager.getLvlData(), this);
        objectManager = new ObjectManager(levelManager.getLvlData(), this);
        pauseScreen = new PauseScreen(this);
        gameoverScreen = new GameoverScreen(this);
        gameCompleteScreen = new GameCompleteScreen(this);
        firstScreen = new FirstScreen(this);
        loadingScreen = new Loading();
        loadEntity();
        loading = true;
    }

    private void loadEntity() {
        player.loadLvlData(levelManager.getLvlData());
        player.loadMap(levelManager.getMap()); // MINI MAP: DO MAP NHỎ NÊN LƯỢC BỎ
        enemyManager.setLvlData(levelManager.getLvlData());
        objectManager.setLvlData(levelManager.getLvlData());
        setPosition();
        enemyManager.addEnemies();
        objectManager.addObject();
    }

    private void setPosition() {
        float x = 0, y = 0;
        int[][] value = levelManager.getLvlData();
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[0].length; j++) {
                if (value[i][j] == 255) {
                    x = Game.TILES_SIZE * j;
                    y = Game.TILES_SIZE * i;
                }
            }
        }
        player.setPosition(x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !lvlComplete && !gameComplete && !gamePause) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A ->
                    player.setLeft(true);
                case KeyEvent.VK_D ->
                    player.setRight(true);
                case KeyEvent.VK_SPACE ->
                    player.setUp(true);
                case KeyEvent.VK_ESCAPE ->
                    gamePause = !gamePause;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !lvlComplete && !gameComplete && !gamePause) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A ->
                    player.setLeft(false);
                case KeyEvent.VK_D ->
                    player.setRight(false);
                case KeyEvent.VK_SPACE ->
                    player.setUp(false);
            }
        }
    }

    @Override
    public void update() {
        if (firstUpdate) {
            firstScreen.update();

        } else if (loading) {
            loadingScreen.update(this);
           if (!gameComplete && !loading) game.getAudioPlayer().setLevelSong(Gamestate.getIndex());
        } else if (lvlComplete) {
            resetAll();
             lvlComplete = false;
            
        } else if (gameComplete) {
            gameCompleteScreen.update();
        } else if (gamePause) {
            pauseScreen.update();
        } else if (gameOver) {
            gameoverScreen.update();
        } else {
            cameraLR();
            cameraUD();
            enemyManager.update();
            objectManager.update();
            player.update();

        }
    }

    @Override
    public void draw(Graphics g) {

        if (firstUpdate) {
            firstScreen.draw(g);
        } else {
            levelManager.render(g, xLvlOffset, yLvlOffset);
            objectManager.render(g, xLvlOffset, yLvlOffset);
            player.render(g, xLvlOffset, yLvlOffset);
            enemyManager.render(g, xLvlOffset, yLvlOffset);

            if (gamePause) {
                pauseScreen.render(g);
            } else if (gameOver) {
                gameoverScreen.render(g);
            } else if (gameComplete) {
                gameCompleteScreen.render(g);

            }

            if (loading) {
                loadingScreen.draw(g);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }
        if (firstUpdate) {
            firstScreen.mousePressed(e);
        } else if (gamePause) {
            pauseScreen.mousePressed(e);
        } else if (gameComplete) {
            gameCompleteScreen.mousePressed(e);
        } else if (gameOver) {
            gameoverScreen.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (firstUpdate) {
            firstScreen.mouseReleased(e);
        } else if (gamePause) {
            pauseScreen.mouseReleased(e);
        } else if (gameComplete) {
            gameCompleteScreen.mouseReleased(e);
        } else if (gameOver) {
            gameoverScreen.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (firstUpdate) {
            firstScreen.mouseMoved(e);
        } else if (gamePause) {
            pauseScreen.mouseMoved(e);
        } else if (gameOver) {
            gameoverScreen.mouseMoved(e);
        } else if (gameComplete) {
            gameCompleteScreen.mouseMoved(e);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void checkHitObject(Player player) {
        objectManager.checkHitObject(player);
    }

    public void checkHitEnemy(Rectangle2D.Float attackBox, Rectangle2D.Float hitBox) {
        enemyManager.checkHitEnemy(attackBox, hitBox);
    }

    public void unPause() {
        this.gamePause = false;
    }
    public boolean getPause(){
        return gamePause; 
    }

    public void levelComplete() {
        this.lvlComplete = true;
    }

    public void gameOver() {
        this.gameOver = true;
    }

    public void endGame() {
        this.gameComplete = true;
        resetAll();
    }

    public void reset() {
        this.gameComplete = false;
    }

    public void unFirstUpdate() {
        this.firstUpdate = false;
        setStartTime();
    }

    public boolean nextLevel() {
        return enemyManager.nextLevel();
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setStartTime() {
        loading = true;
        loadingScreen.setStartTime();
    }

    public void setLoading(boolean value) {
        this.loading = value;
    }

    public boolean getLoading() {
        return loading;
    }

}
