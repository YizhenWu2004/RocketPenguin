package com.raccoon.mygame.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.enums.enums;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;
import java.util.ArrayList;


public class SightCone extends BoxObstacle {
    private ShapeRenderer shapeRenderer;
    private float x;
    private float y;
    private World w;
    private Guard g;
    private GameCanvas canvas;
    private boolean isActivated;
    RayCastCallback[] rays = new RayCastCallback[10];

    private ArrayList<Float> range;

    private boolean playerDetected;

    public SightCone(float x1, float y1, float width, float height, World world, GameCanvas c, Guard guard) {
        super(width, height);
//        this.canvas = c;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        setPosition(x1, y1);
        activatePhysics(world);
        this.setBodyType(BodyDef.BodyType.KinematicBody);
        canvas = c;
        w = world;
        g = guard;
        create();
        isActivated = true;
        range = new ArrayList<>();
        for(int i = 0; i < 31; i++){
            range.add(300f);
        }
//        range = 300;
    }
    public void setPosition(float x1, float y1) {
        x = x1;
        y = y1;
//        System.out.println("range in setPosition: " + range);
    }

//    public float getRange(){
//        return range;
//    }
    public Vector2 getPosition() {
//        System.out.println("range in getPosition: " + range);
        return new Vector2(x, y);
    }

    public void create() {
//        System.out.println("range in create: " + range);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(canvas.getCamera().combined);

    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public boolean isPlayerDetected() {
//        System.out.println("range in isPLayerDetected: " + range);
        return playerDetected;
    }

    public void render() {
//        range = range * 40;
//        System.out.println("range in render: " + range);
        playerDetected = false;
//        System.out.println("the range is: " + range);
        if (isActivated) {
            Vector2 position = new Vector2(x, y);
            float direction = 180;
            float fov = 60;

            //shape render debug lines
            canvas.drawSightCone(position, direction, fov, range, Color.YELLOW, this.g);
            if (g.getAIController().getOrien()== GuardAIController.GuardOrientation.LEFT){
                int count = 0;
                for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18), new Vector2(vertex.x / canvas.getWidth() * 32, vertex.y / canvas.getHeight() * 18), w, count);
                    count++;
                }
            } else if (g.getAIController().getOrien()== GuardAIController.GuardOrientation.RIGHT){
                int count = 0;
                for (Vector2 vertex : calculateSightConeVertices(position, direction + 180, fov, range)) {
//                float flippedDirection = direction + 180;
                    float endX = vertex.x;
                    float endY = vertex.y;
                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                            new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                            w, count);
                    count++;
                }
            } else if(g.getAIController().getOrien()== GuardAIController.GuardOrientation.DOWN){
                int count = 0;
                for (Vector2 vertex : calculateSightConeVertices(position, direction + 90, fov, range)) {
//                float flippedDirection = direction + 180;
                    float endX = vertex.x;
                    float endY = vertex.y;
                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                            new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                            w, count);
                    count++;
                }
            } else {
                int count = 0;
                for (Vector2 vertex : calculateSightConeVertices(position, direction -90, fov, range)) {
//                float flippedDirection = direction + 180;
                    float endX = vertex.x;
                    float endY = vertex.y;
                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                            new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                            w, count);
                    count++;
                }
            }
            }
        }

    public void deactivateSight(){
        isActivated = false;
    }
    public void reactivateSight(){
        isActivated = true;
    }
    public void update(Vector2 speed) {
        setLinearVelocity(speed);
    }

    public void updatePosition(Vector2 newPosition) {
//        System.out.println("range in updatePosition: " + range);
        this.x = newPosition.x;
        this.y = newPosition.y;
    }

    private double distance(Vector2 first, Vector2 second){
        float x1 = first.x;
        float y1 = first.y;
        float x2 = second.x;
        float y2 = second.y;

        float deltaX = x2 - x1;
        float deltaY = y2 - y1;

        float squareDeltaX = deltaX * deltaX;
        float squareDeltaY = deltaY * deltaY;

        float sumOfSquares = squareDeltaX + squareDeltaY;

        return Math.sqrt(sumOfSquares);
    }

    /**
     * susMeter incrementation lives here!
     */
    private void performRaycast(Vector2 origin, Vector2 target, World w, int count) {

        RayCastCallback callback = (fixture, point, normal, fraction) -> {
            Object userData = fixture.getBody().getUserData();
            if (userData instanceof Player) {
                g.getAIController().setAIStateSus();

                double distance = distance(fixture.getBody().getPosition(),g.getPosition());
                if(distance > 8){
                    g.getAIController().incrementSusMeter(0.3f);
                }
                else if(distance > 7){
                    g.getAIController().incrementSusMeter(0.5f);
                }
                else if(distance > 7){
                    g.getAIController().incrementSusMeter(1);
                }
                else if(distance > 2){
                    g.getAIController().incrementSusMeter(2);
                }
                else{
                    g.getAIController().incrementSusMeter(-1);
                }

                range.set(count, 300f);
                playerDetected = true;
                return 0;
            }
            /**
             * commented out cuz not working right now
             */
            if (userData instanceof NormalObstacle) {
                    float temp = origin.dst(point) * 40;
//                    System.out.println("temp is: " + temp);
                    range.set(count, Math.min(temp, 300f));
//                    System.out.println("the range is: " + range);
//                    System.out.println("the obstacle is: " + userData);
                return fraction;
            }
            else {
//                System.out.println("else is being called");
                range.set(count, 300f);
                return -1;
            }
        };
        range.set(count, 300f);
        w.rayCast(callback, origin, target);
    }

    private Vector2[] calculateSightConeVertices(Vector2 position, float direction, float fov, ArrayList<Float> range2) {
//        System.out.println("range in calculateSightconeVertices: " + range);
//        System.out.println("range2 in calculateSightConeVertices: " + range2);

        int numVertices = 30; // Number of vertices for the cone

        Vector2[] vertices = new Vector2[numVertices];

        float startAngle = direction - fov / 2;
        float angleIncrement = fov / (numVertices - 1);

        for (int i = 0; i < numVertices; i++) {
            float angle = startAngle + angleIncrement * i;
            float x = position.x + range2.get(i) * MathUtils.cosDeg(angle);
            float y = position.y + range2.get(i) * MathUtils.sinDeg(angle);
            vertices[i] = new Vector2(x, y);
        }
        return vertices;
    }
    public boolean getActivation(){
        return isActivated;
    }
}
