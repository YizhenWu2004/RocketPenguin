package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.enums.enums;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.obstacle.SightCone;
import com.raccoon.mygame.obstacle.WheelObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

import static com.raccoon.mygame.enums.enums.PatrolDirection;

public class Guard extends WheelObstacle {
    //initially 0.1, changing to 1
    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;

    private Texture patrolTexture;

    private SightCone sight;
    private GuardAIController aiController;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private float scaleX;
    private float scaleY;
    private GameCanvas canvas;

    public Guard(float x, float y, float width, float height,
                 FilmStrip defaultAnimation, World world, GameCanvas canvas,
                 PatrolDirection patrolDirection, boolean[][] collisionLayer,
                 Vector2[] nodes) {
        super(width/3);
//        patrolTexture = texture;
//        setTexture(new TextureRegion(texture));
        this.sprite = defaultAnimation;
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        setPosition(x, y);
        activatePhysics(world);
        this.setBodyType(BodyType.DynamicBody);
        this.getBody().setUserData(this);
        setDrawScale(scaleX, scaleY);
        sight = new SightCone(x * scaleX, y * scaleY + 100, 1, 1, world, canvas, this);
        this.aiController =
                new GuardAIController(this.getX(), this.getY(), 32, 18,
                        150, 2, patrolDirection, collisionLayer, new Vector2(width,height)
                ,nodes);

    }

    public float getTextureWidth() {
        return patrolTexture.getWidth() * TEXTURE_SX;
    }

    public float getTextureHeight() {
        return patrolTexture.getHeight() * TEXTURE_SY;
    }

    public GuardAIController getAIController(){
        return aiController;
    }

    public void update(float delta, Array<Float> info) {
        if (aiController != null) {
            this.setLinearVelocity(new Vector2(aiController.getSpeed(this.getPosition(), delta, info)));
            Vector2 newPosition = this.getPosition().cpy().scl(scaleX, scaleY); // Assuming scaleX and scaleY are the scales you need to apply
//            if(getY() < 10){
//            newPosition.add(-40, 130);} else if(getY() < 13){
//                newPosition.add(-40, 150);
//                // Adjust this value based on where you want the sightcone relative to the guard
//            }else{
//                newPosition.add(-40, 160);
//            }
////            System.out.println("goose height is: " + getY());
//            sight.updatePosition(newPosition);
            if(getAIController().getOrien() == GuardAIController.GuardOrientation.LEFT){
                newPosition.add(-30, 100);
            } else if(getAIController().getOrien() == GuardAIController.GuardOrientation.RIGHT){
                newPosition.add(-60, 100);
            } else if((getAIController().getOrien() == GuardAIController.GuardOrientation.UP)){
                newPosition.add(0, 100);
            } else {
                newPosition.add(0, 100);
            }
            sight.updatePosition(newPosition);
        }
    }

    public void draw(float scaleX, float scaleY) {
        if(getAIController().getDirection()){
        drawSprite(canvas, scaleX, scaleY, 30, 20);
        sight.render();}
        if(!getAIController().getDirection()){
            drawSprite(canvas, -scaleX, scaleY, 30, 20);
            sight.render();
        }
    }

    public SightCone getSight() {
        return sight;
    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
//        aiController.debugDrawPath(canvas);
    }

    public void switchToChaseMode() {
        this.aiController.setAIStateChase();
    }

    public void switchToWanderMode() {
        this.aiController.setAIStateWander();
    }
    public PatrolDirection getP(){
        return this.aiController.getPatrolDirection();
    }
}
