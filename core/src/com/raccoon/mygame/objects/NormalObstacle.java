package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

public class NormalObstacle extends BoxObstacle {
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;
    private Texture texture;
    private float scaleX;
    private float scaleY;
    private float ox;
    private float oy;
    private float defaultSX;
    private float defaultSY;
    private float sx;
    private float sy;
    private GameCanvas canvas;
    private Ingredient ingredient;
    private boolean drawPriority = false;
    private boolean isTrashcan = false;

    public boolean interactingTrash;

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.defaultSX = sx;
        this.defaultSY = sy;
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
        interactingTrash = false;
    }

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas, boolean drawPriority) {
        super(x, y, width, height);
        this.texture = texture;
        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.defaultSX = sx;
        this.defaultSY = sy;
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
        this.defaultSX = sx;
        this.defaultSY = sy;
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

    public NormalObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, FilmStrip defaultSprite, World world, GameCanvas canvas) {
        super(x, y, width, height);
        this.sprite = defaultSprite;
//        setTexture(new TextureRegion(texture));
        scaleX = canvas.getWidth() / WORLD_WIDTH;
        scaleY = canvas.getHeight() / WORLD_HEIGHT;
        this.defaultSX = sx;
        this.defaultSY = sy;
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

    public void setSX(float sx){this.sx = sx;}
    public void setSY(float sy){this.sy = sy;}

    public void resetSX(){
        this.sx = defaultSX;
    }
    public void resetSY(){
        this.sy = defaultSY;
    }
    public void resetScales(){
        this.sx = defaultSX;
        this.sy = defaultSX;
    }

    public float getDefaultSX(){return this.defaultSX;}
    public float getDefaultSY(){return this.defaultSY;}

    public void setOY(float oy){this.oy = oy;}
    public void setOX(float ox){this.ox = ox;}

    public boolean getDrawPriority() {return drawPriority;}

    public void draw() {
        if(this.sprite == null)
            draw(canvas, sx, sy, ox, oy);
        if(this.sprite != null)
            drawSprite(canvas, sx, sy, ox, oy);
    }
    public void drawWithOffset(float ox, float oy) {
        draw(canvas, sx, sy, ox, oy);
    }

    public void debug(GameCanvas canvas) {
        drawDebug(canvas);
    }

    public Ingredient getIngredient(){
        if (this.ingredient == null) return null;
        return this.ingredient.clone();
    }
    public void setTrashcan(boolean bool){
        this.isTrashcan = bool;
    }
    public boolean getTrashcan(){
        return this.isTrashcan;
    }
}
