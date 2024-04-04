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
import com.raccoon.mygame.objects.VentObstacle;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.objects.TableObstacle;
import com.raccoon.mygame.view.GameCanvas;

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

    private int globalIndex = 0;
    private int tick;
    private Vector2 localStartingPos;
    Array<CookingStationObject> stations;

    private void addTable(float x, float y, boolean flip) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 2.5f, (flip ? -0.25f : 0.25f), 0.25f, -50f, 50f,
                new Texture("table.png"), world, canvas);
        obstacles.add(t);
        tables.add(t);
    }

    private void addWallBump(float x, float y) {
        obstacles.add(new TableObstacle(x, y, 2.5f, 5f, 1f, 1f, 0f, 0f,
                new Texture("wallbump.png"), world, canvas));
    }

    public RestaurantController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv) {
        world = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.background = texture;
        player = new Player(0f, 0f, 1, 0.7f, new Texture("rockoReal.png"), sharedInv, canvas, world);
        obstacles = new Array();
        obstacles.add(new NormalObstacle(16f, 17f, 32f, 2.5f, 1f, 1f, 0f, 500f,
                new Texture("restaurantwall.png"), world, canvas));

        stations = new Array<>();
        CookingStationObject temp = new CookingStationObject(28f, 15f, 3.25f, 4f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterleft.png"), world, canvas, player, 1);
        obstacles.add(temp);
        stations.add(temp);
        temp = new CookingStationObject(30.3f, 14.1f, 1.25f, 5f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterright.png"), world, canvas, player, 2);
        obstacles.add(temp);
        stations.add(temp);

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
        customers.add(new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 1));
        Filter f = new Filter();
        f.categoryBits = 0x0002;
        f.maskBits = 0x0001;
        for (Customer c : customers) {
            c.setFilterData(f);
        }
        this.input = input;

        vent1 = new VentObstacle(30.5f,1f, 1.5f,1.5f, 1, 1, 0, 0f, new Texture("vent.png"),world, canvas);
        localStartingPos = new Vector2(vent1.getX()-1.5f, vent1.getY());

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
            customers.add(new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 2));
        }else if (tick == 200){
            customers.add(new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 3));
        }else if (tick == 300){
            customers.add(new Customer(0f, 8.5f, 1f, 0.7f, new Texture("customer1.png"), world, canvas, tables, 4));
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

    public void draw() {
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        vent1.draw();
        for (NormalObstacle o : obstacles) {
            o.draw();
        }
        player.draw(0.25f, 0.25f);
        for (Customer c : customers) {
            if (c.isActive()) {
                c.draw(0.1f, 0.1f);
            }
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
