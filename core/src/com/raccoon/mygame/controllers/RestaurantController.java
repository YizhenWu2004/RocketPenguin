package com.raccoon.mygame.controllers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.*;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.Font;

import java.lang.reflect.GenericArrayType;
import java.util.HashMap;

public class RestaurantController extends WorldController implements ContactListener {
    private World world;
    private GameCanvas canvas;
    private Texture background;
    private Array<Customer> customers;
    private Array<Customer> customersToAdd;
    private Array<NormalObstacle> obstacles;
    private Player player;
    private InputController input;
    private boolean active;

    private VentObstacle vent1;
    private boolean ventCollision;
    Array<TableObstacle> tables;
    CollisionController collision;
    private Array<Object> drawableObjects = new Array<>();

    private int globalIndex = 0;
    private int tick;
    private Vector2 localStartingPos;
    private boolean paused = false;
    Array<CookingStationObject> stations;
    private Worldtimer t;
    public int score;
    BitmapFont f = new BitmapFont();
    GlyphLayout layout = new GlyphLayout(f, "");
    public int happy;
    public int neutral;
    public int angry;
    public int total;

    public int current = -3;

    NormalObstacle trash;


    //Default filmstrips.
    //We need these because the constructors of these objects require a texture (Filmstrip)
    private FilmStrip playerIdle;
    //Do not worry about it, it can be literally any filmstrip.
    //It will get updated immediately by the animation controller.
    private FilmStrip goatIdle;

    //The animation controller in question.
    private AnimationController animator;
    private SoundController sounds;

    private float respawnTimer;

    public boolean ventOutFlag;
    private float ventOutTimer;
    Texture light = new Texture("light.png");

    private Texture singleInv = new Texture("trashInv.png");
    private Texture table = new Texture("720/table.png");
    private Texture wallbump =  new Texture("720/wallbump.png");
    private Texture invisible = new Texture("invisible" + ".png");
    private Texture rockoidle = new Texture("720/rockoidle.png");
    private Texture wallrestaurant =  new Texture("720/wallrestaurant.png");
    private Texture new_pot_station = new Texture("new_pot_station.png");
    private Texture new_wok_station = new Texture("new_wok_station.png");
    private Texture temp_cutting_station =  new Texture("temp_cutting_station.png");
    private Texture new_counter = new Texture("new_counter.png");
    private Texture goat = new Texture("720/goat.png");
    private Texture BaseTimer = new Texture("720/BaseTimer.png");
    private Texture smallwall = new Texture("720/smallwall.png");
    private Texture notAsTallWall = new Texture("720/notAsTallWall.png");
    private Texture window = new Texture("720/window.png");
    private Texture plant = new Texture("720/plant.png");
    private Texture sidelamp = new Texture("720/sidelamp.png");
    private Texture decorativeshelf = new Texture("720/decorativeshelf.png");
    private Texture trashcan = new Texture("720/trashcan.png");
    private Texture vent = new Texture("720/vent.png");
    private HashMap<String, Texture> decorationTextures = new HashMap();


    private HashMap<String, Texture> obstacleTextures = new HashMap<>();

    private void addTable(float x, float y, boolean flip) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 2.5f, (flip ? -1 : 1), 1, -0f, 0f,
                table, world, canvas);
        obstacles.add(t);
        drawableObjects.add(t);
        tables.add(t);
    }

    private void addWallBump(float x, float y) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 5f, 1f, 1f, 0f, 0f,
               wallbump, world, canvas);
        obstacles.add(t);
        drawableObjects.add(t);
    }
    //obstacles are real, their collisions affect things
    private void addNormalObstacle(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
                decorationTextures.get(texturename), world, canvas);

        obstacles.add(t);
        drawableObjects.add(t);
    }
    private void addNormalObstacle(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset, boolean drawPriority) {
        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
                decorationTextures.get(texturename), world, canvas, drawPriority);

        obstacles.add(t);
        drawableObjects.add(t);
    }

    public void setPaused(boolean b){
        paused = b;
    }
    //decorations are sensors, no collision will be detected
    private void addDecoration(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset, boolean drawPriority) {
        NormalObstacle t = new NormalDecoration(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
                decorationTextures.get(texturename), world, canvas, drawPriority);

        obstacles.add(t);
        drawableObjects.add(t);
    }
