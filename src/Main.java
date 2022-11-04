import miniCAD.*;

import javax.swing.*;

public class Main {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public static void main(String[] args) {
        Window window = new Window();
        window.setSize(WIDTH, HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}