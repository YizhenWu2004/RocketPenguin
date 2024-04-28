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
    private final Texture napkinbox = new Texture("menu/napkinbox.png");
    private final Texture week = new Texture("menu/week.png");
    //canvas to draw onto
    private GameCanvas canvas;
    //input controller to use
    private final InputController input;

    private final ScrollController scroller = new ScrollController();

    private SaveController saveController;


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

    private Array<Integer> shiftingYs = new Array<>();
    private int cameraShiftI = 0;
    private boolean shouldIScroll = false;
    private float  targetCameraY;
    private float scrollAmount = 0;

    private boolean goToMainMenu = false;

    /**
     * Creates a new level select controller
     *
     * @param canvas Canvas to draw with
     * @param input InputController to use
     * */
    public LevelSelectController(GameCanvas canvas, InputController input, LevelLoader loader, SaveController saveController){
        this.canvas = canvas;
        this.input = input;
        this.saveController = saveController;

        System.out.println(loader.getLevels().size);
        generateLevelSelectors(loader.getLevels().size);

        UIButton up = new UIButton(new Texture("menu/upunhovered.png"),"up", 1100,600,1,1,canvas,canvas.getCamera(),true);
        up.setDefaultScale(0.5f,0.5f);
        addButton(up, ()->{upCameraShiftI();System.out.println(cameraShiftI);}, ()->{up.setSX(0.6f);up.setSY(0.6f);up.setTexture(new Texture("menu/uphovered.png"));}, up::resetStyleProperties);

        UIButton down = new UIButton(new Texture("menu/downunhovered.png"),"down", 1100,20,1,1,canvas,canvas.getCamera(),true);
        down.setDefaultScale(0.5f,0.5f);
        addButton(down, ()->{downCameraShiftI();System.out.println(cameraShiftI);}, ()->{down.setSX(0.6f);down.setSY(0.6f);down.setTexture(new Texture("menu/downhovered.png"));}, down::resetStyleProperties);

        UIButton backtotitle = new UIButton(new Texture("menu/titlebackunhovered.png"), "titleback", 10,10,1,1,canvas,canvas.getCamera(),true);
        backtotitle.setDefaultScale(0.6f, 0.6f);
        addButton(backtotitle, ()->{this.goToMainMenu = true;},()->{backtotitle.setTexture(new Texture("menu/titlebackhovered.png"));},()->{backtotitle.resetStyleProperties();});

        this.shiftingYs = makeBackgroundPoints(loader.getLevels().size);
        this.targetCameraY = canvas.getCamera().position.y;
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {

        //this code is for scrolling via buttons
        //I LOVE LERP
        float lerpFactor = 0.1f;

        System.out.println(cameraShiftI);
        scrollAmount = scroller.getScroll();
        if (scrollAmount != 0) {
            canvas.getCamera().position.y -= scrollAmount * 10;
            shouldIScroll = false;
        }

        if(shouldIScroll) {
            if (Math.abs(canvas.getCamera().position.y - targetCameraY) > 1) {
                canvas.getCamera().position.y += (targetCameraY - canvas.getCamera().position.y) * lerpFactor;
                canvas.getCamera().update();
            } else {
                canvas.getCamera().position.y = targetCameraY;
                setShouldIScroll(false);
            }
        }

        clampCameraPosition();
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
        for(int i = 0; i < shiftingYs.size; i++) {
            if(i == 0){
                canvas.draw(background, Color.WHITE, 0, 0,
                        0, shiftingYs.get(i), 0.0f, 0.7f, 0.7f);
                canvas.draw(napkinbox,Color.WHITE, 0,0,670,shiftingYs.get(i)+600,0,0.6f,0.6f);
                canvas.draw(week,Color.WHITE, 0,0,740,shiftingYs.get(i)+630,0,0.6f,0.6f);
                drawSimpleNumber(i, 870,shiftingYs.get(i)+601,0.8f,0.8f);
                continue;
            }
            canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
                    0, shiftingYs.get(i), 0.0f, 0.7f, 0.7f);
            canvas.draw(napkinbox,Color.WHITE, 0,0,670,shiftingYs.get(i)+600,0,0.6f,0.6f);
            canvas.draw(week,Color.WHITE, 0,0,740,shiftingYs.get(i)+630,0,0.6f,0.6f);
            drawSimpleNumber(i, 870,shiftingYs.get(i)+601,0.8f,0.8f);
        }
//        canvas.draw(background, Color.WHITE, 0, 0,
//                0, -0, 0.0f, 0.7f, 0.7f);
//        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
//                0, -757, 0.0f, 0.7f, 0.7f);
//        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
//                0, -1440, 0.0f, 0.7f, 0.7f);
//        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
//                0, -2160, 0.0f, 0.7f, 0.7f);
//        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
//                0, -2880, 0.0f, 0.7f, 0.7f);
//        canvas.draw(backgroundBlank, Color.WHITE, 0, 0,
//                0, -3600, 0.0f, 0.7f, 0.7f);

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

    /**
     * amount of levels divided by 3
     * @return the scroll y points of each background
     * last y in this array is the maximum camera point
     * */
    private Array<Integer> makeBackgroundPoints(int amountOfLevels){
        int spacing = 0;
        int amountOfBackgroundsToDraw = (int)(amountOfLevels/3);
        Array<Integer> ypoints = new Array<>();
        for(int i = 0; i <= amountOfBackgroundsToDraw+2; i++){
            ypoints.add(spacing);
            spacing -= 757;
        }
        return ypoints;
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

        Array<UIButton> multipleNums = createMultipleNumbers((saveController.getKeyvaluepairs().get(num)),655,  285,0.5f,0.5f);
        Array<UIButton> stars = generateStars((saveController.getKeyvaluepairs().get(num)),540,  200,0.7f,0.7f);


        //add the buttons to the modal thingy to the modal
        selectModal.addElement(booklet);
        selectModal.addElement(back);
        selectModal.addElement(start);
        selectModal.addElement(dayNumber);
        for (UIButton butt:
                multipleNums) {
            selectModal.addElement(butt);
        }
        for (UIButton star:
                stars) {
            selectModal.addElement(star);
        }
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
            UIButton levelButton = new UIButton(new Texture("menu/levelbooklet.png"),is,90 + (i*400),120,0.7f,0.7f,canvas);
            UIButton dayNumber = createNumberElement(i, 270,260,1,1);
            Array<UIButton> stars = generateStars((saveController.getKeyvaluepairs().get(i)),118,  170,1f,1f);

            levelButton.addChild(dayNumber);
            for(UIButton star: stars){
                levelButton.addChild(star);
            }
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
                        levelButton.setSX(0.75f);
                        levelButton.setSY(0.75f);
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

    //for modal
    private void drawSimpleNumber(int num, int x, int y, float sx, float sy){
        String is = Integer.toString(num);
        canvas.draw(new Texture("menu/" + is + ".png"),Color.BLACK,0,0,x,y,0,sx,sy);
    }

    private Array<UIButton> createMultipleNumbers(int num, int x, int y, float sx, float sy){
        Array<UIButton> elements = new Array<>();
        String numstring = Integer.toString(num);
        for(int i = 0; i < numstring.length(); i++){
            String currentchar = numstring.substring(i,i+1);
            UIButton number = new UIButton(new Texture("menu/" + currentchar + ".png"),currentchar + "multnum", x + (i * 25), y, sx,sy,canvas);
            number.setCOLOR(Color.BLACK);
            elements.add(number);
        }
        return elements;
    }
    private Array<UIButton> generateStars(int score, int x, int y, float sx, float sy){
        Array<UIButton> elements = new Array<>();
        Texture starFilled = new Texture("menu/filledstar.png");
        Texture starEmpty = new Texture("menu/unfilledstar.png");
        int[] star_req = new int[]{50,75,100};
        if(score < star_req[0]){
            for(int i = 0; i < 3; i++){
                UIButton star = new UIButton(starEmpty,"unstar",x + (i*70),y,sx,sy, canvas);
                elements.add(star);
            }
        }
        else if(score < star_req[1]){
            int checkedStars = 1;
            for(int i = 0; i < 3; i++){
                if(i >= checkedStars){
                    UIButton star = new UIButton(starEmpty,"unstar",x + (i*70),y,sx,sy, canvas);
                    elements.add(star);
                } else {
                    UIButton stary = new UIButton(starFilled, "unstar", x + (i * 70), y, sx, sy, canvas);
                    elements.add(stary);
                }
            }
        }
        else if(score < star_req[2]){
            int checkedStars = 2;
            for(int i = 0; i < 3; i++){
                if(i == checkedStars){
                    UIButton star = new UIButton(starEmpty,"unstar",x + (i*70),y,sx,sy, canvas);
                    elements.add(star);
                }else {
                    UIButton stary = new UIButton(starFilled, "unstar", x + (i * 70), y, sx, sy, canvas);
                    elements.add(stary);
                }
            }
        }
        else{
            int checkedStars = 3;
            for(int i = 0; i < 3; i++){
                UIButton star = new UIButton(starFilled,"unstar",x + (i*70),y,sx,sy, canvas);
                elements.add(star);
            }
        }
        return elements;
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

    private void upCameraShiftI(){
        if (this.cameraShiftI > 0) {
            this.cameraShiftI -= 1;
            performSmoothScroll();
        }
    }
    private void downCameraShiftI(){
        if (this.cameraShiftI < shiftingYs.size - 1) {
            this.cameraShiftI += 1;
            performSmoothScroll();
        }
    }

    private void performSmoothScroll() {
        if (cameraShiftI < 0)
            cameraShiftI = 0;
        else if (cameraShiftI >= shiftingYs.size)
            cameraShiftI = shiftingYs.size - 1;
        this.targetCameraY = shiftingYs.get(cameraShiftI) + canvas.getCamera().viewportHeight / 2f;
        shouldIScroll = true;
    }
    private void setShouldIScroll(boolean a){
        this.shouldIScroll = a;
    }

    private void clampCameraPosition(){
        if (canvas.getCamera().position.y < shiftingYs.peek() + canvas.getCamera().viewportHeight / 2) {
            canvas.getCamera().position.y = shiftingYs.peek() + canvas.getCamera().viewportHeight / 2;
        }
        if (canvas.getCamera().position.y > shiftingYs.first() + canvas.getCamera().viewportHeight / 2) {
            canvas.getCamera().position.y = shiftingYs.first() + canvas.getCamera().viewportHeight / 2;
        }

    }

    public boolean checkForGoToMainMenu(){return this.goToMainMenu;}
    public void setForGoToMainMenu(boolean a){
        this.goToMainMenu = a;
    }
}
