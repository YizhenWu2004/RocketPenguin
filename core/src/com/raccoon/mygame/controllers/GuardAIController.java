package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.enums.enums.PatrolDirection;
import com.raccoon.mygame.util.Node;
import com.raccoon.mygame.view.GameCanvas;

import java.util.PriorityQueue;

public class GuardAIController extends ScreenAdapter {
    public enum AIState {
        WANDER,
        SUS,
        CHASE,
        SLEEP,
        WAKE,
        ROTATE
    }

    public enum GuardOrientation {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private float upperBoundary;
    private float lowerBoundary;
    private float speed;

    private float chaseSpeed;
    private boolean movingRight = true;

    private boolean movingup = true;
    private boolean movingPositive = true;

    public PatrolDirection patrolDirection;

    boolean[][] collisionLayer;
    private float timerSeconds = 10;

    int counter;
    int counter2;
    private AIState currentState;
    private GuardOrientation defaultOrien;
    private GuardOrientation orien;

    private Array<Vector2> currentPath;
    private int currentPathIndex;

    Vector2 guardDimension;

    private int chaseCounter;
    public float susMeter;

    /**
     * constant that determines after how many updates, findPath is called in
     * updateChaseMode
     */
    int CHASE_COUNTER_CONSTANT = 25;

    Array<Vector2> nodes;
    private int currentNodeIndex = 0;

    private static final float AWAKE_DURATION = 5.0f;
    private static final float SLEEP_DURATION = 3.0f;
    private static final float ROTATE_DURATION = 3.0f;
    private float sleepWakeTimer = AWAKE_DURATION;
    private float rotateTimer = ROTATE_DURATION;
    private int count3 = 0;
    private SoundController sounds;

    public GuardAIController(float x, float y, float worldWidth,
                             float worldHeight, float patrolRange,
                             float speed, PatrolDirection patrolDirection,
                             boolean[][] collisionLayer, Vector2 guardDimension,
                             Array<Vector2> nodes, GuardOrientation spawnOrien) {
        this.speed = speed;
        this.patrolDirection = patrolDirection;
        this.collisionLayer = collisionLayer;
        if (patrolDirection == PatrolDirection.LEFT_RIGHT) {
            currentState = AIState.WANDER;
            this.lowerBoundary = Math.max(x - patrolRange, 0);
            this.upperBoundary = Math.min(x + patrolRange, worldWidth);
        } else if (patrolDirection == PatrolDirection.UP_DOWN) {
            currentState = AIState.WANDER;
            this.lowerBoundary = Math.max(y - patrolRange, 0);
            this.upperBoundary = Math.min(y + patrolRange, worldHeight);
        } else if (patrolDirection == PatrolDirection.SLEEP_WAKE) {
            currentState = AIState.WAKE;
        }else if (patrolDirection == PatrolDirection.ROTATE_CW || patrolDirection == PatrolDirection.ROTATE_CCW){
            currentState = AIState.ROTATE;
        }

        this.currentPath = new Array<>();
        this.currentPathIndex = 0;

        this.guardDimension = guardDimension;
        this.counter = 0;
        this.chaseSpeed = speed * 3;
        this.chaseCounter = 0;
        this.counter2 = 0;

        this.nodes = nodes;

        susMeter = 0;

        orien = spawnOrien;
        defaultOrien = spawnOrien;
        sounds = new SoundController();
    }

    public void resetRotateTimer(){
        rotateTimer = ROTATE_DURATION;
    }

    public void setAIStateChase() {
        currentState = AIState.CHASE;
    }

    public void setAIStateSus() {
        currentState = AIState.SUS;
    }
    public void setAIStateRotate() {
        currentState = AIState.ROTATE;
    }

    public void incrementSusMeter(float scale) {
        if(scale == -1){
            susMeter = 30;
        }
        else{
            susMeter += scale * 0.166;
        }
    }

    public void decrementSusMeter(float scale) {
        susMeter = (float) Math.max(susMeter - 0.166 * scale*0.5, 0);
    }

    public float getSusMeter() {
        return susMeter;
    }

    public void setAIStateWander() {
        currentState = AIState.WANDER;
    }

    public void setAIStateWake() {
        currentState = AIState.WAKE;
    }

    public boolean isSleep() {
        return currentState == AIState.SLEEP;
    }

    public boolean isSus() {
        return currentState == AIState.SUS;
    }

