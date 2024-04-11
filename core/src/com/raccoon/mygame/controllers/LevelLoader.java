package com.raccoon.mygame.controllers;

import com.raccoon.mygame.assets.AssetDirectory;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.view.GameCanvas;

import java.util.ArrayList;

public class LevelLoader {
    private JsonValue restaurantJson;
    private JsonValue levelJson;
    private ArrayList<LevelModelAlt> levels;

    public LevelLoader(AssetDirectory directory, GameCanvas canvas) {
        gatherAssets(directory);
        levels = new ArrayList<>();
        for (int i = 0; i < levelJson.size; i++) {
            levels.add(new LevelModelAlt(levelJson.get(i), restaurantJson, canvas));
        }
    }

    public void gatherAssets(AssetDirectory directory) {
        restaurantJson = directory.getEntry("restaurant", JsonValue.class);
        levelJson = directory.getEntry("levels", JsonValue.class).get("levels");
    }

    public ArrayList<LevelModelAlt> getLevels() { return levels; }
}
