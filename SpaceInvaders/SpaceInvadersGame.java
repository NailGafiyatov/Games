package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;

    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        setTurnTimer(40);
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<>();
        score = 0;
        drawScene();
    }

    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        for (Bullet object: enemyBullets) {
            object.draw(this);
        }
        playerShip.draw(this);
        for (Bullet object: playerBullets) {
            object.draw(this);
        }
    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for(Star object: stars) {
            object.draw(this);
        }
    }

    private void createStars() {
        stars = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            Star newStar = new Star(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            stars.add(newStar);
        }
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet result = enemyFleet.fire(this);
        if (result != null) {
            enemyBullets.add(result);
        }
        setScore(score);
        drawScene();
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        playerShip.move();
        for (Bullet object: enemyBullets) {
            object.move();
        }
        for (Bullet object: playerBullets) {
            object.move();
        }
    }

    private void removeDeadBullets() {
        for (Bullet object: new ArrayList<>(enemyBullets)) {
            if (!object.isAlive || object.y >= HEIGHT - 1) {
                enemyBullets.remove(object);
            }
        }
        for (Bullet object: new ArrayList<>(playerBullets)) {
            if (!object.isAlive || object.y + object.height < 0) {
                playerBullets.remove(object);
            }
        }
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        score += enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive) {stopGameWithDelay();}
        if (enemyFleet.getBottomBorder() >= playerShip.y) {playerShip.kill();}
        if (enemyFleet.getShipsCount() == 0) {playerShip.win();stopGameWithDelay();}
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.NONE, "YOU WIN", Color.GREEN, 64);
        } else { showMessageDialog(Color.NONE, "GAMEOVER", Color.RED, 64);}
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped) {createGame();return;}
        else if (key == Key.LEFT) {playerShip.setDirection(Direction.LEFT);}
        else if (key == Key.RIGHT) {playerShip.setDirection(Direction.RIGHT);}
        else if (key == Key.SPACE) {
            Bullet bullet = playerShip.fire();
            if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(bullet);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT || (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT)) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color color, String value) {
        if (x > WIDTH - 1 || x < 0 || y > HEIGHT - 1 || y < 0) {return;}
        super.setCellValueEx(x, y, color, value);
    }
}
