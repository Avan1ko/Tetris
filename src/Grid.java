import java.awt.*;
public class Grid {
    // field values
    private int gridWidth;
    private int gridHeight;
    private Cell[][] gridArray;
    private Tetromino currBlock;
    private int points;

    // constructor
    public Grid() {
        // visible width is 10, but we need 4 extra columns to check for block bounds
        this.gridWidth = 14;
        // visible height is 20, but we need 4 extra rows to check for block bounds
        this.gridHeight = 24;

        this.points = 0;

        this.gridArray = new Cell[gridHeight][gridWidth];
        Tetromino.fillEmptyCells(gridArray, 0, 0);
    }

    /////////////////////////////////
    //// Getter/Setter Functions ////
    /////////////////////////////////


    public Cell[][] getGridArray() {
        return gridArray;
    }

    public Tetromino getCurrBlock() {
        return this.currBlock;
    }

    public void setCurrBlock(Tetromino newBlock) {
        this.currBlock = newBlock;
    }

    // has offset
    public void spawn(Tetromino t) {
//        removeFromGrid(t);
        setCurrBlock(t);
        for (int i = 0; i < t.getBlockCoords().length; i++) {
            for (int j = 0; j < t.getBlockCoords().length; j++) {
                gridArray[i + 2][j + 2] = t.getBlockCoords()[i][j];
            }
        }
    }

    public void translate(Tetromino t, Direction dir) {
        switch (dir) {
            case LEFT -> {
                if (isTransformValid(t, dir)) {
                    removeFromGrid(t);
                    t.setXOrigin(t.getXOrigin() - 1);
                    placeInGrid(t);
                }

            }
            case RIGHT -> {
                if (isTransformValid(t, dir)) {
                    removeFromGrid(t);
                    t.setXOrigin(t.getXOrigin() + 1);
                    placeInGrid(t);
                }
            }
            case DOWN -> {
                if (isTransformValid(t,dir)) {
                    removeFromGrid(t);
                    t.setYOrigin(t.getYOrigin() + 1);
                    placeInGrid(t);
                }
            }
            default -> {
                throw new IllegalArgumentException("Please input a valid translation direction.");
            }
        }
    }

    public void rotate (Tetromino t) {
        if (isTransformValid(t, Direction.UP)) {
            removeFromGrid(t);
            t.rotate();
            placeInGrid(t);
        }
    }

    private void removeFromGrid(Tetromino t) {
        for (int i = 0; i < t.getBlockCoords().length; i++) {
            for (int j = 0; j < t.getBlockCoords().length; j++) {
                int yCoord = i + t.getYOrigin();
                int xCoord = j + t.getXOrigin();
                if (t.getBlockCoords()[i][j].isFilled()) {
                    gridArray[yCoord][xCoord] = new Cell(xCoord, yCoord, Color.BLACK, false);
                }
            }
        }
    }

    // Grid Placement Helper
    private void placeInGrid(Tetromino t) {
        for (int i = 0; i < t.getBlockCoords().length; i++) {
            for (int j = 0; j < t.getBlockCoords().length; j++) {
                int yCoord = i + t.getYOrigin();
                int xCoord = j + t.getXOrigin();
                if (t.getBlockCoords()[i][j].isFilled()) {
                    gridArray[yCoord][xCoord] = t.getBlockCoords()[i][j];
                }
            }
        }
    }


