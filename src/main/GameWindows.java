package main;

import javax.swing.JFrame;

public class GameWindows extends JFrame {

    private JFrame jframe;

    public GameWindows(GamePanel gamePanel) {
        jframe = new JFrame("Kings and Pigs");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }
}
