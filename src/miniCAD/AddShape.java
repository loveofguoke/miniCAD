package miniCAD;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


abstract public class AddShape {

    ArrayList<Shape> shapes;

    AddShape(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    Shape addShape(Color color, float thickness, MouseEvent e) {
        return null;
    }

    void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
}

class AddLine extends AddShape {

    AddLine(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, float thickness, MouseEvent e) {
        super.addShape(color, thickness, e);
        Line line = new Line(color, thickness, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(line);
        return line;
    }
}

class AddRectangle extends AddShape {

    AddRectangle(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, float thickness, MouseEvent e) {
        super.addShape(color, thickness, e);
        Rectangle rec = new Rectangle(color, thickness, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(rec);
        return rec;
    }

}

class AddCircle extends AddShape {

    AddCircle(ArrayList<Shape> shapes) {
        super(shapes);
    }

    @Override
    Shape addShape(Color color, float thickness, MouseEvent e) {
        super.addShape(color, thickness, e);
        Circle circle = new Circle(color, thickness, e.getX(), e.getY(), e.getX(), e.getY());
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
    Shape addShape(Color color, float thickness, MouseEvent e) {
        super.addShape(color, thickness, e);
        Text text = new Text(content, color, thickness, e.getX(), e.getY(), e.getX(), e.getY());
        shapes.add(text);
        return text;
    }

}