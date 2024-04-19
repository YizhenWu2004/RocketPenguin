package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;

/**
 * Level select menu.
 *
 * Will add orthographic camera later.
 * */
public class LevelSelectController extends WorldController{
    //the background background
    private Texture background = new Texture("menu/levelselectbackground.png");
    //canvas to draw onto
    private GameCanvas canvas;
    //input controller to use
    private InputController input;


    /**
     * All of the buttons within this level select menu
     * This does not include the buttons within modals.
     * */
    private Array<UIButton> buttons = new Array<>();


    /**
     * All of the modals within this level select menu.
     * See modal class for more info on modals
     * */
    private Array<Modal> modals = new Array<>();

    //set this if we want to go to a level. idk we only have one level
    private boolean goToLevel = false;
    //If one modal is active, we set this to true.
    private boolean aModalIsActive = false;

    /**
     * Creates a new level select controller
     *
     * @param canvas Canvas to draw with
     * @param input InputController to use
     * */
    public LevelSelectController(GameCanvas canvas, InputController input){
        this.canvas = canvas;
        this.input = input;

        //this is the modal for when you click on an individual level entry
        //mostly just for testing now
        Modal selectModal = new Modal("modalOne", 100, 100, new Texture("menu/modalbackground.png"));
        //This is the menu that displays for that modal
        UIButton levelbutton = new UIButton(new Texture("menu/templevelpreview.png"),"levelbutton",0,0,canvas);
        levelbutton.setSX(0.5f);
        levelbutton.setSY(0.5f);
        //add that menu thingy to the modal
        selectModal.addElement(levelbutton);
        //add modal to the list of modals.
        modals.add(selectModal);

        //level one button
        UIButton levelOneButton = new UIButton(new Texture("menu/levelbook.png"),"level1",10,10,canvas);
        //The addbutton method has many overloads. Please see them below.
        addButton(levelOneButton,
                ()-> {
//          //on click
            System.out.println(levelOneButton.getID());
            selectModal.setActive(true);
                    },
                ()->{
            //on hover
            levelOneButton.setSX(1.05f);
            levelOneButton.setSY(1.05f);
        }, ()->{
            //on un-hover
            levelOneButton.resetStyleProperties();
        }


        );

        UIButton levelTwoButton = new UIButton(new Texture("menu/levelbook.png"),"level2",420,10,canvas);
        addButton(levelTwoButton, ()-> {
            //on click
            this.goToLevel = true;
            System.out.println(levelTwoButton.getID());
        },()->{
            //on hover
                    levelTwoButton.setSX(1.05f);
                    levelTwoButton.setSY(1.05f);
        }, levelTwoButton::resetStyleProperties //on un-hover
        );

        UIButton levelThreeButton = new UIButton(new Texture("menu/levelbook.png"),"level3",840,10,canvas);
        addButton(levelThreeButton, ()-> {
            //on click
            this.goToLevel = true;
            System.out.println(levelThreeButton.getID());
        },()->{
            //on hover
            levelThreeButton.setSX(1.05f);
            levelThreeButton.setSY(1.05f);
        }, levelThreeButton::resetStyleProperties //on un-hover
        );

    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {
        aModalIsActive = false;
        //if a single modal is active set this to true
        for (Modal modal : modals) {
            if (modal.getActive()) {
                aModalIsActive = true;
                break;
            }
        }
        //check for button state.
        checkButtons();
    }

    public void draw(){
        //draw the background background. Might have to change this later.
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 0.7f, 0.7f);

        //for every button in this scene (excluding those within a modal)
        //draw them
        for(UIButton button : buttons){
            button.draw(canvas);
        }

        //for all active modals in this scene
        //draw them
        for(Modal modal : modals){
            if(modal.getActive())
                modal.draw(canvas);
        }
    }

    public void debug() {

    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * @param hover Action to perform on hover
     * @param unhover Action to perform when not being hovered on
     * */
    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * */
    private void addButton(UIButton button){
        buttons.add(button);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * */
    private void addButton(UIButton button, ButtonAction action){
        buttons.add(button);
        button.setOnClickAction(action);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * @param hover Action to perform on hover
     * */
    private void addButton(UIButton button, ButtonAction action, ButtonHover hover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
    }

    /**
     * Checks if a coordinate is within bounds of a range
     * */
    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    /**
     * For every button in the scene (modal and non modal)
     * Check for clicks, hovers, and unhovers.
     * Call events that should happen in those circumstances.
     * */
    private void checkButtons(){
        //If no modals are active, check state for normal buttons.
        if(!aModalIsActive) {
            for (UIButton button : buttons) {
                float minX = button.getX();
                float maxX = button.getX() + button.getWidth();
                float minY = button.getY();
                float maxY = button.getY() + button.getHeight();
                if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)) {
                    button.setHovered(true);
                    button.onHoverEvent();
                } else {
                    button.setHovered(false);
                    button.onUnhoverEvent();
                }
                //if input is within bounds of button
                if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY) && input.click) {
                    button.setIsClicked(true);
                    button.onClickEvent();
                    button.setIsClicked(false);
                }
            }
        }
        //for all active modals, check (and process) their button states
        for(Modal modal : modals){
            if(modal.getActive()){
                for(UIButton button : modal.getElements()){
                    float minX = button.getX();
                    float maxX = button.getX() + button.getWidth();
                    float minY = button.getY();
                    float maxY = button.getY() + button.getHeight();
                    if(processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)){
                        button.setHovered(true);
                        button.onHoverEvent();
                    }
                    else{
                        button.setHovered(false);
                        button.onUnhoverEvent();
                    }
                    //if input is within bounds of button
                    if(processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY) && input.click){
                        button.setIsClicked(true);
                        button.onClickEvent();
                        button.setIsClicked(false);
                    }
                }
            }
        }
    }
    public boolean checkForGoToLevel(){return this.goToLevel;}
}
