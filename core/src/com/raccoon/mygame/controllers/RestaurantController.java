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
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.*;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.Font;

import java.lang.reflect.GenericArrayType;
import java.util.Arrays;
import java.util.HashMap;

public class RestaurantController extends WorldController implements ContactListener {
    private World world;
    private boolean potplay;
    private boolean panplay;
    private GameCanvas canvas;
    private Texture background;
    private Timer.Task timerTask;
    private Array<Customer> customers;
    private Array<Customer> customersToAdd;
    private Array<NormalObstacle> obstacles;
    private Player player;
    private InputController input;
    private boolean active;
    private boolean isTutorial;

    private VentObstacle vent1;
    private boolean ventCollision;
    Array<TableObstacle> tables;
    CollisionController collision;
    private Array<Object> drawableObjects = new Array<>();

    private int globalIndex = 0;

    public boolean timerStarted = false;
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
    private FilmStrip vent;
    private int a = 0;

    //The animation controller in question.
    private AnimationController animator;
    private SoundController sounds;
    private boolean canplay = true;

    private float respawnTimer;

    public boolean ventOutFlag;
    private float ventOutTimer;
    Texture light;

    private Texture singleInv;
    private Texture table;
    private Texture wallbump;
    private Texture invisible;
    private Texture rockoidle;
    private Texture wallrestaurant;
    private Texture new_pot_station;
    private Texture new_wok_station;
    private Texture temp_cutting_station;
    private Texture new_counter;
    private Texture goat;
    private Texture BaseTimer;
    private Texture smallwall;
    private Texture notAsTallWall;
    private Texture window;
    private Texture plant;
    private Texture sidelamp;
    private Texture decorativeshelf;
    private Texture trashcan;
    private Texture zero;
    private Texture one;
    private Texture two;
    private Texture three;
    private Texture star0;
    private Texture star1;
    private Texture star2;
    private Texture star3;
    private Texture star4;
    private Texture star5;
    private Texture star6;
    private Texture star7;
    private Texture star8;
    private Texture star9;
    private Texture score0;
    private Texture score1;
    private Texture score2;
    private Texture score3;
    private Texture score4;
    private Texture score5;
    private Texture score6;
    private Texture score7;
    private Texture score8;
    private Texture score9;

    private HashMap<String, Texture> decorationTextures = new HashMap();


    private HashMap<String, Texture> obstacleTextures = new HashMap<>();

    public int[] star_req;

    private AssetDirectory directory;

    boolean isEndless;

    float infTimer = 0;

    public int unsatisfiedCustomers = 0;

    public int totalCustomer;

    public int customerServed = 0;


    private void createTextures(AssetDirectory directory) {
        light = directory.getEntry("light", Texture.class);
        singleInv = directory.getEntry("trashInv", Texture.class);
        table = directory.getEntry("table", Texture.class);
        wallbump =  directory.getEntry("wallbump", Texture.class);
        invisible = directory.getEntry("invisible", Texture.class);
        rockoidle = directory.getEntry("rockoidle", Texture.class);
        wallrestaurant =  directory.getEntry("wallrestaurant", Texture.class);
        new_pot_station = directory.getEntry("new_pot_station", Texture.class);
        new_wok_station = directory.getEntry("new_wok_station", Texture.class);
        temp_cutting_station =  directory.getEntry("cutting_station", Texture.class);
        new_counter = directory.getEntry("new_counter", Texture.class);
        goat = directory.getEntry("goat", Texture.class);
        BaseTimer = directory.getEntry("basetimer", Texture.class);
        smallwall = directory.getEntry("smallwall", Texture.class);
        notAsTallWall = directory.getEntry("notastallwall", Texture.class);
        window = directory.getEntry("window", Texture.class);
        plant = directory.getEntry("plant", Texture.class);
        sidelamp = directory.getEntry("sidelamp", Texture.class);
        decorativeshelf = directory.getEntry("decorativeshelf", Texture.class);
        trashcan = directory.getEntry("trashcan", Texture.class);
        vent = directory.getEntry("vent.strip", FilmStrip.class);
        zero = directory.getEntry("s_zero", Texture.class);
        one = directory.getEntry("s_one", Texture.class);
        two = directory.getEntry("s_two", Texture.class);
        three = directory.getEntry("s_three", Texture.class);
        star0 = directory.getEntry("s_star0", Texture.class);
        star1 = directory.getEntry("s_star1", Texture.class);
        star2 = directory.getEntry("s_star2", Texture.class);
        star3 = directory.getEntry("s_star3", Texture.class);
        star4 = directory.getEntry("s_star4", Texture.class);
        star5 = directory.getEntry("s_star5", Texture.class);
        star6 = directory.getEntry("s_star6", Texture.class);
        star7 = directory.getEntry("s_star7", Texture.class);
        star8 = directory.getEntry("s_star8", Texture.class);
        star9 = directory.getEntry("s_star9", Texture.class);
        score0 = directory.getEntry("s_0", Texture.class);
        score1 = directory.getEntry("s_1", Texture.class);
        score2 = directory.getEntry("s_2", Texture.class);
        score3 = directory.getEntry("s_3", Texture.class);
        score4 = directory.getEntry("s_4", Texture.class);
        score5 = directory.getEntry("s_5", Texture.class);
        score6 = directory.getEntry("s_6", Texture.class);
        score7 = directory.getEntry("s_7", Texture.class);
        score8 = directory.getEntry("s_8", Texture.class);
        score9 = directory.getEntry("s_9", Texture.class);

        playerIdle = directory.getEntry("rockoidle.strip", FilmStrip.class);
        goatIdle = directory.getEntry("goatwalk.strip", FilmStrip.class);
        this.directory = directory;
    }

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

