package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class Customer extends BoxObstacle {
    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;

    private Texture texture;

    //private GuardAIController aiController;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private float scaleX;
    private float scaleY;
    private GameCanvas canvas;

    private Ingredient[] order = new Ingredient[3];
    private boolean satisfied;
    private boolean isActive;
    private int patience;

    public Customer(float x, float y, float width, float height,Texture texture, World world, GameCanvas canvas) {
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
        this.setBodyType(BodyType.KinematicBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
        order[0] = (new Ingredient("apple",200,200,new Texture("apple.png"),-1));
        order[1] = (new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
        order[2] = (new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
        satisfied = false;
        isActive = true;
        patience = 100;
    }

    public Ingredient[] getOrder(){
        return order;
    }
    public boolean getSatisfied(){
        return satisfied;
    }
    public boolean isActive(){
        return isActive;
    }

    public void deactivate(){
        isActive = false;
    }

    public int patience(){
        return patience;
    }

    public void decreasePatience(){
        patience -= 1;
    }

    public boolean isPatient(){
        return patience > 0;
    }

    public boolean serve(Dish dish){
        //change later, order doesn't matter? or some other action?
        if (dish.type.equals(order)){
            satisfied = true;
            return true;
        }
        return false;
    }

    public void move(float x, float y){
        this.setLinearVelocity(new Vector2(x,y));
    }



    public void draw(float scaleX, float scaleY) {
        draw(canvas, scaleX,scaleY, 0, -300);
    }

    public void debug(GameCanvas canvas){
        drawDebug(canvas);
    }
}
