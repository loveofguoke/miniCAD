package miniCAD;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window() {
        this.setTitle("miniCAD by yyb");
        this.setJMenuBar(new Menubar());

        this.setLayout(new BorderLayout());
        this.add(new Sidebar(), BorderLayout.WEST);
    }
}
