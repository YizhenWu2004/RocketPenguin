package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.*;

import java.util.Arrays;

//detects collision for now
public class CollisionController {
    /**
     * Maximum distance a player must be from an ingredient to pick it up
     */
    protected static final float PICKUP_RADIUS = 2.4f;

    private static final float DROP_RADIUS = 2.45f;

    protected static final float TRASH_RADIUS = 1.0f;

    /**
     * Maximum distance a player must be from a guard to be caught
     */
    protected static final float GUARD_RADIUS = 2.0f;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;

    private SoundController sounds;
    /**
     * Width of the collision geometry
     */
    private float width;
    /**
     * Height of the collision geometry
     */
    private float height;
    private Player player;

    private Array<Guard> guards;

    private Array<Customer> takenOrders = new Array<>();

    /**
     * Creates a CollisionController for the given screen dimensions.
     *
     * @param width  Width of the screen
     * @param height Height of the screen
     */
    public CollisionController(float width, float height, SoundController s) {
        sounds = s;
        this.width = width;
        this.height = height;
        //this.player = p;
        //this.guards = guards;
        //collide=false;
        //inSight=false;
    }

    public void processBounds(Player p) {
        if (p.getX() < 0) {
            p.setX(0);
        } else if (p.getX() >= width) {
            p.setX(width - 1);
        }

        if (p.getY() < 0) {
            p.setY(0);
        } else if (p.getY() >= height) {
            p.setY(height - 1);
        }
    }

//    public void processIngredients(Player p, Array<Ingredient> ingredients) {
//        for (Ingredient i: ingredients) {
//            handleCollision(p, i);
//        }
//    }

    public void processIngredients(Player p, Array<Ingredient> ingredients) {
        //I am sorry for changing this lol I really dont know what good it did
        //everything still works tho lolll
        for (GameObject i : ingredients) {
            if (i instanceof Ingredient)
                handleCollision(p, (Ingredient) i);
        }
    }

    public void processCustomers(Player p, Array<Customer> customers) {
        for (Customer c : customers) {
            if (p.getPosition().dst(c.getPosition()) <= 4) {
                if(c.getVX() == 0 && c.getVY() == 0) {
//                    c.setScaleX(1.1f);
//                    c.setScaleY(1.1f);
                }
                if (p.space && p.getPosition().dst(c.getPosition()) <= 4) {
                    if (c.canShow() && !c.getShow() && !c.isSatisfied()) {
                        addOrder(c);
//                    sounds.orderPlay();
                        if (c.getCustomerType() == "bear") {
                            sounds.bearPlay();
                        } else if (c.getCustomerType() == "goat") {
                            sounds.goatPlay();
                        } else if (c.getCustomerType() == "cat") {
                            sounds.catPlay();
                        } else if (c.getCustomerType() == "otter") {
                            sounds.otterPlay();
                        } else {
                            sounds.ferretPlay();
                        }
                        c.setShow(true);
                    }
                    if (p.dishInventory.leftFilled()) {
                        if (c.serve(p.dishInventory.get(0))) {
//                        System.out.println("served");
                            p.dishInventory.clear(0);
                            c.setShow(false);
                            return;
//                        System.out.println(c.getShow());
                        }
                    }
                    if (p.dishInventory.rightFilled()) {

                        if (c.serve(p.dishInventory.get(1))) {
                            p.dishInventory.clear(1);
                            c.setShow(false);
                        }
                    }
//                if (c.serve(p.getInventory().getSelectedItem())) {
//                    c.setShow(false);
//                    //p.getInventory().drop();
//                }
                }
            }
            else{
                c.resetScales();
            }

            if (p.getPosition().dst(c.getPosition()) <= 4 && (c.getVX() == 0 && c.getVY() == 0)) {
                if (!c.isScaling() && !c.isAtMaxScale()) {
                    c.startScaling(System.currentTimeMillis());
                }
                if (c.isScaling()) {
                    long currentTime = System.currentTimeMillis();
                    long startTime = c.getScalingStartTime();
                    float elapsedTime = (currentTime - startTime) / 500;

                    float newScale = Math.min(1.0f + 0.4f * elapsedTime, 1.1f);
                    c.setScaleX(newScale);
                    c.setScaleY(newScale);

                    if (newScale >= 1.1f) {
                        c.completeScaling();
                        c.setAtMaxScale(true);
                    }
                }

//                    c.setScaleX(1.1f);
//                    c.setScaleY(1.1f);
            }
            else if(p.getPosition().dst(c.getPosition()) > 4 && (c.getVX() == 0 && c.getVY() == 0)){
//                if ((c.isAtMaxScale() || c.scaleX > 1)){
//                    if (!c.isScaling()) {
//                        c.startScaling(System.currentTimeMillis());
//                    }
//                    long currentTime = System.currentTimeMillis();
//                    long startTime = c.getScalingStartTime();
//                    float elapsedTime = (currentTime - startTime) / 100f;
//
//                    float newScale = Math.max(1.4f - 0.4f * elapsedTime, 1.0f);
//                    c.setScaleX(newScale);
//                    c.setScaleY(newScale);
//
//                    if (newScale <= 1.0f) {
//                        c.completeScaling();
//                        c.setAtMaxScale(false);
//                    }
//                }

                c.completeScaling();
                c.setAtMaxScale(false);
                c.resetScales();
            }
        }


    }

//    public void processGuards(Player p, Array<Guard> guards) {
////        System.out.println(guards.size);
//        for (Guard g: guards) {
////            System.out.println(g.getY());
//            handleCollision(p, g);
//        }
//    }

