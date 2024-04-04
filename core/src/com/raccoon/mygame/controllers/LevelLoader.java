package com.raccoon.mygame.controllers;

import com.badlogic.gdx.utils.Json;
import com.raccoon.mygame.assets.AssetDirectory;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.controllers.LevelModel;

import java.util.ArrayList;

public class LevelLoader {
    private JsonValue restaurantJson;
    private JsonValue levelJson;
    private ArrayList<LevelModel> levels;

    public LevelLoader(AssetDirectory directory) {
        gatherAssets(directory);
        levels = new ArrayList<>();
        for (int i = 0; i < levelJson.size; i++) {
            levels.add(new LevelModel(levelJson.get(i)));
        }
    }
    public void gatherAssets(AssetDirectory directory) {
        restaurantJson = directory.getEntry("restaurant", JsonValue.class);
        levelJson = directory.getEntry("levels", JsonValue.class).get("levels");
    }

    public ArrayList<LevelModel> getLevels() { return levels; }
}
