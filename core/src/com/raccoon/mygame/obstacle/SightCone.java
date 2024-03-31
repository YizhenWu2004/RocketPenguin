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
import com.raccoon.mygame.enums.enums;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;


public class SightCone extends BoxObstacle {
    private ShapeRenderer shapeRenderer;
    private float x;
    private float y;
    private World w;
    private Guard g;
    private GameCanvas canvas;

    RayCastCallback[] rays = new RayCastCallback[10];

    public SightCone(float x1, float y1, float width, float height, World world, GameCanvas c, Guard guard) {
        super(width, height);
        this.canvas = canvas;
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
    }
    public void setPosition(float x1, float y1) {
        x = x1;
        y = y1;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public void create() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(canvas.getCamera().combined);

    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public void render() {
        Vector2 position = new Vector2(x, y);
        float direction = 180;
        float fov = 60;
        float range = 300;

        //shape render debug lines
        canvas.drawSightCone(position, direction, fov, range, Color.YELLOW, this.g);
        if(g.getP() == enums.PatrolDirection.LEFT_RIGHT){
        if(g.getAIController().getDirection()) {
            for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
                performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18), new Vector2(vertex.x / canvas.getWidth() * 32, vertex.y / canvas.getHeight() * 18), w);
            }
        } else {
            for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
                float flippedDirection = direction + 180;

                float endX = position.x + range * MathUtils.cosDeg(flippedDirection);
                float endY = position.y + range * MathUtils.sinDeg(flippedDirection);

                performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                        new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                        w);
            }}
    } else {
            if(g.getAIController().getDirection2()){
                for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
                    float flippedDirection = direction + 90;

                    float endX = position.x + range * MathUtils.cosDeg(flippedDirection);
                    float endY = position.y + range * MathUtils.sinDeg(flippedDirection);

                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                            new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                            w);
                }
            } else {
                for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
                    float flippedDirection = direction - 90;

                    float endX = position.x + range * MathUtils.cosDeg(flippedDirection);
                    float endY = position.y + range * MathUtils.sinDeg(flippedDirection);

                    performRaycast(new Vector2(position.x / canvas.getWidth() * 32, position.y / canvas.getHeight() * 18),
                            new Vector2(endX / canvas.getWidth() * 32, endY / canvas.getHeight() * 18),
                            w);
                }
            }
        }
    }


    public void update(Vector2 speed) {
        setLinearVelocity(speed);
    }

    public void updatePosition(Vector2 newPosition) {
        this.x = newPosition.x;
        this.y = newPosition.y;
    }

    private void performRaycast(Vector2 origin, Vector2 target, World w) {
//        System.out.println(origin);
//        System.out.println(target);
        RayCastCallback callback = (fixture, point, normal, fraction) -> {
            Object userData = fixture.getBody().getUserData();
            if (userData instanceof Player) {
//            System.out.println("chasing from raycast");
                g.switchToChaseMode();
                return 0;
            } else {
                return -1;
            }
        };
        w.rayCast(callback, origin, target);
    }


    private Vector2[] calculateSightConeVertices(Vector2 position, float direction, float fov, float range) {
        int numVertices = 30; // Number of vertices for the cone

        Vector2[] vertices = new Vector2[numVertices];

        float startAngle = direction - fov / 2;
        float angleIncrement = fov / (numVertices - 1);

        for (int i = 0; i < numVertices; i++) {
            float angle = startAngle + angleIncrement * i;
            float x = position.x + range * MathUtils.cosDeg(angle);
            float y = position.y + range * MathUtils.sinDeg(angle);
            vertices[i] = new Vector2(x, y);
        }

        return vertices;
    }
}