//    private void addDecoration(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
//        NormalObstacle t = new NormalDecoration(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
//                new Texture("720/" + texturename + ".png"), world, canvas);
//
//        obstacles.add(t);
//        drawableObjects.add(t);
//    }

    private void addTrashcan(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset, boolean drawPriority, boolean isForDishes) {
        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
                decorationTextures.get(texturename), world, canvas, drawPriority);

        obstacles.add(t);
        drawableObjects.add(t);
        t.setTrashcan(true);
        trash = t;

    }

    private void addInvisibleWall(float x, float y, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
               invisible, world, canvas);

        obstacles.add(t);
    }

    public World getWorld() { return world; }

    public RestaurantController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv, Worldtimer sharedtimer) {

        world = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.background = texture;

        decorationTextures.put("smallwall", smallwall);
        decorationTextures.put("notAsTallWall", notAsTallWall);
        decorationTextures.put("window", window);
        decorationTextures.put("plant", plant);
        decorationTextures.put("sidelamp", sidelamp);
        decorationTextures.put("decorativeshelf", decorativeshelf);
        decorationTextures.put("trashcan", trashcan);

        //Setting the default filmstrip for the player
        playerIdle = new FilmStrip(rockoidle, 1, 1, 1);
        player = new Player(3f, 6.5f, 2, 0.7f, playerIdle, sharedInv, canvas, world);

        drawableObjects.add(player);

        obstacles = new Array();

        //this seems pointless now im not sure
        NormalObstacle normalOb1 = (new NormalObstacle(16f, 17f, 32f, 2.5f, 1f, 1f, 0f, 320f,
                wallrestaurant, world, canvas));
        obstacles.add(normalOb1);
        drawableObjects.add(normalOb1);

        stations = new Array<>();

        CookingStationObject temp = new CookingStationObject(27f, 15f, 6f, 3f, 1, 1, 0f, 0f,
                new_pot_station, world, canvas, 1,1);
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);
        temp.drawoy=-30;

        temp = new CookingStationObject(30.97f, 10f, 2f, 4f, 1.2f, 1.2f, 0f, 0f,
                new_wok_station, world, canvas, 2,0);
        temp.drawoy = 0;
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);

        temp = new CookingStationObject(24.5f, 2f, 2.2f, 4f, 1.2f, 1f, 0f, 0f,
               temp_cutting_station, world, canvas, 2,2);
        temp.drawoy = -10;
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);


        NormalObstacle obs = new NormalObstacle(30.97f, 14.5f, 2,5, 1.2f,0.9f, 0,0,
                new_counter, world, canvas, false);
        obstacles.add(obs);
        drawableObjects.add(obs);

        t = sharedtimer;
        score = 0;

        tables = new Array();
        addTable(17.5f, 12f, false);
        addTable(17.5f, 4f, true);
        addTable(10.25f, 12f, true);
        addTable(10.25f, 4f, false);
        //addTable(3f, 12f, false);
        addTable(3f, 4f, true);


        //System.out.println(tables.size);
        addWallBump(6f, 16.5f);
        addWallBump(14.5f, 16.5f);
        addWallBump(23f, 16.5f);

        addNormalObstacle(23-0.4f, 16.0f, "smallwall", 1,11.5f,1,1,0,0);
        addNormalObstacle(23-0.4f, -1, "notAsTallWall", 1,14.5f,1,1,0,-165);
