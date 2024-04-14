package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
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
import com.raccoon.mygame.objects.Shadow;
import com.raccoon.mygame.objects.TableObstacle;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

import java.util.*;

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
    public PatienceMeter pat;
    private Ingredient[] order;
    private int cooking_method;
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

    float height;
    float width;
    private Shadow shadow;
    private String customerType;
    public String[] types = new String[]{"goat","cat","otter","ferret","bear"};

    private float offsetX = 0;
    private float offsetY = 0;
    public Dish servedDish;

    public int orderSize;
    private Texture one = new Texture("singleorder.png");
    private Texture two = new Texture("doubleorder.png");
    private Texture three = new Texture("tripleorder.png");
    private Texture wok =new Texture("wok.png");
    private Texture pot = new Texture("pot.png");
    private Texture cutting_board = new Texture("cutting_board.png");
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
        orderSize = r;
        for(int i = 0; i < r; i++){
            int r_ing = random.nextInt(menu.size);
            order[i] = menu.get(r_ing).clone();
            //System.out.println(menu.get(r_ing).type);
            //System.out.println("ORDER");
        }
        cooking_method = random.nextInt(3);
//        System.out.println(cooking_method);
        satisfied = false;
        isActive = true;

        shadow = new Shadow(x,y,1f,1f);

        controller = new CustomerAIController(tables, this,shadow,
                new float[]{scaleX,scaleY,this.height});
        show = false;
        flipScale = -1;
        onRight = false;
        pat = new PatienceMeter(120, canvas, this);
        pat.create();
        justSatisfied=false;

        this.height = height;
        this.width = width;

        setCustomerType();
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

    public String getCustomerType(){return this.customerType;}

    private void setCustomerType(){
        this.customerType = types[(int)(Math.random()*types.length)];
    }

    public boolean serve(Dish d) {
        for (Ingredient i : this.order){
            if(i!= null) {
//                System.out.println(i.type);
            }
        }
        for (Ingredient i : d.type){
            if (i!=null) {
//                System.out.println(i.type);
            }
        }
        if(d == null){
            return false;
        }
        if(d.station_type != cooking_method){
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
        servedDish = d;
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

    private float provideOXoffset(){
        return 0;
    }

    public void setOffsetX(float offsetX){
        this.offsetX = offsetX;
    }
    public void setOffsetY(float offsetY){
        this.offsetY = offsetY;
    }


    public void draw(float scaleX, float scaleY) {

        if(isActive && controller.state != CustomerAIController.FSMState.WAIT){
            shadow.draw(canvas);
        }

        //somehow give the ability to specify the ox and oy offset
        drawSprite(canvas, (flipScale*-1) * scaleX, scaleY, this.sprite.getRegionWidth()/2f + offsetX, offsetY);
        if (pat.getTime() > 0){
            pat.draw(this.drawScale.x, this.drawScale.y);
        }
        //Texture image, Color tint, float ox, float oy,
        //float x, float y, float angle, float sx, float sy
        if (show) {
            if (!onRight) {
                if (orderSize == 1) {
                    canvas.draw(two, Color.WHITE, 0, -150, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 1f, 0.8f);
                } else if (orderSize == 2) {
                    canvas.draw(three, Color.WHITE, 0, -150, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 0.9f, 0.8f);
                } else if (orderSize == 3) {
                    canvas.draw(three, Color.WHITE, 0, -150, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 1.1f, 0.8f);
                }
                if (cooking_method == 0) {
                    canvas.draw(wok, Color.WHITE, -20, -130,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y,
                            0.0f, 1f, 1f);
                } else if (cooking_method == 1) {
                    canvas.draw(pot, Color.WHITE, -20, -130,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0.0f, 1f, 1f);
                } else {
                    canvas.draw(cutting_board, Color.WHITE, -20, -130,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0.0f, 1f, 1f);
                }
                for (int i = 0; i < order.length; i++) {
                    if (order[i] != null) {

                        order[i].drawTextBubble(canvas, this.getX() * this.drawScale.x,
                                (this.getY()) * drawScale.y, -20 - 50 * (i + 1), -140);
                    }
                }
            } else {
                if (orderSize == 1) {
                    canvas.draw(two, Color.WHITE, 0, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -1f, 0.8f);
                } else if (orderSize == 2) {
                    canvas.draw(three, Color.WHITE, 0, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -0.9f, 0.8f);
                } else if (orderSize == 3) {
                    canvas.draw(three, Color.WHITE, 0, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -1.1f, 0.8f);
                }
                if (cooking_method == 0) {
                    canvas.draw(wok, Color.WHITE, 85, -190,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y,
                            0.0f, 1f, 1f);
                } else if(cooking_method == 1) {
                    canvas.draw(pot, Color.WHITE, 85, -190,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0.0f, 1f, 1f);
                }else {
                    canvas.draw(cutting_board, Color.WHITE, 85, -190,
                            this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0.0f, 1f, 1f);
                }
                for (int i = 0; i < order.length; i++) {
                    if (order[i] != null) {

                        order[i].drawTextBubble(canvas, this.getX() * this.drawScale.x,
                                (this.getY()) * drawScale.y, 70 + 50 * (i + 1), -195);
                    }
                }

            }
        }

    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
    }
}
