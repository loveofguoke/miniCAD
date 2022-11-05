package miniCAD;

import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.ISO;
import javax.swing.JTextArea;

import java.awt.*;
import java.io.Serializable;

abstract public class Shape implements Serializable {

    final int WIDTH = 800;
    final int HEIGHT = 600;
    double zero = 12.0;
    static final int OUTSIDE = -1;
    static final int INSIDE = 0;
    static final int ONPOINT1 = 1;
    static final int ONPOINT2 = 2;

    protected ArrayList<Point> points = new ArrayList<>(); // For more points
    protected Color color;
    protected float stroke = 3.0f;

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
        g.setStroke(new BasicStroke(stroke));
    }

    protected int contain(Point p) { // contain point p ?
        return OUTSIDE;
    }

    protected void move(int dx, int dy) {
        for (Point p : points) {
            p.translate(dx, dy);
        }
    }
    protected void setPoint(int index, int x, int y) {
        Point p = points.get(index);
        p.setLocation(x, y);
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

    @Override
    protected int contain(Point p) {
        // TODO Auto-generated method stub
        Point p1 = points.get(0);
        Point p2 = points.get(1);

        if (Math.sqrt((p1.x - p.x)*(p1.x - p.x)+(p1.y - p.y)*(p1.y - p.y)) <= zero) return ONPOINT1;
        if (Math.sqrt((p2.x - p.x)*(p2.x - p.x)+(p2.y - p.y)*(p2.y - p.y)) <= zero) return ONPOINT2;
        if (p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x) &&
            p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y) &&
            distanceToLine(p1, p2, p) <= zero) {
            return INSIDE;
        }
        return OUTSIDE;
    }

    // L: (y1-y2)x - (x1-x2)y + (x1y2-x2y1) = 0; D = abs(Ax + By + C) / sqrt(A^2 + B^2)
    protected double distanceToLine(Point p1, Point p2, Point p) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX(); 
        double y2 = p2.getY();
        double x = p.getX();
        double y = p.getY();
        double A = y1 - y2;
        double B = -(x1 - x2);
        double C = x1 * y2 - x2 * y1;
        return Math.abs(A * x + B * y + C) / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
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

    @Override
    protected int contain(Point p) {
        Point p1 = points.get(0);
        Point p2 = points.get(1);

        if (Math.sqrt((p1.x - p.x)*(p1.x - p.x)+(p1.y - p.y)*(p1.y - p.y)) <= zero) return ONPOINT1;
        if (Math.sqrt((p2.x - p.x)*(p2.x - p.x)+(p2.y - p.y)*(p2.y - p.y)) <= zero) return ONPOINT2;
        if (p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x) &&
            p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y)) return INSIDE;
        return OUTSIDE;
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

    @Override
    protected int contain(Point p) {

        // if (status == 2) {
        //     if ((beginPoint.x - point.x) * (beginPoint.x - point.x) + (beginPoint.y - point.y) * (beginPoint.y - point.y) <= ((int)width + 6) * ((int)width + 6) / 4) {
        //         return 1;
        //     }
        //     if ((endPoint.x - point.x) * (endPoint.x - point.x) + (endPoint.y - point.y) * (endPoint.y - point.y) <= ((int)width + 6) * ((int)width + 6) / 4) {
        //         return 2;
        //     }
        // }

        Point p1 = points.get(0);
        Point p2 = points.get(1);

        if (Math.sqrt((p1.x - p.x)*(p1.x - p.x)+(p1.y - p.y)*(p1.y - p.y)) <= zero) return ONPOINT1;
        if (Math.sqrt((p2.x - p.x)*(p2.x - p.x)+(p2.y - p.y)*(p2.y - p.y)) <= zero) return ONPOINT2;

        // (x-cx)^2/a^2 + (y-cy)^2/b^2 <= 1
        double cx = (p1.x + p2.x) / 2.;
        double cy = (p1.y + p2.y) / 2.;
        double a = cx - minX();
        double b = cy - minY();
        double dx = p.x - cx;
        double dy = p.y - cy;
        if((dx*dx)/(a*a) + (dy*dy)/(b*b) <= 1) return INSIDE;
        return OUTSIDE;
    }

}

class Text extends Shape {
    String content;

    Text(String content, Color color, int x1, int y1, int x2, int y2) {
        super(color, x1, y1, x2, y2);
        this.content = content;
    }

    @Override
    protected void render(Graphics2D g) {
        if(content == null) return;
        super.render(g);
        Font font = new Font("宋体", Font.BOLD, maxY() - minY());
        g.setFont(font);
        g.drawString(content, minX(), maxY());
    }

    @Override
    protected int contain(Point p) {
        Point p1 = points.get(0);
        Point p2 = points.get(1);

        if (Math.sqrt((p1.x - p.x)*(p1.x - p.x)+(p1.y - p.y)*(p1.y - p.y)) <= zero) return ONPOINT1;
        if (Math.sqrt((p2.x - p.x)*(p2.x - p.x)+(p2.y - p.y)*(p2.y - p.y)) <= zero) return ONPOINT2;
        if (p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x) &&
            p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y)) return INSIDE;
        return OUTSIDE;
    }
}