//        addNormalObstacle(23,12.0f,"kitchendoor", 1,2,1,1,-30,70, true);

        addNormalObstacle(10.3f,16.4f, "window",1,1,1,1,0,-50);
        addNormalObstacle(2.2f,16.4f, "window",1,1,1,1,0,-50);
        addNormalObstacle(19,16.4f, "window",1,1,1,1,0,-50);

        addNormalObstacle(1f, 15, "plant",0.5f,0.5f,1,1,0,-35);
        addNormalObstacle(8.3f, 15, "plant",0.5f,0.5f,1,1,0,-35);
        addNormalObstacle(12.1f, 15, "plant",0.5f,0.5f,1,1,0,-35);
        addNormalObstacle(2f, 0, "plant",0.5f,0.5f,1,1,0,-35);
        addNormalObstacle(7f, 0, "plant",0.5f,0.5f,1,1,0,-35);

        addDecoration(22, 6, "sidelamp",0.1f,0.1f, 1,1,4,0,true);
        addDecoration(22, 2, "sidelamp",0.1f,0.1f, 1,1,4,0,true);

        addNormalObstacle(22-0.3f,13,"decorativeshelf", 1,4, 1,1, 0,-20);

        addTrashcan(31, 5, "trashcan", 1, 0.5f,1,1,0,-30f, false, false);

        addInvisibleWall(0,-1,80,1,1,1,0,0);
        addInvisibleWall(-1,0,1,40,1,1,0,0);
        addInvisibleWall(33,0,1,40,1,1,0,0);

        customers = new Array();

        //Setting the default filmstrip for the customer.
        //I think I just use the same default for all customers.
        //Again it's fine because they ger set immediately in the animation controller
        //We just wrote shitty code that requires textures off-rip.
        goatIdle = new FilmStrip(goat, 1,4,4);

        customersToAdd = new Array<>();
        Array<String> customer1Order = new Array<String>();
        customer1Order.add("0");
        customer1Order.add("red");
        Customer customer1 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, 1, 178,customer1Order);
        Customer customer2 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, 1, 175,customer1Order);
        Customer customer3 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, 1, 173,customer1Order);
        customersToAdd.add(customer1);
        customersToAdd.add(customer2);
        customersToAdd.add(customer3);

        for(Customer c : customersToAdd){
            c.initializeAIController(tables);
        }

        //todo CUSTOMER
//        Customer customer1 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, tables, 1);
      
//        customers.add(customer1);
//        drawableObjects.add(customer1);

        Filter f = new Filter();
        f.categoryBits = 0x0002;
        f.maskBits = 0x0001;
        for (Customer c : customers) {
            c.setFilterData(f);
        }
        if (!player.isIgnoreInput()) {
            this.input = input;
        }
        vent1 = new VentObstacle(30.5f,1f, 1.5f,1.5f, 1, 1, 27, 27f, new FilmStrip(vent,1,1,1) ,world, canvas);
        localStartingPos = new Vector2(vent1.getX()-2.3f, vent1.getY());
        drawableObjects.add(vent1);


        collision = new CollisionController(canvas.getWidth(), canvas.getHeight());
        active = true;
        tick = 0;
        world.setContactListener(this);

        animator = new AnimationController(input);

        //dont touch this its my debug dummy obstacle