    //A transformation is valid if block does not fill the outer two rows or columns on either side afterwards
    public boolean isTransformValid(Tetromino t, Direction dir) {
        Tetromino tNew = new Tetromino(t);
        tNew.setBlockCoords(t.getBlockCoords().clone());
        switch (dir) {
            case LEFT -> {
                if (t.getXOrigin() - 1 < 2) {
                    return !isSectionFilled(t, dir) && (t.getXOrigin() - 1 >= 0);
                } else {
                    return !willInterfere(t, dir);
                }
            }
            case RIGHT -> {
                if ((t.getXOrigin() + t.getBlockCoords().length - 1) + 1 > gridWidth - 3) {
                    return !isSectionFilled(t, dir) && (t.getXOrigin() + t.getBlockCoords().length <= 13);
                } else {
                    return !willInterfere(t, dir);
                }
            }
            case DOWN -> {
                boolean active = true;
                if ((t.getYOrigin() + t.getBlockCoords().length - 1) + 1 > gridHeight - 3) {
                    active = !isSectionFilled(t, Direction.DOWN) && (t.getYOrigin() + t.getBlockCoords().length <= 23);
                }
                else {
                    active = !willInterfere(t, dir);
                }
                if (!active) {
                    currBlock.setActive(false);
                }
                return active;
            }

            case UP -> {
                if (t.getYOrigin() < 2) {
                    tNew.rotate();
                    return !isSectionFilled(tNew, Direction.UP);
                } else if (t.getYOrigin() > gridHeight - 3) {
                    tNew.rotate();
                    return !isSectionFilled(tNew, Direction.DOWN);
                } else if (t.getXOrigin() < 2) {
                    tNew.rotate();
                    return !isSectionFilled(tNew, Direction.LEFT);
                } else if (t.getXOrigin() > gridWidth - 3) {
                    tNew.rotate();
                    return !isSectionFilled(tNew, Direction.RIGHT);
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean isSectionFilled(Tetromino t, Direction dir) {
        boolean sectionFilled = false;
        int[][] firstBlocks = firstBlocksMatrix(t, dir);
        switch (dir) {
            case LEFT -> {
                for (int[] firstBlock : firstBlocks) {
                    int xCoord = firstBlock[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = firstBlock[1];

                    if (gridArray[yCoord][xCoord - 1].getXPos() < 2) {
                        sectionFilled = true;
                        break;
                    }
                }
            }
            case RIGHT -> {
                for (int[] firstBlock : firstBlocks) {
                    int xCoord = firstBlock[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = firstBlock[1];

                    if (gridArray[yCoord][xCoord + 1].getXPos() > 11) {
                        sectionFilled = true;
                        break;
                    }
                }
            }
            case DOWN -> {
                for (int[] firstBlock : firstBlocks) {
                    int xCoord = firstBlock[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = firstBlock[1];

                    if (gridArray[yCoord + 1][xCoord].getYPos() > 21) {
                        sectionFilled = true;
                        break;
                    }
                }
            }
            case UP -> {
                for (int i = 0; i < t.getBlockCoords().length; i++) {
                    if (t.getBlockCoords()[0][i].isFilled()) {
                        sectionFilled = true;
                    }
                }
            }
        }
        return sectionFilled;
    }

    // the following function checks whether a Tetromino, t, will interfere with filled blocks on the grid after a translation
    //in the direction of dir. If it will interfere, the function returns true, otherwise it returns false.
    private boolean willInterfere(Tetromino t, Direction dir) {
        boolean interfere = false;

        int[][] firstBlocks = firstBlocksMatrix(t, dir);
        switch (dir) {
            case LEFT -> {
                for (int[] xyCoords : firstBlocks) {
                    int xCoord = xyCoords[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = xyCoords[1];
                    if (gridArray[yCoord][xCoord - 1].isFilled()) {
                        interfere = true;
                        break;
                    }
                }
            }
            case RIGHT -> {

                for (int[] xyCoords : firstBlocks) {
                    int xCoord = xyCoords[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = xyCoords[1];
                    if (gridArray[yCoord][xCoord + 1].isFilled()) {
                        interfere = true;
                        break;
                    }
                }
            }
            case DOWN -> {
                for (int[] xyCoords : firstBlocks) {
                    int xCoord = xyCoords[0];
                    if (xCoord == -1) {
                        continue;
                    }
                    int yCoord = xyCoords[1];
                    if (gridArray[yCoord + 1][xCoord].isFilled()) {
                        interfere = true;
                        break;
                    }
                }
            }
            case UP -> {
                interfere = false;
            }
        }
        return interfere;
    }

    // this matrix finds the x and y positions of the first blocks that are filled based on the direction of the translation
    public int[][] firstBlocksMatrix(Tetromino t, Direction dir) {
        t.getBlockCoords();
        int[][] firstBlocksMatrix = new int[t.getBlockCoords().length][2];
        switch (dir) {
            case LEFT -> {
                for (int i = 0; i < t.getBlockCoords().length; i++) {
                    for (int j = 0; j < t.getBlockCoords().length; j++) {
                        Cell cell = t.getBlockCoords()[i][j];
                        if (cell.isFilled()) {
                            int[] coords = {cell.getXPos(), cell.getYPos()};
                            firstBlocksMatrix[i] = coords;
                            break;
                        } else {
                            int[] coords = {-1, -1};
                            firstBlocksMatrix[i] = coords;
                        }
                    }
                }
            }
            case RIGHT -> {
                for (int i = 0; i < t.getBlockCoords().length; i++) {
                    for (int j = t.getBlockCoords().length - 1; j >= 0; j--) {
                        Cell cell = t.getBlockCoords()[i][j];
                        if (cell.isFilled()) {
                            int[] coords = {cell.getXPos(), cell.getYPos()};
                            firstBlocksMatrix[i] = coords;
                            break;
                        }
                        else {
                            int[] coords = {-1, -1};
                            firstBlocksMatrix[i] = coords;
                        }
                    }
                }
            }
            case DOWN -> {
                for (int i = 0; i < t.getBlockCoords().length; i++) {
                    for (int j = t.getBlockCoords().length - 1; j >= 0; j--) {
                        Cell cell = t.getBlockCoords()[j][i];
                        if (cell.isFilled()) {
                            int[] coords = {cell.getXPos(), cell.getYPos()};
                            firstBlocksMatrix[i] = coords;
                            break;
                        }
                        else {
                            int[] coords = {-1, -1};
                            firstBlocksMatrix[i] = coords;
                        }

                    }
                }
            }
            default -> {
                throw new IllegalArgumentException("Up is not a valid direction for firstBlocksMatrix");
            }
        }
        return firstBlocksMatrix;

    }

    public int[][] isFilledMatrix() {
        int[][] isFilledMatrix = new int[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (gridArray[i][j].isFilled()) {
                    isFilledMatrix[i][j] = 1;
                }
            }
        }
        return isFilledMatrix;
    }

    public void lineClear() {
        int[][] isFilledMatrix = isFilledMatrix();
        for (int row = 2; row < gridHeight - 2; row++) {
            int sum = 0;
            for (int col = 2; col < gridWidth - 2; col++) {
                sum += isFilledMatrix[row][col];
            }
            if (sum == (gridWidth - 4)) {
                for (int col = 2; col < gridWidth - 2; col++) {
                    int x = gridArray[row][col].getXPos();
                    int y = gridArray[row][col].getYPos();
                    gridArray[row][col] = new Cell(x, y, Color.pink, false);
                    points += 100;
                }
                for (int k = row; k > 0; k--) {
                    for (int col = 0; col < gridWidth; col++) {
                        gridArray[k][col] = gridArray[k - 1][col];
                        gridArray[k][col].setYPos(gridArray[k][col].getYPos() + 1);
                    }
                }
            }
        }
    }

    public int getPoints() {
        return points;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                gridArray[i][j].draw(g);
            }
        }
    }

    public static void main(String[] args) {
//
//
//        Grid grid = new Grid();
//        Tetromino t = new Tetromino('L');
//
//
//        grid.spawn(t);
//        grid.translate(currBlock, Direction.RIGHT);
//        grid.translate(currBlock, Direction.DOWN);
//        grid.rotate(currBlock);
//
//
//        int[][] isFilledMatrix = new int[grid.gridHeight][grid.gridWidth];
//        for (int i = 0; i < grid.gridHeight; i++) {
//            for (int j = 0; j < grid.gridWidth; j++) {
//                if (grid.gridArray[i][j].isFilled()) {
//                    isFilledMatrix[i][j] = 1;
//                }
//            }
//        }
//
//        for (int i = 0; i < isFilledMatrix.length; i++) {
//            System.out.println(Arrays.toString(isFilledMatrix[i]));
//        }


    }
}

