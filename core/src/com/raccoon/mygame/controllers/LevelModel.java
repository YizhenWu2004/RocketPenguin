package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.enums.enums;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.objects.NormalDecoration;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

public class LevelModel {
    private static final float CELL_SIZE = 230f/40f;
    private final int GRID_WIDTH = 32;
    private final int GRID_HEIGHT = 18;
    private World storeWorld;
    private GameCanvas canvas;
    private Array<NormalObstacle> storeObjects = new Array<>();
    private Array<Guard> guards = new Array<>();
    private Array<Array<Vector2>> guardNodes = new Array<>();
    private Array<Boolean> guardSleep = new Array<>();
    private TiledMap tiledMap;
    private MapLayer storeObjectsLayer;
    private MapLayer ingredientsLayer;
    private MapLayer guardsLayer;
    private MapLayer guardNodesLayer;
    private FilmStrip guardIdle;
    private boolean[][] collisionLayer = new boolean[GRID_WIDTH][GRID_HEIGHT];

    private float[] customerTimes;
    private int numStations;
    private int minOrder;
    private int maxOrder;
    private String[] orderIngredients;
    private float guardSpeed;
    private float patienceTime;

    private void addShelfHorizontal(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 5.25f, 1f, 1f, 1f, 0f, -40f,
                new Texture("720/groceryshelfhorizontal.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addShelfVertical(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 1f, 4f, 1f, 1f, 0f, 0f,
                new Texture("720/shelfvertical.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addFruitCrate(float x, float y, Ingredient ingredient) {
        NormalObstacle obstacle = new NormalObstacle(x+CELL_SIZE/2, y+CELL_SIZE/2, 2f, 1f, 1f, 1f, 0f, 0f,
                new Texture("720/" + ingredient.type + ".png"), storeWorld, canvas, ingredient);
        storeObjects.add(obstacle);
    }

    private void addGuard(float x, float y, boolean sleep, Array<Vector2> nodes) {
        System.out.println(sleep);
        System.out.println(nodes.size);
        guards.add(new Guard(x, y, 1.67f, 0.83f, guardIdle, storeWorld, canvas,
                (sleep? enums.PatrolDirection.SLEEP_WAKE : enums.PatrolDirection.UP_DOWN),collisionLayer,
                (sleep ? new Array<>() : nodes), GuardAIController.GuardOrientation.LEFT));
    }

    public LevelModel(String tmxFile, float[] customerTimes, int numStations, int minOrder, int maxOrder,
                      String[] orderIngredients, float guardSpeed, float patienceTime, GameCanvas canvas) {
        storeWorld = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        this.customerTimes = customerTimes;
        this.numStations = numStations;
        this.minOrder = minOrder;
        this.maxOrder = maxOrder;
        this.orderIngredients = orderIngredients;
        this.guardSpeed = guardSpeed;
        this.patienceTime = patienceTime;
        tiledMap = new TmxMapLoader().load("tiled/" + tmxFile + ".tmx");
        storeObjectsLayer = tiledMap.getLayers().get("Obstacles");
        ingredientsLayer = tiledMap.getLayers().get("IngredientBins");
        guardsLayer = tiledMap.getLayers().get("Guards");
        guardNodesLayer = tiledMap.getLayers().get("GuardNodes");
        guardIdle = new FilmStrip(new Texture("720/gooseidle.png"),1,1,1);
        processObjects();
        processIngredients();
        initializeCollisionLayer();
        processGuards();
    }

    public World getStoreWorld() { return storeWorld; }

    public float[] getCustomerTimes() { return customerTimes; }
    public int getNumStations() { return numStations; }
    public int getMinOrder() { return minOrder; }
    public int getMaxOrder() { return maxOrder; }
    public String[] getOrderIngredients() { return orderIngredients; }
    public float getGuardSpeed() { return guardSpeed; }
    public float getPatienceTime() { return patienceTime; }

    public Array<NormalObstacle> getStoreObjects() { return storeObjects; }

    public Array<Guard> getGuards() { return guards; }

    private void processObjects() {
        for (MapObject o : storeObjectsLayer.getObjects()) {
            float x = ((TextureMapObject) o).getX()/40f + CELL_SIZE/2;
            float y = ((TextureMapObject) o).getY()/40f + CELL_SIZE/2;
            NormalObstacle obstacle;
            switch (o.getName()) {
                case "HorizShelf":
                    addShelfHorizontal(x-0.1f, y-1.5f);
                    break;
                case "VertShelf":
                    addShelfVertical(x, y);
                    break;
                case "HorizWall":
                    obstacle = new NormalObstacle(x, y-1.5f, 5f, 1f, 1f, 1f, 0f, -60f,
                            new Texture("720/ventwallhorizontal.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "VertWall":
                    obstacle = new NormalObstacle(x, y-1.5f, 0.6f, 2f, 1f, 1f, 0f, -70f,
                            new Texture("720/ventwallvertical.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "JanitorTools":
                    obstacle = new NormalDecoration(x, y-1.5f, 1f, 0.5f, 1f, 1f, 0f, -50f,
                            new Texture("720/janitoritems.png"), storeWorld, canvas, false);
                    storeObjects.add(obstacle);
                    break;
                case "Ladder":
                    obstacle = new NormalDecoration(x, y-1.5f, 1.2f, -0.5f, 1f, 1f, 0f, -50f,
                            new Texture("720/ladder.png"), storeWorld, canvas, false);
                    storeObjects.add(obstacle);
                    break;
                case "BoxS":
                    obstacle = new NormalDecoration(x, y-1.5f, 0.8f, -0.5f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxsmall.png"), storeWorld, canvas, false);
                    storeObjects.add(obstacle);
                    break;
                case "BoxM":
                    obstacle = new NormalDecoration(x, y-1.5f, 1f, 1f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxmedium.png"), storeWorld, canvas, false);
                    storeObjects.add(obstacle);
                    break;
                case "BoxL":
                    obstacle = new NormalDecoration(x, y-1.5f, 1.2f, -0.5f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxlarge.png"), storeWorld, canvas, false);
                    storeObjects.add(obstacle);
                    break;
                case "Fridge":
                    obstacle = new NormalObstacle(x, y, 1f, 1f, 0.3f, 0.3f, 0f, 0f,
                            new Texture("720/fridge.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                default:
                    break;
            }
        }
    }

    private void processIngredients() {
        for (MapObject i : ingredientsLayer.getObjects()) {
            Ingredient ing = new Ingredient(i.getName(), new Texture("720/" + i.getName() + ".png"), -1);
            addFruitCrate(((TextureMapObject) i).getX()/40f, ((TextureMapObject) i).getY()/40f, ing);
        }
    }


    private void initializeCollisionLayer() {
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                collisionLayer[i][j] = false;
            }
        }

        for (NormalObstacle obstacle : storeObjects) {
            if (!(obstacle instanceof NormalDecoration)) {
                Vector2 position = obstacle.getPosition();
                Vector2 dimension = obstacle.getDimension();

                int left = Math.max(0, (int) position.x - (int) (dimension.x / 2));
                int bottom = Math.max(0, (int) position.y - (int) (dimension.y / 2));
                int right = Math.min(GRID_WIDTH - 1, (int) position.x + (int) (dimension.x / 2));
                int top = Math.min(GRID_HEIGHT - 1, (int) position.y + (int) (dimension.y / 2));

                for (int i = left; i <= right; i++) {
                    for (int j = bottom; j <= top; j++) {
                        collisionLayer[i][j] = true;
                    }
                }
            }
        }
    }

    private Vector2 createNode(MapObject n) {
        Vector2 node = new Vector2();
        node.x = (n instanceof TextureMapObject ? ((TextureMapObject) n).getX()/40f : ((RectangleMapObject) n).getRectangle().getX()/40f);
        node.y = (n instanceof TextureMapObject ? ((TextureMapObject) n).getY()/40f : ((RectangleMapObject) n).getRectangle().getY()/40f);
        return node;
    }

    private void processGuards() {
        int numGuards = guardsLayer.getObjects().getCount();
        for (int i = 0; i < numGuards; i++) {
            guardNodes.add(new Array<>());
            guardSleep.add(false);
        }
        for (MapObject g : guardsLayer.getObjects()) {
            int idx = Integer.parseInt((String) g.getProperties().get("GuardNum"));
            String sleep = (String)g.getProperties().get("Sleep");
            guardNodes.get(idx - 1).add(createNode(g));
            if (sleep.equals("Yes")) {
                System.out.println("this is happening");
                guardSleep.set(idx-1, true);
            }
        }
        for (MapObject n : guardNodesLayer.getObjects()) {
            int gIdx = Integer.parseInt((String) n.getProperties().get("GuardNum"));
            int nIdx = Integer.parseInt((String) n.getProperties().get("NodeNum"));
            Array<Vector2> gN = guardNodes.get(gIdx - 1);
            while (nIdx >= gN.size) {
                gN.add(new Vector2());
            }
            guardNodes.get(gIdx - 1).set(nIdx, createNode(n));
        }
        for (int i = 0; i < numGuards; i++) {
            Vector2 init = guardNodes.get(i).get(0);
            addGuard(init.x, init.y, guardSleep.get(i), guardNodes.get(i));
        }
    }
}