    public void processGuards(Guard g, Guard[] guards, int i, Array<NormalObstacle> obstacles) {
//        System.out.println(guards.size);
        for (int j = i + 1; j < guards.length; j++) {
//            System.out.println(g.getY());
            handleCollision(guards[j], g);
        }
//        for(NormalObstacle obstacle: obstacles) {
//            if (handleCollision(g, obstacle)) {
//                g.getAIController().reverseDirection();
//            }
//        }
    }


    private Vector2 canvasToWorld(Vector2 canvasCoords) {
        return new Vector2(canvasCoords.x * WORLD_WIDTH / width, canvasCoords.y * WORLD_HEIGHT / height);
    }


//    private long lastScaleChangeTime = 0;
//    private static final long SCALE_CHANGE_COOLDOWN = 1000;

    //todo this is the smooth increase
//    private void handleCollision(Player p, Ingredient i) {
//        Vector2 iPosCanvas = new Vector2(i.getXPosition() + i.getTextureWidth() / 2f,
//                i.getYPosition() + i.getTextureHeight() / 2f);
//        Vector2 iPosWorld = canvasToWorld(iPosCanvas);
//        float distance = p.getPosition().dst(iPosWorld);
//
//        float scale = 1.0f;
//        if (distance < PICKUP_RADIUS) {
//            float factor = (PICKUP_RADIUS - distance) / PICKUP_RADIUS;
//            scale = 1.0f + 0.4f * factor;
//            if (p.getSpace()) {
//                    p.setSwiping(true);
//                    p.pickUpItem(i.clone());
//                    p.setSpace(false);
//                }
//        }
//        i.setSX(scale);
//        i.setSY(scale);
//    }

