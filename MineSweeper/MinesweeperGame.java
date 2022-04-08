package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private final static String MINE = "\uD83D\uDCA3";
    private int countFlags;
    private final static String FLAG = "\uD83D\uDEA9";
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    //Возвращает количество соседей
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    //Возвращает количество мин по соседству
    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField [x][y];
                if(!gameObject.isMine) {
                    for(GameObject neighbor: getNeighbors(gameObject)) {
                        if(neighbor.isMine) {
                            gameObject.countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    //Открытие ячейки
    private void openTile(int x, int y) {
        GameObject gameObject = gameField [y][x];
        
        if (gameObject.isOpen || gameObject.isFlag || isGameStopped) {
            return;
        } 
        gameObject.isOpen = true;
        countClosedTiles--;
        setCellColor(x, y, Color.GREEN);
        if (gameObject.isMine) {
            setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
            gameOver();
            return;
        }
        score += 5;
        setScore(score);
        if (gameObject.countMineNeighbors == 0) {
            setCellValue(gameObject.x, gameObject.y, "");
            List<GameObject> result = getNeighbors(gameObject);
            for (GameObject neighbors: result) {
                if (!neighbors.isOpen) {
                    openTile(neighbors.x,neighbors.y);
                }
            }
        } else {
                setCellNumber(x,y,gameObject.countMineNeighbors);
                setCellColor(x,y,Color.GREEN);
        }
        if (countClosedTiles == countMinesOnField) {
            win();
        }
    }

    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        
        if (gameObject.isOpen || (countFlags == 0 && !gameObject.isFlag) || isGameStopped) {
            return;
        }
        if (gameObject.isFlag) {
            gameObject.isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.ORANGE);
        } else {
            gameObject.isFlag = true;
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.YELLOW);
        }
    }

    private void win() {
        isGameStopped = true;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x <= SIDE; x++) {
                if (gameField[y][x].isMine) {
                    setCellValueEx(x, y,Color.RED, MINE);
                }
            }
        }
        showMessageDialog( Color.GREEN, "YOU ARE A WINNER", Color.BLACK, 36);
    }

    private void gameOver() {
        isGameStopped = true;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x <= SIDE; x++) {
                if (gameField[y][x].isMine) {
                    setCellValueEx(x, y,Color.RED, MINE);
                }
            }
            }
        showMessageDialog(Color.BLACK, "GAMEOVER", Color.RED, 36);
    }

    private void restart() {
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;
        isGameStopped = false;
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped){
            restart();
            return;
        }
        openTile(x,y);
    }
    @Override
    public void onMouseRightClick(int x, int y) { markTile(x, y); }
}