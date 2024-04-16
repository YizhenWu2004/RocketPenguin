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
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.view.GameCanvas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

public class LevelModel {
    private static final float CELL_SIZE = 230f/40f;
    private World storeWorld;
    private GameCanvas canvas;
    private Array<NormalObstacle> storeObjects = new Array<>();
    private Array<NormalObstacle> ingredients = new Array<>();
    private Array<Guard> guards = new Array<>();
    private Array<Array<Array<Float>>> guardNodes = new Array<>();
    private TiledMap tiledMap;
    private MapLayer storeObjectsLayer;
    private MapLayer ingredientsLayer;
    private MapLayer guardsLayer;
    private MapLayer guardNodesLayer;

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
        ingredients.add(obstacle);
    }

    public LevelModel(String tmxFile, GameCanvas canvas) {
        storeWorld = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        tiledMap = new TmxMapLoader().load("tiled/" + tmxFile + ".tmx");
        storeObjectsLayer = tiledMap.getLayers().get("Obstacles");
        ingredientsLayer = tiledMap.getLayers().get("Ingredients");
        guardsLayer = tiledMap.getLayers().get("Guards");
        guardNodesLayer = tiledMap.getLayers().get("GuardNodes");
        processObjects();
        processIngredients();
        processGuards();
    }

    public World getStoreWorld() { return storeWorld; }

    public Array<NormalObstacle> getStoreObjects() { return storeObjects; }

    public Array<NormalObstacle> getIngredients() { return ingredients; }

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
                    obstacle = new NormalObstacle(x, y-1.5f, 1f, 0.5f, 1f, 1f, 0f, -50f,
                            new Texture("720/janitoritems.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "Ladder":
                    obstacle = new NormalObstacle(x, y-1.5f, 1.2f, -0.5f, 1f, 1f, 0f, -50f,
                            new Texture("720/ladder.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "BoxS":
                    obstacle = new NormalObstacle(x, y-1.5f, 0.8f, -0.5f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxsmall.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "BoxM":
                    obstacle = new NormalObstacle(x, y-1.5f, 1f, 1f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxmedium.png"), storeWorld, canvas);
                    storeObjects.add(obstacle);
                    break;
                case "BoxL":
                    obstacle = new NormalObstacle(x, y-1.5f, 1.2f, -0.5f, 1f, 1f, 0f, 0f,
                            new Texture("720/boxlarge.png"), storeWorld, canvas);
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

    private Array<Float> createNode(MapObject n) {
        Array<Float> node = new Array<Float>();
        node.add(n instanceof TextureMapObject ? ((TextureMapObject) n).getX()/40f : ((RectangleMapObject) n).getRectangle().getX()/40f);
        node.add(n instanceof TextureMapObject ? ((TextureMapObject) n).getY()/40f : ((RectangleMapObject) n).getRectangle().getY()/40f);
        node.add(((n.getProperties().get("Sleep")).equals("Yes") ? 1f : 0f));
        return node;
    }

    private void processGuards() {
        for (int i = 0; i < guardsLayer.getObjects().getCount(); i++) {
            guardNodes.add(new Array<>());
        }
        for (MapObject g : guardsLayer.getObjects()) {
            int idx = Integer.parseInt((String)g.getProperties().get("GuardNum"));
            Array<Array<Float>> nodes = new Array<>();
            nodes.add(createNode(g));
            System.out.println(nodes.size);
            guardNodes.set(idx-1, nodes);
        }
        for (MapObject n : guardNodesLayer.getObjects()) {
            int gIdx = Integer.parseInt((String)n.getProperties().get("GuardNum"));
            int nIdx = Integer.parseInt((String)n.getProperties().get("NodeNum"));
            Array<Array<Float>> gN = guardNodes.get(gIdx-1);
            while (nIdx >= gN.size) {
                gN.add(new Array<Float>());
            }
            guardNodes.get(gIdx-1).set(nIdx, createNode(n));
        }
        //CREATE ACTUAL GUARD OBJECT
    }
}
