package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class TableObstacle extends NormalObstacle {
    private boolean[] occupation;
    public TableObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        this.occupation = new boolean[2];
        occupation[0] = false;
        occupation[1] = false;
    }

    public boolean isOccupied(int index){
        return occupation[index];
    }

    public boolean isFullyOccupied(){
        return isOccupied(0) && isOccupied(1);
    }

    public Vector2 occupy(int index){
            //i have to tweak the offset later such that this will return the vector corresponding
            // to the "chair" position which the animal will move towards to sit down
       if (index == 1){
            occupation[1] = true;
            return new Vector2((float)(this.getX() + 1.75), (float)(this.getY()-0.8));
        } else if (index == 0) {
           occupation[0] = true;
           return new Vector2((float) (this.getX() - 1.75), (float) (this.getY() - 0.8));
       }
        return this.getPosition();
    }
}
