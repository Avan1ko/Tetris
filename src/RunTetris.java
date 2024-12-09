import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunTetris implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TETRIS");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);


        final JPanel feature_panel = new JPanel();
        frame.add(feature_panel, BorderLayout.EAST);
        final JLabel points = new JLabel(" Points: N/A");


        // Main playing area
        final TetrisCourt court = new TetrisCourt(status, points);
        frame.add(court, BorderLayout.CENTER);




        //Next queue
        final NextQueue nextQueue = new NextQueue(court.getNextQueue());
        feature_panel.setLayout(new GridLayout(2, 1));
        feature_panel.add(nextQueue);
        //point label
        feature_panel.add(points);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        // Increase speed button
//        final JButton speed = new JButton("Increase Speed");
//        speed.addActionListener(e -> court.speed(0));
//        control_panel.add(speed);

        final JButton easy = new JButton("Easy");
        easy.addActionListener(e -> court.speed(1));
        control_panel.add(easy);

        final JButton medium = new JButton("Medium");
        medium.addActionListener(e -> court.speed(2));
        control_panel.add(medium);

        final JButton hard = new JButton("Hard");
        hard.addActionListener(e -> court.speed(3));
        control_panel.add(hard);

        final JButton instr = new JButton("Instructions");
        instr.addActionListener(e -> court.instructions(instr));
        control_panel.add(instr);

        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> court.save("savefile.txt"));
        control_panel.add(save);

        final JButton load = new JButton("Load Game");
        load.addActionListener(e -> court.load("savefile.txt"));
        control_panel.add(load);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // Start game
        court.reset();
    }
}
