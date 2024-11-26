package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import main.Game;

public class EnemyManager {

    private ArrayList<PigPatrol> listPigPatrol = new ArrayList<>();
    private ArrayList<PigGuard> listPigGuard = new ArrayList<>();
    private KingPig kingPig = new KingPig(0, 0, 38, 28);
    private BufferedImage pig, kingpig;
    private BufferedImage[][] aniPig, aniKingPig;
    private Playing playing;
    private int[][] lvlData;

    public EnemyManager(int[][] lvlData, Playing playing) {
        this.playing = playing;
        this.lvlData = lvlData;
        addEnemies();
    }

    public void addEnemies() {
        for (int i = 0; i < lvlData.length; i++) {
            for (int j = 0; j < lvlData[0].length; j++) {
                switch (lvlData[i][j]) {
                    case 32:
                        listPigPatrol.add(new PigPatrol(j * Game.TILES_SIZE, i * Game.TILES_SIZE, 34, 28));
                        break;
                    case 33:
                        listPigGuard.add(new PigGuard(j * Game.TILES_SIZE, i * Game.TILES_SIZE, 34, 28));
                        break;
                    case 139:
                        kingPig = new KingPig(j * Game.TILES_SIZE, i * Game.TILES_SIZE, 38, 28);
                        break;
                }
            }
        }
    }

    public void update() {
        for (PigPatrol pig : listPigPatrol) {
            if (pig.isActive()) {
                pig.update(lvlData, playing);
            }
        }
        for (PigGuard pig : listPigGuard) {
            if (pig.isActive()) {
                pig.update(lvlData, playing);
            }
        }

        if (kingPig.isActive()) {
            kingPig.update(lvlData, playing);
        }

    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (PigPatrol pig : listPigPatrol) {
            pig.render(g, xLvlOffset, yLvlOffset);
        }
        for (PigGuard pig : listPigGuard) {
            pig.render(g, xLvlOffset, yLvlOffset);
        }
        kingPig.render(g, xLvlOffset, yLvlOffset);
    }

    public void checkHitEnemy(Rectangle2D.Float attackBox, Rectangle2D.Float hitBox) {
        for (PigPatrol pig : listPigPatrol) {
            if (pig.isActive()) {
                if (attackBox.intersects(pig.getHitBox())) {
                    pig.hurt();
                    return;
                }
            }
        }

        for (PigGuard pig : listPigGuard) {
            if (pig.isActive()) {
                if (attackBox.intersects(pig.getHitBox())) {
                    pig.hurt();
                    return;
                }
            }
        }

        if (kingPig.isActive()) {
            if (attackBox.intersects(kingPig.getHitBox()) && kingPig.canSeePlayerStatic(lvlData, hitBox)) {
                kingPig.hurt();
            }
        }

    }

    public void resetAll() {
        listPigPatrol.clear();
        listPigGuard.clear();
        kingPig.reset();
    }

    public boolean nextLevel() {
        return kingPig.isActive();
    }

    public void setLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        resetAll();
    }

}