    public RestaurantController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv, Worldtimer sharedtimer,int[] star_req, SoundController s, AssetDirectory directory, boolean isEndless,boolean isTutorial) {
        this.isEndless = isEndless;
        this.isTutorial = isTutorial;

        createTextures(directory);
        sounds = s;
        collision = new CollisionController(canvas.getWidth(), canvas.getHeight(), sounds);

        panplay = false;
        potplay = false;
        world = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.background = texture;
        this.star_req = star_req;
        decorationTextures.put("smallwall", smallwall);
        decorationTextures.put("notAsTallWall", notAsTallWall);
        decorationTextures.put("window", window);
        decorationTextures.put("plant", plant);
        decorationTextures.put("sidelamp", sidelamp);
        decorationTextures.put("decorativeshelf", decorativeshelf);
        decorationTextures.put("trashcan", trashcan);

        //Setting the default filmstrip for the player
        player = new Player(25f, 7.5f, 2, 0.7f, playerIdle, sharedInv, canvas, world, sounds, directory);

        drawableObjects.add(player);

        obstacles = new Array();

        //this seems pointless now im not sure
        NormalObstacle normalOb1 = (new NormalObstacle(16f, 17f, 32f, 2.5f, 1f, 1f, 0f, 320f,
                wallrestaurant, world, canvas));
        obstacles.add(normalOb1);
        drawableObjects.add(normalOb1);

        stations = new Array<>();

        CookingStationObject temp = new CookingStationObject(27f, 15f, 6f, 3f, 1, 1, 0f, 0f,
                new_pot_station, world, canvas, 1,1, sounds, directory);
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);
        temp.drawoy=-30;

        temp = new CookingStationObject(30.97f, 10f, 2f, 4f, 1.2f, 1.2f, 0f, 0f,
                new_wok_station, world, canvas, 2,0, sounds, directory);
        temp.drawoy = 0;
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);

        temp = new CookingStationObject(24.5f, 2f, 2.2f, 4f, 1.2f, 1f, 0f, 0f,
               temp_cutting_station, world, canvas, 2,2, sounds, directory);
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
        addTable(17f, 12f, false);
        addTable(18f, 4f, true);
        addTable(10f, 12f, true);
        addTable(11f, 4f, false);
        //addTable(3f, 12f, false);
        addTable(4f, 4f, true);


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
        //goatIdle = new FilmStrip(goat, 1,4,4);

        customersToAdd = new Array<>();

        Filter f = new Filter();
        f.categoryBits = 0x0002;
        f.maskBits = 0x0001;
        for (Customer c : customers) {
            c.setFilterData(f);
        }
        if (!player.isIgnoreInput()) {
            this.input = input;
        }
        vent1 = new VentObstacle(30.5f,1f, 1.5f,1.5f, 1, 1, 27, 27f, vent ,world, canvas);
        localStartingPos = new Vector2(vent1.getX()-2.3f, vent1.getY());
        drawableObjects.add(vent1);
        active = true;
        tick = 0;
        world.setContactListener(this);

        animator = new AnimationController(input, directory);

        //dont touch this its my debug dummy obstacle