//        NormalObstacle ob = (new NormalObstacle(15f, 11f, 5f, 1.5f, 0.1f, 0.1f, 0f, 0f,
//                new Texture("pot.png"), world, canvas));
//        obstacles.add(ob);
//        drawableObjects.add(ob);


        sounds = new SoundController();
        happy=0;
        neutral=0;
        angry=0;
        total=9; //hardcoded for now
        //score = 78;

        respawnTimer = -1;
    }

    public void setActive(boolean b) {
        active = b;
    }

    @Override
    public void dispose() {
        world.dispose();
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    @Override
    public void render(float delta) {

    }
    public void update() {
        tick += 1;

        //We will use this deltatime to update animation frames
        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta);
        player.current = this.current;

        //todo CUSTOMER
//        if ((t.getTime() == 178 || t.getTime() == 176 ||t.getTime() == 130|| t.getTime() == 128 || t.getTime() ==126 || t.getTime() ==80 || t.getTime() == 78 || t.getTime() == 76) && !t.action_round && paused == false){
//            Customer customer1 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, tables, 2);
//
//            customers.add(customer1);
//            sounds.doorPlay();
//            drawableObjects.add(customer1);
//            t.action_round=true;

//        }else if (t.getTime() == 120&& !t.action_round){
//            Customer customer2 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, tables, 3);
//            customers.add(customer2);
//            drawableObjects.add(customer2);
//            t.action_round=true;
//        }else if (t.getTime() == 90&& !t.action_round){
//            Customer customer3 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, tables, 4);
//            customers.add(customer3);
//            drawableObjects.add(customer3);
//            t.action_round=true;
//        }else if (t.getTime() == 60&& !t.action_round){
//            Customer customer4 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, tables, 4);
//            customers.add(customer4);
//            drawableObjects.add(customer4);
//            t.action_round=true;

//    }
        for(Customer c: customersToAdd){
//            System.out.println(c.showUpTime);
            if(c.showUpTime >= t.getTime() && !t.action_round && paused == false){
//                System.out.println("Customer incoming");
                customers.add(c);
                sounds.doorPlay();
                drawableObjects.add(c);
                t.action_round=true;
                customersToAdd.removeValue(c,true);
            }
        }

        if (active && !respawning()) {
            if(!ventingOut()&& !player.playerIsVenting && !player.playerIsCooking){
                float x = 7f * input.getXMovement();
                float y = 7f * input.getYMovement();
                player.setLinearVelocity(new Vector2(x, y));
                player.setSpace(input.getSpace());
                player.setInteraction(input.getInteraction());
                player.getInventory().setSelected((int) input.getScroll());
                collision.processCustomers(player, customers);
            }
            //Here we handle the vent animation
            //I do this here because it only makes sense to update the vent animation if
            //the current scene is active.
            //to be fair all animations should probably be handled here but it matters more for the vent.
            animator.handleAnimation(vent1, player, delta, ventingOut());

            boolean trashActive = false;
            for(NormalObstacle obstacle : obstacles){
                boolean colliding = collision.handleCollision(player, obstacle);
                if(colliding && obstacle.getTrashcan()){
                    trashActive = true;
                    if(input.getSpace()){
                        player.dishInventory.clear(0);
                        player.dishInventory.clear(1);
                    }
                    if(input.getUp()){
                        player.removeItem();
                    }
                }
            }
            if(trashActive){
                trash.interactingTrash = true;
            }
            else{
                trash.interactingTrash = false;
            }
        }
        for (Customer c : customers) {
            if (c.justSatisfied){
                if (c.time() > 0){
                    score += c.servedDish.getScore() * c.pat.multiplier();
                    if (c.pat.multiplier() == 1){
                        happy += 1;
                    } else if (c.pat.multiplier() == 0.7){
                        neutral += 1;
                    } else {
                        angry += 1;
                    }
                }
                c.justSatisfied = false;
            }
            if (c.isActive()) {
                c.move();
            }
            if(c.time() <= 0){
                c.timeOut();
                c.satisfied = Customer.SATISFIED.NO;
            }
        }
        for (CookingStationObject c : stations){
            if (c.state == 0){
                if(c.pot.size != 0 && player.space && c.interacting){
                    c.state = 1;
                    int time;
                    if(c.station_type == 0){
                        sounds.cookplay();
                        time = 15;
                    } else if(c.station_type == 1){
                        sounds.cookplay();
                        time =30;
                    }else {
                        sounds.chopPlay();
                        time = 1;
                    }
                    c.timer = new Worldtimer(time, canvas, BaseTimer);
                    c.timer.create();
                    c.setMaxTime();
                } else if ((c.pot.size != 0 && !c.interacting) || input.getDown()){
                    Ingredient[] temp = c.pot.drop();
                    for(Ingredient i : temp){
                        if(i != null) {
                            player.inventory.add(i);
                        }
                    }
                    c.pot.clearAll();
                } else if (input.getUp() && c.pot.size < 3 && player.inventory.isCurrFilled() && c.interacting){
                    c.pot.add(player.inventory.getSelectedItem());
                    player.inventory.drop();
                }
            }else if (c.state == 1){
                //System.out.println(c.timer.getTime());
                if (c.timer.getTime() <= 0){
                    sounds.bellPlay();
                    c.state = 2;
                }
            } else if (c.state == 2){
                if(c.interacting && player.space){
                    if (!player.dishInventory.leftFilled() || !player.dishInventory.rightFilled()) {
                        player.dishInventory.fill(c.getCookedDish());
                        c.state = 0;
                        c.timer = null;
                    }
                }
            }
//            if(c.timer!=null) {
//                if (Math.abs(c.timer.getTime() - c.getMaxTime()) < 1) {
//                    player.playerIsCooking = true;
//
//                }
//                else {
//                    player.playerIsCooking = false;
//                }
//            }
        }
        boolean playCooking = false;
        for (CookingStationObject c : stations){
            if(c.timer!=null) {
                if (Math.abs(c.timer.getTime() - c.getMaxTime()) < 1) {
                    playCooking = true;
                }
            }
        }
        player.playerIsCooking = playCooking;
        if(vent1.ventTimer != null) {
            //System.out.println(vent1.ventTimer.getTime());
            if (vent1.ventTimer.getTime() <= 0) {
                setVentCollision(true);
                vent1.ventTimer = null;
                player.playerIsVenting = false;
            }
        }

        //process the rest of the animations
        animator.handleAnimation(player, tick, respawning());
        animator.processCustomers(customers, tick);
        animator.processCookingStations(stations, tick);

        if(player.justDied == true){
            respawnTimer = 1.6666f;
            player.justDied = false;
        }
        player.respawning = respawning();

        respawnTimer = Math.max(respawnTimer-delta,0);

        if(ventOutFlag == true){
            ventOutTimer = 1.1666f;
            ventOutFlag = false;
        }

        ventOutTimer = Math.max(ventOutTimer-delta,0);

        world.step(1 / 60f, 6, 2);
    }

    private float getYPosOfAnyObject(Object obj){
        if(obj instanceof VentObstacle)
            return ((VentObstacle) obj).getPosition().y;
        if(obj instanceof TableObstacle)
            return ((TableObstacle) obj).getPosition().y;
        if(obj instanceof Player)
            return ((Player) obj).getPosition().y;
        if(obj instanceof NormalObstacle)
            return ((NormalObstacle) obj).getPosition().y;
        if(obj instanceof Customer)
            return (((Customer) obj).getPosition().y);

        //shouldn't get here.
        return 0f;
    }

    private void drawAnyType(Object obj){
        if(obj instanceof VentObstacle)
            ((VentObstacle) obj).draw();
        if(obj instanceof TableObstacle)
            ((TableObstacle) obj).draw();
        if(obj instanceof Player)
            ((Player) obj).draw(1, 1);
        if(obj instanceof NormalObstacle)
            ((NormalObstacle) obj).draw();
        if(obj instanceof Customer)
            ((Customer) obj).draw(1, 1);
    }

    private boolean getDrawPriorityOfAnyType(Object obj){
        if(obj instanceof NormalObstacle)
            return ((NormalObstacle) obj).getDrawPriority();
        return false;
    }

    public void draw() {
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);

