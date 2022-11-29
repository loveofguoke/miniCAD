package miniCAD;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(View view) {
        this.setTitle("miniCAD by yyb");
        ImageIcon imageIcon = new ImageIcon("img/icon.jpg");
        Image image = imageIcon.getImage();
        this.setIconImage(image);
        this.setJMenuBar(new Menubar());

        this.setLayout(new BorderLayout());
        this.add(new Sidebar(), BorderLayout.WEST);
        view.addMouseListener(new Control.ViewListener());
        view.addMouseMotionListener(new Control.ViewListener());
        view.addKeyListener(new Control.ViewListener());
        this.add(view, BorderLayout.CENTER);

        this.addWindowListener(new Control.CloseWindowListener());
    }
}