//        NormalObstacle ob = (new NormalObstacle(15f, 11f, 5f, 1.5f, 0.1f, 0.1f, 0f, 0f,
//                new Texture("pot.png"), world, canvas));
//        obstacles.add(ob);
//        drawableObjects.add(ob);


        sounds =s;
        happy=0;
        neutral=0;
        angry=0;
        total=9; //hardcoded for now
        //score = 78;

        respawnTimer = -1;
        s.isStore = false;
    }

    public void setCustomers(Array<Array<String>> customerData){
        if(!isEndless){
            for(Array<String> arr : customerData){
                int time = Integer.parseInt(arr.get(0));
                Array<String> copied = new Array<String>();
                for(int i = 1; i<arr.size; i++){
                    copied.add(arr.get(i));
                }

                Customer customer1 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, 1, time,copied, directory);
                customersToAdd.add(customer1);
            }
        }
        else{
//            System.out.println("ENDLESS");
            Array<Array<String>> customerOrders = new Array<>();

            customerOrders.add(new Array<>(new String[]{"2", "2", "greenpepper"}));
            customerOrders.add(new Array<>(new String[]{"12", "0", "green", "green"}));
            customerOrders.add(new Array<>(new String[]{"14", "2", "green"}));
            customerOrders.add(new Array<>(new String[]{"60", "1", "green"}));
            customerOrders.add(new Array<>(new String[]{"62", "0", "green"}));
            customerOrders.add(new Array<>(new String[]{"64", "2", "yellow", "yellow"}));
            customerOrders.add(new Array<>(new String[]{"120", "0", "corn", "banana", "lemon"}));
            customerOrders.add(new Array<>(new String[]{"132", "2", "orange"}));
            customerOrders.add(new Array<>(new String[]{"134", "1", "yellow", "yellow"}));
            customerOrders.add(new Array<>(new String[]{"180", "1", "greenpepper", "greenpepper"}));
            customerOrders.add(new Array<>(new String[]{"182", "0", "greenpepper", "redpepper"}));
            customerOrders.add(new Array<>(new String[]{"184", "2", "apple", "greenpepper"}));
            customerOrders.add(new Array<>(new String[]{"240", "2", "persimmon", "orange", "carrot"}));
            customerOrders.add(new Array<>(new String[]{"252", "1", "greenpepper"}));
            customerOrders.add(new Array<>(new String[]{"254", "2", "orange"}));
            customerOrders.add(new Array<>(new String[]{"300", "0", "green", "yellow", "orange"}));
            customerOrders.add(new Array<>(new String[]{"302", "2", "green", "yellow", "orange"}));
            customerOrders.add(new Array<>(new String[]{"304", "0", "green", "yellow", "orange"}));
            customerOrders.add(new Array<>(new String[]{"360", "2", "tomato", "redpepper", "apple"}));
            customerOrders.add(new Array<>(new String[]{"372", "2", "red"}));
            customerOrders.add(new Array<>(new String[]{"374", "2", "red"}));
            customerOrders.add(new Array<>(new String[]{"420", "1", "red"}));
            customerOrders.add(new Array<>(new String[]{"422", "1", "red"}));
            customerOrders.add(new Array<>(new String[]{"424", "1", "red"}));
            customerOrders.add(new Array<>(new String[]{"440", "1", "red", "yellow", "orange"}));
            customerOrders.add(new Array<>(new String[]{"444", "1", "red", "yellow", "orange"}));
            customerOrders.add(new Array<>(new String[]{"448", "1", "red", "yellow", "orange"}));

            for(Array<String> arr : customerOrders){
                //System.out.println(arr);
                int time = Integer.parseInt(arr.get(0));
                arr.removeIndex(0);
                Customer customer1 = new Customer(0f, 7.5f, 1f, 0.7f, goatIdle, world, canvas, 1, time,arr,directory);
                customersToAdd.add(customer1);
            }
        }

        totalCustomer = customersToAdd.size;

        for(Customer c : customersToAdd){
            c.initializeAIController(tables);
        }
//        customersToAdd = inputCustomers;
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

        infTimer+= 0.0166666;

