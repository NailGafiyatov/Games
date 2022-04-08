package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<GameObject> snakeParts = new ArrayList<>();
    /*private final static String HEAD_SIGN = "\uD83D\uDC7E";
    private final static String BODY_SIGN = "\u26AB";*/
    private Direction direction = Direction.LEFT;

    public boolean isAlive = true;

    public void setDirection(Direction direction) {
        if (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x ) {
            return;
        } if (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x ) {
            return;
        } if (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y ) {
            return;
        } if (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y ) {
            return;
        } else {
            this.direction = direction;
        }
    }

    public Snake(int x, int y) {
        GameObject snake1 = new GameObject(x, y);
        snakeParts.add(snake1);
        GameObject snake2 = new GameObject(x + 1, y);
        snakeParts.add(snake2);
        GameObject snake3 = new GameObject(x + 2, y);
        snakeParts.add(snake3);
    }

    public void draw(Game game) {
        Color color = isAlive ? Color.GREEN : Color.RED;
        for (GameObject snake: snakeParts) {
             if (snakeParts.get(0).equals(snake)) {
                game.setCellValueEx(snake.x, snake.y, Color.NONE, "", color, 75);
            }else {
                game.setCellValueEx(snake.x, snake.y, Color.NONE, "", color, 75);
            }
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();

        if (newHead.x >= SnakeGame.WIDTH || newHead.y >= SnakeGame.HEIGHT || newHead.x < 0 || newHead.y < 0 ){
            isAlive = false;
            return;
        }
        if(checkCollision(newHead) == false) {
            snakeParts.add(0, newHead);
        } else {
            isAlive = false;
            return;
        }
        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    public GameObject createNewHead() {
        GameObject gameObject = snakeParts.get(0);

        if (direction == Direction.LEFT) {
            gameObject = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
        }else if (direction == Direction.RIGHT) {
            gameObject = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
        }else if (direction == Direction.DOWN) {
            gameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
        }else if (direction == Direction.UP) {
            gameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
        }
        return gameObject;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size()-1);
    }

    public boolean checkCollision(GameObject gameObject) {
        boolean result = false;
        for (GameObject list : snakeParts) {
            if (list.x == gameObject.x && list.y == gameObject.y) {
                result =true;
        }
    }
        return result;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
