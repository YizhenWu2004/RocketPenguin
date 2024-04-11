package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class NormalObstacle extends BoxObstacle {
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private Texture texture;
    private float scaleX;
    private float scaleY;
    private float ox;
    private float oy;
    private float sx;
    private float sy;
    private GameCanvas canvas;
    private Ingredient ingredient;
    private boolean drawPriority = false;

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.sx = sx;
        this.sy = sy;
        this.ox = ox;
        this.oy = oy;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.StaticBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
    }

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas, boolean drawPriority) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.sx = sx;
        this.sy = sy;
        this.ox = ox;
        this.oy = oy;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.StaticBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);

        this.drawPriority = drawPriority;
    }

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas, Ingredient ingredient) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.sx = sx;
        this.sy = sy;
        this.ox = ox;
        this.oy = oy;
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        activatePhysics(world);
        this.setBodyType(BodyType.StaticBody);
        setDrawScale(scaleX, scaleY);
        this.getBody().setUserData(this);
        this.ingredient = ingredient;
    }

    public float getOX() {
        return ox;
    }

    public float getOY() {
        return oy;
    }

    public float getSX() {
        return sx;
    }

    public float getSY() {
        return sy;
    }

    public boolean getDrawPriority() {return drawPriority;}

    public void draw() {
        draw(canvas, sx, sy, ox, oy);
    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
    }

    public Ingredient getIngredient(){
        if (this.ingredient == null) return null;
        return this.ingredient.clone();
    }
}