//        System.out.println("customersToAdd"+customersToAdd);
//        System.out.println("customers"+customers);
//        System.out.println("allCustomersLeave"+allCustomersLeave());
        //We will use this deltatime to update animation frames
        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta);
        player.current = this.current;

        if(isEndless){
            //System.out.println(infTimer);
            //System.out.println(customersToAdd.size);
            for(Customer c: customersToAdd){
//            System.out.println(c.showUpTime);
                if(c.showUpTime <= infTimer && paused == false){
                    //System.out.println("c.showUpTime" + c.showUpTime + "infTimer" + infTimer);
                    customers.add(c);
                    sounds.doorPlay();
                    drawableObjects.add(c);
                    t.action_round=true;
                    customersToAdd.removeValue(c,true);
                }
            }
        }
        else{
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
        }



        if (active && !respawning()) {
            player.venting_out = ventingOut();
            if(!ventingOut()&& !player.playerIsVenting && !player.playerIsCooking){
                float x = 7f * input.getXMovement();
                float y = 7f * input.getYMovement();
                player.setLinearVelocity(new Vector2(x, y));
                player.setSpace(input.getSpace());
                player.setInteraction(input.getInteraction());
                if(input.getOneThroughFivePressed()){
                    player.getInventory().setIndex(input.getNumIndex());
                }
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
                    obstacle.setSX(1.1f);
                    obstacle.setSY(1.1f);
                    if(input.getSpace()){
                        player.dishInventory.clear(0);
                        player.dishInventory.clear(1);
                    }
                    if(input.getDown()){
                        if(player.inventory.isCurrFilled()) {
                            sounds.trashPlay();
                        }
                        player.removeItem();
                    }
                } else if(!colliding && obstacle.getTrashcan()){
                    obstacle.resetScales();
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
                customerServed++;
                sounds.nomPlay();
                float satisfaction = c.pat.multiplier();
                if (c.time() > 0){
                    sounds.happyPlay();
                    if(isTutorial){
                        if(happy + neutral + angry == 0){
                            score += 50;
                        } else {
                            score += 25;
                        }
                        if (satisfaction == 1) {
                            happy += 1;
                        } else if (satisfaction < 1 && satisfaction > 0.4) {
                            neutral += 1;
                        } else{
                            angry += 1;
                        }
                    }else {
                        score += c.servedDish.getScore() * c.pat.multiplier();
                        if (satisfaction == 1) {
                            happy += 1;
//                            sounds.happyPlay();
                        } else if (satisfaction < 1 && satisfaction > 0.4) {
                            neutral += 1;
//                            sounds.happyPlay();
                        } else{
                            angry += 1;
                        }
                    }
                }
                c.justSatisfied = false;
            }
            if (c.isActive()) {
                c.move();
            }
            if(c.time() <= 0){
                unsatisfiedCustomers++;
                c.timeOut();
                c.satisfied = Customer.SATISFIED.NO;
//                sounds.sadPlay();
            }
        }
        for (CookingStationObject c : stations){
            if (c.state == 0){
                if(c.pot.size != 0 && player.space && c.interacting){
                    c.state = 1;
                    int time;
                    if(c.station_type == 0){
                        panplay = true;
                        sounds.cookplay();
                        sounds.panPlay();
                        time = 15;
                    } else if(c.station_type == 1){
                        potplay = true;
                        sounds.cookplay();
                        sounds.potPlay();
                        time =30;
                    }else {
                        sounds.chopPlay();
                        time = 1;
                    }
                    c.timer = new Worldtimer(time, canvas, BaseTimer, directory);
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
                    if(c.getStationType() == 1){
                        sounds.potStop();
                        potplay = false;
                        sounds.potplaying = false;
                    } else if (c.getStationType() == 0){
                        sounds.panStopp();
                        sounds.panplaying = false;
                        panplay = false;
                    }
//                    sounds.potStop();
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
            create2();
            respawnTimer = 1.6666f;
            player.justDied = false;
        }
        player.respawning = respawning();

        respawnTimer = Math.max(respawnTimer-delta,0);

        if(ventOutFlag == true){
            animator.resetVentOut();
            vent1.animeframe = 0;
            ventOutTimer = 1.1666f;
            ventOutFlag = false;
            if(panplay){
                sounds.panPlay();
            }
            if(potplay){
                sounds.potPlay();
            }
        }

        ventOutTimer = Math.max(ventOutTimer-delta,0);

        world.step(1 / 60f, 6, 2);
    }

    public void create2() {
        // Schedule the task to run after 0.5 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                sounds.kickPlay();
            }
        }, 0.5f);
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
            ((Customer) obj).draw();
    }

    private boolean getDrawPriorityOfAnyType(Object obj){
        if(obj instanceof NormalObstacle)
            return ((NormalObstacle) obj).getDrawPriority();
        return false;
    }

    public void create () {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                canplay = true;
            }
        }, 3);
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
        //canvas.drawText("Score:", f,20, 600,2,2, layout);
        //canvas.drawText(Integer.toString(score), f, 130, 600, 2, 2,layout);
        canvas.draw(light,Color.WHITE, 0, 0,
                0, 5f*40, 0.0f, 1f, 1f);

