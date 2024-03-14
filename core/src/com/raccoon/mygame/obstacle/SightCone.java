
package com.raccoon.mygame.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.StoreController;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;


public class SightCone extends BoxObstacle{
    private ShapeRenderer shapeRenderer;
    private float x;
    private float y;
    private World w;
    private Guard g;
    private GameCanvas canvas;

    RayCastCallback [] rays = new RayCastCallback[10];

//    RayHandler rayHandler = new RayHandler(world);

    public SightCone(float x1, float y1, float width, float height, World world, GameCanvas c, Guard guard){
        super(width, height);
        setPosition(x1, y1);
        w = world;
        canvas = c;
        g =guard;
        create();
    }

    public void setPosition(float x1, float y1){
        x = x1;
        y = y1;
    }
    public Vector2 getPosition(){
        return new Vector2(x, y);
    }
    public void create() {
        shapeRenderer = new ShapeRenderer();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public void render() {
        // Clear screen
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        System.out.println("rendering sightcone");
        // Begin shape rendering
//        System.out.println(getPosition());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        Vector2 position = new Vector2(x,y);
        float direction = 180;
        float fov = 60;
        float range = 300;

        // Calculate the vertices of the sight cone
        Vector2[] vertices = calculateSightConeVertices(position, direction, fov, range);// Draw the sight cone
        for (int i = 0; i < vertices.length; i++) {
            int nextIndex = (i + 1) % vertices.length;
            shapeRenderer.line(position.x, position.y, vertices[i].x, vertices[i].y);
            shapeRenderer.line(position.x, position.y, vertices[nextIndex].x, vertices[nextIndex].y);
        }
        for (Vector2 vertex : calculateSightConeVertices(position, direction, fov, range)) {
            performRaycast(new Vector2(position.x/canvas.getWidth() * 32, position.y/canvas.getHeight() * 18), new Vector2(vertex.x/canvas.getWidth() * 32, vertex.y/canvas.getHeight() * 18), w);
        }
        // End shape rendering
        shapeRenderer.end();
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
