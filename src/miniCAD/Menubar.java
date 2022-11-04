package miniCAD;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menubar extends JMenuBar {
    Menubar() {
        super();
        JMenu file = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        // Add actions
        file.add(openFile);
        file.add(saveFile);
        this.add(file);
    }
}
