package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class NormalObstacle extends BoxObstacle {
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private Texture texture;
    private float scaleX;
    private float scaleY;
    private GameCanvas canvas;

    public NormalObstacle(float x, float y, float width, float height, Texture texture, World world, GameCanvas canvas) {
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
    }

    public void draw(float scaleX, float scaleY, int ox, int oy) {
        draw(canvas, scaleX,scaleY, ox, oy);
    }

    public void debug(GameCanvas canvas){
        drawDebug(canvas);
    }
}