    public boolean isChase() {
        return currentState == AIState.CHASE;
    }
    public void show() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
               count3 = 0;
            }
        }, timerSeconds);
    }

    public void reverseDirection() {
        if (patrolDirection == PatrolDirection.LEFT_RIGHT) {
            if (counter == 1) {
                movingRight = false;
                counter = 0;
            } else {
                movingRight = true;
                counter++;
            }
        }
        movingPositive = !movingPositive;
        if (patrolDirection == PatrolDirection.UP_DOWN) {
            if (counter2 == 1) {
                movingup = false;
                counter2 = 0;
            } else {
                movingup = true;
                counter2++;
            }
        }
    }

    public void playHonk(){
        if(count3 == 0){
            sounds.honkPlay();
            count3++;
            show();
        }
    }
    public Vector2 getSpeed(Vector2 guardPosition, float deltaTime, Array<Float> info) {

//        System.out.println("Current State: " + currentState);
//        System.out.println("Orientation: " + orien);
//        System.out.println("Current Path: " + currentPath);
//        System.out.println("Current Path Index: " + currentPathIndex);
//        System.out.println("Chase Counter: " + chaseCounter);
//        System.out.println("Rotate Timer: " + rotateTimer);

        // System.out.println(currentState);
        Vector2 speedVector = new Vector2(0f, 0f);
//        if (susMeter != 0) {
//            System.out.println("SUS" + susMeter);
//        }

        if (susMeter >= 30) {
            setAIStateChase();
            playHonk();
        }

        else if (susMeter <= 0) {
            if(currentState == AIState.SUS){
                if(patrolDirection == PatrolDirection.SLEEP_WAKE){
                    setAIStateWake();
                }
                else if(patrolDirection == PatrolDirection.ROTATE_CCW || patrolDirection == PatrolDirection.ROTATE_CW){
                    setAIStateRotate();
                }
                else{
                    setAIStateWander();
                }
            }
        }

        rotateTimer -= deltaTime;

        sleepWakeTimer -= deltaTime;
        if(currentState == AIState.ROTATE && rotateTimer <= 0){
            getNextRotateOrien(patrolDirection);
            rotateTimer = ROTATE_DURATION;
            return speedVector;
        }

        if((patrolDirection == PatrolDirection.ROTATE_CW || patrolDirection == PatrolDirection.ROTATE_CCW) && currentState == AIState.ROTATE){
            return speedVector;
        }
        else if (currentState == AIState.WAKE && sleepWakeTimer <= 0) {
            currentState = AIState.SLEEP;
            sleepWakeTimer = SLEEP_DURATION;
            orien = defaultOrien;
            return speedVector;
        }
        else if (currentState == AIState.SLEEP && sleepWakeTimer <= 0) {
            currentState = AIState.WAKE;
            orien = defaultOrien;
            sleepWakeTimer = AWAKE_DURATION;
            return speedVector;
        }
        else if (currentState == AIState.SLEEP) {
            orien = defaultOrien;
            return speedVector;
        }
        else if (currentState == AIState.WAKE) {
            orien = defaultOrien;
            return speedVector;
        }
        else if(currentState == AIState.SUS && patrolDirection == PatrolDirection.SLEEP_WAKE){
            orien = defaultOrien;
            return speedVector;
        }
        else if(currentState == AIState.SUS && (patrolDirection == PatrolDirection.ROTATE_CCW || patrolDirection == PatrolDirection.ROTATE_CW)){
            return speedVector;
        }
        else if ((currentState == AIState.WANDER || currentState == AIState.SUS) && nodes.size > 0) {
            Vector2 targetNode = nodes.get(currentNodeIndex);
            Vector2 direction = new Vector2(targetNode.x - guardPosition.x, targetNode.y - guardPosition.y).nor();
            speedVector.set(direction.scl(speed));

            if (guardPosition.dst(targetNode) < 1f) {
                currentNodeIndex = (currentNodeIndex + 1) % nodes.size;
            }
        }
        else if (currentState == AIState.WANDER || currentState == AIState.SUS) {
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
            chaseCounter++;
//            Vector2 playerPosition = new Vector2(info.get(2), info.get(3));
            Vector2 chaseSpeed = updateChaseMode(guardPosition, info, chaseCounter);
            if (chaseCounter >= CHASE_COUNTER_CONSTANT) {
                chaseCounter = 0;
            }
            if (chaseSpeed != null) {
                updateOrienChase(chaseSpeed);
                return chaseSpeed;
            }
        }
        updateOrien(speedVector);
        return speedVector;
    }

    private void updateOrien(Vector2 speedVector) {
//        if(currentState == AIState.ROTATE)
        if(currentState == AIState.SLEEP || currentState == AIState.WAKE){
            orien = defaultOrien;
            return;
        }
        if (Math.abs(speedVector.x) > Math.abs(speedVector.y)) {
            if (speedVector.x > 0) {
                orien = GuardOrientation.RIGHT;
            } else {
                orien = GuardOrientation.LEFT;
            }
        } else if (Math.abs(speedVector.y) >= Math.abs(speedVector.x)) {
            if (speedVector.y > 0) {
                orien = GuardOrientation.UP;
            } else {
                orien = GuardOrientation.DOWN;
            }
        }
    }

    private void updateOrienChase(Vector2 speedVector) {
//        if(patrolDirection != PatrolDirection.ROTATE_CW && patrolDirection != PatrolDirection.ROTATE_CCW){
            if (speedVector.x > 0) {
                orien = GuardOrientation.RIGHT;
            } else {
                orien = GuardOrientation.LEFT;
            }
//        }
    }

    public Array<Vector2> findPathMain(Vector2 start, Array<Float> info) {
        Vector2 upperLeft = new Vector2(info.get(0), info.get(1));
        Vector2 upperRight = new Vector2(info.get(2), info.get(3));
        Vector2 lowerLeft = new Vector2(info.get(4), info.get(5));
        Vector2 lowerRight = new Vector2(info.get(6), info.get(7));
        Vector2 middle = new Vector2(info.get(8), info.get(9));
        Vector2 outlier = new Vector2(info.get(4), info.get(5)-1f);

        Array<Vector2> pathUpperLeft = findPath(start, upperLeft,1);
        Array<Vector2> pathUpperRight = findPath(start, upperRight,1);
        Array<Vector2> pathLowerLeft = findPath(start, lowerLeft,-1);
        Array<Vector2> pathLowerRight = findPath(start, lowerRight,1);
        Array<Vector2> pathMiddle = findPath(start, middle,1);
        Array<Vector2> pathOutlier = findPath(start, outlier,1);

        if (pathUpperRight.size != 0) {
            return pathUpperRight;
        } else if (pathUpperLeft.size != 0) {
            return pathUpperLeft;
        } else if (pathLowerLeft.size != 0) {
            return pathLowerLeft;
        } else if (pathLowerRight.size != 0) {
            return pathLowerRight;
        } else if (pathMiddle.size != 0) {
            return pathMiddle;
        } else if(pathOutlier.size != 0){
            Vector2 lastPoint = pathOutlier.peek();
            lastPoint.y += 1f;
            pathOutlier.set(pathOutlier.size - 1, lastPoint);
            return pathOutlier;
        }
        else {
//            System.out.println("OOPS");
            return new Array<Vector2>();
        }
    }


    /**
     * runs dijskra's and finds the shortest path from start to goal
     *
     * @param start guard's position
     * @param goal  the goal tile
     * @return array of coordinates that the guard should take to reach to goal
     */
    public Array<Vector2> findPath(Vector2 start, Vector2 goal, int upper) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(new Node(start, null, 0));

        Vector2 goalFloor = new Vector2((float) Math.floor(goal.x), (float) Math.floor(goal.y));
        boolean[][] visited = new boolean[collisionLayer.length][collisionLayer[0].length];
        Node[][] cameFrom = new Node[collisionLayer.length][collisionLayer[0].length];

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            Vector2 currentFloor = new Vector2((float) Math.floor(current.position.x), (float) Math.floor(current.position.y));
            if (currentFloor.equals(goalFloor)) {
                Array<Vector2> path = reconstructPath(cameFrom, current);
                //todo fix this
//                if (!path.isEmpty()) {
//                    Vector2 lastPoint = path.peek();
////                    lastPoint.y -= 0.7f;
//                    path.set(path.size - 1, lastPoint);
//                }
                return path;
            }

            for (Vector2 next : getNeighbors(current.position, guardDimension.x, guardDimension.y)) {
                if (!visited[(int) next.x][(int) next.y]) {
                    visited[(int) next.x][(int) next.y] = true;
                    float newCost = current.cost + 1;
                    frontier.add(new Node(next, current, newCost));
                    cameFrom[(int) next.x][(int) next.y] = current;
                }
            }
        }

        return new Array<>();
    }

    private Array<Vector2> reconstructPath(Node[][] cameFrom, Node current) {
        Array<Vector2> path = new Array<>();
        while (current != null) {
            path.add(current.position);
            current = current.parent;
        }
        path.reverse();
        return path;
    }


    private Array<Vector2> getNeighbors(Vector2 position, float width, float height) {
//        System.out.println("Width" + width);
//        System.out.println("Height" + height);
        Array<Vector2> neighbors = new Array<>();
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        int guardWidthCells = 2;
        int guardHeightCells = 2;

        for (int[] direction : directions) {
            int nx = (int) position.x + direction[0];
            int ny = (int) position.y + direction[1];

            if (canFitInPosition(nx, ny, guardWidthCells, guardHeightCells)) {
                neighbors.add(new Vector2(nx, ny));
            }
        }
        return neighbors;
    }

    /**
     * @param x
     * @param y
     * @param widthCells
     * @param heightCells
     * @return if guard can fit into a position
     */
    private boolean canFitInPosition(int x, int y, int widthCells, int heightCells) {
        for (int offsetX = 0; offsetX < widthCells; offsetX++) {
            for (int offsetY = 0; offsetY < heightCells; offsetY++) {
                int checkX = x + offsetX - widthCells / 2;
                int checkY = y + offsetY - heightCells / 2;
                if (checkX < 0 || checkX >= collisionLayer.length || checkY < 0 || checkY >= collisionLayer[0].length || collisionLayer[checkX][checkY]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * First finds a path, if necessary, calling findPath, storing it in
     * currentPath, then initializing currentPathIndex to 0
     * <p>
     * Then,
     *
     * @param guardPosition position of guard right now
     * @param info          information (see store controller)
     * @return the appropriate velocity according to the shortest path
     */
    public Vector2 updateChaseMode(Vector2 guardPosition, Array<Float> info, int chaseCounter) {
        Vector2 playerPosition = new Vector2(info.get(0), info.get(1));
        if (chaseCounter >= CHASE_COUNTER_CONSTANT) {
            currentPath = findPathMain(guardPosition, info);
            currentPathIndex = 0;
        } else if (currentPath.isEmpty() || currentPathIndex >= currentPath.size || playerPosition.dst(guardPosition) > 10) {
            currentPath = findPathMain(guardPosition, info);
            currentPathIndex = 0;
        }

        if (!currentPath.isEmpty() && currentPathIndex < currentPath.size) {
            Vector2 nextStep = currentPath.get(currentPathIndex);
            if (guardPosition.dst(nextStep) < 1f) {
                currentPathIndex++;
                if (currentPathIndex < currentPath.size) {
                    nextStep = currentPath.get(currentPathIndex);
                }
            }

            Vector2 direction = new Vector2(nextStep.x - guardPosition.x, nextStep.y - guardPosition.y).nor();
            //System.out.println("direction" + direction);
            return direction.scl(chaseSpeed);
        }

        return null;
    }

    public boolean getDirection() {
        //returns true if moving right
        return movingRight;
    }

    public boolean getDirection2() {
        return movingup;
    }

    public PatrolDirection getPatrolDirection() {
        return this.patrolDirection;
    }

    public AIState getCurrentState() {
        return currentState;
    }

    public GuardOrientation getOrien() {
        return orien;
    }

    /**
     * requires PatrolDirection to be ROTATE_CCW or ROTATE_CW
     * @param dir
     * @return
     */
    public void getNextRotateOrien(PatrolDirection dir){
        System.out.println("getNextRotateOrien" + orien);
        if(dir == PatrolDirection.ROTATE_CCW){
            if(orien == GuardOrientation.LEFT){
                orien = GuardOrientation.DOWN;
            }
            else if(orien == GuardOrientation.UP){
                orien = GuardOrientation.LEFT;
            }
            else if(orien == GuardOrientation.RIGHT){
                orien = GuardOrientation.UP;
            }
            else{
                orien = GuardOrientation.RIGHT;
            }
        }
        else{
            if(orien == GuardOrientation.LEFT){
                orien = GuardOrientation.UP;
            }
            else if(orien == GuardOrientation.UP){
                orien = GuardOrientation.RIGHT;
            }
            else if(orien == GuardOrientation.RIGHT){
                orien = GuardOrientation.DOWN;
            }
            else{
                orien = GuardOrientation.LEFT;
            }
        }
    }
}