//        if(trash.interactingTrash){
//            float midpoint = (float)canvas.getWidth()/2- (float)singleInv.getWidth()/2;
//            canvas.draw(singleInv, Color.WHITE, 10, 10,
//                    midpoint, 90, 0.0f, 1, 1);
//        }
        t.draw(isEndless, unsatisfiedCustomers);
        drawOutline();
        drawReq();
        drawScore();
        player.drawDishInv();
        player.inventory.draw(canvas);
    }

    //----my draw method for score----
    public void drawOutline(){
        Texture t = zero;
        if(score >= star_req[2]){
            t = three;
        } else if (score >= star_req[1]){
            t = two;
        }else if(score >= star_req[0]){
            t = one;
        }
        canvas.draw(t,Color.WHITE, 0, 0,
                0, 490, 0.0f, 1f, 1f);
    }

    public Texture getTexture(boolean isScore, int num){
        if(isScore){
            if(num == 0){
                return score0;
            } else if (num == 1){
                return score1;
            }else if (num == 2){
                return score2;
            }else if (num == 3){
                return score3;
            }else if (num == 4){
                return score4;
            }else if (num == 5){
                return score5;
            }else if (num == 6){
                return score6;
            }else if (num == 7){
                return score7;
            }else if (num == 8){
                return score8;
            }else if (num == 9){
                return score9;
            }
        }else {
            if(num == 0){
                return star0;
            } else if (num == 1){
                return star1;
            }else if (num == 2){
                return star2;
            }else if (num == 3){
                return star3;
            }else if (num == 4){
                return star4;
            }else if (num == 5){
                return star5;
            }else if (num == 6){
                return star6;
            }else if (num == 7){
                return star7;
            }else if (num == 8){
                return star8;
            }else if (num == 9){
                return star9;
            }
        }
        return score0;
    }
    public void drawReqHelper(int x, int y, int ox, int num){
        int hundred = num / 100;
        int ten = (num/10)%10;
        int one = num%10;
        if(num >= 100){
            canvas.draw(getTexture(false,hundred),Color.WHITE, 0, 0,
                    x, y, 0.0f, 1f, 1f);
            canvas.draw(getTexture(false,ten),Color.WHITE, 0, 0,
                    x+12, y, 0.0f, 1f, 1f);
            canvas.draw(getTexture(false,one),Color.WHITE, 0, 0,
                    x+24, y, 0.0f, 1f, 1f);
        }else if (num >= 10){
            canvas.draw(getTexture(false,ten),Color.WHITE, 0, 0,
                    x+8, y, 0.0f, 1f, 1f);
            canvas.draw(getTexture(false,one),Color.WHITE, 0, 0,
                    x+20, y, 0.0f, 1f, 1f);
        }else{
            canvas.draw(getTexture(false,one),Color.WHITE, 0, 0,
                    x+12, y, 0.0f, 1f, 1f);
        }
    }
    public void drawReq(){
        drawReqHelper(65,550,0,star_req[0]);
        drawReqHelper(120,550,0,star_req[1]);
        drawReqHelper(185,550,0,star_req[2]);

    }

    public void drawScore(){
        int hundred = score / 100;
        int ten = (score/10)%10;
        int one = score%10;

        if(score >= 1000){

        }else if (score >= 100){
            canvas.draw(getTexture(true,hundred),Color.WHITE, 0, 0,
                    110, 510, 0.0f, 1f, 1f);
            canvas.draw(getTexture(true,ten),Color.WHITE, 0, 0,
                    110+18, 510, 0.0f, 1f, 1f);
            canvas.draw(getTexture(true,one),Color.WHITE, 0, 0,
                    110+36, 510, 0.0f, 1f, 1f);
        }else if (score >= 10){
            canvas.draw(getTexture(true,ten),Color.WHITE, 0, 0,
                    110+12, 510, 0.0f, 1f, 1f);
            canvas.draw(getTexture(true,one),Color.WHITE, 0, 0,
                    110+30, 510, 0.0f, 1f, 1f);
        } else {
            canvas.draw(getTexture(true,one),Color.WHITE, 0, 0,
                    110+20, 510, 0.0f, 1f, 1f);

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
            sounds.panStopp();
            sounds.potStop();
            if(canplay) {
                sounds.ventPlay();
                canplay = false;
                create();
            }
//                System.out.println("vent playing");

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
        o.ventTimer = new Worldtimer((int) o.maxTime, canvas, BaseTimer,directory);
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

    public Array<Customer> getTakenCustomers(){
        return collision.getOrders();
    }

    public boolean allCustomersLeave(){
        if(customersToAdd.size > 0){
            return false;
        }
        else{
            for(Customer customer : customers){
                if(customer.controller.state != CustomerAIController.FSMState.LEAVE){
                    return false;
                }
            }
            return true;
        }
    }
}
