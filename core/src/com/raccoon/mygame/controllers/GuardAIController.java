package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GuardAIController {
    private enum AIState {
        WANDER,
        CHASE
    }
    private float leftBoundary;
    private float rightBoundary;
    private float speed;
    private boolean movingRight = true;

    private AIState currentState = AIState.WANDER;

    public GuardAIController(float x, float y, float worldWidth, float worldHeight, float patrolRange, float speed) {
        this.leftBoundary = Math.max(x - patrolRange, 0);
        this.rightBoundary = Math.min(x + patrolRange, worldWidth);
        this.speed = speed;
    }

    public void setAIStateChase(){
        currentState =  AIState.CHASE;
    }

//    public Vector2 updatePosition(Vector2 guardPosition, float deltaTime) {
//        if(currentState == AIState.WANDER){
//            if (movingRight) {
//                guardPosition.x += speed * deltaTime;
//                if (guardPosition.x >= rightBoundary) {
//                    guardPosition.x = rightBoundary;
//                    movingRight = false;
//                }
//            } else {
//                guardPosition.x -= speed * deltaTime;
//                if (guardPosition.x <= leftBoundary) {
//                    guardPosition.x = leftBoundary;
//                    movingRight = true;
//                }
//            }
//        }
//        else if (currentState == AIState.CHASE){
//
//        }
//
//        return guardPosition;
//    }

    public Vector2 getSpeed(Vector2 guardPosition, float deltaTime, Array<Float> info) {
        Vector2 speedVector = new Vector2(0f, 0f);

        if (currentState == AIState.WANDER) {
            if (movingRight) {
                if (guardPosition.x + speed * deltaTime >= rightBoundary) {
                    movingRight = false;
                    speedVector.x = -(guardPosition.x + speed * deltaTime - rightBoundary);
                } else {
                    speedVector.x = speed;
                }
            } else {
                if (guardPosition.x - speed * deltaTime <= leftBoundary) {
                    movingRight = true;
                    speedVector.x = leftBoundary - (guardPosition.x - speed * deltaTime);
                } else {
                    speedVector.x = -speed;
                }
            }
        } else if (currentState == AIState.CHASE) {
            float playerX = info.get(0);
            float playerY = info.get(1);

            Vector2 direction = new Vector2(playerX - guardPosition.x, playerY - guardPosition.y).nor();
            Vector2 chaseSpeed = direction.scl(speed);

            guardPosition.add(chaseSpeed.x * deltaTime, chaseSpeed.y * deltaTime);
            speedVector.set(chaseSpeed.x, chaseSpeed.y);
        }

        return speedVector;
    }
}
