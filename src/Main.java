import miniCAD.*;

import javax.swing.*;

public class Main {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        Control control = new Control(model, view);

        Window window = new Window(view);
        window.setSize(WIDTH, HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}