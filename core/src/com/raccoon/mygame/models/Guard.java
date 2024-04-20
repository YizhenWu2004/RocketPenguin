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
import com.raccoon.mygame.objects.Expression;
import com.raccoon.mygame.objects.QuestionExpression;
import com.raccoon.mygame.objects.Shadow;
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

    private float height;
    private float width;
    private GameCanvas canvas;

    private Expression exclam;

    private Expression zzz;

    private QuestionExpression question;

    private Shadow shadow;

    public Guard(float x, float y, float width, float height,
                 FilmStrip defaultAnimation, World world, GameCanvas canvas,
                 PatrolDirection patrolDirection, boolean[][] collisionLayer,
                 Array<Vector2> nodes, GuardAIController.GuardOrientation spawnOrien) {
        super(width/3);
//        patrolTexture = texture;
//        setTexture(new TextureRegion(texture));
        this.sprite = defaultAnimation;
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.height = height;
        this.width = width;
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
                ,nodes, spawnOrien);

        exclam = new Expression("exclamation",x,y);
        zzz = new Expression("zzz",x,y);
        question = new QuestionExpression("question",x,y,aiController.getSusMeter(),30);

        shadow = new Shadow(x,y,0.8f,0.8f);

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
        Vector2 newPosition = this.getPosition().cpy().scl(scaleX, scaleY);
        
        switch (getAIController().getOrien()) {
            case LEFT:
                newPosition.add(0, 10);
                break;
            case RIGHT:
                newPosition.add(0, 20);
                break;
            default:
                newPosition.add(0, 20);
                break;
        }
        
        sight.updatePosition(newPosition);
        if(!sight.isPlayerDetected()){
            aiController.decrementSusMeter(1);
        }

        float exclamationX = this.getX() * scaleX;
        float exclamationY = (this.getY() + height + 1) * scaleY;
        exclam.setPosition(exclamationX, exclamationY);

        if(aiController.getOrien() == GuardAIController.GuardOrientation.RIGHT){
            question.setPosition((this.getX()-1.5f) * scaleX,exclamationY);
        }
        else if(aiController.getOrien() == GuardAIController.GuardOrientation.RIGHT){
            question.setPosition((this.getX()+3) * scaleX,exclamationY);
        }
        else{
            question.setPosition(exclamationX,exclamationY);
        }

        question.updateCurProgress(aiController.getSusMeter());

        zzz.setPosition(exclamationX,exclamationY);

        float shadowX = this.getX()* scaleX-shadow.getTextureWidth()/2;
        float shadowY = (this.getY() * scaleY) - (this.height * scaleY / 2);
        float offsetCons = shadow.getTextureWidth()/3;
        if(aiController.getOrien() == GuardAIController.GuardOrientation.LEFT){
            shadow.setPosition(shadowX+offsetCons, shadowY);
        }
        else if(aiController.getOrien() == GuardAIController.GuardOrientation.RIGHT){
            shadow.setPosition(shadowX-offsetCons*0.5f, shadowY);
        }
        else{
            shadow.setPosition(shadowX, shadowY);
        }
    }

    if (aiController.isChase() || aiController.isSleep()){
        sight.deactivateSight();
    }
    else{
        sight.reactivateSight();
    }
  }


    public void draw(float scaleX, float scaleY) {
        shadow.draw(canvas);
        if(getAIController().getOrien() == GuardAIController.GuardOrientation.LEFT){
            drawSprite(canvas, scaleX, scaleY, 30, 20);
            sight.render();
        }
        else if(getAIController().getOrien() == GuardAIController.GuardOrientation.RIGHT){
            drawSprite(canvas, -scaleX, scaleY, 30, 20);
            sight.render();
        }
        else{
            drawSprite(canvas, -scaleX, scaleY, 30, 20);
            sight.render();
        }

        if (aiController.isChase()) {
            exclam.draw(canvas);
        }
        else if(aiController.isSleep()){
            zzz.draw(canvas);
        }
        else if(aiController.isSus()){
            question.draw(canvas);
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
//        chaseDelay = CHASE_DELAY;
    }

    public void switchToDefaultMode() {
        if(aiController.patrolDirection ==  PatrolDirection.SLEEP_WAKE){
            this.aiController.setAIStateWake();
        }
        else {
            this.aiController.setAIStateWander();
        }
//        isChaseMode = false;
    }
    public PatrolDirection getP(){
        return this.aiController.getPatrolDirection();
    }

    public void resetSusMeter(){
        aiController.susMeter =0;
    }
}
