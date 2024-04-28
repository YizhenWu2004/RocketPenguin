package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.view.GameCanvas;

public class MainMenuController extends WorldController{
    private Texture background = new Texture("menu/fullmenubackground.png");
    private Texture audio_background = new Texture("menu_option/audio.png");
    private Texture settings_background = new Texture("menu_option/settings.png");
    private Texture control_background = new Texture("menu_option/controls.png");
    private Texture cont_hover = new Texture("menu_option/control_button_underline.png");
    private Texture audio_hover = new Texture("menu_option/audio_button_underline.png");
    private Texture back_hover = new Texture("menu_option/back_underline.png");

    private GameCanvas canvas;
    private InputController input;

    private Array<UIButton> buttons = new Array<>();
    private Array<UIButton> setting_buttons = new Array<>();
    private Array<UIButton> audio_buttons = new Array<>();
    private Array<UIButton> control_buttons = new Array<>();
    private Array<Modal> modals = new Array<>();

    private boolean goToLevelSelect = false;
    private boolean goToOptionsSelect = false;
    private boolean exit = false;

    //new stuff i add
    public boolean on_main = false;
    public boolean on_settings = false;
    public boolean on_audio = false;
    public boolean on_control = false;

    private boolean aModalIsActive = false;
    private SaveController saveController;
    private LevelSelectController levelSelectController;

    public MainMenuController(GameCanvas canvas, InputController input, SaveController saveController, LevelSelectController levelSelect){
        this.canvas = canvas;
        this.input = input;
        this.saveController = saveController;
        this.levelSelectController = levelSelect;

        UIButton play = new UIButton(new Texture("menu/play.png"),"play",20,330, 0.5f,0.5f,canvas);
        addButton(play, ()-> {
            this.goToLevelSelect = true;
            System.out.println(play.getID());
        }, ()->{
            play.setSX(0.6f);
            play.setSY(0.6f);
        }, play::resetStyleProperties,buttons);

        UIButton options = new UIButton(new Texture("menu/options.png"),"options", 20,230,0.5f,0.5f,canvas);
        addButton(options, ()-> {
            this.goToOptionsSelect = true;
            this.on_settings = true;
            this.on_main = false;
            System.out.println(options.getID());
        }, ()->{
            options.setSX(0.6f);
            options.setSY(0.6f);
        },options::resetStyleProperties,buttons);


        UIButton exit = new UIButton(new Texture("menu/exit.png"),"exit",20,130,0.5f,0.5f,canvas);
        addButton(exit, ()-> {
            this.exit = true;
            System.out.println(exit.getID());
        },()->{
            exit.setSX(0.6f);
            exit.setSY(0.6f);
            },exit::resetStyleProperties,buttons);


        UIButton cont = new UIButton(new Texture("menu_option/control_button_normal.png"),"exit",230,430,canvas);
        addButton(cont, ()-> {
            this.on_control = true;
            this.on_settings = false;
        },()->{
            //cont.setSX(1.1f);
            //cont.setSY(1.1f);
            cont.setTexture(cont_hover);
        },cont::resetStyleProperties,setting_buttons);

        UIButton aud = new UIButton(new Texture("menu_option/audio_button_normal.png"),"exit",270,310,canvas);
        addButton(aud, ()-> {
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            aud.setSX(1.1f);
//            aud.setSY(1.1f);
            aud.setTexture(audio_hover);
        },aud::resetStyleProperties,setting_buttons);

        UIButton back1 = new UIButton(new Texture("menu_option/back_setting.png"),"exit",290,180,canvas);
        addButton(back1, ()-> {
            this.on_main = true;
            this.on_settings = false;
        },()->{
//            back1.setSX(1.1f);
//            back1.setSY(1.1f);
            back1.setTexture(back_hover);
        },back1::resetStyleProperties,setting_buttons);

        UIButton back2 = new UIButton(new Texture("menu_option/back.png"),"exit",150,150,canvas);
        addButton(back2, ()-> {
            this.on_control = false;
            this.on_settings = true;
        },()->{
            back2.setSX(0.8f);
            back2.setSY(0.8f);
            //back1.setTexture(cont_hover);
        },back2::resetStyleProperties,control_buttons);
        back2.setDefaultScale(0.7f,0.7f);

        UIButton back3 = new UIButton(new Texture("menu_option/back.png"),"exit",160,150,canvas);
        addButton(back3, ()-> {
            this.on_audio = false;
            this.on_settings = true;
        },()->{
            back3.setSX(1.1f);
            back3.setSY(1.1f);
            //back1.setTexture(cont_hover);
        },back3::resetStyleProperties,audio_buttons);

        UIButton save = new UIButton(new Texture("menu_option/save.png"),"exit",440,160,canvas);
        addButton(save, ()-> {
            this.on_audio = false;
            this.on_settings = true;
        },()->{
            save.setSX(1.1f);
            save.setSY(1.1f);
            //back1.setTexture(cont_hover);
        },save::resetStyleProperties,audio_buttons);

        Modal deletesure = new Modal("deletesure", 0,0, new Texture("menu/modalbackground.png"));
        UIButton deletetext = new UIButton(new Texture("menu/deleteconfirmation.png"),"deleteconfirm",0,0,canvas);
        deletetext.setSX(0.65f);
        deletetext.setSY(0.65f);

        UIButton confirmtext = new UIButton(new Texture("menu/confirmtext.png"),"confirm",380,320,canvas);
        confirmtext.setSX(0.6f);
        confirmtext.setSY(0.6f);

        UIButton yes = new UIButton(new Texture("menu/yes.png"),"yes",450,210,canvas);
        yes.setOnClickAction(()->{this.saveController.deleteSaveFile();});
        yes.setOnHoverAction(()->{yes.setTexture(new Texture("menu/yeshovered.png"));});
        yes.setOnUnhoverAction(()->{yes.resetStyleProperties();        yes.setSX(0.6f);
            yes.setSY(0.6f);});
        yes.setSX(0.6f);
        yes.setSY(0.6f);

        UIButton no = new UIButton(new Texture("menu/no.png"),"no",740,207,canvas);
        no.setOnClickAction(()->{deletesure.setActive(false);});
        no.setOnHoverAction(()->{no.setTexture(new Texture("menu/nohovered.png"));});
        no.setOnUnhoverAction(()->{no.resetStyleProperties();        no.setSX(0.6f);
            no.setSY(0.6f);});
        no.setSX(0.6f);
        no.setSY(0.6f);

        deletesure.addElement(deletetext);
        deletesure.addElement(confirmtext);
        deletesure.addElement(yes);
        deletesure.addElement(no);
        modals.add(deletesure);

        UIButton deletesave = new UIButton(new Texture("menu/deletesave.png"),"deletesave",1020,10,canvas);
        addButton(deletesave, ()-> {
            deletesure.setActive(true);
        },()->{
            deletesave.setSX(1.1f);
            deletesave.setSY(1.1f);
            //back1.setTexture(cont_hover);
        },()->{deletesave.resetStyleProperties();});
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
        if(on_main){
            checkButtons(buttons);
        } else if (on_settings){
            checkButtons(setting_buttons);
        } else if (on_audio){
            checkButtons(audio_buttons);
        } else if (on_control){
            checkButtons(control_buttons);
        }

    }

