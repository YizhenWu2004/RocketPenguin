package com.raccoon.mygame.objects;
import com.badlogic.gdx.graphics.Texture;

public interface GameObject {
    public void discard();
    public boolean isActive();
    public Texture getTexture();
    public int posInInventory();

}

