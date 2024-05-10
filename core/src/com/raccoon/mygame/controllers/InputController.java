package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.*;

import java.util.*;

import com.badlogic.gdx.controllers.Controller;

public class InputController {
    /**
     * for x and y movement, 1 represent up/right and -1 represent down/left
     * for scroll, -1 represent left and 1 represent right scroll direction
     */
    private float y_movement;
    private float x_movement;
    private int scroll;
    private boolean interaction;
    private boolean space;

    private SoundController sounds;
    private boolean reset;
    public boolean click;
    private boolean up;
    private boolean down;
    private boolean pause;
    private boolean oppositePause;

    private boolean shiftHeld;
    private int numIndex;
    private boolean oneThroughFivePressed;

    private boolean camUp;
    private boolean camDown;

    public boolean resume_clicked;

    public float getYMovement() {
        return y_movement;
    }

    public float getXMovement() {
        return x_movement;
    }

    public float getScroll() {
        return scroll;
    }

    public boolean getInteraction() {
        return interaction;
    }

    public boolean getSpace() {
        return space;
    }

    public boolean getReset() {
        return reset;
    }
    public boolean getUp() {return up;};
    public boolean getDown() {return down;};

    public boolean getCamUp(){
        return this.camUp;
    }
    public boolean getCamDown(){
        return this.camDown;
    }

    public boolean getPause() {return pause;}
    public boolean getOppositePause(){
        return oppositePause;
    }

    public float getMouseX(){
        return Gdx.input.getX();
    }
    public float getMouseY(){
        return Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y axis
    }

    public float getAdjustedMouseX(OrthographicCamera camera){
        return Gdx.input.getX() + (camera.position.x - camera.viewportWidth / 2);
    }
    public float getAdjustedMouseY(OrthographicCamera camera){
        return (Gdx.graphics.getHeight() - Gdx.input.getY()) + (camera.position.y - camera.viewportHeight / 2); // Flip Y axis
    }


    public InputController(SoundController s) {
        y_movement = 0;
        x_movement = 0;
        scroll = 0;
        numIndex = 0;
        interaction = false;
        click = false;
        resume_clicked = false;
        shiftHeld = false;
        oneThroughFivePressed = false;
        sounds = s;
    }

    public void readInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y_movement = -1;
        } else {
            y_movement = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x_movement = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x_movement = -1;
        } else {
            x_movement = 0;
        }

        this.shiftHeld = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);

        if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            scroll = -1;
            sounds.switchPlay();
        } else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            scroll = 1;
            sounds.switchPlay();
        } else {
            scroll = 0;
        }

        if (Gdx.input.isKeyPressed(Keys.E)) {
            interaction = true;
        } else {
            interaction = false;
        }

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            space = true;
        } else {
            space = false;
        }
        if (Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            pause = true;
            oppositePause = false;
//            sounds.clickPlay();
        } else {
            pause = false;
            oppositePause = true;
        }

        if (Gdx.input.isKeyPressed(Keys.R)) {
            sounds.cafeeactualstop();
            sounds.storeStop();
            reset = true;
        } else {
            reset = false;
        }
        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            click = true;
//            sounds.clickPlay();
        } else {
            click = false;
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            up = true;
        } else {
            up = false;
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            down = true;
        } else {
            down = false;
        }

        //huh
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y-axis
            if (mouseX >= 500 && mouseX <= 700 && mouseY >= 410 && mouseY <= 470) {
                //System.out.println("happened");
                resume_clicked = true;
            } else {
                resume_clicked = false;
            }
        } else {
            resume_clicked = false;
        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            camUp = true;
        } else {
            up = false;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            down = true;
        } else {
            camDown = false;
        }

        if(Gdx.input.isKeyJustPressed(Keys.NUM_1)){
            oneThroughFivePressed = true;
            numIndex = 0;}
        else if(Gdx.input.isKeyJustPressed(Keys.NUM_2)){
            oneThroughFivePressed = true;
            numIndex = 1;}
        else if(Gdx.input.isKeyJustPressed(Keys.NUM_3)){
            oneThroughFivePressed = true;
            numIndex = 2;}
        else if(Gdx.input.isKeyJustPressed(Keys.NUM_4)){
            oneThroughFivePressed = true;
            numIndex = 3;}
        else if(Gdx.input.isKeyJustPressed(Keys.NUM_5)){
            oneThroughFivePressed = true;
            numIndex = 4;}
        else{
            oneThroughFivePressed = false;
        }

        //15f, 11f, 5f, 1.5f

    }

    public boolean getShiftHeld(){
        return this.shiftHeld;
    }
    public int getNumIndex(){
        return this.numIndex;
    }
    public boolean getOneThroughFivePressed(){
        return this.oneThroughFivePressed;
    }

}
