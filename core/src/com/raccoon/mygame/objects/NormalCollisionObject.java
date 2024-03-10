package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.raccoon.mygame.view.GameCanvas;

public class NormalCollisionObject implements GameObject{
    private float xPos;
    private float yPos;
    private Texture texture;
    private boolean isActive;
    private float width;
    private float height;
    private boolean isTeleporter;
    private NormalCollisionObject objectToTeleportTo;
    private boolean isBeingTeleportedTo = false;

    //normal collision objects don't move or change properties. No getters or setters usually.
    public NormalCollisionObject(Texture texture, float x, float y, float width, float height, boolean teleporter){
        this.isActive = true;
        this.texture = texture;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.isTeleporter = teleporter;
    }

    public NormalCollisionObject(Texture texture, float x, float y, float width, float height){
        this.isActive = true;
        this.texture = texture;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
    }

    public float getX(){
        return this.xPos;
    }
    public float getY(){
        return this.yPos;
    }
    public float getWidth(){
        return this.width;
    }
    public float getHeight(){
        return this.height;
    }

    public void setObjectToTeleportTo(NormalCollisionObject objectToTeleportTo){
        if(!this.isTeleporter)
            return;
        this.objectToTeleportTo = objectToTeleportTo;
        //this is so funy lol
        this.objectToTeleportTo.objectToTeleportTo = this;
    }

    public boolean getIsTeleporter(){
        return this.isTeleporter;
    }

    public void setBeingTeleportedTo(boolean setTeleport){
        this.isBeingTeleportedTo = setTeleport;
    }
    public boolean getBeingTeleportedTo(){
        return this.isBeingTeleportedTo;
    }

    public NormalCollisionObject getObjectToTeleportTo(){
        return this.objectToTeleportTo;
    }

    public Vector2 getPosition(){
        return new Vector2(this.getX(), this.getY());
    }

    public void draw(GameCanvas canvas){
        canvas.draw(texture, xPos, yPos);
    }


    @Override
    public void discard() {
        isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }

    @Override
    public int posInInventory() {
        //this shouldn't be in an inventory, ever.
        return -1;
    }
}
