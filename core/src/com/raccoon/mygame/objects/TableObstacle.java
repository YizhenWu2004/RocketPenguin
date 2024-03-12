package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class TableObstacle extends BoxObstacle {
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private Texture texture;
    private float scaleX;
    private float scaleY;
    private boolean[] occupation;
    private GameCanvas canvas;
    public TableObstacle(float x, float y, float width, float height, Texture texture, World world, GameCanvas canvas) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX= canvas.getWidth()/WORLD_WIDTH;
        scaleY = canvas.getHeight()/WORLD_HEIGHT;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.StaticBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
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
        if (index == 0){
            occupation[0] = true;
            return this.getPosition();
            //i have to tweak the offset later such that this will return the vector corresponding
            // to the "chair" position which the animal will move towards to sit down
        }else if (index == 1){
            occupation[1] = true;
            return this.getPosition();
        }
        return this.getPosition();
    }


    public void draw(float scaleX, float scaleY) {
        draw(canvas, scaleX,scaleY, -80, -300);
    }

    public void debug(GameCanvas canvas){
        drawDebug(canvas);
    }
}
