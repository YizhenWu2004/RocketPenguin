package com.raccoon.mygame.controllers;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.CookingStationObject;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.VentObstacle;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.objects.TableObstacle;
import com.raccoon.mygame.view.GameCanvas;

import java.lang.reflect.GenericArrayType;

public class RestaurantController extends WorldController implements ContactListener {
    private World world;
    private GameCanvas canvas;
    private Texture background;
    private Array<Customer> customers;
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
    Array<CookingStationObject> stations;

    private void addTable(float x, float y, boolean flip) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 2.5f, (flip ? -0.25f : 0.25f), 0.25f, -50f, 50f,
                new Texture("table.png"), world, canvas);
        obstacles.add(t);
        drawableObjects.add(t);
        tables.add(t);
    }

    private void addWallBump(float x, float y) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 5f, 1f, 1f, 0f, 0f,
                new Texture("wallbump.png"), world, canvas);
        obstacles.add(t);
        drawableObjects.add(t);
    }

    public RestaurantController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv) {
        world = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.background = texture;
        player = new Player(0f, 0f, 1, 0.7f, new Texture("rockoReal.png"), sharedInv, canvas, world);
        drawableObjects.add(player);

        obstacles = new Array();

        NormalObstacle normalOb1 = (new NormalObstacle(16f, 17f, 32f, 2.5f, 1f, 1f, 0f, 500f,
                new Texture("restaurantwall.png"), world, canvas));
        obstacles.add(normalOb1);
        drawableObjects.add(normalOb1);

        stations = new Array<>();
        CookingStationObject temp = new CookingStationObject(28f, 15f, 3.25f, 4f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterleft.png"), world, canvas, player, 1);
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);
        temp = new CookingStationObject(30.3f, 14.1f, 1.25f, 5f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterright.png"), world, canvas, player, 2);
        obstacles.add(temp);
        stations.add(temp);
        drawableObjects.add(temp);

        tables = new Array();
        addTable(16f, 11f, false);
        addTable(16f, 6f, true);
        addTable(10.25f, 11f, true);
        addTable(10.25f, 6f, false);
        addTable(4.5f, 11f, false);
        addTable(4.5f, 6f, true);


        //System.out.println(tables.size);
        addWallBump(6f, 16.5f);
        addWallBump(14.5f, 16.5f);
        addWallBump(23f, 16.5f);


        customers = new Array();
        Customer customer1 = new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 1);
        customers.add(customer1);
        drawableObjects.add(customer1);

        Filter f = new Filter();
        f.categoryBits = 0x0002;
        f.maskBits = 0x0001;
        for (Customer c : customers) {
            c.setFilterData(f);
        }
        this.input = input;

        vent1 = new VentObstacle(30.5f,1f, 1.5f,1.5f, 1, 1, 0, 0f, new Texture("vent.png"),world, canvas);
        localStartingPos = new Vector2(vent1.getX()-1.5f, vent1.getY());
        drawableObjects.add(vent1);

        collision = new CollisionController(canvas.getWidth(), canvas.getHeight());
        active = true;
        tick = 0;
        world.setContactListener(this);
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
        if (tick == 100){
            Customer customer1 = new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 2);
            customers.add(customer1);
            drawableObjects.add(customer1);

        }else if (tick == 200){
            Customer customer2 = new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 3);
            customers.add(customer2);
            drawableObjects.add(customer2);
        }else if (tick == 300){
            Customer customer3 = new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 4);
            customers.add(customer3);
            drawableObjects.add(customer3);
        }
        if (active) {
            float x = 5f * input.getXMovement();
            float y = 5f * input.getYMovement();
            player.setLinearVelocity(new Vector2(x, y));
            player.setSpace(input.getSpace());
            player.setInteraction(input.getInteraction());
            player.getInventory().setSelected((int) input.getScroll());
            collision.processCustomers(player, customers);
        }
        for (Customer c : customers) {
            if (c.isActive()) {
                c.move();
            }
        }
        for (CookingStationObject c : stations){
            c.update();
        }

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
            ((Player) obj).draw(0.25f, 0.25f);
        if(obj instanceof NormalObstacle)
            ((NormalObstacle) obj).draw();
        if(obj instanceof Customer)
            ((Customer) obj).draw(0.1f, 0.1f);
    }

    public void draw() {
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);



        //bubble sort for drawing
        boolean swapped;
        for (int i = 0; i < drawableObjects.size-1; i++) {
            swapped = false;
            for(int j = 0; j < drawableObjects.size-1-i; j++){
                float currentY = getYPosOfAnyObject(drawableObjects.get(j));
                float nextY = getYPosOfAnyObject(drawableObjects.get(j+1));
                if(currentY <= nextY){
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
//        for (Customer c : customers) {
//            if (c.isActive()) {
//                c.draw(0.1f, 0.1f);
//            }
//        }
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
            setVentCollision(true);
        }

        if (body1.getUserData() instanceof Player && body2.getUserData() instanceof CookingStationObject){
            CookingStationObject obj = (CookingStationObject) body2.getUserData();
            obj.interacting = true;
            obj.interacting_with = obj.id;
        }
        else if (body1.getUserData() instanceof CookingStationObject && body2.getUserData() instanceof Player){
            CookingStationObject obj = (CookingStationObject) body1.getUserData();
            obj.interacting = true;
            obj.interacting_with = obj.id;
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
    }
}
