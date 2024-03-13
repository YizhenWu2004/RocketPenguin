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
import com.raccoon.mygame.view.GameCanvas;

public class RestaurantController extends WorldController implements ContactListener {
    private World world;
    private GameCanvas canvas;
    private Texture background;
    private Array<Customer> customers;
    private Player player;
    private InputController input;
    private boolean active;
    public RestaurantController(GameCanvas canvas, Texture texture, InputController input){
        world = new World(new Vector2(0,0), false);
        this.canvas = canvas;
        this.background = texture;
        customers = new Array();
        customers.add(new Customer(0f,1f,1f,0.7f,new Texture("rockoReal.png"),world, canvas));
        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),new Inventory(new Texture("inventorybar.png")), canvas, world);
        this.input = input;
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
                0, 0, 0.0f, 2f, 2f);
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
    }

    @Override
    public void beginContact(Contact contact) {
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
}
