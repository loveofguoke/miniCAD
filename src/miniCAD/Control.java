package miniCAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Color;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import java.awt.Point;

public class Control {

    static final String SELECT = "Select";
    static final String LINE = "Line";
    static final String RECTANGLE = "Rectangle";
    static final String CIRCLE = "Circle";
    static final String TEXT = "Text";

    static final String BLACK = "Black";
    static final String RED = "Red";
    static final String GREEN = "Green";
    static final String YELLOW = "Yellow";
    static final String BLUE = "Blue";

    static Model model = null;
    static View view = null;
    static String curDrawMode = SELECT;
    static int curSelectMode = Shape.INSIDE;
    static Shape curDrawingShape = null;
    static Shape curSelectedShape = null;
    static Color curColor = Color.BLACK;
    static Point preMousePoint;
    static private HashMap<String, AddShape> addShapeMap = new HashMap<>();
    static private HashMap<String, Color> preSetColors = new HashMap<>();

    public Control(Model m, View v) {
        model = m;
        view = v;
        ArrayList<Shape> shapes = model.getShapes();
        addShapeMap.put(LINE, new AddLine(shapes));
        addShapeMap.put(RECTANGLE, new AddRectangle(shapes));
        addShapeMap.put(CIRCLE, new AddCircle(shapes));
        addShapeMap.put(TEXT, new AddText(shapes));

        preSetColors.put(BLACK, Color.BLACK);
        preSetColors.put(RED, Color.RED);
        preSetColors.put(GREEN, Color.GREEN);
        preSetColors.put(YELLOW, Color.YELLOW);
        preSetColors.put(BLUE, Color.BLUE);
    }

    static Shape getShapeUnderMouse(Point point) {
        for (Shape s: model.getShapes()) {
            if (s.contain(point) != Shape.OUTSIDE) {
                return s;
            }
        }
        return null;
    }

    static class DrawBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            curDrawMode = e.getActionCommand();
            System.out.println(curDrawMode);
            if(curDrawMode == TEXT) {
                AddText addText = (AddText)addShapeMap.get(TEXT);
                addText.setContent(JOptionPane.showInputDialog("Enter text:"));
            }
        }
    }

    static class ColorBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            curColor = preSetColors.get(e.getActionCommand());
            if(curDrawMode.equals(SELECT) && curSelectedShape != null) {
                curSelectedShape.setColor(curColor);
                view.refresh();
            }
        }
    }

    static class MoreColorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            curColor = JColorChooser.showDialog(null, "Choose Color", Color.black);
            if(curDrawMode.equals(SELECT) && curSelectedShape != null) {
                curSelectedShape.setColor(curColor);
                view.refresh();
            }
        }
    }

    static class ViewListener implements MouseListener, MouseMotionListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
            view.requestFocusInWindow(); // Get Focus

            if(!curDrawMode.equals(SELECT)) {
                curDrawingShape = addShapeMap.get(curDrawMode).addShape(curColor, e);
            }
            else if(curDrawMode.equals(SELECT)) {
                preMousePoint = e.getPoint();
                curSelectedShape = getShapeUnderMouse(e.getPoint());
                if(curSelectedShape != null) {
                    curSelectMode = curSelectedShape.contain(e.getPoint());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
            curDrawingShape = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            if(!curDrawMode.equals(SELECT)) {
                curDrawingShape.setPoint(1, e.getX(), e.getY());
                view.refresh();
            }
            else if(curDrawMode.equals(SELECT)) {
                if(curSelectedShape != null) {
                    if(curSelectMode == Shape.INSIDE) {
                        curSelectedShape.move(e.getX() - preMousePoint.x, e.getY() - preMousePoint.y);
                        preMousePoint = e.getPoint();
                    } else if(curSelectMode == Shape.ONPOINT1) {
                        curSelectedShape.setPoint(0, e.getX(), e.getY());
                    } else if(curSelectMode == Shape.ONPOINT2) {
                        curSelectedShape.setPoint(1, e.getX(), e.getY());
                    }
                    view.refresh();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            System.out.println(e.getKeyCode());
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if(curDrawMode.equals(SELECT) && curSelectedShape != null) {
                    model.getShapes().remove(curSelectedShape);
                    curSelectedShape = null;
                    view.refresh();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
        }
    }

    static class OpenFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("miniCAD File (*.mcad)", "mcad");
            chooser.setFileFilter(filter);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                    ArrayList<Shape> shapes = new ArrayList<>();
                    while (true) {
                        try {
                            Shape s = (Shape)input.readObject();
                            shapes.add(s);
                        }
                        catch (EOFException e1) {
                            break;
                        }
                    }
                    // reset
                    model.setShapes(shapes);
                    for(AddShape elem : addShapeMap.values()) {
                        elem.setShapes(shapes);
                    }
                    curDrawMode = SELECT;
                    curSelectedShape = null;
                    curColor = Color.BLACK;
                    view.refresh();
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(chooser, "Failed to open file");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            
        }
    }

    static class SaveFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("miniCAD File (*.mcad)", "mcad");
            chooser.setFileFilter(filter);
            if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile() + ".mcad");
                try {
                    ObjectOutputStream output = new ObjectOutputStream((new FileOutputStream(file)));
                    for (Shape s : model.getShapes()) {
                        output.writeObject(s);
                    }
                    output.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(chooser, "Failed to save file");
                }
            }  
        }
    }
}