//        canvas.draw(light,Color.WHITE, 0, 0,
//                0, 5f*40, 0.0f, 1f, 1f);

        //bubble sort for drawing
        boolean swapped;
        for (int i = 0; i < drawableObjects.size-1; i++) {
            swapped = false;
            for(int j = 0; j < drawableObjects.size-1-i; j++){
                float currentY = getYPosOfAnyObject(drawableObjects.get(j));
                if(getDrawPriorityOfAnyType(drawableObjects.get(j)))
                    currentY = -1;
                float nextY = getYPosOfAnyObject(drawableObjects.get(j+1));
                if(getDrawPriorityOfAnyType(drawableObjects.get(j+1)))
                    nextY = -1;
                if(currentY < nextY){
                    Object tempNext = drawableObjects.get(j+1);
                    drawableObjects.set(j+1, drawableObjects.get(j));
                    drawableObjects.set(j, tempNext);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }

        //System.out.println("DRAWING");
        for(Object obj : drawableObjects){

            drawAnyType(obj);
        }
        for(CookingStationObject o : stations){
            o.drawInventory();
        }
        canvas.drawText("Score:", f,20, 600,2,2, layout);
        canvas.drawText(Integer.toString(score), f, 130, 600, 2, 2,layout);
        canvas.draw(light,Color.WHITE, 0, 0,
                0, 5f*40, 0.0f, 1f, 1f);

        if(trash.interactingTrash){
            float midpoint = (float)canvas.getWidth()/2- (float)singleInv.getWidth()/2;
            canvas.draw(singleInv, Color.WHITE, 10, 10,
                    midpoint, 90, 0.0f, 1, 1);
        }
    }

    public void debug() {
        player.drawDebug(canvas);
        for (Customer c : customers) {
            if (c.isActive()) {
                c.debug(canvas);
            }
        }
        vent1.drawDebug(canvas);
        for (NormalObstacle o : obstacles) {
            o.debug(canvas);
        }
    }

    @Override
    public void beginContact(Contact contact) {


        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof VentObstacle) || (body2.getUserData() instanceof Player && body1.getUserData() instanceof VentObstacle)) {
            //System.out.println("colliding with vent");
            //execute
            startVentTimer(vent1, player);
            sounds.ventPlay();
//            setVentCollision(true);
        }

        if (body1.getUserData() instanceof Player && body2.getUserData() instanceof CookingStationObject) {
            CookingStationObject obj = (CookingStationObject) body2.getUserData();
            Player player = (Player) body1.getUserData();
            obj.interacting = true;
            obj.interacting_with = obj.id;
            player.setPotCookingIn(obj.getStationType());
        } else if (body1.getUserData() instanceof CookingStationObject && body2.getUserData() instanceof Player) {
            CookingStationObject obj = (CookingStationObject) body1.getUserData();
            Player player = (Player) body2.getUserData();
            obj.interacting = true;
            obj.interacting_with = obj.id;
            player.setPotCookingIn(obj.getStationType());
        }



        //System.out.println("contact");
