package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.view.GameCanvas;

public class LevelModelAlt {
    private World storeWorld;
    private GameCanvas canvas;
    private Array<NormalObstacle> storeObjects = new Array<>();
    private Array<Ingredient> ingredients = new Array<>();
    private Array<Guard> guards = new Array<>();
    private Array<Float> guardX = new Array<>();
    private Array<Float> guardY = new Array<>();

//    private void addShelfHorizontal(float x, float y) {
//        NormalObstacle obstacle = new NormalObstacle(x, y, 5.25f, 1f, 0.25f, 0.25f, 0f, -100f,
//                new Texture("groceryshelfhorizontal.png"), storeWorld, canvas);
//        storeObjects.add(obstacle);
//    }
//
//    private void addShelfVertical(float x, float y) {
//        NormalObstacle obstacle = new NormalObstacle(x, y, 1f, 6f, 0.3f, 0.3f, 0f, 0f,
//                new Texture("groceryshelfvertical.png"), storeWorld, canvas);
//        storeObjects.add(obstacle);
//    }

    private void addFruitCrate(float x, float y, Ingredient ingredient) {
        NormalObstacle obstacle = new NormalObstacle(x, y, 2f, 1f, 0.4f, 0.4f, 0f, 0f,
                new Texture(ingredient.type + ".png"), storeWorld, canvas, ingredient);
        storeObjects.add(obstacle);
    }

    /*private void addGuard(float x, float y, enums patrolType,
            guards.add(new Guard(28, 13.3f, 1.67f, 0.83f,guardIdle, world, canvas, enums.PatrolDirection.SLEEP_WAKE, collisionLayer,new Vector2[0]));
        guardX.add(28f);
        guardY.add(13.3f);*/

    public LevelModelAlt(JsonValue levelData, JsonValue restaurantData, GameCanvas canvas) {
        storeWorld = new World(new Vector2(0, 0), false);
        this.canvas = canvas;
    }
}
