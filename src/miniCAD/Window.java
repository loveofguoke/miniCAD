package miniCAD;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(View view) {
        this.setTitle("miniCAD by yyb");
        this.setJMenuBar(new Menubar());

        this.setLayout(new BorderLayout());
        this.add(new Sidebar(), BorderLayout.WEST);
        this.add(view, BorderLayout.CENTER);
        view.addMouseListener(new Control.ViewListener());
        view.addMouseMotionListener(new Control.ViewListener());
        // view

    }
}
