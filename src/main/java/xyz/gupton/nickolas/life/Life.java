package xyz.gupton.nickolas.life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

public class Life extends JFrame {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int CELL_WIDTH = 5;
    public static final int CELL_HEIGHT = 5;
    public static final int NUM_COLS = WINDOW_WIDTH / CELL_WIDTH;
    public static final int NUM_ROWS = WINDOW_HEIGHT / CELL_HEIGHT;
    public static final int[][] CELLS = new int[NUM_COLS][NUM_ROWS];
    public static final int[][] COPY = new int[NUM_COLS][NUM_ROWS];

    final Timer TIMER = new Timer(100, new TimerListener());

    public static boolean stopGraphics = false;
    public static boolean initialize = true;
    public static JSlider threshold;

    public Life() {
        TIMER.start();
        DrawingPanel p = new DrawingPanel();

        JButton stop = new JButton("Start/Stop");
        stop.addActionListener(e -> stopGraphics = !stopGraphics);

        JButton init = new JButton("Initialize");
        init.addActionListener(e -> {
            initialize = true;
            stopGraphics = true;
        });

        threshold = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        p.add(init);
        p.add(stop);
        p.add(threshold);
        this.add(p);
    }

    static class DrawingPanel extends JPanel {
        private final Random RAND = new Random();
        private double fillRate = 0.92;

        public DrawingPanel() {
            initializeCells();
        }

        public void initializeCells() {
            for (int x = 0; x < NUM_COLS; x++) {
                for (int y = 0; y < NUM_ROWS; y++) {
                    CELLS[x][y] = Math.random() > fillRate ? 1 : 0;
                }
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponents(g);
            if (initialize) {
                fillRate = threshold.getValue() / 100.0;
                initializeCells();
            }

            initialize = false;
            drawGrid(g);


            if (!stopGraphics) {
                createCopy();
                updateGrid();
            }
        }


        private void updateGrid() {
            for (int x = 0; x < NUM_COLS; x++) {
                System.arraycopy(COPY[x], 0, CELLS[x], 0, NUM_ROWS);
            }
        }

        private void createCopy() {
            for (int x = 0; x < NUM_COLS; x++) {
                for (int y = 0; y < NUM_ROWS; y++) {
                    COPY[x][y] = CELLS[x][y];
                    int count = getNeighborCount(x, y);
                    if (CELLS[x][y] == 1 && count < 2) {
                        COPY[x][y] = 0;
                    }
                    if (CELLS[x][y] == 1 && (count == 2 || count == 3)) {
                        COPY[x][y] = 1;
                    }
                    if (CELLS[x][y] == 1 && count > 3) {
                        COPY[x][y] = 0;
                    }
                    if (CELLS[x][y] == 0 && count == 3) {
                        COPY[x][y] = 1;
                    }
                }
            }
        }

        private void drawGrid(Graphics g) {
            g.drawRect(0, 0, Life.WINDOW_WIDTH, Life.WINDOW_HEIGHT);
            for (int x = 0; x < NUM_COLS; x++) {
                for (int y = 0; y < NUM_ROWS; y++) {
                    int i = x * CELL_WIDTH;
                    int o = y * CELL_HEIGHT;
                    if (CELLS[x][y] == 1) {
                        g.setColor(Color.BLACK);
                        g.fillRect(i, o, CELL_WIDTH, CELL_HEIGHT);
                        g.drawRect(i, o, CELL_WIDTH, CELL_HEIGHT);
                    }

                    if (CELLS[x][y] == 0) {
                        if (RAND.nextFloat() < 0.0001) {
                            CELLS[x][y] = 1;
                        }
                    }
                }
            }
        }

        public static int getNeighborCount(int x, int y) {
            int xMinus = (x - 1) % NUM_COLS;
            if (xMinus == -1) {
                xMinus = NUM_COLS - 1;
            }
            int xPlus = (x + 1) % NUM_COLS;

            int yMinus = (x - 1) % NUM_ROWS;
            if (yMinus == -1) {
                yMinus = NUM_ROWS - 1;
            }
            int yPlus = (y + 1) % NUM_ROWS;

            return CELLS[xMinus][yMinus]
                    + CELLS[x][yMinus]
                    + CELLS[xPlus][yMinus]
                    + CELLS[xMinus][y]
                    + CELLS[xPlus][y]
                    + CELLS[xMinus][yPlus]
                    + CELLS[x][yPlus]
                    + CELLS[xPlus][yPlus];

        }
    }

    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

}
