package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.ButtonHover;
import com.raccoon.mygame.models.ButtonUnhover;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class ResultController extends WorldController {

    private Texture background;
    private GameCanvas canvas;
    private InputController input;
    private Array<UIButton> buttons = new Array<>();
    private Texture retry_hover = new Texture("result/retry_hover.png");
    private Texture next_hover = new Texture("result/next_hover.png");
    private Texture select_hover = new Texture("result/select_hover.png");

    public boolean retry = false;
    public boolean next = false;
    public boolean select = false;

    public int happy;
    public int neutral;
    public int angry;
    public int total;
    public int score;

    public int[] star_req;

    public Texture c_0 = new Texture("result/complete/0.png");
    public Texture c_1 = new Texture("result/complete/1.png");
    public Texture c_2 = new Texture("result/complete/2.png");
    public Texture c_3 = new Texture("result/complete/3.png");
    public Texture c_4 = new Texture("result/complete/4.png");
    public Texture c_5 = new Texture("result/complete/5.png");
    public Texture c_6 = new Texture("result/complete/6.png");
    public Texture c_7 = new Texture("result/complete/7.png");
    public Texture c_8 = new Texture("result/complete/8.png");
    public Texture c_9 = new Texture("result/complete/9.png");

    public Texture s_0 = new Texture("result/score/0.png");
    public Texture s_1 = new Texture("result/score/1.png");
    public Texture s_2 = new Texture("result/score/2.png");
    public Texture s_3 = new Texture("result/score/3.png");
    public Texture s_4 = new Texture("result/score/4.png");
    public Texture s_5 = new Texture("result/score/5.png");
    public Texture s_6 = new Texture("result/score/6.png");
    public Texture s_7 = new Texture("result/score/7.png");
    public Texture s_8 = new Texture("result/score/8.png");
    public Texture s_9 = new Texture("result/score/9.png");

    public Texture t_0 = new Texture("result/total/0.png");
    public Texture t_1 = new Texture("result/total/1.png");
    public Texture t_2 = new Texture("result/total/2.png");
    public Texture t_3 = new Texture("result/total/3.png");
    public Texture t_4 = new Texture("result/total/4.png");
    public Texture t_5 = new Texture("result/total/5.png");
    public Texture t_6 = new Texture("result/total/6.png");
    public Texture t_7 = new Texture("result/total/7.png");
    public Texture t_8 = new Texture("result/total/8.png");
    public Texture t_9 = new Texture("result/total/9.png");
    public Texture zero_star= new Texture("result/zero_star.png");
    public Texture one_star= new Texture("result/one_star.png");
    public Texture two_stars= new Texture("result/two_stars.png");
    public Texture three_stars= new Texture("result/three_stars.png");


    public ResultController(GameCanvas canvas, Texture background, InputController input){
        this.background = background;
        this.canvas = canvas;
        this.input = input;

        UIButton retry = new UIButton(new Texture("result/retry.png"),"retry",375,102,canvas);
        addButton(retry, ()-> {
            this.retry = true;
        }, ()->{
            retry.setSX(1.1f);
            retry.setSY(1.1f);
            retry.setTexture(retry_hover);
        }, retry::resetStyleProperties);

        UIButton next = new UIButton(new Texture("result/next.png"),"next", 545,120,canvas);
        addButton(next, ()-> {
            this.next = true;
        }, ()->{
            next.setSX(1.1f);
            next.setSY(1.1f);
            next.setTexture(next_hover);
        },next::resetStyleProperties);

        UIButton select = new UIButton(new Texture("result/select.png"),"select",425,65,canvas);
        addButton(select, ()-> {
            this.select = true;
        },()->{
            select.setSX(1.1f);
            select.setSY(1.1f);
            select.setTexture(select_hover);
        },select::resetStyleProperties);

    }


    public void setStatus(int happy, int neutral, int angry, int total, int score, int[] star_req){
        this.happy=happy;
        this.neutral = neutral;
        this.angry=angry;
        this.total=total;
        this.score = score;
        this.star_req = star_req;
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {
        checkButtons();
    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        for(UIButton button : buttons){
            button.draw(canvas);
        }
        drawTotal(canvas);
        drawComplete(canvas);
        drawScore();

    }

    public Texture get(int index, int num){ //index = 0 = complete, 1=score, 2=total
        if (index == 0){
            if(num == 0){
                return c_0;
            } else if (num == 1){
                return c_1;
            }
            else if (num == 2){
                return c_2;
            }
            else if (num == 3){
                return c_3;
            }
            else if (num == 4){
                return c_4;
            }
            else if (num == 5){
                return c_5;
            }
            else if (num == 6){
                return c_6;
            }
            else if (num == 7){
                return c_7;
            }
            else if (num == 8){
                return c_8;
            }
            else if (num == 9){
                return c_9;
            }
        } else if (index == 1){
            if(num == 0){
                return s_0;
            } else if (num == 1){
                return s_1;
            }
            else if (num == 2){
                return s_2;
            }
            else if (num == 3){
                return s_3;
            }
            else if (num == 4){
                return s_4;
            }
            else if (num == 5){
                return s_5;
            }
            else if (num == 6){
                return s_6;
            }
            else if (num == 7){
                return s_7;
            }
            else if (num == 8){
                return s_8;
            }
            else if (num == 9){
                return s_9;
            }
        } else if (index == 2){
            if(num == 0){
                return t_0;
            } else if (num == 1){
                return t_1;
            }
            else if (num == 2){
                return t_2;
            }
            else if (num == 3){
                return t_3;
            }
            else if (num == 4){
                return t_4;
            }
            else if (num == 5){
                return t_5;
            }
            else if (num == 6){
                return t_6;
            }
            else if (num == 7){
                return t_7;
            }
            else if (num == 8){
                return t_8;
            }
            else if (num == 9){
                return t_9;
            }
        }
        return c_0;
    }

    public void drawAt(int one, int ten, int hundred, int x, int y, int total){
        if(total < 10){
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x-20, y+5, 0.0f, 1f, 1f);
        } else if (total < 100){
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x-10, y+5, 0.0f, 1f, 1f);
            canvas.draw(get(1,ten), Color.WHITE, 0, 0,
                    x-30, y+3, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x, y+5, 0.0f, 1f, 1f);
            canvas.draw(get(1,ten), Color.WHITE, 0, 0,
                    x-20, y+3, 0.0f, 1f, 1f);
            canvas.draw(get(1,hundred), Color.WHITE, 0, 0,
                    x-40, y, 0.0f, 1f, 1f);
        }
    }

    public void drawScore(){
        //star_req[0] = 50;
        int one_one = star_req[0]%10;
        int one_ten = (star_req[0]/10)%10;
        int one_hundred = star_req[0]/100;
        int two_one = star_req[1]%10;
        int two_ten = (star_req[1]/10)%10;
        int two_hundred = star_req[1]/100;
        int three_one = star_req[2]%10;
        int three_ten = (star_req[2]/10)%10;
        int three_hundred = star_req[2]/100;


        drawAt(one_one, one_ten, one_hundred,370,310,star_req[0]);
        drawAt(two_one, two_ten, two_hundred,475,325,star_req[1]);
        drawAt(three_one, three_ten, three_hundred,580,340,star_req[2]);







        if(score >= star_req[2]){
            canvas.draw(three_stars, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        } else if (score >= star_req[1]){
            canvas.draw(two_stars, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);

        } else if (score >= star_req[0]){
            canvas.draw(one_star, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        }else{
            canvas.draw(zero_star, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        }
    }

    public void drawComplete(GameCanvas canvas){
//        int happy = 1;
//        int neutral = 34;
//        int angry = 56;
//        int total = 78;
//        int score = 123;


        int happy_one = happy%10;
        int happy_ten = (happy/10)%10;
        int neu_one = neutral%10;
        int neu_ten = (neutral/10)%10;
        int angry_one = angry%10;
        int angry_ten = (angry/10)%10;
        int total_one = total%10;
        int total_ten = (total/10)%10;

        if(total < 10){
            canvas.draw(get(0,total_one), Color.WHITE, 0, 0,
                    502, 545, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,total_one), Color.WHITE, 0, 0,
                    517, 545, 0.0f, 1f, 1f);
            canvas.draw(get(0,total_ten), Color.WHITE, 0, 0,
                    502, 543, 0.0f, 1f, 1f);
        }

        if(happy < 10){
            canvas.draw(get(0,happy_one), Color.WHITE, 0, 0,
                    350, 483, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,happy_one), Color.WHITE, 0, 0,
                    365, 483, 0.0f, 1f, 1f);
            canvas.draw(get(0,happy_ten), Color.WHITE, 0, 0,
                    350, 480, 0.0f, 1f, 1f);
        }

        if(neutral < 10){
            canvas.draw(get(0,neu_one), Color.WHITE, 0, 0,
                    475, 497, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,neu_one), Color.WHITE, 0, 0,
                    490, 497, 0.0f, 1f, 1f);
            canvas.draw(get(0,neu_ten), Color.WHITE, 0, 0,
                    475, 495, 0.0f, 1f, 1f);
        }

        if(angry < 10){
            canvas.draw(get(0,angry_one), Color.WHITE, 0, 0,
                    580, 512, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,angry_one), Color.WHITE, 0, 0,
                    595, 512, 0.0f, 1f, 1f);
            canvas.draw(get(0,angry_ten), Color.WHITE, 0, 0,
                    580, 510, 0.0f, 1f, 1f);
        }

        float pos1 = 560;
        float pos2 = 575;
        float pos3 = 590;
        int one = score %10;
        int ten = (score/10)%10;
        int hundred =(score/100);
        if(score < 10){
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
        } else if (score < 100){
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
            canvas.draw(get(0,ten), Color.WHITE, 0, 0,
                    pos2, 555, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
            canvas.draw(get(0,ten), Color.WHITE, 0, 0,
                    pos2, 555, 0.0f, 1f, 1f);
            canvas.draw(get(0,hundred), Color.WHITE, 0, 0,
                    pos1, 552, 0.0f, 1f, 1f);
        }

    }
    public void drawTotal(GameCanvas canvas){
        //score = 123;
        float pos1 = 560;
        float pos2 = 580;
        float pos3 = 600;
        int one = score %10;
        int ten = (score/10)%10;
        int hundred =(score/100);
        if(score < 10){
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
        } else if (score < 100){
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
            canvas.draw(get(2,ten), Color.WHITE, 0, 0,
                    pos2, 445, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
            canvas.draw(get(2,ten), Color.WHITE, 0, 0,
                    pos2, 445, 0.0f, 1f, 1f);
            canvas.draw(get(2,hundred), Color.WHITE, 0, 0,
                    pos1, 442, 0.0f, 1f, 1f);
        }
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
    private void checkButtons() {
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
            if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)
                    && input.click) {
                button.setIsClicked(true);
                button.onClickEvent();
                button.setIsClicked(false);
            }
        }
    }

    public void debug() {

    }
}
