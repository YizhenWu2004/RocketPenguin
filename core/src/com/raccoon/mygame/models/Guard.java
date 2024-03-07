package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class Guard {
    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;
    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    private Texture patrolTexture;

    private GuardAIController aiController;

    private BoxObstacle g;
    private BoxObstacle g2;

    private BoxObstacle sight;



    public Guard(float x, float y, float width, float height, Texture texture, World world){
        velocity = new Vector2(0.0f, 0.0f);
        this.width = width;
        this.height = height;
        patrolTexture = texture;

        float leftBoundary = x-150;
        float rightBoundary = x+150;
        float speed = 60;
        this.aiController = new GuardAIController(leftBoundary, rightBoundary, speed);

        g = new BoxObstacle(100,50);
        g.setDensity(1.0f);
        g.activatePhysics(world);
        Texture t = new Texture("gooseReal.png");
        TextureRegion te = new TextureRegion(t);
        g.setTexture(te);
        g.setPosition(x,y);
        g.setBodyType(BodyType.KinematicBody);
        g.setFixedRotation(true);

        sight = new BoxObstacle(250, 3*getTextureHeight());
        sight.setSensor(true);
        sight.setDensity(1.0f);
        sight.activatePhysics(world);
        sight.setPosition(x + getTextureWidth(), y + getTextureHeight()*5);
        sight.setBodyType(BodyType.KinematicBody);

    }

    //Setters
    public void setX(float x){g.setX(x);}
    public void setY(float y){g.setY(y);}
    public void setPosition(Vector2 position){
        g.setPosition(position);
        sight.setPosition(position.x + getTextureWidth(), position.y + getTextureHeight()*5);
    }

    public void setVX(float value) {
        velocity.x = value;
    }

    public float getVY() {
        return velocity.y;
    }

    //Getters
    public float getX(){return g.getX();}
    public float getY(){return g.getY();}
    public Vector2 getPosition(){return g.getPosition();}

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getVX() {
        return velocity.x;
    }

    public void setVY(float value) {
        velocity.y = value;
    }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public float getTextureWidth() { return patrolTexture.getWidth() * TEXTURE_SX; }
    public float getTextureHeight() { return patrolTexture.getHeight() * TEXTURE_SY; }

    public void update(float delta) {
        //g.setPosition(g.getPosition().add(velocity));
        if (aiController != null) {
            g.setLinearVelocity(new Vector2(aiController.getSpeed(g.getPosition(),delta),0));
            sight.setLinearVelocity(new Vector2(aiController.getSpeed(sight.getPosition(),delta),0));
            //sight.setAngle((float) ((sight.getAngle() + 0.01f) % (2*Math.PI)));
            //System.out.println(g.getLinearVelocity().x);

        }
    }

    public void draw(GameCanvas canvas) {
//        canvas.draw(patrolTexture, Color.WHITE, 10, 10,
//                position.x, position.y, 0.0f, TEXTURE_SX, TEXTURE_SY);
          g.draw(canvas, TEXTURE_SX, TEXTURE_SY, 0, -600);
//        float debugX = position.x;
//        float debugY = position.y;
//        float debugWidth = getTextureWidth();
//        float debugHeight = getTextureHeight();
//        Color debugColor = Color.RED;
//        canvas.drawDebugRectangle(debugX, debugY, debugWidth, debugHeight, debugColor);
    }

    public void debug(GameCanvas canvas){

        g.drawDebug(canvas);
        sight.drawDebug(canvas);
    }

}
