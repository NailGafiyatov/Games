package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            } else {
                return;
            }
        }
        if (canUserMove() == false) {
            gameOver();
            return;
        } else if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if (key == Key.DOWN) {
            moveDown();
            drawScene();
        } else if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if (key == Key.UP) {
            moveUp();
            drawScene();
        }
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(y, x, gameField[x][y]);
            }
        }
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048) {
            win();
        }
        
        int i;
        int j;
        do {
            i = getRandomNumber(SIDE);
            j = getRandomNumber(SIDE);
        } while (gameField[j][i] != 0);
        if (getRandomNumber(10) == 9) {
            gameField[j][i] = 4;
        } else {
            gameField[j][i] = 2;
        }
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0:
                return Color.WHITE;
            case 2:
                return Color.RED;
            case 4:
                return Color.BLUE;
            case 8:
                return Color.ORANGE;
            case 16:
                return Color.YELLOW;
            case 32:
                return Color.GREEN;
            case 64:
                return Color.PURPLE;
            case 128:
                return Color.BLUEVIOLET;
            case 256:
                return Color.GREENYELLOW;
            case 512:
                return Color.INDIANRED;
            case 1024:
                return Color.OLIVE;
            case 2048:
                return Color.LIGHTYELLOW;
            default:
                return Color.NONE;
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value > 0) {
            setCellValueEx(x, y, getColorByValue(value), "" + String.valueOf(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    private boolean compressRow(int[] row) {
        int insertPosition = 0;
        boolean result = false;
        for (int x = 0; x < SIDE; x++) {
            if (row[x] > 0) {
                if (x != insertPosition) {
                    row[insertPosition] = row[x];
                    row[x] = 0;
                    result = true;
                }
                insertPosition++;
            }
        }
        return result;
    }

    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int x = 0; x < row.length - 1; x++) {
            if (row[x] != 0 && row[x] == row[x + 1]) {
                row[x] += row[x + 1];
                row[x + 1] = 0;
                result = true;
                score += row[x];
                setScore(score);
            }
        }
        return result;
    }

    private void moveLeft() {
        boolean isNewNumberNeeded = false;
        for (int[] row : gameField) {
            boolean wasCompressed = compressRow(row);
            boolean wasMerged = mergeRow(row);
            if (wasMerged) {
                compressRow(row);
            }
            if (wasCompressed || wasMerged) {
                isNewNumberNeeded = true;
            }
        }
        if (isNewNumberNeeded) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] result = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                result[j][SIDE - 1 - i] = gameField[i][j];
            }
        }
        gameField = result;
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField.length; y++) {
                if (gameField[y][x] > max) {
                    max = gameField[y][x];
                }
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "YOU WIN", Color.GREEN, 64);
    }


    private boolean canUserMove() {
        boolean result = false;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {
                    result = true;
                } else if (y < SIDE - 1 && gameField[y][x] == gameField[y + 1][x]) {
                    result = true;
                } else if ((x < SIDE - 1) && gameField[y][x] == gameField[y][x + 1]) {
                    result = true;
                }
            }
        }
        return result;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.NONE,"GAMEOVER", Color.BLACK, 64);
    }
}
        
