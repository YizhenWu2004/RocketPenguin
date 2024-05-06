package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

public class NormalDecoration extends NormalObstacle {
    public NormalDecoration(float x, float y, float width, float height, float sx, float sy, float ox, float oy, TextureRegion texture, World world, GameCanvas canvas, boolean drawPriority) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas, drawPriority);
        this.body.getFixtureList().get(0).setSensor(true);
    }

    public NormalDecoration(float x, float y, float width, float height, float sx, float sy, float ox, float oy, TextureRegion texture, World world, GameCanvas canvas) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        this.body.getFixtureList().get(0).setSensor(true);
    }
}
