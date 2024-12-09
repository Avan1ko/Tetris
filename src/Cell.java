import java.awt.*;
public class Cell {
    private int xPos;
    private int yPos;
    private Color color;
    private boolean isFilled;
    private boolean isActive;

    public static final int SIDE_LENGTH = 25;

    public Cell(int xPos, int yPos, Color color, boolean isFilled) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
        this.isFilled = isFilled;
        this.isActive = true;
    }

    /////////////////////////////////
    //// Getter/Setter Functions ////
    /////////////////////////////////
    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /////////////
    //// GUI ////
    /////////////
    public void draw(Graphics g) {
        g.setColor(color);
        if(xPos > 1 && xPos < 12 && yPos > 1 && yPos < 22 ) {
            g.fillRect(this.getXPos() * 30, this.getYPos() * 30, SIDE_LENGTH, SIDE_LENGTH);
        }
    }
}
