package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    private void createShips() {
        ships = new ArrayList<>();
        for (int y = 0; y < ROWS_COUNT; y++) {
            for (int x = 0; x < COLUMNS_COUNT; x++) {
                EnemyShip newShip = new EnemyShip(x * STEP, y * STEP + 12);
                ships.add(newShip);
            }
        }
        Boss boss = new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5);
        ships.add(boss);
    }

    public EnemyFleet() {
        createShips();
    }

    public void draw(Game game) {
        for (EnemyShip ship: ships) {
            ship.draw(game);
        }
    }

    private double getLeftBorder() {
        double min = 100;
        for (EnemyShip ship: ships) {
            if (min > ship.x) {
                min = ship.x;
            }
        }
        return min;
    }

    private double getRightBorder() {
        double max = 0;
        for (EnemyShip ship: ships) {
            if (max < ship.x + ship.width) {
                max = ship.x + ship.width;
            }
        }
        return max;
    }

    private double getSpeed() {
        return Math.min(2.0, 3.0 / (double) ships.size());
    }

    public void move() {
        Direction currentD = direction;
        if (ships.size() == 0) {
            return;
        } else if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            currentD = Direction.DOWN;
        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            currentD = Direction.DOWN;
        }
        double speed = getSpeed();
        for (EnemyShip ship: ships) {
            ship.move(currentD, speed);
        }
    }

    public Bullet fire(Game game) {
        if (ships.isEmpty() || game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        }
        return ships.get(game.getRandomNumber(ships.size())).fire();
    }

    public void deleteHiddenShips() {
        for (EnemyShip ship : new ArrayList<>(ships)) {
            if (!ship.isVisible()) {ships.remove(ship);}
        }
    }

    public double getBottomBorder() {
        double max = 0;
        for (EnemyShip ship : ships) {
            if (max < ship.y + ship.height) {max = ship.y + ship.height;}
        }
        return max;
    }

    public int getShipsCount() {
        return ships.size();
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()) {return 0;}
        int sum = 0;
        for (EnemyShip ship : ships) {
            sum += ship.score;
        }
        return sum;
    }
}
