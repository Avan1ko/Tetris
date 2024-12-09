import javax.swing.*;
public class Game {
    public static void main(String[] args) {
        // Set the game you want to run here
        Runnable game = new RunTetris();
        SwingUtilities.invokeLater(game);
    }
}