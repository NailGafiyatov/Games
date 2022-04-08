package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private int score;
    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;

    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isGameStopped;

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    private void drawScene() {
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                setCellColor(x, y, Color.ORANGE);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }

    private void createGame() {
        score = 1000;
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isGameStopped = false;
        setTurnTimer(50);
        createGameObjects();
        drawScene();

    }

    @Override
    public void onTurn(int step) {
        if (score > 0) {
            score--;
        }
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > WIDTH - 1 || x < 0 || y > HEIGHT - 1 || y < 0) {
            return;
        } else {
            super.setCellColor(x, y, color);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped) {
            createGame();
            return;
        } else if (key == Key.UP) {
            isUpPressed = true;
        } else if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.UP) {
            isUpPressed = false;
        } else if (key == Key.LEFT) {
            isLeftPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = false;
        }
    }

    private void check() {
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped())) {
            gameOver();
        } else if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        }
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "YOU WIN " + score, Color.GREEN, 64);
        stopTurnTimer();
    }

    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        score = 0;
        showMessageDialog(Color.NONE, "GAMEOVER", Color.RED, 64);
        stopTurnTimer();
    }


}
