package object;

import java.awt.Graphics;

public class Container {

    private Canon canon;
    private BombByPig bomb;

    public Container(Canon canon, BombByPig bomb) {
        this.canon = canon;
        this.bomb = bomb;
    }

    public void update() {
        canon.update(bomb);
        bomb.update();
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        canon.render(g, xLvlOffset, yLvlOffset);
        bomb.render(g, xLvlOffset, yLvlOffset);
    }

    public void reset() {
        canon.reset();
        bomb.reset();
    }

    public BombByPig getBomb() {
        return bomb;
    }

}
