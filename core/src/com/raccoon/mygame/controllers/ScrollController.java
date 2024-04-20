package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class ScrollController implements InputProcessor {
    private int scroll;

    public ScrollController() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scroll = (int) amountY;
        return true;
    }

    public int getScroll() {
        return scroll;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public void resetScroll() {
        scroll = 0;
    }
}
