import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class TetrisCourt extends JPanel {
    // the state of the game logic
    private Grid grid; // the game board
    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private final JLabel pointLabel;
    private int timerState;
    private NextQueue nextQueue;

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 1200;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    private int dropSpeed = 3500;

    public TetrisCourt(JLabel status, JLabel pointLabel) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        grid = new Grid();
        grid.setCurrBlock(randomBlock());
        grid.spawn(grid.getCurrBlock());

        nextQueue = new NextQueue(new LinkedList<Tetromino>());

        nextQueue.add(randomBlock());
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    grid.translate(grid.getCurrBlock(), Direction.LEFT);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    grid.translate(grid.getCurrBlock(), Direction.RIGHT);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    grid.translate(grid.getCurrBlock(), Direction.DOWN);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    grid.rotate(grid.getCurrBlock());
                }
            }
        });

        this.status = status;
        this.pointLabel = pointLabel;
    }

    /**
     * (Re-)set the game to its initial state.
     */


    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            repaint();

            // advance the square and snitch in their current direction.
            timerState++;

            if (timerState > dropSpeed / INTERVAL) {
                grid.translate(grid.getCurrBlock(), Direction.DOWN);
                timerState = 0;
            }

            if(!grid.getCurrBlock().isActive()) {
                Tetromino newBlock = nextQueue.remove();
                nextQueue.add(randomBlock());
                grid.setCurrBlock(newBlock);
                grid.spawn(newBlock);
                int oldPt = grid.getPoints();
                grid.lineClear();
                if (grid.getPoints() > oldPt) {
                    nextQueue.add(new Tetromino('T'));
                    nextQueue.add(new Tetromino('T'));
                    nextQueue.add(new Tetromino('T'));
                }
                pointLabel.setText(" Points: " + grid.getPoints());
            }
            boolean filled = false;
            for (int i = 0; i < 10; i++) {
                filled = grid.getGridArray()[5][i].isFilled() && !grid.getGridArray()[5][i].isActive();
                if (filled) {
                    break;
                }
            }
            if (filled) {
                playing = false;
                status.setText("YOU LOSE!");
            } else if (grid.getPoints() > 3999) {
                playing = false;
                status.setText("YOU WIN!");
            }

            // update the display
            repaint();
        }

    }

    public void reset() {
        grid = new Grid();
        grid.setCurrBlock(randomBlock());

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void speed(int mode) {
        if (mode == 0) {
            if (dropSpeed > 550) {
                this.dropSpeed -= 500;
            }
        }
        else if (mode == 1) {
            reset();
            this.dropSpeed = 1750;
        }
        else if (mode == 2) {
            reset();
            this.dropSpeed = 500;
        }
        else if (mode == 3) {
            reset();
            this.dropSpeed = 200;
        }

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();

    }

    public void instructions(JComponent instr) {
        String instrString =
        "Tetris is a game that deals with falling four block shapes called tetrominoes. You can rotate a tetromino\n" +
        "CW by clicking the up arrow. The other arrow keys can be used for moving the piece around. The piece \n" +
        "automatically falls down after a certain time, so work fast! You can change modes by selecting from the \n" +
        "menu above. Each line counts as 1000 pts, you win when you cross 5000. Good Luck and Have Fun!\n" +
                "PS: clearing a line gives you the best piece (T) three times in a row!";
        JOptionPane.showMessageDialog(instr, instrString);
        repaint();
    }

    public void save(String fileName) {
        //This creates a new file writer
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            for (int i = 0; i < this.grid.getGridArray().length; i++) {
                for (int j = 0; j < this.grid.getGridArray()[i].length; j++) {
                    writer.append(String.valueOf(this.grid.getGridArray()[i][j].getXPos()));
                    writer.append(",");
                    writer.append(String.valueOf(this.grid.getGridArray()[i][j].getYPos()));
                    writer.append(",");
                    writer.append(String.valueOf(this.grid.getGridArray()[i][j].getColor().getRGB()));
                    writer.append(",");
                    writer.append(String.valueOf(this.grid.getGridArray()[i][j].isFilled()));
                    writer.append("\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file");
        }
    }

    public void load(String filename) {
        //This creates a new file reader
        FileReader reader = null;
        try {
            reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int i = 0;
            int j = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArr = line.split(",");
                int xPos = Integer.parseInt(lineArr[0]);
                int yPos = Integer.parseInt(lineArr[1]);
                int color = Integer.parseInt(lineArr[2]);
                boolean filled = Boolean.parseBoolean(lineArr[3]);
                this.grid.getGridArray()[i][j].setXPos(xPos);
                this.grid.getGridArray()[i][j].setYPos(yPos);
                this.grid.getGridArray()[i][j].setColor(new Color(color));
                this.grid.getGridArray()[i][j].setFilled(filled);
                j++;
                //This allows for the 2-D Array to be parsed properly
                if (j == 14) {
                    j = 0;
                    i++;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading file");
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    public int getPoints() {
        return grid.getPoints();
    }

    public Tetromino randomBlock() {
        int randomSeed = (int) (Math.random() * 7);
        char[] blockTypeArr = {'I', 'J', 'L', 'O', 'S', 'T', 'Z'};
        return new Tetromino(blockTypeArr[randomSeed]);
    }

    public Queue<Tetromino> getNextQueue() {
        return nextQueue.queue;
    }

    // how would i create an intput output stream to save the game state?

}
