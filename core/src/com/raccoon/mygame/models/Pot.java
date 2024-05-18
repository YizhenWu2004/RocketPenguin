package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

/**
 * I have to make this just to animate pots :middle-finger:
 * */
public class Pot implements GameObject {

    /**
     * The number of animation frames in our filmstrip
     * This is just a default, and gets changed upon setting a new filmstrip.
     * */
    protected int   NUM_ANIM_FRAMES = 1;

    /**
     * Current animation frame.
     * Updated with deltaTime
     * */
    protected float animeframe = 0;

    /**
     * The speed of our animation at 60 fps
     * */
    protected float ANIMATION_SPEED = 0.15f;

    private int type;
    private FilmStrip sprite;
    private FilmStrip spriteCooked;
    private Texture texture;
    public float x;
    private float y;
    private float ox;
    private float oy;

    public float sx;
    public float sy;

    private boolean scaling;
    private long scalingStartTime;

    public boolean isCooking = false;

    public Pot(int type, FilmStrip sprite, float x, float y, float ox, float oy, float sx, float sy,FilmStrip spriteCooked){
        this.type = type;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.ox = ox;
        this.oy = oy;
        this.sx = sx;
        this.sy = sy;
        this.texture = sprite.getTexture();
        this.spriteCooked = spriteCooked;
        this.scaling = false;
        this.scalingStartTime = 0;
    }

    @Override
    public void discard() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public int posInInventory() {
        return 0;
    }

    @Override
    public void draw(GameCanvas canvas) {
        if(isCooking){
            canvas.draw(this.sprite, Color.WHITE, this.x, this.y, this.ox, this.oy, 0.0f,this.sx*-1, this.sy);
        }
        else{
            canvas.draw(this.sprite, Color.WHITE, this.x, this.y, this.ox, this.oy, 0.0f,this.sx, this.sy);
        }

    }

    //    @Override
    public void draw(GameCanvas canvas, boolean cooked){
        if(cooked){
            canvas.draw(this.spriteCooked, Color.WHITE, this.x, this.y, this.ox, this.oy, 0.0f,this.sx, this.sy);
        }
        else{
            if(isCooking){
                canvas.draw(this.sprite, Color.WHITE, this.x, this.y, this.ox, this.oy, 0.0f,this.sx*-1, this.sy);
            }
            else{
                canvas.draw(this.sprite, Color.WHITE, this.x, this.y, this.ox, this.oy, 0.0f,this.sx, this.sy);
            }
        }
    }

    public void setFilmStrip(FilmStrip value) {
        NUM_ANIM_FRAMES = value.getSize();
        sprite = value;
        //set to 0th frame if the current animation frame is larger that the amount of frames...
        //of the filmstrip we wish to set to.
        if(animeframe > NUM_ANIM_FRAMES)
            animeframe = 0;
        //no float frames.
        sprite.setFrame((int)animeframe);
    }
    public void updateAnimation(){
        // Increase animation frame
        animeframe += ANIMATION_SPEED;
        if (animeframe >= NUM_ANIM_FRAMES) {
            animeframe -= NUM_ANIM_FRAMES;
        }
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public void setSX(float sx) {
        this.sx = sx;
    }

    public void setSY(float sy) {
        this.sy = sy;
    }

    public void resetScales() {
        sx = 1;
        sy = 1;
    }

    public void startScaling(long startTime) {
        this.scaling = true;
        this.scalingStartTime = startTime;
    }

    public boolean isScaling() {
        return scaling;
    }

    public long getScalingStartTime() {
        return scalingStartTime;
    }

    public void completeScaling() {
        this.scaling = false;
    }

    public void stopScaling() {
        this.scaling = false;
        resetScales();
    }

    private boolean atMaxScale;

    public boolean isAtMaxScale() {
        return atMaxScale;
    }

    public void setAtMaxScale(boolean atMax) {
        this.atMaxScale = atMax;
    }
}
