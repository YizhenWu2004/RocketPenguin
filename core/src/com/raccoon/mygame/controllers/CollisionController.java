package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.objects.*;

//detects collision for now
public class CollisionController {
    /** Maximum distance a player must be from an ingredient to pick it up */
    protected static final float PICKUP_RADIUS = 100.0f;

    /** Maximum distance a player must be from a guard to be caught */
    protected static final float GUARD_RADIUS = 100.0f;

    /** Width of the collision geometry */
    private float width;
    /** Height of the collision geometry */
    private float height;

    /**
     * Creates a CollisionController for the given screen dimensions.
     *
     * @param width   Width of the screen
     * @param height  Height of the screen
     */
    public CollisionController(float width, float height) {
        this.width = width;
        this.height = height;
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
            if (p.getInteraction()) {
                p.pickUpItem(i);
                p.setInteraction(false);
            }
        }
    }

    private void handleCollision(Player p, Guard g) {

        Vector2 dst = new Vector2(p.getPosition().x, p.getPosition().y);
        dst.sub((g.getPosition()));
        if (Math.abs(dst.x) < 25 && Math.abs(dst.y) < 25) {
            p.setPosition(new Vector2());
            p.clearInv();
        }
    }
}
