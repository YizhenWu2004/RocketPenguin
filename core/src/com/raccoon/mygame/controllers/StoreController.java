package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.*;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.obstacle.CapsuleObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

import java.util.Arrays;
import java.util.HashMap;

import static com.raccoon.mygame.enums.enums.PatrolDirection;

public class StoreController extends WorldController implements ContactListener {
    private World world;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private GameCanvas canvas;
    private Texture background;
    private InputController input;
    public Player player;
    private Array<Ingredient> ingredients;
    private Array<Guard> guards;

    private SoundController sounds;
    private Array<Float> guardX;

    private boolean canplay = true;
    private Array<Float> guardY;

    public VentObstacle vent1;
    private boolean ventCollision;

    private int globalIndex = 1;
    private Vector2 localStartingPos;

    private Array<NormalObstacle> obstacles;
    public boolean playerGuardCollide;
    private CollisionController collision;
    boolean active;
    public boolean playerJustDied;

    public boolean duringventing;

    private final int GRID_WIDTH = WORLD_WIDTH;
    private final int GRID_HEIGHT = WORLD_HEIGHT;
    private boolean[][] collisionLayer = new boolean[GRID_WIDTH][GRID_HEIGHT];

    private Array<Object> drawableObjects = new Array<>();

    private FilmStrip playerIdle;
    private FilmStrip vent;

    private AnimationController animator;
    private Array<Customer> notepadOrders;

    public int current = -3;

    public boolean ventOutFlag;
    private float ventOutTimer;

    public float playerJustCaughtTimer;

    private Array<UIButton> buttons = new Array<>();
    private UIButton orders;

    private int a = 0;

    public Guard guardInAction;


    private Texture invisible;
    private Texture BaseTimer;
    private Texture apple;

    //TODO ADD THESE TO ASSET DIRECTORY I AM WORKING ON AN OLD BRANCH
    private Texture notepadhappy;
    private Texture notepadneutral;
    private Texture notepadangry;
    private Texture notepadtoggled;
    private Texture notepaduntoggled;

    private HashMap<String, Texture> ingredientTextures;
    public Worldtimer t;
    AssetDirectory directory;

    public boolean isEndless;
    private void createTextures(AssetDirectory directory) {
        playerIdle = directory.getEntry("rockoidle.strip", FilmStrip.class);
        vent = directory.getEntry("vent.strip", FilmStrip.class);
        invisible = directory.getEntry("invisible", Texture.class);
        BaseTimer = directory.getEntry("basetimer", Texture.class);
        notepadhappy = directory.getEntry("notepadhappy", Texture.class);
        notepadneutral = directory.getEntry("notepadneutral", Texture.class);
        notepadangry = directory.getEntry("notepadangry", Texture.class);
        notepadtoggled = directory.getEntry("notepadtoggled", Texture.class);
        notepaduntoggled = directory.getEntry("notepaduntoggled", Texture.class);
        apple = directory.getEntry("apple", Texture.class);
    }

//    public boolean totalReset = false;

//    private void addShelfHorizontal(float x, float y) {
//        //0.95
//        NormalObstacle obstacle = new NormalObstacle(x, y, 5.25f, 1f, 0.95f, 1f, 0f, -30f,
//
//        new Texture("720/groceryshelfhorizontal.png"), world, canvas);
//        obstacles.add(obstacle);
//        drawableObjects.add(obstacle);
//    }
//
//    private void addShelfVertical(float x, float y) {
//        NormalObstacle obstacle = new NormalObstacle(x, y, 1f, 4f, 0.95f, 1, 0f, 0f,
//                new Texture("720/shelfvertical.png"), world, canvas);
//        obstacles.add(obstacle);
//        drawableObjects.add(obstacle);
//    }
//
//    private void addFruitCrate(float x, float y, Ingredient ingredient) {
//        NormalObstacle obstacle = new NormalObstacle(x, y, 2f, 1f, 1, 1, 0f, 0f,
//               getIngredientTexture(ingredient.type), world, canvas, ingredient);
//        obstacles.add(obstacle);
//        drawableObjects.add(obstacle);
//    }
//
//    //obstacles are real, their collisions affect things
//    private void addNormalObstacle(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
//        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
//                new Texture("720/" + texturename + ".png"), world, canvas);
//
//        obstacles.add(t);
//        drawableObjects.add(t);
//    }
//    private void addNormalObstacle(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset, boolean drawPriority) {
//        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
//                new Texture("720/" + texturename + ".png"), world, canvas, drawPriority);
//
//        obstacles.add(t);
//        drawableObjects.add(t);
//    }
//    //decorations are sensors, no collision will be detected
//    private void addDecoration(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset, boolean drawPriority) {
//        NormalObstacle t = new NormalDecoration(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
//                new Texture("720/" + texturename + ".png"), world, canvas, drawPriority);
//
//        obstacles.add(t);
//        drawableObjects.add(t);
//    }
//    private void addDecoration(float x, float y, String texturename, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
//        NormalObstacle t = new NormalDecoration(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
//                new Texture("720/" + texturename + ".png"), world, canvas);
//
//        obstacles.add(t);
//        drawableObjects.add(t);
//    }

