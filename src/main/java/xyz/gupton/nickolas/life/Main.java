package xyz.gupton.nickolas.life;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        Life frame = new Life();
        frame.setTitle("The Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Life.WINDOW_WIDTH, Life.WINDOW_HEIGHT);
        frame.setVisible(true);
    }
}
