package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
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
    private final Texture background = new Texture("menu/levelselectbackground.png");
    private final Texture backgroundBlank = new Texture("menu/levelselectbackgroundblank.png");
    //canvas to draw onto
    private GameCanvas canvas;
    //input controller to use
    private final InputController input;

    private final ScrollController scroller = new ScrollController();


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

    private int levelToGoTo = 0;

    //If one modal is active, we set this to true.
    private boolean aModalIsActive = false;

    private LevelLoader loader;

    /**
     * Creates a new level select controller
     *
     * @param canvas Canvas to draw with
     * @param input InputController to use
     * */
    public LevelSelectController(GameCanvas canvas, InputController input, LevelLoader loader){
        this.canvas = canvas;
        this.input = input;

//        constructBooklet("1");
//        constructBooklet("2");
//        constructBooklet("3");
//
//        //level one button
//        UIButton levelOneButton = new UIButton(new Texture("menu/levelbooklet.png"),"level1",10,30,0.8f,0.8f,canvas);
//        //The addbutton method has many overloads. Please see them below.
//        addButton(levelOneButton,
//                ()-> {
////          //on click
//            System.out.println(levelOneButton.getID());
//            findModalOfID("1").setActive(true);
//                    },
//                ()->{
//            //on hover
//            levelOneButton.setSX(0.9f);
//            levelOneButton.setSY(0.9f);
//        }, ()->{
//            //on un-hover
//            levelOneButton.resetStyleProperties();
//        }
//
//
//        );
//
//        UIButton levelTwoButton = new UIButton(new Texture("menu/levelbooklet.png"),"level2",420,30,0.8f,0.8f,canvas);
//        addButton(levelTwoButton, ()-> {
//            //on click
//                    System.out.println(levelOneButton.getID());
//                    findModalOfID("2").setActive(true);
//        },()->{
//            //on hover
//                    levelTwoButton.setSX(0.9f);
//                    levelTwoButton.setSY(0.9f);
//        }, levelTwoButton::resetStyleProperties //on un-hover
//        );
//
//        UIButton levelThreeButton = new UIButton(new Texture("menu/levelbooklet.png"),"level3",840,30,0.8f,0.8f,canvas);
//        addButton(levelThreeButton, ()-> {
//            //on click
//                    System.out.println(levelOneButton.getID());
//                    findModalOfID("3").setActive(true);
//        },()->{
//            //on hover
//            levelThreeButton.setSX(0.9f);
//            levelThreeButton.setSY(0.9f);
//        }, levelThreeButton::resetStyleProperties //on un-hover
//        );
        System.out.println(loader.getLevels().size);
        generateLevelSelectors(loader.getLevels().size);

    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {

        //camera scrolling
        float cameraSpeed = 10;
        int scrollAmount = scroller.getScroll();
        canvas.getCamera().position.y -= scrollAmount * cameraSpeed;
        if(canvas.getCamera().position.y < -3000)
            canvas.getCamera().position.y = -3000;
        if(canvas.getCamera().position.y > 360)
            canvas.getCamera().position.y = 360;
        canvas.getCamera().update();
        scroller.resetScroll();

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
        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                0, -720, 0.0f, 0.7f, 0.7f);
        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                0, -1440, 0.0f, 0.7f, 0.7f);
        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                0, -2160, 0.0f, 0.7f, 0.7f);
        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                0, -2880, 0.0f, 0.7f, 0.7f);
        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                0, -3600, 0.0f, 0.7f, 0.7f);

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

    public void setGoToLevel(boolean b){
        goToLevel = b;
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
                    float minX = button.getX();
                    float maxX = button.getX() + button.getWidth();
                    float minY = modalY + button.getY();
                    float maxY = modalY + button.getY() + button.getHeight();
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
    public void constructBooklet(String id){
        int num = Integer.parseInt(id);
        //this is the modal for when you click on an individual level entry
        //mostly just for testing now
        Modal selectModal = new Modal(id, 125, 75, new Texture("menu/modalbackground.png"));
        //This is the menu that displays for that modal
        UIButton booklet = new UIButton(new Texture("menu/booklet.png"),"levelbutton",0,0,0.5f, 0.5f, canvas);

        UIButton back = new UIButton(new Texture("menu/back.png"), "back", 190, 75, 0.5f, 0.5f,canvas);
        back.setOnClickAction(()->{selectModal.setActive(false);});
        back.setOnHoverAction(()->{back.setSX(0.6f);back.setSY(0.6f);});
        back.setOnUnhoverAction(()->{back.resetStyleProperties();});

        UIButton start = new UIButton(new Texture("menu/start.png"), "start", 650, 75, 0.5f, 0.5f,canvas);
        start.setOnClickAction(()->{selectModal.setActive(false);this.goToLevel=true;this.setLevelToGoTo(num);});
        start.setOnHoverAction(()->{start.setSX(0.6f);start.setSY(0.6f);});
        start.setOnUnhoverAction(()->{start.resetStyleProperties();});

        UIButton dayNumber = createNumberElement(num,270, 415, 0.5f, 0.5f);
        dayNumber.setCOLOR(Color.BLACK);

        //add the buttons to the modal thingy to the modal
        selectModal.addElement(booklet);
        selectModal.addElement(back);
        selectModal.addElement(start);
        selectModal.addElement(dayNumber);
        //add modal to the list of modals.
        modals.add(selectModal);
    }
    public boolean checkForGoToLevel(){return this.goToLevel;}
    public Modal findModalOfID(String id){
        for (Modal modal:
             modals) {
            if(modal.getId().equals(id)){
                return modal;
            }
        }
        //fake modal
        return new Modal("-1", 0,0,new Texture("menu/filledstar.png"));
    }

    private void generateLevelSelectors(int numberOfLevels){
        for(int i = 0; i < numberOfLevels; i++){
            String is = Integer.toString(i);

            //level one button
            UIButton levelButton = new UIButton(new Texture("menu/levelbooklet.png"),is,10 + (i*400),30,0.8f,0.8f,canvas);
            UIButton dayNumber = createNumberElement(i, 270,260,1,1);
            levelButton.addChild(dayNumber);
            //The addbutton method has many overloads. Please see them below.
            //on un-hover
            addButton(levelButton,
                    ()-> {
//          //on click
                        System.out.println(levelButton.getID());
                        findModalOfID(is).setActive(true);
                    },
                    ()->{
                        //on hover
                        levelButton.setSX(0.9f);
                        levelButton.setSY(0.9f);
                    }, levelButton::resetStyleProperties
            );

            constructBooklet(is);
        }
    }

    //for modal
    private UIButton createNumberElement(int num, int x, int y, float sx, float sy){
        String is = Integer.toString(num);
        //270, 415, 0.5f, 0.5f
        UIButton number = new UIButton(new Texture("menu/" + is + ".png"),is + "num", x, y, sx,sy,canvas);
        return number;
    }

    private Array<UIButton> createMultipleNumbers(int num, int x, int y, float sx, float sy){
        int digits = Integer.toString(num).length();

        //empty
        return new Array<UIButton>();
    }

    private void goToLevel(StoreController store, int level, Inventory inv){
        store.setLevel(loader.getLevels().get(level), inv);
    }
    private void setLevelToGoTo(int level){
        this.levelToGoTo = level;
    }
    public int getLevelToGoTo(){
        return this.levelToGoTo;
    }
}
