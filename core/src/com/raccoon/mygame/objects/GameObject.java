package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public interface GameObject {
    public void discard();

    public boolean isActive();

    public Texture getTexture();

    public int posInInventory();

    public void draw(GameCanvas canvas);

}

