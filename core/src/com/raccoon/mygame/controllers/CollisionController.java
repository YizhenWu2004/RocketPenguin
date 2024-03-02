package com.raccoon.mygame.controllers;

import com.raccoon.mygame.models.Player;

//detects collision for now
public class CollisionController {
    /** Maximum distance a player must be from an ingredient to pick it up */
    protected static final float PICKUP_RADIUS = 3.0f;

    /** Maximum distance a player must be from a guard to be caught */
    protected static final float GUARD_RADIUS = 3.0f;

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
        if (p.getX() )
    }
}
