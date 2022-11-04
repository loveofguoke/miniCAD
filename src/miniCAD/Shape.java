package miniCAD;

import java.util.ArrayList;
import java.awt.*;


abstract public class Shape {

    final int WIDTH = 800;
    final int HEIGHT = 600;

    protected ArrayList<Point> points = new ArrayList<>(); // For more points
    protected Color color;

    Shape(Color color, int x1, int y1, int x2, int y2) {
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
        setColor(color);
    }

    protected int minX() {
        int min = WIDTH;
        for (Point p : points) {
            min = Math.min(min, p.x);
        }
        return min;
    }
    protected int minY() {
        int min = HEIGHT;
        for (Point p : points) {
            min = Math.min(min, p.y);
        }
        return min;
    }
    protected int maxX() {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.x);
        }
        return max;
    }
    protected int maxY() {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.y);
        }
        return max;
    }
    protected void setColor(Color color) {
        this.color = color;
    }
    protected int width() {
        return Math.abs(points.get(0).x - points.get(1).x);
    }
    protected int height() {
        return Math.abs(points.get(0).y - points.get(1).y);
    }    

    protected void render(Graphics2D g) {
        g.setColor(color);
    }


}

class Line extends Shape {

    Line(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
    }


    @Override
    protected void render(Graphics2D g) {
        super.render(g);
        g.drawLine(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);


    }




}

class Rectangle extends Shape {

    Rectangle(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
        
    }





    @Override
    protected void render(Graphics2D g) {
        super.render(g);
        g.drawRect(minX(), minY(), width(), height());
    }

}

class Circle extends Shape {

    Circle(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
        
    }

    @Override
    protected void render(Graphics2D g) {
        super.render(g);
        g.drawOval(minX(), minY(), width(), height());

    }

}

class Text extends Shape {

    Text(Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
        
    }


    @Override
    protected void render(Graphics2D g) {
        super.render(g);
    }
}
