package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;

public class HelpMethod {
    

    public static final int FALL = 1;
    public static final int JUMP = 2;

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData)) {
            if (!IsSolid(x + width, y + height, lvlData)) {
                if (!IsSolid(x + width, y, lvlData)) {
                    if (!IsSolid(x, y + height, lvlData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean CanMoveHereStatic(float x, float y, float width, float height, int[][] lvlData) {

        int tileX1 = (int) x / Game.TILES_SIZE;
        int tileY1 = (int) y / Game.TILES_SIZE;
        int tileX2 = (int) (x + width) / Game.TILES_SIZE;
        int tileY2 = (int) y / Game.TILES_SIZE;
        int tileX3 = (int) x / Game.TILES_SIZE;
        int tileY3 = (int) (y + height) / Game.TILES_SIZE;
        int tileX4 = (int) (x + width) / Game.TILES_SIZE;
        int tileY4 = (int) (y + height) / Game.TILES_SIZE;
        if (lvlData[tileY1][tileX1] == 249
                && lvlData[tileY2][tileX2] == 249
                && lvlData[tileY3][tileX3] == 249
                && lvlData[tileY4][tileX4] == 249) {
            return true;
        }
        return false;

    }

    public static boolean CanMoveHereWhenJumping(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData, JUMP)) {
            if (!IsSolid(x + width, y, lvlData, JUMP)) // nhảy lên thì chỉ cần kiểm tra ở
            {
                return true;
            }
        }
        return false;
    }

    public static boolean CanMoveHereWhenFalling(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y + height, lvlData, FALL)) {
            if (!IsSolid(x + width, y + height, lvlData, FALL)) // nếu nó đang nhảy xuống thì chỉ cần kiểm tra 2 cạnh dưới 
            {
                return true;
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData, int state) {
        if (x < 0 || x >= 1280) {
            return true; 
        }
        if (y < 0 || y >= 1280) {
            return true; 
        }
        float xIndex = x / Game.TILES_SIZE;  
        float yIndex = y / Game.TILES_SIZE; 
        int value = lvlData[(int) yIndex][(int) xIndex];
        if (state == FALL) {
            if (value == 0 || value == 56) {
                return true;
            }
            return false;
        } else {
            if (value == 0) {
                return true;
            }
            return false;
        }
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= 1280) {
            return true; 
        }
        if (y < 0 || y >= 1280) {
            return true; 
        }
        float xIndex = x / Game.TILES_SIZE; 
        float yIndex = y / Game.TILES_SIZE;  
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    private static boolean IsTileSolid(int x, int y, int[][] lvlData) {
        int value = lvlData[y][x]; 
        switch (value) {
            case 0:
                return true;
            case 56:
                return true;
            default:
                return false;
        }
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) 
        {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) { 
                return false;

            }
        }
        return true;
    }

}
