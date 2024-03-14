package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.controllers.CustomerAIController;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.TableObstacle;
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

    private Ingredient order;
    private boolean satisfied;
    private boolean isActive;
    private int patience;
    private boolean show;
    private CustomerAIController controller;
    public Vector2 position_on_table;
    public TableObstacle t;
    public int collided = 0;

    public Customer(float x, float y, float width, float height,Texture texture, World world, GameCanvas canvas, Array<TableObstacle> tables, int ordernum) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX= canvas.getWidth()/WORLD_WIDTH;
        scaleY = canvas.getHeight()/WORLD_HEIGHT;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);//heavy so player cant move them
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.DynamicBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
        if(ordernum == 1) {
            order = (new Ingredient("apple", 200, 200, new Texture("apple.png"), -1));
        } else if (ordernum == 2){
            order = (new Ingredient("banana", 200, 200, new Texture("banana.png"), -1));
        } else if (ordernum == 3){
            order = new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1);

        } else {
            order = new Ingredient("orange",900,400,new Texture("orange.png"),-1);
        }
       // order[0] = (new Ingredient("apple",200,200,new Texture("apple.png"),-1));
        //order[1] = (new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
        //order[2] = (new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
        satisfied = false;
        isActive = true;
        patience = 100;
        controller = new CustomerAIController(tables, this);
        show = false;

    }

    public Ingredient getOrder(){
        return order;
    }
    public boolean isSatisfied(){
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

    public boolean getShow(){return show;}
    public void setShow(boolean b){
        show = b;
    }

    public boolean serve(Ingredient ing){
        //change later, order doesn't matter? or some other action?
        if(ing == null){
            return false;
        }
        if (ing.type.equals(order.type)){
            satisfied = true;
            return true;
        }
        return false;
    }

    public void move(){
        controller.getAction();
    }



    public void draw(float scaleX, float scaleY) {
        draw(canvas, scaleX,scaleY, 0, -600);
        if (show){
            order.drawTextBubble(canvas, this.getX() * 60, this.getY() * 60, 0,0);
        }
    }

    public void debug(GameCanvas canvas){
        drawDebug(canvas);
    }
}
