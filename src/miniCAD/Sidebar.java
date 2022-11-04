package miniCAD;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    Sidebar() {
        super();
        this.setLayout(new GridLayout(0, 1));

        // Add draw buttons 
        JButton[] drawBtns = {
            new JButton("Select"),
            new JButton("Line"),
            new JButton("Rectangle"),
            new JButton("Circle"),
            new JButton("Text"),
        };
        for(JButton btn : drawBtns) {
            // Add actions
            this.add(btn);
        }

        // Set color panel
        JPanel color = new JPanel();
        color.setBorder(BorderFactory.createTitledBorder("Color Panel"));
        color.setLayout(new GridLayout(3, 0));
        JButton[] colorBtns = {
            new JButton("Black"),
            new JButton("Red"),
            new JButton("Green"),
            new JButton("Yellow"),
            new JButton("Blue"),
            new JButton("More"),
        };
        for(JButton btn : colorBtns) {
            // Add actions
            color.add(btn);
        }
        this.add(color);
    }
}
