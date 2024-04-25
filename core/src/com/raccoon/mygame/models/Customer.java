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
import com.raccoon.mygame.objects.*;
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
    public GameCanvas canvas;
    public PatienceMeter pat;
    private Ingredient[] order;
    private int cooking_method;

    public enum SATISFIED {
        NOTYET,
        HAPPY,
        SAD,
        NO
    }

    public SATISFIED satisfied;
    private boolean isActive;
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
    private Shadow shadow = new Shadow(0,0,1f,1f);;
    private String customerType;
    public String[] types = new String[]{"goat","cat","otter","ferret","bear"};

    private float offsetX = 0;
    private float offsetY = 0;
    public Dish servedDish;

    public int orderSize;
    private Texture one = new Texture("order/one.png");
    private Texture two = new Texture("order/two.png");
    private Texture three = new Texture("order/three.png");
    private Texture wok =new Texture("order/wok.png");
    private Texture pot = new Texture("order/pot.png");
    private Texture cutting_board = new Texture("order/cutting_board.png");

    private Expression question;

    private Expression thumbsUp;

    private Expression thumbsDown;
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
        satisfied = SATISFIED.NOTYET;
        isActive = true;

//        shadow;

        controller = new CustomerAIController(tables, this,shadow,
                new float[]{scaleX,scaleY,this.height});
        show = false;
        flipScale = -1;
        onRight = false;
//        pat = new PatienceMeter(120, canvas, this);
//        pat.create();

        pat = null;

        justSatisfied=false;

        this.height = height;
        this.width = width;

        setCustomerType();

        question = new Expression("customerQuestion",x,y);

        thumbsUp = new Expression("customerThumbsUp",x,y);

        thumbsDown = new Expression("customerThumbsDown",x,y);
    }

    public Ingredient[] getOrder() {
        return order;
    }

    public boolean isSatisfied() {
        return (satisfied == SATISFIED.SAD || satisfied == SATISFIED.HAPPY);
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

    public boolean canShow(){
        return controller.state == CustomerAIController.FSMState.WAIT;
    }

    public boolean serve(Dish d) {
        if(!(controller.state == CustomerAIController.FSMState.WAIT)){
            return false;
        }
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
        satisfied = SATISFIED.HAPPY;
        justSatisfied =true;
        servedDish = d;
        return true;
    }

    public void move() {
        controller.getAction();
    }

    public int time(){
        if(pat == null){
            //means pat hasn't been initialized yet
            return 1;
        }
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

//        //todo still shows when order has been taken, and when order has been satisfied, and also weird that doesn't show for some customers
//        //todo essentially, i only want this to show during WAIT and if order has not been taken
//        if(controller.state == CustomerAIController.FSMState.WAIT && !satisfied && !show){
//            question.drawCustomerQuestion(canvas,this.getX(),this.getY()+10,this.drawScale.x,this.drawScale.y);
//        }

        float customerYOffset;
        float customerXOffset;

        if(customerType == "cat") {
            customerYOffset = 2;
            customerXOffset = 0.6f;
        }
        else if(customerType == "bear" ) {
            customerYOffset = 1;
            customerXOffset = 0.6f;
        }
        else if(customerType == "goat"){
            customerYOffset = 0;
            customerXOffset = 0;
        }
        else {
            customerYOffset = 0;
            customerXOffset = 0;
        }

        if(isSatisfied()){
            thumbsUp.drawCustomerQuestion(canvas,this.getX()+customerXOffset,this.getY()+3+customerYOffset,this.drawScale.x,this.drawScale.y);
        }

        if(satisfied == SATISFIED.NO){
            thumbsDown.drawCustomerQuestion(canvas,this.getX()+customerXOffset,this.getY()+3+customerYOffset,this.drawScale.x,this.drawScale.y);
        }

        //somehow give the ability to specify the ox and oy offset
        drawSprite(canvas, (flipScale*-1) * scaleX, scaleY, this.sprite.getRegionWidth()/2f + offsetX, offsetY);
        if (!(pat == null) &&pat.getTime() > 0){
            pat.draw(this.drawScale.x, this.drawScale.y);
        }
        //Texture image, Color tint, float ox, float oy,
        //float x, float y, float angle, float sx, float sy
        if (show && controller.state == CustomerAIController.FSMState.WAIT) {
            if (!onRight) {
                //System.out.println(this.getX() * this.getDrawScale().x);
                if (orderSize == 1) {
                    canvas.draw(one, Color.WHITE, 150, -280, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -0.7f, 0.7f);
                    drawCooking(-10,-285);
                    drawOrder(-85,-288);
                } else if (orderSize == 2) {
                    canvas.draw(two, Color.WHITE, 190, -280, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -0.7f, 0.7f);
                    drawCooking(0,-285);
                    drawOrder(-125,-288);
                } else if (orderSize == 3) {
                    canvas.draw(three, Color.WHITE, 220, -280, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, -0.7f, 0.7f);
                    drawCooking(20, -285);
                    drawOrder(-150,-288);
                }

            } else {
                if (orderSize == 1) {
                    canvas.draw(one, Color.WHITE, 150, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 0.7f, 0.7f);
                    drawCooking(170,-225);
                    drawOrder(90,-225);
                } else if (orderSize == 2) {
                    canvas.draw(two, Color.WHITE, 190, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 0.7f, 0.7f);
                    drawCooking(210,-225);
                    drawOrder(90,-225);
                } else if (orderSize == 3) {
                    canvas.draw(three, Color.WHITE, 220, -220, this.getX() * this.getDrawScale().x,
                            this.getY() * this.getDrawScale().y, 0, 0.7f, 0.7f);
                    drawCooking(240,-225);
                    drawOrder(70,-225);
                }

            }
        }
    }

    public void drawOrder(int ox, int oy){
        for (int i = order.length-1; i >= 0; i--) {
            if (order[i] != null) {
                order[i].drawTextBubble(canvas, this.getX() * this.drawScale.x,
                        (this.getY()) * drawScale.y, ox + (45 * i), oy);
            }
        }
    }

    public void drawCooking(int ox, int oy){
        if (cooking_method == 0) {
            canvas.draw(wok, Color.WHITE, ox, oy,
                    this.getX() * this.getDrawScale().x,
                    this.getY() * this.getDrawScale().y,
                    0.0f, 0.7f, 0.7f);
        } else if (cooking_method == 1) {
            canvas.draw(pot, Color.WHITE, ox-30, oy,
                    this.getX() * this.getDrawScale().x,
                    this.getY() * this.getDrawScale().y, 0.0f, 0.7f, 0.7f);
        } else {
            canvas.draw(cutting_board, Color.WHITE, ox-30, oy,
                    this.getX() * this.getDrawScale().x,
                    this.getY() * this.getDrawScale().y, 0.0f, 0.7f, 0.7f);
        }
    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
    }
}