//
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        short categoryA = fixtureA.getFilterData().categoryBits;
//        short categoryB = fixtureB.getFilterData().categoryBits;
//
//        // Check if both fixtures belong to BoxObstacle objects
//        if ((categoryA & 0x0002) != 0 && (categoryB & 0x0002) != 0) {
//            // Ignore collision between them
//            contact.setEnabled(false);
//            return;
//        }

//        if ((body1.getUserData() instanceof Customer && body2.getUserData() instanceof TableObstacle)) {
//            //System.out.println("here");
//            Customer c = (Customer) body1.getUserData();
//            TableObstacle t = (TableObstacle) body2.getUserData();
//            if (c.collided >= 1) {
//                c.collided = 2;
//            } else if (c.t.equals(t)) {
//                c.collided = 1;
//            }
//        }
//        if ((body1.getUserData() instanceof TableObstacle && body2.getUserData() instanceof Customer)) {
//            Customer c = (Customer) body2.getUserData();
//            TableObstacle t = (TableObstacle) body1.getUserData();
//            if (c.collided >= 1) {
//                c.collided = 2;
//            } else if (c.t.equals(t)){
//
//                c.collided= 1;
//            }
//        }


    }

    @Override
    public void endContact(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if (body1.getUserData() instanceof Player && body2.getUserData() instanceof CookingStationObject){
            CookingStationObject obj = (CookingStationObject) body2.getUserData();
            obj.interacting = false;
        } else if (body1.getUserData() instanceof CookingStationObject && body2.getUserData() instanceof Player){
            CookingStationObject obj = (CookingStationObject) body1.getUserData();
            obj.interacting = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int getIndex() {
        if (this.active)
            return this.globalIndex;
        return 0;
    }

    public void setIndex(int index) {
        this.globalIndex = index;
    }

    public boolean getActive() {
        return this.active;
    }

    public boolean getVentCollision() {
        return this.ventCollision;
    }

    public void setVentCollision(boolean isColliding) {
        this.ventCollision = isColliding;
    }

    /**
     * Called whenever this level just got switched to.
     *
     * */
    public void onSet(){
        player.setPosition(localStartingPos);
        player.direction = 1;
    }

    public void startVentTimer(VentObstacle o, Player p){
        p.playerIsVenting = true;
        o.ventTimer = new Worldtimer((int) o.maxTime, canvas, BaseTimer);
        o.ventTimer.create();
    }

    public void pauseTimer(){
        for(Customer c: customers){
            //idk why I had to do this
            if(c.pat != null)
                c.pat.pauseTimer();
        }
        for(CookingStationObject c : stations){
            if(c.timer!=null) {
                c.timer.pauseTimer();
            }
        }
    }

    public void startTimer(){
        for(Customer c: customers){
            if(!(c.pat == null)){
                c.pat.resumeTimer();
            }
        }
        for(CookingStationObject c : stations){
            if(c.timer!=null){
                c.timer.resumeTimer();
            }
        }
    }

    public void uponPlayerDeathReset(){
        player.setPosition(7.5f, 8.5f);
    }

    public void setPlayerJustDied(boolean v){
        player.justDied = v;
    }

    public boolean getPlayerJustDied(){
        return player.justDied;
    }

    public boolean respawning(){
        return respawnTimer > 0;
    }

    public boolean ventingOut(){
        return ventOutTimer > 0;
    }
}
