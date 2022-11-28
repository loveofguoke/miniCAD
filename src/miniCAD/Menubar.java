package miniCAD;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Menubar extends JMenuBar {
    Menubar() {
        super();
        // File Menu
        JMenu file = new JMenu("File(F)");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");

        // Add keyboard shortcuts
        file.setMnemonic(KeyEvent.VK_F);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        // Add actions
        openFile.addActionListener(new Control.OpenFileListener());
        saveFile.addActionListener(new Control.SaveFileListener());

        file.add(openFile);
        file.add(saveFile);
        this.add(file);

        // Help Menu
        JMenu help = new JMenu("Help(H)");
        JMenuItem tutorial = new JMenuItem("Tutorial");

        // Add keyboard shortcuts
        help.setMnemonic(KeyEvent.VK_H);
        tutorial.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));

        // Add actions
        tutorial.addActionListener(new Control.TutorialListener());

        help.add(tutorial);
        this.add(help);
    }
}
