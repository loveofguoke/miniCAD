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

    static final String DefaultDir = "./";

    static Model model = null;
    static View view = null;
    static String curDrawMode = SELECT;
    static int curSelectMode = Shape.INSIDE;
    static Shape curDrawingShape = null;
    static Shape curSelectedShape = null;
    static Color curColor = Color.BLACK;
    static float curThickness = 3.0f;
    static Point preMousePoint;
    static Point curMousePoint;
    static Shape curClonedShape = null;
    static boolean textEmpty = true;
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
        preSetColors.put(GREEN, new Color(0, 193, 80));
        preSetColors.put(YELLOW, new Color(240, 178, 27));
        preSetColors.put(BLUE, new Color(0, 127, 246));
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
            if(curSelectedShape != null) {
                curSelectedShape.setRenderMode(Shape.NORMAL);
                curSelectedShape = null;
                view.refresh();
            }
            if(curDrawMode == TEXT) {
                AddText addText = (AddText)addShapeMap.get(TEXT);
                String newContent = JOptionPane.showInputDialog("Enter text:", addText.content);
                if(newContent != null) {
                    addText.setContent(newContent);
                    textEmpty = false;
                }
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

    static class ViewListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
            // Click right button, switch to select mode
            if(curDrawingShape == null && e.getButton() == MouseEvent.BUTTON3) {
                curDrawMode = SELECT;
            } 
            // Double click left button, modify text content
            else if(curSelectedShape instanceof Text && e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) { 
                Text text = (Text)curSelectedShape;
                String newContent = JOptionPane.showInputDialog("Enter new text:", text.content);
                if(newContent != null) {
                    text.setContent(newContent);
                } 
                view.refresh();
            }
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

            // Left button
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(!curDrawMode.equals(SELECT)) {
                    if(!(curDrawMode.equals(TEXT) && textEmpty)) {
                        curDrawingShape = addShapeMap.get(curDrawMode).addShape(curColor, curThickness, e);
                    }
                }
                else if(curDrawMode.equals(SELECT)) {
                    boolean isRefresh = false;
    
                    if(curSelectedShape != null) {
                        curSelectedShape.renderMode = Shape.NORMAL;
                        isRefresh = true;
                    }
    
                    preMousePoint = e.getPoint();
                    curSelectedShape = getShapeUnderMouse(e.getPoint());
                    if(curSelectedShape != null) {
                        curSelectMode = curSelectedShape.contain(e.getPoint());
                        curSelectedShape.setRenderMode(Shape.BOLD);
                        isRefresh = true;
                    }
    
                    if(isRefresh) view.refresh();
                }
            }

            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
            if(e.getButton() == MouseEvent.BUTTON1) {
                curDrawingShape = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

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
            curMousePoint = e.getPoint();
            // TODO 鼠标悬浮在图形边界时突出显示
            System.out.println("X: " + e.getX() + ", " + "Y: " + e.getY());
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            System.out.println(e.getKeyCode());
            if(curDrawMode.equals(SELECT) && curSelectedShape != null) {
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    model.getShapes().remove(curSelectedShape);
                    curSelectedShape = null;
                } else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                    curSelectedShape.decreaseThickness();
                } else if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
                    curSelectedShape.increaseThickness();
                } else if(e.getKeyCode() == KeyEvent.VK_S) {
                    // TODO
                } else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    try {
                        curClonedShape = (Shape)curSelectedShape.clone();
                    }
                    catch (CloneNotSupportedException e1) {
                        System.out.println("Failed to clone shape");
                    }
                } else if(e.getKeyCode() == KeyEvent.VK_T) { // move to top layer
                    model.getShapes().remove(curSelectedShape);
                    model.getShapes().add(0, curSelectedShape);
                }
                view.refresh();
            }
            if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                if(curClonedShape != null) {
                    try {
                        Shape newShape = (Shape)curClonedShape.clone();
                        newShape.move(curMousePoint.x - newShape.minX(), curMousePoint.y - newShape.minY());
                        model.getShapes().add(newShape);
                    }
                    catch (CloneNotSupportedException e1) {
                        System.out.println("Failed to clone shape");
                    }
                }
                view.refresh();
            }
            if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_L) {
                model.getShapes().clear();
                curDrawingShape = null;
                curSelectedShape = null;
                view.refresh();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            float newThickness = curThickness - e.getWheelRotation();
            curThickness = newThickness < Shape.MINTHICKNESS ? Shape.MINTHICKNESS 
                         : newThickness > Shape.MAXTHICKNESS ? Shape.MAXTHICKNESS 
                         : newThickness;
        }
    }

    static class OpenFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("miniCAD File (*.mcad)", "mcad");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File(DefaultDir));
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
                    curDrawingShape = null;
                    curSelectedShape = null;
                    curClonedShape = null;
                    curColor = Color.BLACK;
                    curThickness = 3.0f;
                    textEmpty = true;
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
            if(curSelectedShape != null) {
                curSelectedShape.setRenderMode(Shape.NORMAL);
                curSelectedShape = null;
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("miniCAD File (*.mcad)", "mcad");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File(DefaultDir));
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

    static class TutorialListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            final String message = "miniCAD written by yyb in 2022\n"
                                 + "Right btn: Switch to select mode\n"
                                 + "Double left btn: Modify text content\n"
                                 + "Mouse wheel: Modify brush thickness\n"
                                 + "Backspace: Delete a shape\n"
                                 + "-/=:       Decrease/Increase thickness\n"
                                 + "T:         Move a shape to the top layer\n"
                                 + "Ctrl+C:    Clone a shape\n"
                                 + "Ctrl+V:    Paste the cloned shape\n"
                                 + "Ctrl+L:    Clear the canvas";
			JOptionPane.showMessageDialog(null, message);
        }
    }

    static class CloseWindowListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e){
            
            int option = JOptionPane.showConfirmDialog(null,"File not saved yet, sure to exit?","",JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}