    public void draw(){
        if (on_main){
            canvas.draw(background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 0.7f, 0.7f);
//        canvas.draw(debug, Color.WHITE, 0, 0,
//                500, 400, 0.0f, 1f, 1f);

            for(UIButton button : buttons){
                button.draw(canvas);
            }
            for(Modal modal : modals){
                if(modal.getActive())
                    modal.draw(canvas);
            }
        } else if (on_settings){
            canvas.draw(background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 0.7f, 0.7f);
            canvas.draw(settings_background, Color.WHITE, 0, 0,
                    -250, 0, 0.0f, 1f, 1f);
            for(UIButton button : setting_buttons){
                button.draw(canvas);
            }
        } else if (on_audio){
            canvas.draw(background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 0.7f, 0.7f);
            canvas.draw(audio_background, Color.WHITE, 0, 0,
                    -250, 0, 0.0f, 1f, 1f);
            for(UIButton button : audio_buttons){
                button.draw(canvas);
            }
        } else if (on_control){
            canvas.draw(background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 0.7f, 0.7f);
            canvas.draw(control_background, Color.WHITE, 0, 0,
                    -250, 0, 0.0f, 1f, 1f);
            for(UIButton button : control_buttons){
                button.draw(canvas);
            }
        }
    }

    public void debug() {

    }

    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover, Array<UIButton> b){
        b.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }
    private void addButton(UIButton button){
        buttons.add(button);
    }
    private void addButton(UIButton button, ButtonAction action){
        buttons.add(button);
        button.setOnClickAction(action);
    }
    private void addButton(UIButton button, ButtonAction action, ButtonHover hover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
    }
    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkButtons(Array<UIButton> buttons){
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
    public boolean checkForGoToLevelSelect(){return this.goToLevelSelect;}
    public void setForGoToLevelSelect(boolean a){this.goToLevelSelect = a;}
    public boolean checkForGoToOptions(){return this.goToOptionsSelect;}
    public boolean checkForExit(){return this.exit;}
}
