package miniCAD;

import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;

public class View extends JPanel {

    public View(Model model) {
        this.model = model;
    }

    void refresh() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Shape> shapes = model.getShapes();
        for (Shape s : shapes) {
            s.render((Graphics2D) g);
        }
    }

    Model model;
}
