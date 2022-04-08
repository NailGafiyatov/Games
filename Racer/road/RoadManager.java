package com.javarush.games.racer.road;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.GameObject;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public final static int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public final static int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;

    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        Thorn result;
        if (type == RoadObjectType.DRUNK_CAR) {
            return new MovingCar(x, y);
        } else if (type == RoadObjectType.THORN) {
            result = new Thorn(x, y);
        } else {
            result = null;
            return new Car(type,x , y);
        }
        return result;
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject object = createRoadObject(type, x, y);
        if (object != null && isRoadSpaceFree(object)) {
                items.add(object);
        }

    }

    public void draw(Game game) {
        for (RoadObject object: items) {
            object.draw(game);
        }
    }

    public void move(int boost) {
        for (RoadObject object: items) {
            object.move(boost + object.speed, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists() {
        boolean result  = true;
        for (RoadObject object: items) {
            if (object instanceof Thorn ) {
                result = true;
            } else result = false;
        }
        return result;
    }

    private void generateThorn(Game game) {
        int randomN = game.getRandomNumber(100);
        if (randomN < 10 && isThornExists() == false) {
            addRoadObject(RoadObjectType.THORN,game);
        }
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {
        for (RoadObject object: new ArrayList<>(items)) {
            if (object.y >= RacerGame.HEIGHT) {
                items.remove(object);
                if (object instanceof Thorn) {
                    return;
                } passedCarsCount++;
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar) {
        boolean result = true;
        for (GameObject object: items) {
            if (object.isCollision(playerCar)) {
                result = true;
            } else { result = false;}
        }
        return result;
    }

    private void generateRegularCar(Game game) {
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100) < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object) {
        boolean result = true;
        for (RoadObject item: items) {
            if (item.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                result = false;
            } else result = true;
        }
        return result;
    }

    private boolean isMovingCarExists() {
        boolean result = true;
        for (RoadObject item: items) {
            if (item instanceof MovingCar) {
                result = true;
            } else result = false;
        }
        return result;
    }

    private void generateMovingCar(Game game) {
        if (game.getRandomNumber(100) < 10 && !isMovingCarExists()) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }
}
