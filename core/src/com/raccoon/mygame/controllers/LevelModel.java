package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
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
        NormalObstacle obstacle = new NormalObstacle(x, y, 5.25f, 1f, 0.25f, 0.25f, 0f, -100f,
                new Texture("720/groceryshelfhorizontal.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addShelfVertical(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 1f, 6f, 0.3f, 0.3f, 0f, 0f,
                new Texture("720/groceryshelfvertical.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addFruitCrate(float x, float y, Ingredient ingredient) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 2f, 1f, 0.4f, 0.4f, 0f, 0f,
                new Texture("720/" + ingredient.type + ".png"), storeWorld, canvas, ingredient);
        ingredients.add(obstacle);
    }

    public LevelModel(String tmxFile, GameCanvas canvas) {
        storeWorld = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
        tiledMap = new TmxMapLoader().load(tmxFile + ".tmx");
        storeObjectsLayer = tiledMap.getLayers().get("Obstacles");
        ingredientsLayer = tiledMap.getLayers().get("Ingredients");
        guardsLayer = tiledMap.getLayers().get("Guards");
        guardNodesLayer = tiledMap.getLayers().get("GuardNodes");
    }

    private Array<Float> createNode(MapObject n) {
        Array<Float> node = new Array<Float>();
        node.add((float)n.getProperties().get("X"));
        node.add((float)n.getProperties().get("Y"));
        node.add((float)n.getProperties().get("Sleep"));
        node.add((float)n.getProperties().get("Time"));
        return node;
    }

    private void processObjects() {
        for (MapObject o : storeObjectsLayer.getObjects()) {
            switch (o.getName()) {
                case "HorizShelf":
                    break;
                case "VertShelf":
                    break;
                default:
                    break;
            }
        }
    }

    private void processIngredients() {
        for (MapObject i : ingredientsLayer.getObjects()) {
            Ingredient ing = new Ingredient(i.getName(), new Texture("720/" + i.getName() + ".png"), -1);
            addFruitCrate((float)i.getProperties().get("X"), (float)i.getProperties().get("Y"), ing);
        }
    }

    private void processGuards() {
        for (MapObject g : guardsLayer.getObjects()) {
            int idx = (int)g.getProperties().get("GuardNum");
            Array<Array<Float>> nodes = new Array<>();
            nodes.add(createNode(g));
            guardNodes.set(idx, nodes);
        }
        for (MapObject n : guardNodesLayer.getObjects()) {
            int gIdx = (int)n.getProperties().get("GuardNum");
            int nIdx = (int)n.getProperties().get("NodeNum");
            guardNodes.get(gIdx).set(nIdx, createNode(n));
        }
        //CREATE ACTUAL GUARD OBJECT
    }
}
