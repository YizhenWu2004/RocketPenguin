package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.enums.enums.PatrolDirection;

public class GuardAIController {
    private enum AIState {
        WANDER,
        CHASE
    }

    private float upperBoundary;
    private float lowerBoundary;
    private float speed;
    private boolean movingRight = true;

    private boolean movingPositive = true;

    private PatrolDirection patrolDirection;

    private AIState currentState = AIState.WANDER;

//    public GuardAIController(float x, float y, float worldWidth, float worldHeight, float patrolRange, float speed, PatrolDirection patrolDirection) {
//        this.leftBoundary = Math.max(x - patrolRange, 0);
//        this.rightBoundary = Math.min(x + patrolRange, worldWidth);
//        this.speed = speed;
//        this.patrolDirection = patrolDirection;
//    }

    public GuardAIController(float x, float y, float worldWidth, float worldHeight, float patrolRange, float speed, PatrolDirection patrolDirection) {
        this.speed = speed;
        this.patrolDirection = patrolDirection;
        if (patrolDirection == PatrolDirection.LEFT_RIGHT) {
            this.lowerBoundary = Math.max(x - patrolRange, 0);
            this.upperBoundary = Math.min(x + patrolRange, worldWidth);
        } else { // UP_DOWN
            this.lowerBoundary = Math.max(y - patrolRange, 0);
            this.upperBoundary = Math.min(y + patrolRange, worldHeight);
        }
    }

    public void setAIStateChase() {
        currentState = AIState.CHASE;
    }

    public void reverseDirection() {
        movingPositive = !movingPositive;
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
            switch (patrolDirection) {
                case LEFT_RIGHT:
                    if (movingPositive) {
                        if (guardPosition.x + speed * deltaTime >= upperBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.x = speed;
                        }
                    } else {
                        if (guardPosition.x - speed * deltaTime <= lowerBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.x = -speed;
                        }
                    }
                    break;
                case UP_DOWN:
                    if (movingPositive) {
                        if (guardPosition.y + speed * deltaTime >= upperBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.y = speed;
                        }
                    } else {
                        if (guardPosition.y - speed * deltaTime <= lowerBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.y = -speed;
                        }
                    }
                    break;
            }
        } else if (currentState == AIState.CHASE) {
            float playerX = info.get(0);
            float playerY = info.get(1);

            Vector2 direction = new Vector2(playerX - guardPosition.x, playerY - guardPosition.y).nor();
            Vector2 chaseSpeed = direction.scl(speed);

//            guardPosition.add(chaseSpeed.x * deltaTime, chaseSpeed.y * deltaTime);
            speedVector.set(chaseSpeed.x, chaseSpeed.y);
        }

        return speedVector;
    }
}
