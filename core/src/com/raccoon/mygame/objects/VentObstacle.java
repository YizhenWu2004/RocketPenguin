package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.view.GameCanvas;

public class VentObstacle extends NormalObstacle{

    private boolean isTeleporter = true;
    private VentObstacle objectToTeleportTo;
    private boolean isBeingTeleportedTo = false;

    public VentObstacle(float x, float y, float width, float height, float sx, float sy, float ox, float oy, Texture texture, World world, GameCanvas canvas){
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
    }

    public void setObjectToTeleportTo(VentObstacle objectToTeleportTo){
        this.objectToTeleportTo = objectToTeleportTo;
        this.objectToTeleportTo.objectToTeleportTo = this;
    }

    public boolean getIsTeleporter() {return this.isTeleporter;}
    public void setBeingTeleportedTo(boolean setTeleport){this.isBeingTeleportedTo = setTeleport;}
    public boolean getBeingTeleportedTo(){return this.isBeingTeleportedTo;}

    public void teleport(Player player){
        Vector2 newPos = objectToTeleportTo.getPosition();
        //little offset
        newPos.x += 2;
        player.setPosition(newPos);
    }
}

