package main;

import gamestates.Playing;
import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.*;

public class Game implements Runnable {

    private GamePanel gamePanel;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static int TILES_HEIGHT = 18;
    public final static int TILES_WIDTH = 28;
    public final static int TILES_SIZE = TILES_DEFAULT_SIZE;
    public final static int GAME_WIDTH = TILES_SIZE * TILES_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_HEIGHT;

    private Playing playing;
    private Menu menu;
    private Options options;
    private AudioPlayer audioPlayer;
    private Choose choose;

    public Game() {
        System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
        initClasses();
        gamePanel = new GamePanel(this);
        new GameWindows(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        audioPlayer = new AudioPlayer();
        playing = new Playing(this);
        menu = new Menu(this);
        options = new Options(this);
        choose = new Choose(this);

    }

    public void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {
        switch (Gamestate.getState()) {
            case MENU ->
                menu.draw(g);
            case CHOOSE ->
                choose.draw(g);
            case PLAYING ->
                playing.draw(g);
            case OPTIONS ->
                options.draw(g);
            case QUIT ->
                System.exit(0);
        }
    }

    @SuppressWarnings("incomplete-switch")
    public void update() {
        switch (Gamestate.getState()) {
            case MENU ->
                menu.update();
            case CHOOSE ->
                choose.update();
            case PLAYING ->
                playing.update();
            case OPTIONS ->
                options.update();
            case QUIT ->
                System.exit(0);
        }
        audioPlayer.updateSfxMute();
        audioPlayer.updateMusicMute();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;

            }

            if (true) {
                if (System.currentTimeMillis() - lastCheck >= 1000) {

                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;

                }
            }

        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public Options getOptions() {
        return options;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public Choose getChoose() {
        return choose;
    }

}
