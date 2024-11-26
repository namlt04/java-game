package object;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import entities.Player;
import gamestates.Playing;
import main.Game;

public class ObjectManager {

    private ArrayList<Heart> listHeart;
    private ArrayList<Diamond> listDiamond;
    private ArrayList<Container> listContainer;
    private int[][] lvlData;
    private Door door;

    private Playing playing;

    public ObjectManager(int[][] lvlData, Playing playing) {
        this.playing = playing;
        this.lvlData = lvlData;
        addObject();
    }

    public void addObject() {

        listHeart = new ArrayList<>();
        listDiamond = new ArrayList<>();
        listContainer = new ArrayList<>();
        int row = lvlData.length;
        int col = lvlData[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int value = lvlData[i][j];
                switch (value) {
                    case 30:
                        listDiamond.add(new Diamond(j * Game.TILES_SIZE + 15, i * Game.TILES_SIZE + 18));
                        break;
                    case 31:
                        listHeart.add(new Heart(j * Game.TILES_SIZE + 15, i * Game.TILES_SIZE + 18));
                        break;

                    case 34:
                        Canon canonL = new Canon(j * Game.TILES_SIZE - 10, i * Game.TILES_SIZE - 10, 1, playing);
                        BombByPig bombL = new BombByPig(j * Game.TILES_SIZE + 20, i * Game.TILES_SIZE + 20, 1, lvlData);
                        listContainer.add(new Container(canonL, bombL));
                        break;
                    case 35:
                        Canon canonR = new Canon(j * Game.TILES_SIZE + 90, i * Game.TILES_SIZE - 10, -1, playing);
                        BombByPig bombR = new BombByPig(j * Game.TILES_SIZE + 30, i * Game.TILES_SIZE + 20, -1, lvlData);
                        listContainer.add(new Container(canonR, bombR));
                        break;
                    case 139:
                        (this.door) = new Door(j * Game.TILES_SIZE, i * Game.TILES_SIZE + 15);
                        break;

                }
            }

        }

        playing.getPlayer().setExpectKey(listDiamond.size());
    }

    public void update() {
        for (Heart heart : listHeart) {
            heart.update();
        }
        for (Diamond diamond : listDiamond) {
            diamond.update();
        }
        for (Container container : listContainer) {
            container.update();
        }
        door.update();
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        door.render(g, xLvlOffset, yLvlOffset);
        for (Heart heart : listHeart) {
            heart.render(g, xLvlOffset, yLvlOffset);
        }

        for (Diamond diamond : listDiamond) {
            diamond.render(g, xLvlOffset, yLvlOffset);
        }
        for (Container container : listContainer) {
            container.render(g, xLvlOffset, yLvlOffset);
        }

    }

    public void checkHitObject(Player player) {
        Rectangle2D.Float hitBox = player.getHitBox();
        for (Heart heart : listHeart) {
            if (heart.isActive()) {
                if (hitBox.intersects(heart.getHitBox())) {
                    playing.getPlayer().changeHealth(+1);
                    playing.getGame().getAudioPlayer().playEffect(8);
                    heart.setActive(false);
                }
            }
        }
        for (Diamond diamond : listDiamond) {
            if (diamond.isActive()) {
                if (hitBox.intersects(diamond.getHitBox())) {
                    playing.getPlayer().changeDiamondCollect();
                    playing.getGame().getAudioPlayer().playEffect(7);
                    diamond.setActive(false);
                }
            }
        }

        for (Container container : listContainer) {
            BombByPig bomb = container.getBomb();
            if (bomb.isActive()) {
                if (hitBox.intersects(bomb.getHitBox())) {
                    bomb.setMove(false);
                    bomb.updateBoom();
                    playing.getPlayer().changeHealth(-1);
                }
            }
        }
        if (door.getHitBox().contains(hitBox) && player.nextLevel() && door.getState() == Door.IDLE && !playing.nextLevel()) {
            door.newState(Door.OPEN);
            player.newState(Player.DISAPPEAR);
        }
    }

    public void resetAll() {
        listHeart.clear();
        listDiamond.clear();
        listContainer.clear();

    }

    public void setLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        resetAll();
        addObject();
    }

}
