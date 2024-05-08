package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.objects.Shadow;
import com.raccoon.mygame.objects.TableObstacle;

public class CustomerAIController implements AIController {
    private AssetDirectory directory;
    public static enum FSMState {
        SPAWN,
        SEAT,
        WAIT,
        LEAVE
    }

    public FSMState state;

    private int tick = 0;
    private int move;
    private Array<TableObstacle> tables;
    private Vector2 goal;
    Vector2 zero = new Vector2();
    Vector2 temp;
    private Customer customer;

    private Shadow shadow;

    /** element:description as follows
     *  0:scaleX
     *  1:scaleY
     *  2:height
     *  3:textureWidth
     */

    private float[] customerInfo;

    public CustomerAIController(Array<TableObstacle> tables, Customer customer,Shadow shadow,float[] customerInfo, AssetDirectory directory) {
        this.tables = tables;
        this.customer = customer;
        state = FSMState.SPAWN;
        move = CONTROL_NO_ACTION;
        goal = null;
        temp=new Vector2();

        this.customerInfo = customerInfo;
        this.shadow = shadow;
        this.directory = directory;
    }


    @Override
    public int getAction() {
        float shadowX = customer.getX()* customerInfo[0]-shadow.getTextureWidth()/2;
        float shadowY = (customer.getY() * customerInfo[1]) - (customerInfo[2] * customerInfo[1] / 2);

        shadow.setPosition(shadowX,shadowY);
        tick -= 1;

        if (customer.getX() < 0) {
            customer.deactivate();
        }
        changeState();
        setGoal();

        switch (state) {
            case SPAWN:
            case WAIT:
                customer.setLinearVelocity(zero);
                break;
            case SEAT:
                if (goal.x > customer.getX()) {
                    customer.setLinearVelocity(temp.set(4, 0));
                } else {
                    float y = goal.y < customer.getY() ? -4 : 4;
                    customer.setLinearVelocity(temp.set(0, y));
                }
                break;
            case LEAVE:
                if (Math.abs(goal.y - customer.getY()) > 0.05) {
                    float y = goal.y < customer.getY() ? -4 : 4;
                    customer.setLinearVelocity(temp.set(0, y));
                }else {
                    customer.setLinearVelocity(temp.set(-4, 0));
                }
                break;
        }
        return 0;
    }

    public void changeState() {
        switch (state) {
            case SPAWN:
                if (goal != null) {
                    state = FSMState.SEAT;
                }
                break;
            case SEAT:
//
                if (goal.dst(customer.getPosition()) <= 0.5) {
                    customer.setPosition(goal);
                    state = FSMState.WAIT;
                    customer.setBodyType(BodyType.StaticBody);
                }
                break;
            case WAIT:
                if (customer.onRight) {
                    customer.flipScale = 1;
                }
                if (customer.isSatisfied()) {
                    state = FSMState.LEAVE;
                    customer.setBodyType(BodyType.KinematicBody);
                }
                createPatienceMeter();
                break;
            case LEAVE:
                customer.flipScale = 1;
                destroyPatienceMeter();
                break;
        }
    }

    public void setGoal() {
        switch (state) {
            case SPAWN:
                for (TableObstacle t : tables) {
                    if (!t.isOccupied(1)) {
                        goal = t.occupy(1);
                        customer.onRight = true;
                        customer.table = t;
                        customer.seatIndex = 1;
                        break;
                    } else if (!t.isOccupied(0)) {
                        goal = t.occupy(0);
                        customer.table = t;
                        customer.seatIndex = 0;
                        break;
                    }
                }
                break;
            case LEAVE:
//                if (Math.abs(customer.getY()-13) >= 0.05){
//                    goal = new Vector2(customer.getX(), 13);
//                }else if (Math.abs(customer.getX()-1) >= 0.05) {
//                    System.out.println("herex");
//                    goal = new Vector2(1, customer.getY());
//                }else{
//                    System.out.println("here");
//                    goal = new Vector2(-0.5f, 2.5f);
//                }
                goal.set(-0.5f, 7f);
        }
    }

    public void timeOut(){
        state = FSMState.LEAVE;
        customer.setBodyType(BodyType.KinematicBody);
    }

    private void createPatienceMeter() {
        if (customer.pat == null) {
            customer.pat = new PatienceMeter(120, customer.canvas, customer, directory);
            customer.pat.create();
        }
    }

    private void destroyPatienceMeter() {
        if (customer.pat != null) {
            customer.pat.pauseTimer();
            customer.pat = null;
        }
    }
}
