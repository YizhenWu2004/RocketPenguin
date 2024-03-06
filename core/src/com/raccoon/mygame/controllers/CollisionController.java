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

//detects collision for now
public class CollisionController implements ContactListener {
    /** Maximum distance a player must be from an ingredient to pick it up */
    protected static final float PICKUP_RADIUS = 100.0f;

    protected static final float TRASH_RADIUS = 100.0f;

    /** Maximum distance a player must be from a guard to be caught */
    protected static final float GUARD_RADIUS = 100.0f;

    /** Width of the collision geometry */
    private float width;
    /** Height of the collision geometry */
    private float height;
    private Player player;

    /**
     * Creates a CollisionController for the given screen dimensions.
     *
     * @param width   Width of the screen
     * @param height  Height of the screen
     */
    public CollisionController(float width, float height, Player p) {
        this.width = width;
        this.height = height;
        this.player = p;
        collide=false;
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
        for (Guard g: guards) {
            handleCollision(p, g);
        }
    }

    private void handleCollision(Player p, Ingredient i) {

        if (p.getPosition().dst(new Vector2(i.getXPosition(), i.getYPosition())) < PICKUP_RADIUS) {
            if (p.getSpace()) {
                p.pickUpItem(i);
                p.setSpace(false);
            }
        }
    }

    private void handleCollision(Player p, Guard g) {
        Vector2 pPos = p.getPosition();
        Vector2 gPos = g.getPosition();

        float pRight = pPos.x + p.getTextureWidth();
        float pTop = pPos.y + p.getTextureHeight();
        float gRight = gPos.x + g.getTextureWidth();
        float gTop = gPos.y + g.getTextureHeight();

        if (pPos.x < gRight && pRight > gPos.x && pPos.y < gTop && pTop > gPos.y) {
            p.setPosition(new Vector2());
            p.clearInv();
        }
    }

    public void handleCollision(Player p, Trash t) {
        if (p.getPosition().dst(t.getPosition()) < TRASH_RADIUS) {
            if (p.getInteraction()) {
                p.getInventory().drop();
            }
        }
    }
    public boolean collide;


    @Override
    public void beginContact(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if (body1.getUserData() == player.p || body2.getUserData() == player.p){
            collide = true;
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
}
