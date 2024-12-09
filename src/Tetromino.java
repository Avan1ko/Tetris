import java.awt.Color;
import java.util.Arrays;

public class Tetromino {
    //Top Left Corner of Blocks' Square Arrays
    private int xOrigin;
    private int yOrigin;
    // Degrees rotated CW relative to original orientation
    private int orientation;
    private final char blockType;
    private final Color color;
    private Cell[][] blockCoords;
    private boolean active;


    public Tetromino(Tetromino t) {
        this.xOrigin = t.getXOrigin();
        this.yOrigin = t.getYOrigin();
        this.orientation = t.getOrientation();
        this.blockType = t.getBlockType();
        this.color = t.getColor();
        this.blockCoords = t.getBlockCoords().clone();


    }

    public Tetromino(char blockType) {
        int xOffset = 2;
        int yOffset = 2;
        this.xOrigin = 2;
        this.yOrigin = 2;
        this.blockType = blockType;
        this.orientation = 0;
        this.active = true;
        switch (blockType) {
            case 'I' -> {
                this.color = Color.CYAN;
                this.blockCoords = new Cell[4][4];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                // Fill Tetromino
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][2] = new Cell(2 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][3] = new Cell(3 + xOffset, 1 + yOffset, color, true);
            }
            case 'J' -> {
                this.color = Color.BLUE;
                this.blockCoords = new Cell[3][3];
                fillEmptyCells(blockCoords,  + xOffset, yOffset);
                // Fill Tetromino
                blockCoords[0][0] = new Cell(0 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][2] = new Cell(2 + xOffset, 1 + yOffset, color, true);
            }
            case 'L' -> {
                this.color = Color.ORANGE;
                this.blockCoords = new Cell[3][3];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                blockCoords[0][2] = new Cell(2 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][2] = new Cell(2 + xOffset, 1 + yOffset, color, true);
            }
            case 'O' -> {
                this.color = Color.YELLOW;
                this.blockCoords = new Cell[2][2];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                blockCoords[0][0] = new Cell(0 + xOffset, 0 + yOffset, color, true);
                blockCoords[0][1] = new Cell(1 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
            }
            case 'S' -> {
                this.color = Color.GREEN;
                this.blockCoords = new Cell[3][3];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                blockCoords[0][1] = new Cell(1 + xOffset, 0 + yOffset, color, true);
                blockCoords[0][2] = new Cell(2 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
            }
            case 'T' -> {
                this.color = Color.MAGENTA;
                this.blockCoords = new Cell[3][3];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                blockCoords[0][1] = new Cell(1 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][0] = new Cell(0 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][2] = new Cell(2 + xOffset, 1 + yOffset, color, true);
            }
            case 'Z' -> {
                this.color = Color.RED;
                this.blockCoords = new Cell[3][3];
                fillEmptyCells(blockCoords, xOffset, yOffset);
                blockCoords[0][0] = new Cell(0 + xOffset, 0 + yOffset, color, true);
                blockCoords[0][1] = new Cell(1 + xOffset, 0 + yOffset, color, true);
                blockCoords[1][1] = new Cell(1 + xOffset, 1 + yOffset, color, true);
                blockCoords[1][2] = new Cell(2 + xOffset, 1 + yOffset, color, true);
            }
            default -> throw new IllegalArgumentException("Please input a real block type.");
        }
        ;
    }
    /////////////////////////////
    // Getter/Setter Functions //
    /////////////////////////////
    public int getOrientation() {
        return orientation;
    }

    public char getBlockType() {
        return blockType;
    }

    public Cell[][] getBlockCoords() {
        return blockCoords;
    }

    public void setBlockCoords(Cell[][] blockCoords) {
        this.blockCoords = blockCoords.clone();
    }

    public Color getColor() {
        return color;
    }

    public int getXOrigin() {
        return xOrigin;
    }

    public void setXOrigin(int xOrigin) {
        updateXPos(xOrigin - this.xOrigin);
        this.xOrigin = xOrigin;

    }

    public int getYOrigin() {
        return yOrigin;
    }

    public void setYOrigin(int yOrigin) {
        updateYPos(yOrigin - this.yOrigin);
        this.yOrigin = yOrigin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        for (int i = 0; i < blockCoords.length; i++) {
            for (int j = 0; j < blockCoords[0].length; j++) {
                blockCoords[i][j].setActive(active);
            }
        }
    }

    public void updateXPos(int offset) {
        for (int i = 0; i < blockCoords.length; i++) {
            for (int j = 0; j < blockCoords[0].length; j++) {
                blockCoords[i][j].setXPos(blockCoords[i][j].getXPos() + offset);
            }
        }
    }

    public void updateYPos(int offset) {
        for (int i = 0; i < blockCoords.length; i++) {
            for (int j = 0; j < blockCoords[0].length; j++) {
                blockCoords[i][j].setYPos(blockCoords[i][j].getYPos() + offset);
            }
        }
    }

    /*
    * Input:
    * Output:
    *
    * Desc: Helper Method to fill in arrays with empty cells
     */
    public static void fillEmptyCells (Cell[][] cellArray, int xOffset, int yOffset ) {
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[0].length; j++) {
                cellArray[i][j] = new Cell(j + yOffset, i + xOffset, Color.BLACK, false);
            }
        }
    }

    //Method to return a binary matrix of filled blocks
    public static int[][] isFilledMatrix (Cell[][] cellArray) {
        int[][] isFilledMatrix = new int[cellArray.length][cellArray[0].length];
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[0].length; j++) {
                if (cellArray[i][j].isFilled()) {
                    isFilledMatrix[i][j] = 1;
                }
            }
        }
        return isFilledMatrix;
    }

    /*
    *
    *
    * Desc: CW rotation of the block
     */
    public void rotate() {
        int n = blockCoords.length;
        //Matrix Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                Cell temp = blockCoords[i][j];
                int xTemp1 = blockCoords[i][j].getXPos();
                int yTemp1 = blockCoords[i][j].getYPos();
                int xTemp2 = blockCoords[j][i].getXPos();
                int yTemp2 = blockCoords[j][i].getYPos();

                blockCoords[i][j] = blockCoords[j][i];
                blockCoords[j][i] = temp;

                blockCoords[i][j].setXPos(xTemp1);
                blockCoords[i][j].setYPos(yTemp1);

                blockCoords[j][i].setXPos(xTemp2);
                blockCoords[j][i].setYPos(yTemp2);

            }
        }

        //Reverse elements of each row
        for (int i = 0; i < n; i++) {
            int low = 0, high = n-1;
            while (low < high) {
                Cell temp = blockCoords[i][low];

                int xTempLow = blockCoords[i][low].getXPos();
                int xTempHigh = blockCoords[i][high].getXPos();

                blockCoords[i][low] = blockCoords[i][high];
                blockCoords[i][high] = temp;

                blockCoords[i][low].setXPos(xTempLow);
                blockCoords[i][high].setXPos(xTempHigh);

                low++;
                high--;
            }
        }
        orientation = (orientation + 90) % 360;
    }
}
