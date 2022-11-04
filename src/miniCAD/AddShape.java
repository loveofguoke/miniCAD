package miniCAD;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


abstract public class AddShape {

    ArrayList<Shape> shapes;

    AddShape(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    Shape addShape(Color color, MouseEvent e) {
        return null;
    }
}

class AddLine extends AddShape {

    AddLine(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, MouseEvent e) {
        // TODO Auto-generated method stub
        super.addShape(color, e);
        Line line = new Line(color, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(line);
        return line;
    }
}

class AddRectangle extends AddShape {

    AddRectangle(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, MouseEvent e) {
        // TODO Auto-generated method stub
        super.addShape(color, e);
        Rectangle rec = new Rectangle(color, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(rec);
        return rec;
    }

}

class AddCircle extends AddShape {

    AddCircle(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, MouseEvent e) {
        // TODO Auto-generated method stub
        super.addShape(color, e);
        Circle circle = new Circle(color, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(circle);
        return circle;
    }

}

class AddText extends AddShape {

    String content;

    AddText(ArrayList<Shape> shapes) {
        super(shapes);
    }

    void setContent(String content) {
        this.content = content;
    }

    @Override
    Shape addShape(Color color, MouseEvent e) {
        // TODO Auto-generated method stub
        super.addShape(color, e);
        Text text = new Text(content, color, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(text);
        return text;
    }

}