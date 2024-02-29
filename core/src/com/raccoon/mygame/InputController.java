package com.raccoon.mygame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.*;
import java.util.*;
import com.badlogic.gdx.controllers.Controller;

public class InputController implements InputProcessor {
    /**for x and y movement, 1 represent up/right and -1 represent down/left
     * for scroll, positive represent scroll up and negative scroll down (may be the opposite, spec unclear).
     *  also, the float scroll might not be 1 or -1, so might need to adjust later in code.*/
    private float y_movement;
    private float x_movement;
    private boolean left_click;
    private boolean right_click;
    private float scroll;

    public float getYMovement(){
        return y_movement;
    }

    public float getXMovement(){
        return x_movement;
    }
    public boolean getLeftClick(){
        return left_click;
    }
    public boolean getRightClick(){
        return right_click;
    }
    public float getScroll(){
        return scroll;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Handle key press events
        return false;
    }

    @Override
    public boolean keyTyped(char keycode) {
        // Handle key press events
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key release events
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Handle touch/mouse down events
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Handle touch/mouse up events
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Handle mouse wheel scrolling
        scroll= amountY;
        return false;
    }

    public InputController(){
        y_movement = 0;
        x_movement = 0;
        left_click = false;
        right_click = false;
        scroll = 0;
    }
    public void readInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            y_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            y_movement = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            x_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            x_movement = -1;
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            left_click = true;
        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            right_click = true;
        }

    }

}
