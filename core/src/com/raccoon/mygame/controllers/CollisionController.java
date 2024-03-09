package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.*;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.*;
import com.raccoon.mygame.obstacle.Obstacle;

//detects collision for now
public class CollisionController implements ContactListener {
    /** Maximum distance a player must be from an ingredient to pick it up */
    protected static final float PICKUP_RADIUS = 1.0f;

    protected static final float TRASH_RADIUS = 1.0f;

    /** Maximum distance a player must be from a guard to be caught */
    protected static final float GUARD_RADIUS = 2.0f;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;

    /** Width of the collision geometry */
    private float width;
    /** Height of the collision geometry */
    private float height;
    private Player player;

    private Array<Guard> guards;

    /**
     * Creates a CollisionController for the given screen dimensions.
     *
     * @param width   Width of the screen
     * @param height  Height of the screen
     */
    public CollisionController(float width, float height, Player p, Array<Guard> guards) {
        this.width = width;
        this.height = height;
        this.player = p;
        this.guards = guards;
        collide=false;
        inSight=false;
    }

    public void processBounds(Player p) {
        if (p.getX() < 0) {
            p.setX(0);
        }
        else if (p.getX() >= width) {
            p.setX(width-1);
        }

        if (p.getY() < 0) {
            p.setY(0);
        }
        else if (p.getY() >= height) {
            p.setY(height-1);
        }
    }

    public void processIngredients(Player p, Array<Ingredient> ingredients) {
        for (Ingredient i: ingredients) {
            handleCollision(p, i);
        }
    }

    public void processGuards(Player p, Array<Guard> guards) {
//        System.out.println(guards.size);
        for (Guard g: guards) {
//            System.out.println(g.getY());
            handleCollision(p, g);
        }
    }

    private Vector2 canvasToWorld(Vector2 canvasCoords) {
        return new Vector2(canvasCoords.x * WORLD_WIDTH / width, canvasCoords.y * WORLD_HEIGHT / height);
    }

    private void handleCollision(Player p, Ingredient i) {
        Vector2 iPosCanvas = new Vector2(i.getXPosition() + i.getTextureWidth()/2f,
                i.getYPosition() + i.getTextureHeight()/2f);
//        System.out.println(iPosCanvas.x + " " + iPosCanvas.y);
        Vector2 iPosWorld = canvasToWorld(iPosCanvas);
        if (p.getPosition().dst(iPosWorld) < PICKUP_RADIUS) {
            if (p.getSpace()) {
                p.pickUpItem(i);
                p.setSpace(false);
            }
        }
    }

    private void handleCollision(Player p, Guard g) {
//        Vector2 pPos = p.getPosition();
//        Vector2 gPos = g.getPosition();
//
//        float pRight = pPos.x + p.getWidth();
//        float pTop = pPos.y + p.getHeight();
//        float gRight = gPos.x + g.getTextureWidth();
//        float gTop = gPos.y + g.getTextureHeight();
//
//        if (pPos.x < gRight && pRight > gPos.x && pPos.y < gTop && pTop > gPos.y) {
//            p.setPosition(new Vector2());
//            p.clearInv();
//        }
//        Vector2 iPosCanvas = new Vector2(g.getX() + g.getTextureWidth()/2f,
//                g.getY() + g.getTextureHeight()/2f);
//        System.out.println(iPosCanvas.x + " " + iPosCanvas.y);
//        Vector2 iPosWorld = canvasToWorld(iPosCanvas);
//        System.out.println(p.getPosition().dst(iPosWorld));
        if (p.getPosition().x > g.getX() - GUARD_RADIUS && p.getPosition().x < g.getX() + GUARD_RADIUS) {
            if(p.getPosition().y > g.getY() - GUARD_RADIUS && p.getPosition().y < g.getY() + GUARD_RADIUS)
//            System.out.println(g.getY());
                g.switchToChaseMode();
        }
    }

    public void handleCollision(Player p, Trash t) {
        Vector2 tPosCanvas = new Vector2(t.getX() + t.getTextureWidth()/2f,
                t.getY() + t.getTextureHeight()/2f);
        Vector2 tPosWorld = canvasToWorld(tPosCanvas);
        if (p.getPosition().dst(tPosWorld) < TRASH_RADIUS) {
            if (p.getInteraction()) {
                p.getInventory().drop();
            }
        }
    }
    public boolean collide;
    public boolean inSight;

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Here");

        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if (body1.getUserData() instanceof Player){
            if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
                inSight = true;
                for(Guard guard : guards){
                    if(body2.getUserData() == guard){
                        System.out.println("I SEE YOU CHASE2");
                        guard.switchToChaseMode();
                    }
                }
            }
            else {
                collide = true;
            }
        }
        else if (body1.getUserData() instanceof Player || body2.getUserData() instanceof Player){
            System.out.println("collision");
            if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
                inSight = true;
                for(Guard guard : guards){
                    if(body1.getUserData() == guard){
                        guard.switchToChaseMode();
                    }
                }
            }
            else {
                collide = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        inSight = false;
        collide = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
