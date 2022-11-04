package miniCAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JOptionPane;

public class Control {

    static Model model = null;
    static View view = null;
    static String curDrawMode = "Select";
    static Shape curShape = null;
    static Color curColor = Color.BLACK;
    static private HashMap<String, AddShape> addShapeMap = new HashMap<>();

    public Control(Model m, View v) {
        model = m;
        view = v;
        ArrayList<Shape> shapes = model.getShapes();
        addShapeMap.put("Line", new AddLine(shapes));
        addShapeMap.put("Rectangle", new AddRectangle(shapes));
        addShapeMap.put("Circle", new AddCircle(shapes));
        addShapeMap.put("Text", new AddText(shapes));
    }

    static class DrawBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            curDrawMode = e.getActionCommand();
            if(curDrawMode == "Text") {
                AddText addText = (AddText)addShapeMap.get("Text");
                addText.setContent(JOptionPane.showInputDialog("Enter text:"));
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
            if(!curDrawMode.equals("Select")) {
                curShape = addShapeMap.get(curDrawMode).addShape(curColor, e);
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
            if(!curDrawMode.equals("Select")) {
                curShape.points.get(1).setLocation(e.getX(), e.getY());
                view.refresh();
            }
            System.out.println("refresh");
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
    }
}