    private void addInvisibleWall(float x, float y, float colliderWidth, float colliderHeight, float scaleX, float scaleY, float xOffset, float yOffset) {
        NormalObstacle t = new NormalObstacle(x, y, colliderWidth, colliderHeight, scaleX, scaleY, xOffset, yOffset,
                invisible, world, canvas);

        obstacles.add(t);
    }


    public StoreController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv, Worldtimer w, Array<Customer> notepadOrders, SoundController s, AssetDirectory directory, boolean isEndless) {
        createTextures(directory);

        this.isEndless = isEndless;

        this.directory = directory;
        world = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.background = texture;
        this.t = w;
        sounds = s;
        collision = new CollisionController(canvas.getWidth(), canvas.getHeight(), sounds);

        this.notepadOrders = notepadOrders;

        obstacles = new Array<>();
        ingredients = new Array<>();
        guards = new Array<>();
        guardX = new Array<>();
        guardY = new Array<>();
        ingredientTextures = new HashMap<>();

        orders = new UIButton(notepaduntoggled, "orders",1100,680,0.6f,0.6f,canvas);
        orders.setOnHoverAction(()->{orders.setY(490);orders.setTexture(notepadtoggled);});
        orders.setOnUnhoverAction(()->{orders.resetPosition();orders.resetStyleProperties();});


        buttons.add(orders);

        player = new Player(0, 0, 1.5f, 0.7f,  playerIdle, sharedInv, canvas, world, sounds, directory);
        drawableObjects.add(player);
        this.input = input;

        vent1 = new VentObstacle(1.5f,1f, 1.5f,1.5f, 1, 1, 27f, 27f, vent, world, canvas);
        drawableObjects.add(vent1);

        localStartingPos = new Vector2(vent1.getX()+2.3f, vent1.getY());
        //constantifying this
        //localStartingPos = new Vector2(3.8f, 1f);

        float nodOff = 1.5f;
        active = false;
        world.setContactListener(this);


        initializeCollisionLayer();

        playerJustDied = false;

        animator = new AnimationController(input, directory);
        duringventing = false;
        s.isStore = true;
    }

    public void setLevel(LevelModel level, Inventory sharedInv) {
        sounds.storeStop();
        guardWanderReset();
        player.deactivatePhysics(world);
        vent1.deactivatePhysics(world);
        world = level.getStoreWorld();
        player = new Player(0, 0, 1.5f, 0.7f,  playerIdle, sharedInv, canvas, world, sounds,directory);
        vent1 = new VentObstacle(1.5f,1f, 1.5f,1.5f, 1, 1, 27f, 27f, vent, world, canvas);
        obstacles = level.getStoreObjects();
        guards = level.getGuards();
//        guards = new Array<Guard>(1);
//        guards.add(new Guard(20f, 10, 1.67f, 0.83f, guardIdle, world, canvas, PatrolDirection.ROTATE_CCW,collisionLayer,new Array<>(), GuardAIController.GuardOrientation.LEFT));
        //System.out.println("Size: " + level.getIngredients().size);
        ingredients = level.getIngredients();
//        for(Ingredient i: ingredients){
//            ingredientTextures.put(i.type, new Texture("720/" + i.type + ".png"));
//        }
        drawableObjects.clear();
        for (Object o : level.getStoreObjectsAndDecor()) {
            drawableObjects.add(o);
        }
        //drawableObjects = level.getStoreObjectsAndDecor();
        drawableObjects.add(player);
        drawableObjects.add(vent1);
        guardX = new Array<>();
        guardY = new Array<>();
        for (Guard guard : guards) {
            drawableObjects.add(guard);
            guardX.add(guard.getX());
            guardY.add(guard.getY());
        }

        active = false;
        world.setContactListener(this);
        initializeCollisionLayer();
        playerJustDied = false;
        duringventing = false;

        addInvisibleWall(0,-1,80,1,1,1,0,0);
        addInvisibleWall(-1,0,1,40,1,1,0,0);
        addInvisibleWall(32f,0f,1,40,1,1,0,0);
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

    public World getWorld() {
        return world;
    }

    @Override
    public void render(float delta) {

    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        player.current = this.current;
        if (playerGuardCollide) {
//            player.setPosition(0, 0);
            player.clearInv();
            //PLAYER JUST DIED
            playerJustCaughtTimer = 1.1666f;
            playerJustDied = true;
            sounds.squeakPlay();
            canplay = false;
            create();
            playerGuardCollide = false;
        }

        if(ventOutFlag == true){
            ventOutTimer = 1.1666f;
            ventOutFlag = false;
        }

        ventOutTimer = Math.max(ventOutTimer-delta,0);

        playerJustCaughtTimer = Math.max(playerJustCaughtTimer-delta,0);

        if (active) {
            if(!ventingOut() && !player.playerIsVenting && !gettingCaught()){
                float x = 5f * input.getXMovement();
                float y = 5f * input.getYMovement();
                //System.out.println(player.getX());
                player.setLinearVelocity(new Vector2(x, y));
                player.setSpace(input.getSpace());
                player.setInteraction(input.getInteraction());
                if(input.getOneThroughFivePressed()){
                    player.getInventory().setIndex(input.getNumIndex());
                }
                player.getInventory().setSelected((int) input.getScroll());
            }
            animator.handleAnimation(vent1, player, delta, ventingOut());
        }
        for (Guard guard : guards) {
            //todo fix the flash of exclamation mark
//            if(gettingCaught() && playerJustCaughtTimer <10){
//                guard.getBody().setLinearVelocity(0,0);
//                player.getBody().setLinearVelocity(0,0);
//            }
            if(gettingCaught()){
//                guard.update(delta, generatePlayerInfo(), gettingCaught());
                guard.getBody().setLinearVelocity(0,0);
                player.getBody().setLinearVelocity(0,0);
            }
            else{
                guard.update(delta, generatePlayerInfo(), gettingCaught());
            }
            if (!duringventing) {
                collision.handleCollision(player, guard);
            }
        }

        collision.processBounds(player);
//        collision.processGuards(player,guards);
        collision.processIngredients(player, ingredients);
        world.step(1 / 60f, 6, 2);

        Guard[] guards2 = new Guard[guards.size];
        for (int i = 0; i < guards.size; i++) {
            guards2[i] = guards.get(i);
        }
        for (int i = 0; i < guards2.length - 1; i++) {
            collision.processGuards(guards.get(i), guards2, i, obstacles);
        }

        for(NormalObstacle obstacle : obstacles){
            boolean colliding = collision.handleCollision(player, obstacle);
            if(colliding && obstacle.getIngredient()!=null){
                if(input.getSpace()){
                    player.pickUpItem(obstacle.getIngredient());
                }
            }
        }
        if(vent1.ventTimer != null) {
            //System.out.println(vent1.ventTimer.getTime());
            if (vent1.ventTimer.getTime() <= 0) {
                setVentCollision(true);
                vent1.ventTimer = null;
                player.playerIsVenting = false;
                duringventing = false;
            }
        }
        if(input.getDown() && current == 1){
            player.removeItem();
        }
        if(gettingCaught()){
            player.stopDrawing = true;
        }
        player.update(delta);
        animator.processGuards(guards, delta, guardInAction,gettingCaught());
        animator.handleAnimation(player, delta, respawning());
        checkButtons();

        addOrdersToNotepad();
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
        if(obj instanceof Ingredient)
            return ((Ingredient) obj).getYPosition();
        if(obj instanceof Guard)
            return ((Guard) obj).getPosition().y;
        //shouldn't get here.
//        System.out.println("I don't know what this object type is: " + obj.getClass().getSimpleName() +"!");
        return 0f;
    }

    private void drawAnyType(Object obj){
        if(obj instanceof VentObstacle)
            ((VentObstacle) obj).draw();
        if(obj instanceof TableObstacle)
            ((TableObstacle) obj).draw();
        if(obj instanceof Player)
            ((Player) obj).draw(1f, 1f);
        if(obj instanceof NormalObstacle)
            ((NormalObstacle) obj).draw();
        if(obj instanceof Customer)
            ((Customer) obj).draw(1f, 1f);
        if(obj instanceof Ingredient)
            ((Ingredient) obj).draw(canvas);
        if(obj instanceof Guard)
            ((Guard) obj).draw(1, 1);
    }

    public void setA(int i){
        a = i;
    }
    public void draw(int unsatisfied) {
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);

        //bubble sort for drawing
        boolean swapped;
        for (int i = 0; i < drawableObjects.size-1; i++) {
            swapped = false;
            for(int j = 0; j < drawableObjects.size-1-i; j++){
                float currentY = getYPosOfAnyObject(drawableObjects.get(j));
                float nextY = getYPosOfAnyObject(drawableObjects.get(j+1));
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

        for(Object obj : drawableObjects){
            drawAnyType(obj);
        }

//        vent1.draw();
//        for (NormalObstacle o : obstacles) {
//            o.draw();
//        }
//        player.draw(0.25f, 0.25f);
        for (Ingredient i : ingredients) {
            i.draw(canvas);
        }
        for(UIButton button: buttons){
            button.draw(canvas);
        }
//        for (Guard g : guards) {
//            g.draw(0.1f, 0.1f);
//        }
        t.draw(isEndless, unsatisfied);
        player.inventory.draw(canvas);
    }

    public void debug() {
        player.drawDebug(canvas);
        for (Guard g : guards) {
            g.debug(canvas);
        }
        vent1.drawDebug(canvas);
        for (NormalObstacle o : obstacles) {
            o.debug(canvas);
        }
    }

    /**
     *
     * @return an array of information, as follows
     * index 0: player's X
     * index 1: player's Y
     * index 2: player's midpoint X
     * index 3: player's midpoint Y
     */
//    public Array<Float> generatePlayerInfo() {
//        Array<Float> playerInfo = new Array<Float>();
//
//        // Upper left corner
//        float upperLeftX = player.getX();
//        float upperLeftY = player.getY();
//        playerInfo.add(upperLeftX);
//        playerInfo.add(upperLeftY);
//
//        // Upper right corner
//        float upperRightX = player.getX() + player.getWidth();
//        float upperRightY = player.getY();
//        playerInfo.add(upperRightX);
//        playerInfo.add(upperRightY);
//
//        // Lower left corner
//        float lowerLeftX = player.getX();
//        float lowerLeftY = player.getY() + player.getHeight();
//        playerInfo.add(lowerLeftX);
//        playerInfo.add(lowerLeftY);
//
//        // Lower right corner
//        float lowerRightX = player.getX() + player.getWidth();
//        float lowerRightY = player.getY() + player.getHeight()+0.5f;
//        playerInfo.add(lowerRightX);
//        playerInfo.add(lowerRightY);
//
//        // Middle point
//        float middleX = player.getX() + player.getWidth() / 2;
//        float middleY = player.getY() + player.getHeight() / 2;
//        playerInfo.add(middleX);
//        playerInfo.add(middleY);
//
//        return playerInfo;
//    }

    public Array<Float> generatePlayerInfo() {
        Array<Float> playerInfo = new Array<Float>();

        float coreWidth = player.getWidth();
        float coreHeight = player.getHeight();
        float offsetX = 0;
        float offsetY = 0;

        float radius = (player.getOrientation() == CapsuleObstacle.Orientation.HORIZONTAL) ? coreHeight / 2 : coreWidth / 2;
        switch (player.getOrientation()) {
            case TOP:
                coreHeight -= radius;
                offsetY += radius;
                break;
            case BOTTOM:
                coreHeight -= radius;
                break;
            case LEFT:
                coreWidth -= radius;
                offsetX += radius;
                break;
            case RIGHT:
                coreWidth -= radius;
                break;
            case VERTICAL:
                coreHeight -= 2 * radius;
                offsetY += radius;
                break;
            case HORIZONTAL:
                coreWidth -= 2 * radius;
                offsetX += radius;
                break;
        }

        // Upper left corner
        float upperLeftX = player.getX() + offsetX;
        float upperLeftY = player.getY() + offsetY;
        playerInfo.add(upperLeftX);
        playerInfo.add(upperLeftY);

        // Upper right corner
        float upperRightX = upperLeftX + coreWidth;
        float upperRightY = upperLeftY;
        playerInfo.add(upperRightX);
        playerInfo.add(upperRightY);

        // Lower left corner
        float lowerLeftX = upperLeftX;
        float lowerLeftY = upperLeftY + coreHeight;
        playerInfo.add(lowerLeftX);
        playerInfo.add(lowerLeftY);

        // Lower right corner
        float lowerRightX = upperRightX;
        float lowerRightY = lowerLeftY;
        playerInfo.add(lowerRightX);
        playerInfo.add(lowerRightY);

        // Middle point
        float middleX = player.getX() + player.getWidth() / 2;
        float middleY = player.getY() + player.getHeight() / 2;
        playerInfo.add(middleX);
        playerInfo.add(middleY);

        return playerInfo;
    }




    @Override
    public void beginContact(Contact contact) {
        //System.out.println("hi!");
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof Guard) || (body2.getUserData() instanceof Player && body1.getUserData() instanceof Guard)) {
            if(!duringventing) {
                playerGuardCollide = true;
                if(body2.getUserData() instanceof Guard){
                    guardInAction = (Guard) body2.getUserData();
                    ((Guard) body2.getUserData()).stopExclam = true;
                }
                else{
                    guardInAction = (Guard) body1.getUserData();
                    ((Guard) body1.getUserData()).stopExclam = true;
                }
            }
        }
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof VentObstacle) || (body2.getUserData() instanceof Player && body1.getUserData() instanceof VentObstacle)) {
//            setVentCollision(true);
            startVentTimer(vent1, player);
            if(a > 1) {
                if (canplay) {
                    sounds.ventPlay();
                    canplay = false;
                    create();
//                    System.out.println("vent playing");
                }
            }
            a++;
        }
        if ((body1.getUserData() instanceof Guard && body2.getUserData() instanceof NormalObstacle) ||
                (body2.getUserData() instanceof Guard && body1.getUserData() instanceof NormalObstacle)) {
            Guard guard = (body1.getUserData() instanceof Guard) ? (Guard) body1.getUserData() : (Guard) body2.getUserData();
            guard.getAIController().reverseDirection();
        }

    }

    @Override
    public void endContact(Contact contact) {
        playerGuardCollide = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int getIndex() {
        return this.globalIndex;
    }

    public void setIndex(int ind) {
        globalIndex = ind;
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

    private void initializeCollisionLayer() {
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                collisionLayer[i][j] = false;
            }
        }

        for (NormalObstacle obstacle : obstacles) {
            Vector2 position = obstacle.getPosition();
            Vector2 dimension = obstacle.getDimension();

            int left = Math.max(0, (int) position.x - (int) (dimension.x / 2));
            int bottom = Math.max(0, (int) position.y - (int) (dimension.y / 2));
            int right = Math.min(GRID_WIDTH - 1, (int) position.x + (int) (dimension.x / 2));
            int top = Math.min(GRID_HEIGHT - 1, (int) position.y + (int) (dimension.y / 2));

            for (int i = left; i <= right; i++) {
                for (int j = bottom; j <= top; j++) {
                    collisionLayer[i][j] = true;
                }
            }
        }
    }

    public void onSet(){
        player.setPosition(localStartingPos);
        player.direction = -1;
    }

    public void guardWanderReset(){
        for(int i = 0; i < guards.size; i++){
            guards.get(i).setPosition(guardX.get(i),guardY.get(i));
            guards.get(i).switchToDefaultMode();
            guards.get(i).resetSusMeter();
            guards.get(i).stopExclam = false;
            guards.get(i).getAIController().currentNodeIndex = 0;
        }
    }

    public void create () {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                canplay = true;
            }
        }, 3);
    }
    public void startVentTimer(VentObstacle o, Player p){
        duringventing = true;
        p.playerIsVenting = true;
        o.ventTimer = new Worldtimer((int) o.maxTime, canvas, BaseTimer,directory);
        o.ventTimer.create();
    }

    public boolean respawning(){
        return false;
    }

    public boolean ventingOut(){
        return ventOutTimer > 0;
    }

    public boolean gettingCaught(){
        return playerJustCaughtTimer > 0;
    }

    private Texture getIngredientTexture(String id){
        try{
            return ingredientTextures.get(id);
        } catch(Exception e){
            //System.out.println("This texture does not exist yet");
            //System.out.println(e);
        }
        return apple;
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    /**
     * For every button in the scene (modal and non modal)
     * Check for clicks, hovers, and unhovers.
     * Call events that should happen in those circumstances.
     * */
    private void checkButtons(){
        //If no modals are active, check state for normal buttons.
        for (UIButton button : buttons) {
            float minX = button.getX();
            float maxX = button.getX() + button.getWidth();
            float minY = button.getY();
            float maxY = button.getY() + button.getHeight();
            if(button.getSticky()){
                minX = button.getAdjustedX();
                maxX = button.getAdjustedX() + button.getWidth();
                minY = button.getAdjustedY();
                maxY = button.getAdjustedY() + button.getHeight();
            }
            if ((processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY)) || input.getShiftHeld()) {
                button.setHovered(true);
                button.onHoverEvent();
            } else {
                button.setHovered(false);
                button.onUnhoverEvent();
            }
            //if input is within bounds of button
            if (processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY) && input.click) {
                button.setIsClicked(true);
                button.onClickEvent();
                button.setIsClicked(false);
            }
        }
    }

    private void addOrdersToNotepad(){
        orders.resetChildren();
        Array<Customer> takenOrders = this.notepadOrders;
        Texture happinessTexture;
        for(int j = 0; j< takenOrders.size; j++){
            Ingredient[] ingredients = takenOrders.get(j).getOrder();
            if(takenOrders.get(j).pat == null)
                continue;
            float happiness = takenOrders.get(j).pat.multiplier();
            switch(Float.toString(happiness)){
                case "0.7":
                    happinessTexture = notepadneutral;
                    break;
                case "1.0":
                    happinessTexture = notepadhappy;
                    break;
                case"0.3":
                    happinessTexture = notepadangry;
                    break;
                default:
                    happinessTexture = apple;
            }
            orders.addChild(new UIButton(happinessTexture,"ingred",20, (-110*j)+295,1f,1f,canvas));
            for(int i = 0; i < ingredients.length; i++){
                Ingredient currentIngredient = ingredients[i];
                if(currentIngredient != null)
                    orders.addChild(new UIButton(currentIngredient.getTexture(),"ingred",(50*i)+90, (-110*j)+293,1f,1f,canvas));
            }
        }
    }
    public void setNotepadOrders(Array<Customer> ordersrs){
        this.notepadOrders = ordersrs;
    }
}
