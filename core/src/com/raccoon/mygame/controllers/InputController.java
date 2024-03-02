package com.raccoon.mygame.controllers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.*;
import java.util.*;
import com.badlogic.gdx.controllers.Controller;

public class InputController{
    /**for x and y movement, 1 represent up/right and -1 represent down/left
     * for scroll, -1 represent left and 1 represent right scroll direction*/
    private float y_movement;
    private float x_movement;
    private float scroll;
    private boolean interaction;
    private boolean space;


    public float getYMovement(){
        return y_movement;
    }

    public float getXMovement(){
        return x_movement;
    }
    public float getScroll(){
        return scroll;
    }

    public boolean getInteraction(){return interaction;}
    public boolean getSpace(){return space;}


    public InputController(){
        y_movement = 0;
        x_movement = 0;
        scroll = 0;
        interaction = false;
    }
    public void readInput(){

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            y_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            y_movement = -1;
        } else {
            y_movement = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            x_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            x_movement = -1;
        } else {
            x_movement = 0;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            scroll = -1;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            scroll = 1;
        } else {
            scroll = 0;
        }

        if (Gdx.input.isKeyPressed(Keys.E)){
            interaction = true;
        } else {
            interaction = false;
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE)){
            space = true;
        } else {
            space = false;
        }

    }

}
