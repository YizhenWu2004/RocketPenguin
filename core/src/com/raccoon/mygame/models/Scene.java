package com.raccoon.mygame.models;

import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.controllers.InputController;
import com.raccoon.mygame.view.GameCanvas;

public class Scene {
    private Array<UIButton> elements = new Array<>();
    private GameCanvas canvas;
    private Array<Modal> modals = new Array<>();
    private InputController input;
    private boolean aModalIsActive = false;
    private boolean goToNextScene = false;

    public Scene(GameCanvas canvas, InputController input){
        this.canvas = canvas;
        this.input = input;
    }
    public void draw(){
        for(UIButton element: elements){
            element.draw(canvas);
        }
    }

    public void update(){
        checkButtons();
    }

    /**
     * For every button in the scene (modal and non modal)
     * Check for clicks, hovers, and unhovers.
     * Call events that should happen in those circumstances.
     * */
    private void checkButtons(){
//        sortButtonsByPriority();
        //If no modals are active, check state for normal buttons.
        if(!aModalIsActive) {
            for (UIButton button : elements) {
                if(!button.getActive()){
                    continue;
                }
                float minX = button.getX();
                float maxX = button.getX() + button.getWidth();
                float minY = button.getY();
                float maxY = button.getY() + button.getHeight();
                if(button.getSticky()){
                    minX = button.getAdjustedX();
                    maxX = button.getAdjustedX() + button.getWidth();
                    minY = button.getAdjustedY();
                    maxY = button.getAdjustedY() + button.getHeight();
                }
                if (processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY)) {
                    button.setHovered(true);
                    button.onHoverEvent();
                } else {
                    button.setHovered(false);
                    button.onUnhoverEvent();
                }
                //if input is within bounds of button
                if (processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY) && input.click) {
                    button.setIsClicked(true);
                    button.onClickEvent();
                    button.setIsClicked(false);
                }

                if(button.getPriority()){
                    return;
                }
            }
        }
        // Current camera position
        float camY = canvas.getCamera().position.y;

        // Modal's fixed position on the screen (make sure to center it correctly)
        float modalY = camY - 360;
        //for all active modals, check (and process) their button states
        for(Modal modal : modals){
            if(modal.getActive()){
                for(UIButton button : modal.getElements()){
                    if(!button.getActive()){
                        continue;
                    }
                    float minX = button.getX();
                    float maxX = button.getX() + button.getWidth();
                    float minY = modalY + button.getY();
                    float maxY = modalY + button.getY() + button.getHeight();
                    if(button.getSticky()){
                        minX = button.getAdjustedX();
                        maxX = button.getAdjustedX() + button.getWidth();
                        minY = button.getAdjustedY();
                        maxY = button.getAdjustedY() + button.getHeight();
                    }
                    if(processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY)){
                        button.setHovered(true);
                        button.onHoverEvent();
                    }
                    else{
                        button.setHovered(false);
                        button.onUnhoverEvent();
                    }
                    //if input is within bounds of button
                    if(processBounds(input.getAdjustedMouseX(canvas.getCamera()), input.getAdjustedMouseY(canvas.getCamera()), minX, maxX, minY, maxY) && input.click){
                        button.setIsClicked(true);
                        button.onClickEvent();
                        button.setIsClicked(false);
                    }
                }
            }
        }
    }
    /**
     * Checks if a coordinate is within bounds of a range
     * */
    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * @param hover Action to perform on hover
     * @param unhover Action to perform when not being hovered on
     * */
    public void addElement(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        elements.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * */
    public void addElement(UIButton button){
        elements.add(button);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * */
    public void addElement(UIButton button, ButtonAction action){
        elements.add(button);
        button.setOnClickAction(action);
    }

    /**
     * Adds a UIButton to the scene.
     * @param button Button to add
     * @param action Action to perform on click
     * @param hover Action to perform on hover
     * */
    public void addElement(UIButton button, ButtonAction action, ButtonHover hover){
        elements.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
    }

    public boolean getGoToNextScene(){
        return this.goToNextScene;
    }
    public void setGoToNextScene(boolean go){
        this.goToNextScene = go;
    }
}