    private void handleCollision(Player p, Ingredient i) {
        Vector2 iPosCanvas = new Vector2(i.getXPosition() + i.getTextureWidth() / 2f,
                i.getYPosition() + i.getTextureHeight() / 2f);
        Vector2 iPosWorld = canvasToWorld(iPosCanvas);
        float distance = p.getPosition().dst(iPosWorld);

        if (distance < PICKUP_RADIUS) {
            if (!i.isScaling() && !i.isAtMaxScale()) {
                i.startScaling(System.currentTimeMillis());
            }
            if (i.isScaling()) {
                long currentTime = System.currentTimeMillis();
                long startTime = i.getScalingStartTime();
                float elapsedTime = (currentTime - startTime) / 100f;

                float newScale = Math.min(1.0f + 0.4f * elapsedTime, 1.4f);
                i.setSX(newScale);
                i.setSY(newScale);

                if (newScale >= 1.4f) {
                    i.completeScaling();
                    i.setAtMaxScale(true);
                }
            }

            if (p.getSpace()) {
                p.setSwiping(true);
                p.pickUpItem(i.clone());
                p.setSpace(false);
            }
        } else if ((distance > DROP_RADIUS && i.isAtMaxScale()) || (distance > DROP_RADIUS && i.TEXTURE_SX > 1)) {
            if (!i.isScaling()) {
                i.startScaling(System.currentTimeMillis());
            }
            long currentTime = System.currentTimeMillis();
            long startTime = i.getScalingStartTime();
            float elapsedTime = (currentTime - startTime) / 100f;

            float newScale = Math.max(1.4f - 0.4f * elapsedTime, 1.0f);
            i.setSX(newScale);
            i.setSY(newScale);

            if (newScale <= 1.0f) {
                i.completeScaling();
                i.setAtMaxScale(false);
            }
        }
    }



//    private void handleCollision(Player p, Ingredient i) {
//            Vector2 iPosCanvas = new Vector2(i.getXPosition() + i.getTextureWidth() / 2f,
//                    i.getYPosition() + i.getTextureHeight() / 2f);
//            Vector2 iPosWorld = canvasToWorld(iPosCanvas);
//            float distance = p.getPosition().dst(iPosWorld);
//
//            if (distance < PICKUP_RADIUS) {
//                i.setSX(1.4f);
//                i.setSY(1.4f);
//                if (p.getSpace()) {
//                    p.setSwiping(true);
//                    p.pickUpItem(i.clone());
//                    p.setSpace(false);
//                }
//            } else if (distance > DROP_RADIUS) {
//                i.resetScales();
//            }
//
//    }

//    private void handleCollision(Player p, Ingredient i) {
//        Vector2 iPosCanvas = new Vector2(i.getXPosition() + i.getTextureWidth() / 2f,
//                i.getYPosition() + i.getTextureHeight() / 2f);
////        System.out.println(iPosCanvas.x + " " + iPosCanvas.y);
//        Vector2 iPosWorld = canvasToWorld(iPosCanvas);
//        if (p.getPosition().dst(iPosWorld) < PICKUP_RADIUS) {
//            //set the scale to let the player know they can pick it up
//            i.setSX(1.4f);
//            i.setSY(1.4f);
//            if (p.getSpace()) {
//                p.setSwiping(true);
//                p.pickUpItem(i.clone());
//                p.setSpace(false);
//            }
//        }
//        else{
//            i.resetScales();
//        }
//    }

    void handleCollision(Player p, Guard g) {
        if (p.getPosition().x > g.getX() - GUARD_RADIUS && p.getPosition().x < g.getX() + GUARD_RADIUS) {
            if (p.getPosition().y > g.getY() - GUARD_RADIUS && p.getPosition().y < g.getY() + GUARD_RADIUS) {
                if(g.getAIController().getCurrentState() == GuardAIController.AIState.WANDER ||
                        g.getAIController().getCurrentState() == GuardAIController.AIState.ROTATE){
                    g.getAIController().setAIStateSus();
                }
                if(!g.getAIController().isSleep()){
                    g.getAIController().incrementSusMeter(5);
                }
                if(g.getAIController().isSleep()){
                    g.getAIController().incrementSusMeter(2);
                }
            }
        }
    }

    private void handleCollision(Guard other, Guard g) {
        if (other.getPosition().x > g.getX() - 0.5 && other.getPosition().x < g.getX() + 0.5) {
            if (other.getPosition().y > g.getY() - 0.5 && other.getPosition().y < g.getY() + 0.5) {
                other.setX(other.getPosition().x + 0.1f);
            }
        }
    }

    public boolean handleCollision(Guard g, NormalObstacle o) {
        Vector2 guardPosition = new Vector2(g.getX(), g.getY());
        Vector2 obstaclePosition = new Vector2(o.getX(), o.getY());

        float guardHalfWidth = g.getTextureWidth() / 2;
        float guardHalfHeight = g.getTextureHeight() / 2;
        float obstacleHalfWidth = o.getWidth() / 2;
        float obstacleHalfHeight = o.getHeight() / 2;

        float collisionBuffer = 10f;

        float distX = Math.abs(guardPosition.x - obstaclePosition.x) - collisionBuffer;
        float distY = Math.abs(guardPosition.y - obstaclePosition.y) - collisionBuffer;

        if (distX <= (guardHalfWidth + obstacleHalfWidth) && distY <= (guardHalfHeight + obstacleHalfHeight)) {
            return true;
        }
        return false;
    }

