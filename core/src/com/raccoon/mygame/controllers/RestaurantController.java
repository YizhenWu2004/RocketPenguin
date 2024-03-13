package com.raccoon.mygame.controllers;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
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

    private int globalIndex = 0;
    private void addTable(float x, float y, boolean flip) {
        obstacles.add(new TableObstacle(x, y, 2.5f, 2.5f, (flip ? -0.25f : 0.25f), 0.25f, -50f, 50f,
                new Texture("table.png"), world, canvas));
    }
    private void addWallBump(float x, float y) {
        obstacles.add(new TableObstacle(x, y, 2.5f, 5f, 1f, 1f, 0f, 0f,
                new Texture("wallbump.png"), world, canvas));
    }
    public RestaurantController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv){
        world = new World(new Vector2(0,0), false);
        this.canvas = canvas;
        this.background = texture;
        customers = new Array();
        customers.add(new Customer(0f,1f,1f,0.7f,new Texture("rockoReal.png"),world, canvas));
        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),sharedInv, canvas, world);
        obstacles = new Array();
        obstacles.add(new NormalObstacle(16f, 17f, 32f, 2.5f, 1f, 1f, 0f, 500f,
                new Texture("restaurantwall.png"), world, canvas));
        obstacles.add(new NormalObstacle(28f, 15f, 3.25f, 4f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterleft.png"), world, canvas));
        obstacles.add(new NormalObstacle(30.3f, 14.1f, 1.25f, 5f, 0.25f, 0.25f, 0f, 0f,
                new Texture("counterright.png"), world, canvas));
        addTable(4.5f, 6f, true);
        addTable(4.5f, 11f, false);
        addTable(10.25f, 6f, false);
        addTable(10.25f, 11f, true);
        addTable(16f, 6f, true);
        addTable(16f, 11f, false);
        addWallBump(6f, 16.5f);
        addWallBump(14.5f, 16.5f);
        addWallBump(23f, 16.5f);
        this.input = input;

        vent1 = new VentObstacle(30.5f,1f, 1.5f,1.5f, 1, 1, 0, 0f, new Texture("vent.png"),world, canvas);

        active = true;
        world.setContactListener(this);
    }

    public void setActive(boolean b){
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
    public void update(){
        if (active){
            float x = 5f*input.getXMovement();
            float y =5f*input.getYMovement();
            player.setLinearVelocity(new Vector2(x,y));
            player.setSpace(input.getSpace());
            player.setInteraction(input.getInteraction());
            player.getInventory().setSelected((int) input.getScroll());
        }
        world.step(1/60f, 6,2);

    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        vent1.draw();
        for (NormalObstacle o : obstacles){
            o.draw();
        }
        player.draw(0.25f,0.25f);
        for(Customer c : customers){
            c.draw(0.25f,0.25f);
        }
    }
    public void debug(){
        player.drawDebug(canvas);
        for(Customer c : customers){
            c.debug(canvas);
        }
        vent1.drawDebug(canvas);
        for (NormalObstacle o: obstacles) {
            o.debug(canvas);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof VentObstacle)|| (body2.getUserData() instanceof Player && body1.getUserData() instanceof VentObstacle)){
            System.out.println("colliding with vent");
            //execute
            setVentCollision(true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int getIndex(){
        if(this.active)
            return this.globalIndex;
        return 0;
    }
    public void setIndex(int index){
        this.globalIndex = index;
    }

    public boolean getActive(){
        return this.active;
    }

    public boolean getVentCollision(){
        return this.ventCollision;
    }
    public void setVentCollision(boolean isColliding){
        this.ventCollision = isColliding;
    }
}
