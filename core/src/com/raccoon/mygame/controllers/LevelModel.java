package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.objects.TableObstacle;
import com.raccoon.mygame.objects.CookingStationObject;
import com.raccoon.mygame.view.GameCanvas;
import com.raccoon.mygame.enums.enums;

public class LevelModel {
    private World restaurantWorld;
    private World storeWorld;
    private GameCanvas canvas;
    private Array<NormalObstacle> restaurantObjects = new Array<>();
    private Array<TableObstacle> restaurantTables = new Array<>();
    private Array<CookingStationObject> restaurantCounters = new Array<>();
    private Array<NormalObstacle> storeObjects = new Array<>();
    private Array<Guard> guards = new Array<>();

    private void addTable(float x, float y, boolean flip) {
        TableObstacle t = new TableObstacle(x, y, 2.5f, 2.5f, (flip ? -0.25f : 0.25f), 0.25f, -50f, 50f,
                new Texture("table.png"), restaurantWorld, canvas);
        restaurantObjects.add(t);
        restaurantTables.add(t);
    }

    private void addWallBump(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 2.5f, 5f, 1f, 1f, 0f, 0f,
                new Texture("wallbump.png"), restaurantWorld, canvas);
        restaurantObjects.add(obstacle);
    }

    private void addShelfHorizontal(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 5.25f, 1f, 0.25f, 0.25f, 0f, -100f,
                new Texture("groceryshelfhorizontal.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addShelfVertical(float x, float y) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 1f, 6f, 0.3f, 0.3f, 0f, 0f,
                new Texture("groceryshelfvertical.png"), storeWorld, canvas);
        storeObjects.add(obstacle);
    }

    private void addFruitCrate(float x, float y, Ingredient ingredient) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 2f, 1f, 0.4f, 0.4f, 0f, 0f,
                new Texture(ingredient.type + ".png"), storeWorld, canvas, ingredient);
        storeObjects.add(obstacle);
    }

    public LevelModel(JsonValue levelData, JsonValue restaurantData, GameCanvas canvas) {
        restaurantWorld = new World(new Vector2(0, 0), false);
        storeWorld = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
    }

    private void gatherRestaurantObstacles(JsonValue data) {
        for (JsonValue obstacle : data) {
            switch (obstacle.getString("type")) {
                case "counterleft":
                    /*CookingStationObject c = new CookingStationObject(28f, 15f, 3.25f, 4f, 0.25f, 0.25f, 0f, 0f,
                            new Texture("counterleft.png"), restaurantWorld, canvas, player, 1);
                    restaurantObjects.add(c);
                    restaurantCounters.add(c);*/
                    break;
                default:
                    break;
            }
        }
    }
}
