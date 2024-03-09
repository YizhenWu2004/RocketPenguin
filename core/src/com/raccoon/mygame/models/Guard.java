package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class Guard extends BoxObstacle {
    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;
    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    private Texture patrolTexture;

    private GuardAIController aiController;


    private BoxObstacle sight;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private float scaleX;
    private float scaleY;
    private GameCanvas canvas;

    public Guard(float x, float y, float width, float height, Texture texture, World world, GameCanvas canvas){
        super(width, height);
        patrolTexture = texture;
        setTexture(new TextureRegion(texture));
        scaleX= canvas.getWidth()/WORLD_WIDTH;
        scaleY = canvas.getHeight()/WORLD_HEIGHT;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        setPosition(x,y);
        activatePhysics(world);
        this.setBodyType(BodyType.KinematicBody);
        setDrawScale(scaleX, scaleY);

        //world height and width currently hard coded in, consider changing later
        this.aiController = new GuardAIController(this.getX(), this.getY(), 32, 18, 150, 2);

        sight = new BoxObstacle(50/scaleX, 1*getTextureHeight()/scaleY);
        sight.setDrawScale(1.0f, 1.0f);

        sight.setSensor(true);
        sight.setDensity(1.0f);
        sight.activatePhysics(world);
        sight.setPosition((x + getTextureWidth())/scaleX, (y + getTextureHeight()*5)/scaleY);
        sight.setBodyType(BodyType.DynamicBody);
        this.getBody().setUserData(this);
        sight.getBody().setUserData(this);
//        sight.drawDebug(canvas);
    }

    public float getTextureWidth() { return patrolTexture.getWidth() * TEXTURE_SX; }
    public float getTextureHeight() { return patrolTexture.getHeight() * TEXTURE_SY; }

    public void update(float delta, Array<Float> info) {
        //g.setPosition(g.getPosition().add(velocity));
        if (aiController != null) {
            this.setLinearVelocity(new Vector2(aiController.getSpeed(this.getPosition(),delta, info)));
            sight.setLinearVelocity(new Vector2(aiController.getSpeed(sight.getPosition(),delta,info)));
            //sight.setAngle((float) ((sight.getAngle() + 0.01f) % (2*Math.PI)));
            //System.out.println(g.getLinearVelocity().x);
        }
    }

    public void draw(float scaleX, float scaleY) {
          draw(canvas, scaleX,scaleY, 0, -600);
    }

    public void debug(GameCanvas canvas){
        drawDebug(canvas);
        sight.drawDebug(canvas);
    }

    public void switchToChaseMode() {
        System.out.println("I SEE YOU CHASE");
        this.aiController.setAIStateChase();
    }
}
