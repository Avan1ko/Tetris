import javax.swing.*;
import java.awt.*;
import java.util.Queue;

//the following class is a component of the GUI that displays a queue of tetromino block up to 4 blocks
public class NextQueue extends JComponent {
    Queue<Tetromino> queue;
    public static final int QUEUE_WIDTH = 200;
    public static final int QUEUE_HEIGHT = 200;


    public NextQueue(Queue<Tetromino> queue) {
        this.queue = queue;
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(TetrisCourt.INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!
    }

    public void add(Tetromino block) {
        queue.add(block);
    }

    public Tetromino remove() {
        return queue.remove();
    }

    public void tick() {
        repaint();
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Cell[][] gridQueue = new Cell[4][4];
        Cell[][] cloneBlock = (queue.peek().getBlockCoords()).clone();
        Tetromino.fillEmptyCells(gridQueue,2 ,2);
        for (int i = 0; i < cloneBlock.length; i++) {
            for (int j = 0; j < cloneBlock[0].length; j++) {
                gridQueue[i][j] = cloneBlock[i][j];
            }
        }


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gridQueue[i][j].draw(g);
            }
        }

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(QUEUE_WIDTH, QUEUE_HEIGHT);
    }
}
