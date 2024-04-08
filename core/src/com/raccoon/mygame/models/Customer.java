package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.controllers.CustomerAIController;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.controllers.PatienceMeter;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.TableObstacle;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

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
    private PatienceMeter pat;
    private Ingredient[] order;
    private boolean satisfied;
    private boolean isActive;
    private int patience;
    private boolean show;
    private CustomerAIController controller;
    //public Vector2 position_on_table;
    //public TableObstacle t;
    //public int collided = 0;
    public int flipScale;
    public boolean onRight;

    public boolean justSatisfied;
    public Customer(float x, float y, float width, float height, FilmStrip defaultCustomerSprite, World world, GameCanvas canvas, Array<TableObstacle> tables, int ordernum) {
        super(x, y, width, height);
//        this.texture = texture;
//        setTexture(new TextureRegion(texture));
        this.sprite = defaultCustomerSprite;
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);//heavy so player cant move them
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.KinematicBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
        order = new Ingredient[3];

        Array<Ingredient> menu = new Array<Ingredient>();
        menu.add(new Ingredient("apple", 200, 200, new Texture("720/apple.png"), -1));
        menu.add(new Ingredient("banana", 200, 200, new Texture("720/banana.png"), -1));
        menu.add(new Ingredient("orange", 200, 200, new Texture("720/orange.png"), -1));
        menu.add(new Ingredient("greenpepper", 200, 200, new Texture("720/greenpepper.png"), -1));

        Random random = new Random();
        int r = random.nextInt(3) + 1;
        for(int i = 0; i < r; i++){
            int r_ing = random.nextInt(menu.size);
            order[i] = menu.get(r_ing).clone();
//            System.out.println(menu.get(r_ing).type);
        }
        satisfied = false;
        isActive = true;
        controller = new CustomerAIController(tables, this);
        show = false;
        flipScale = -1;
        onRight = false;
        pat = new PatienceMeter(60, canvas, this);
        pat.create();
        justSatisfied=false;
    }

    public Ingredient[] getOrder() {
        return order;
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean b) {
        show = b;
    }

    public boolean serve(Dish d) {
        if(d == null){
            return false;
        }
        ArrayList<Ingredient> temp1 = new ArrayList<>();
        ArrayList<Ingredient> temp2 = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if (d.type[i] != null){
                temp1.add(d.type[i]);
                //System.out.println(d.type[i]);
            }
        }
        for(int i = 0; i < 3; i++){
            if (order[i] != null){
                temp2.add(order[i]);
            }
        }
        if(temp1.size() != temp2.size()){
            //System.out.println("stopped here");
            return false;
        }
        //System.out.println("HERE");
        Collections.sort(temp1);
        for(Ingredient i :temp1){
            if (i != null){
//                System.out.println(i.type);
            }
        }
        Collections.sort(temp2);
        for(Ingredient i :temp2){
            if (i != null){
//                System.out.println(i.type);
            }
        }

        for(int i = 0; i < temp1.size(); i++){
            if (!(temp1.get(i).type).equals(temp2.get(i).type)){
                //System.out.println("stopper here");
                return false;
            }
        }
        satisfied = true;
        justSatisfied =true;
        return true;
    }

    public void move() {
        controller.getAction();
    }

    public int time(){
        return pat.getTime();
    }

    public int maxTime(){
        return pat.getMaxTime();
    }

    public void timeOut(){
        controller.timeOut();
    }


    public void draw(float scaleX, float scaleY) {
//0, -600 for the final 2 parameters???
        drawSprite(canvas, (flipScale*-1) * scaleX, scaleY, 50, 0);
        pat.draw();
        if (show) {
            for(int i = 0; i < order.length; i++){
                if (order[i] != null){
                    order[i].drawTextBubble(canvas, this.getX() * 40, (this.getY()+2) * 40, 0, 0);
                }
            }
            //order.drawTextBubble(canvas, this.getX() * 60, this.getY() * 60, 0, 0);
        }
<<<<<<< Updated upstream
=======
        if(isActive && controller.state != CustomerAIController.FSMState.WAIT){
            shadow.draw(canvas);
        }
>>>>>>> Stashed changes
    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
    }
}