    public boolean handleCollision(Player p, NormalObstacle o) {
        if(o.getTrashcan() != true) {
            if (o.getIngredient() == null)
                return false;
        }
        Vector2 playerPosition = new Vector2(p.getX(), p.getY());
        Vector2 obstaclePosition = new Vector2(o.getX(), o.getY());

        float playerHalfWidth = p.getWidth() / 2;
        float playerHaldHeight = p.getHeight() / 2;

        float obstacleHalfWidth = o.getWidth() / 2;
        float obstacleHalfHeight = o.getHeight() / 2;

        float collisionBuffer = 0.5f;

        float distX = Math.abs(playerPosition.x - obstaclePosition.x) - collisionBuffer;
        float distY = Math.abs(playerPosition.y - obstaclePosition.y) - collisionBuffer;

        if (distX <= (playerHalfWidth + obstacleHalfWidth) && distY <= (playerHaldHeight + obstacleHalfHeight)) {
            return true;
        }
        return false;
    }


    public void handleCollision(Player p, NormalCollisionObject o) {

        Vector2 oPosCanvas = new Vector2(o.getX() + o.getWidth() / 2f,
                o.getY() + o.getHeight() / 2f);
//        System.out.println(iPosCanvas.x + " " + iPosCanvas.y);
        Vector2 oPosWorld = canvasToWorld(oPosCanvas);

        NormalCollisionObject goTo = o.getObjectToTeleportTo();
        Vector2 oPosCanvas1 = new Vector2(goTo.getX() + goTo.getWidth() / 2f,
                goTo.getY() + goTo.getHeight() / 2f);
//        System.out.println(iPosCanvas.x + " " + iPosCanvas.y);
        Vector2 oPosWorld1 = canvasToWorld(oPosCanvas1);
        if (p.getPosition().dst(oPosWorld) < PICKUP_RADIUS) {
//            System.out.println("Colliding with Normal Collision Object");
            if (o.getIsTeleporter()) {
                oPosWorld1.x += 2;
                goTo.setBeingTeleportedTo(true);
                p.setPosition(oPosWorld1);
                p.setTeleporting(true);
            }
        }
    }


    public void handleCollision(Player p, Trash t) {
        Vector2 tPosCanvas = new Vector2(t.getX() + t.getTextureWidth() / 2f,
                t.getY() + t.getTextureHeight() / 2f);
        Vector2 tPosWorld = canvasToWorld(tPosCanvas);
        if (p.getPosition().dst(tPosWorld) < TRASH_RADIUS) {
            if (p.getInteraction()) {
                p.getInventory().drop();
            }
        }
    }

    private void addOrder(Customer order) {
        //System.out.println("Adding order" + order);
        if (takenOrders.size >= 3) {
            takenOrders.removeIndex(0);
        }
        takenOrders.add(order);
    }

    public Array<Customer> getOrders(){
        return this.takenOrders;
    }
//    public boolean collide;
//    public boolean inSight;

//    @Override
//    public void beginContact(Contact contact) {
//        Body body1 = contact.getFixtureA().getBody();
//        Body body2 = contact.getFixtureB().getBody();
//        if (body1.getUserData() instanceof Customer || body2.getUserData() instanceof Customer){
//            return;
//        }
//
//        if (body1.getUserData() instanceof Player){
//            if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
//                inSight = true;
//                for(Guard guard : guards){
//                    if(body2.getUserData() == guard){
//                        System.out.println("I SEE YOU CHASE2");
////                        guard.switchToChaseMode();
//                    }
//                }
//            }
//            else {
//                collide = true;
//            }
//        }
//        else if (body1.getUserData() instanceof Player || body2.getUserData() instanceof Player){
//            if (contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) {
//                inSight = true;
//                for(Guard guard : guards){
//                    if(body1.getUserData() == guard){
//                        System.out.println("I SEE YOU CHASE2");
////                        guard.switchToChaseMode();
//                    }
//                }
//            }
//            else {
//                collide = true;
//            }
//        }
//    }
//
//    @Override
//    public void endContact(Contact contact) {
//        inSight = false;
//        collide = false;
//    }
//
//    @Override
//    public void preSolve(Contact contact, Manifold oldManifold) {
//
//    }
//
//    @Override
//    public void postSolve(Contact contact, ContactImpulse impulse) {
//
//    }
}
