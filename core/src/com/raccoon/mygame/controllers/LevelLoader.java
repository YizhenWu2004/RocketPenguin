package com.raccoon.mygame.controllers;

import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.badlogic.gdx.utils.JsonValue;
import com.raccoon.mygame.view.GameCanvas;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class LevelLoader {
    //private JsonValue restaurantJson;
    private String[] levelsData;
    private Array<LevelModel> levels = new Array<>();

    public LevelLoader(AssetDirectory directory, GameCanvas canvas) {
        gatherAssets(directory);
        loadLevels(canvas);
    }

    public LevelLoader(GameCanvas canvas) {
        levelsData = new String[]{"world_0-1", "world_0-2", "world_0-3", "world-1-1", "world-1-2", "rocko store"};
        loadLevels(canvas);
    }

    private void gatherAssets(AssetDirectory directory) {
        //restaurantJson = directory.getEntry("restaurant", JsonValue.class);
        levelsData = directory.getEntry("levels", JsonValue.class).get("levels").asStringArray();
    }

    private void loadLevels(GameCanvas canvas) {
        for (int i = 0; i < levelsData.length; i++) {
            levels.add(new LevelModel(levelsData[i], canvas));
        }
    }

    public Array<LevelModel> getLevels() { return levels; }
}
