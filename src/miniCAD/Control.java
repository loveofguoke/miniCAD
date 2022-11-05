package miniCAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import java.awt.Color;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JColorChooser;
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
    static Shape curShape = null;
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

    static class ViewListener implements MouseListener, MouseMotionListener {

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
            if(!curDrawMode.equals(SELECT)) {
                curShape = addShapeMap.get(curDrawMode).addShape(curColor, e);
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
            curShape = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            if(!curDrawMode.equals(SELECT)) {
                curShape.setPoint(1, e.getX(), e.getY());
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
    }
}
