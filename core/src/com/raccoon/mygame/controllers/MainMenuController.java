package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.view.GameCanvas;

public class MainMenuController extends WorldController{
    private SoundController sounds;
    private Texture background;
    private Texture audio_background ;
    private Texture settings_background ;
    private Texture control_background;
    private Texture cont_hover ;
    private Texture audio_hover;
    private Texture back_hover;

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
    private LevelLoader loader;

    public Texture play_b;
    public Texture option;
    public Texture exit_b;
    public Texture control_b_normal;
    public Texture audio_b_normal;
    public Texture back_setting;
    public Texture back;
    public Texture save_b;
    public Texture modalbackground;
    public Texture deleteconfirmation;
    public Texture confirmtext;
    public Texture yes;
    public Texture yeshover;
    public Texture no;
    public Texture nohover;
    public Texture deletesave;

    public MainMenuController(GameCanvas canvas, InputController input, SaveController saveController, LevelSelectController levelSelect, SoundController s, AssetDirectory directory, LevelLoader levelLoader){
        background = directory.getEntry("m_fullbackground", Texture.class);
        audio_background = directory.getEntry("mo_audio", Texture.class);
        settings_background = directory.getEntry("mo_settings", Texture.class);
        control_background = directory.getEntry("mo_controls", Texture.class);
        cont_hover = directory.getEntry("mo_control_b_underline", Texture.class);
        audio_hover = directory.getEntry("mo_audio_b_underline", Texture.class);
        back_hover = directory.getEntry("mo_back_underline", Texture.class);
        play_b = directory.getEntry("m_play", Texture.class);
        option = directory.getEntry("m_options", Texture.class);
        exit_b = directory.getEntry("m_exit", Texture.class);
        control_b_normal = directory.getEntry("mo_control_b_normal", Texture.class);
        audio_b_normal = directory.getEntry("mo_audio_b_normal", Texture.class);
        back_setting = directory.getEntry("mo_back_setting", Texture.class);
        back = directory.getEntry("mo_back", Texture.class);
        save_b= directory.getEntry("mo_save", Texture.class);
        modalbackground= directory.getEntry("m_modalbackground", Texture.class);
        deleteconfirmation= directory.getEntry("m_deleteconfirmation", Texture.class);
        confirmtext = directory.getEntry("m_confirmtext", Texture.class);
        yes= directory.getEntry("m_yes", Texture.class);
        yeshover= directory.getEntry("m_yeshovered", Texture.class);
        no= directory.getEntry("m_no", Texture.class);
        nohover= directory.getEntry("m_nohovered", Texture.class);
        deletesave = directory.getEntry("m_deletesave", Texture.class);




        this.canvas = canvas;
        this.input = input;
        this.saveController = saveController;
        this.levelSelectController = levelSelect;
        this.loader = levelLoader;

        sounds = s;

        UIButton play = new UIButton(play_b,"play",20,330, 0.5f,0.5f,canvas);
        addButton(play, ()-> {
            sounds.clickPlay();
            this.goToLevelSelect = true;
        }, ()->{
            play.setSX(0.6f);
            play.setSY(0.6f);
        }, play::resetStyleProperties,buttons);

        UIButton options = new UIButton(option,"options", 20,230,0.5f,0.5f,canvas);
        addButton(options, ()-> {
            sounds.clickPlay();
            this.goToOptionsSelect = true;
            this.on_settings = true;
            this.on_main = false;
        }, ()->{
            options.setSX(0.6f);
            options.setSY(0.6f);
        },options::resetStyleProperties,buttons);


        UIButton exit = new UIButton(exit_b,"exit",20,130,0.5f,0.5f,canvas);
        addButton(exit, ()-> {
            sounds.clickPlay();
            this.exit = true;
        },()->{
            exit.setSX(0.6f);
            exit.setSY(0.6f);
            },exit::resetStyleProperties,buttons);

        UIButton cont = new UIButton(control_b_normal,"exit",230,430,canvas);
        addButton(cont, ()-> {
            sounds.clickPlay();
            this.on_control = true;
            this.on_settings = false;
        },()->{
            //cont.setSX(1.1f);
            //cont.setSY(1.1f);
            cont.setTexture(cont_hover);
        },cont::resetStyleProperties,setting_buttons);

        UIButton aud = new UIButton(audio_b_normal,"exit",270,310,canvas);
        addButton(aud, ()-> {
            sounds.clickPlay();
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            aud.setSX(1.1f);
//            aud.setSY(1.1f);
            aud.setTexture(audio_hover);
        },aud::resetStyleProperties, setting_buttons);

        UIButton incmusic = new UIButton(audio_b_normal,"exit",510,480,canvas);
        addButton(incmusic, ()-> {
            sounds.incmusic();
            sounds.clickPlay();
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            System.out.println("inc hovered");
//            inc.setSX(1.1f);
//            inc.setSY(1.1f);
            incmusic.setTexture(audio_hover);
        },incmusic::resetStyleProperties, audio_buttons);

        UIButton decmusic = new UIButton(audio_b_normal,"exit",100,480,canvas);
        addButton(decmusic, ()-> {
            sounds.decmusic();
            sounds.clickPlay();
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            System.out.println("inc hovered");
//            inc.setSX(1.1f);
//            inc.setSY(1.1f);
            decmusic.setTexture(audio_hover);
        },decmusic::resetStyleProperties, audio_buttons);

        UIButton incsfx = new UIButton(audio_b_normal,"exit",510,310,canvas);
        addButton(incsfx, ()-> {
            sounds.clickPlay();
            sounds.incsfx();
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            System.out.println("inc hovered");
//            inc.setSX(1.1f);
//            inc.setSY(1.1f);
            incsfx.setTexture(audio_hover);
        },incsfx::resetStyleProperties, audio_buttons);

        UIButton decsfx = new UIButton(audio_b_normal,"exit",100,310,canvas);
        addButton(decsfx, ()-> {
            sounds.clickPlay();
            sounds.decsfx();
            this.on_audio = true;
            this.on_settings = false;
        },()->{
//            System.out.println("inc hovered");
//            inc.setSX(1.1f);
//            inc.setSY(1.1f);
            decsfx.setTexture(audio_hover);
        }, decsfx::resetStyleProperties, audio_buttons);

        UIButton back1 = new UIButton(back_setting,"exit",290,180,canvas);
        addButton(back1, ()-> {
            sounds.clickPlay();
            this.on_main = true;
            this.on_settings = false;
        },()->{
//            back1.setSX(1.1f);
//            back1.setSY(1.1f);
            back1.setTexture(back_hover);
        },back1::resetStyleProperties,setting_buttons);

        UIButton back2 = new UIButton(back,"exit",150,150,canvas);
        addButton(back2, ()-> {
            sounds.clickPlay();
            this.on_control = false;
            this.on_settings = true;
        },()->{
            back2.setSX(0.8f);
            back2.setSY(0.8f);
            //back1.setTexture(cont_hover);
        },back2::resetStyleProperties,control_buttons);
        back2.setDefaultScale(0.7f,0.7f);

        UIButton back3 = new UIButton(back,"exit",160,150,canvas);
        addButton(back3, ()-> {
            sounds.clickPlay();
            this.on_audio = false;
            this.on_settings = true;
        },()->{
            back3.setSX(1.1f);
            back3.setSY(1.1f);
            //back1.setTexture(cont_hover);
        },back3::resetStyleProperties,audio_buttons);

        UIButton save = new UIButton(save_b,"exit",440,160,canvas);
        addButton(save, ()-> {
            sounds.clickPlay();
            this.on_audio = false;
            this.on_settings = true;
        },()->{
            save.setSX(1.1f);
            save.setSY(1.1f);
            //back1.setTexture(cont_hover);
        },save::resetStyleProperties,audio_buttons);

        Modal deletesure = new Modal("deletesure", 0,0, modalbackground);
        UIButton deletetext = new UIButton(deleteconfirmation,"deleteconfirm",0,0,canvas);
        deletetext.setSX(0.65f);
        deletetext.setSY(0.65f);

        UIButton confirmtext = new UIButton(this.confirmtext,"confirm",380,320,canvas);
        confirmtext.setSX(0.6f);
        confirmtext.setSY(0.6f);

        UIButton yes = new UIButton(this.yes,"yes",450,210,canvas);
        yes.setOnClickAction(()->{sounds.clickPlay();this.saveController.deleteSaveFile();levelSelectController.generateLevelSelectors(loader.getLevels().size);});
        yes.setOnHoverAction(()->{yes.setTexture(yeshover);});
        yes.setOnUnhoverAction(()->{yes.resetStyleProperties();        yes.setSX(0.6f);
            yes.setSY(0.6f);});
        yes.setSX(0.6f);
        yes.setSY(0.6f);

        UIButton no = new UIButton(this.no,"no",740,207,canvas);
        no.setOnClickAction(()->{sounds.clickPlay();deletesure.setActive(false);});
        no.setOnHoverAction(()->{no.setTexture(nohover);});
        no.setOnUnhoverAction(()->{no.resetStyleProperties();
            no.setSX(0.6f);
            no.setSY(0.6f);});
        no.setSX(0.6f);
        no.setSY(0.6f);

        deletesure.addElement(deletetext);
        deletesure.addElement(confirmtext);
        deletesure.addElement(yes);
        deletesure.addElement(no);
        modals.add(deletesure);

        UIButton deletesave = new UIButton(this.deletesave,"deletesave",1020,10,canvas);
        addButton(deletesave, ()-> {
            sounds.clickPlay();
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
//            System.out.println("on audio");
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
//                System.out.println("audio buttons");
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
