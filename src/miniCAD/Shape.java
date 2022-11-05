package miniCAD;

import java.util.ArrayList;

import javax.swing.text.FlowView.FlowStrategy;

import java.awt.*;
import java.io.Serializable;

abstract public class Shape implements Serializable, Cloneable {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final double zero = 12.0;
    static final int OUTSIDE = -1;
    static final int INSIDE = 0;
    static final int ONPOINT1 = 1;
    static final int ONPOINT2 = 2;

    static final int NORMAL = 0;
    static final int BOLD = 1;

    static final float MINTHICKNESS = 1.0f;
    static final float MAXTHICKNESS = 7.0f;

    protected ArrayList<Point> points = new ArrayList<>(); // Remain for more points
    protected Color color;
    protected float thickness = 3.0f;
    protected int renderMode = NORMAL;

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
    protected void setThickness(float thickness) {
        this.thickness = thickness;
    }
    protected void increaseThickness() {
        this.thickness = Math.min(this.thickness + 1.0f, MAXTHICKNESS);
    }
    protected void decreaseThickness() {
        this.thickness = Math.max(this.thickness - 1.0f, MINTHICKNESS);
    }
    protected void setRenderMode(int renderMode) {
        this.renderMode = renderMode;
    }
    protected int width() {
        return Math.abs(points.get(0).x - points.get(1).x);
    }
    protected int height() {
        return Math.abs(points.get(0).y - points.get(1).y);
    }    

    protected void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 抗锯齿
        g.setColor(color);

        if(renderMode == NORMAL) {
            g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Rounding Ends
        } else if(renderMode == BOLD) {
            g.setStroke(new BasicStroke(thickness + 1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Rounding Ends
        }
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
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

        if(renderMode == BOLD) {
            // Draw key points, https://www.codenong.com/cs106019414/
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            g.setColor(Color.BLACK);
            g.setStroke((new BasicStroke(thickness + 6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
            g.drawLine(p1.x, p1.y, p1.x, p1.y);
            g.drawLine(p2.x, p2.y, p2.x, p2.y);
        }
    }

    @Override
    protected int contain(Point p) {
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Line line = new Line(this.color, p1.x, p1.y, p2.x, p2.y);
        line.setThickness(this.thickness);
        return line;
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

        if(renderMode == BOLD) {
            // Draw key points, https://www.codenong.com/cs106019414/
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            g.setColor(Color.BLACK);
            g.setStroke((new BasicStroke(thickness + 6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
            g.drawLine(p1.x, p1.y, p1.x, p1.y);
            g.drawLine(p2.x, p2.y, p2.x, p2.y);
        }
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Rectangle rec = new Rectangle(this.color, p1.x, p1.y, p2.x, p2.y);
        rec.setThickness(this.thickness);
        return rec;
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

        if(renderMode == BOLD) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            g.setColor(Color.BLACK);
            // Draw boundary, https://blog.csdn.net/qq_37663871/article/details/79632737
            g.setStroke(new BasicStroke(1.f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.f, new float[]{10,6}, 0.f));
            g.drawRect(minX(), minY(), width(), height());
            // Draw key points, https://www.codenong.com/cs106019414/
            g.setStroke((new BasicStroke(thickness + 6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
            g.drawLine(p1.x, p1.y, p1.x, p1.y);
            g.drawLine(p2.x, p2.y, p2.x, p2.y);
        }
    }

    @Override
    protected int contain(Point p) {

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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Circle circle = new Circle(this.color, p1.x, p1.y, p2.x, p2.y);
        circle.setThickness(this.thickness);
        return circle;
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

        // Reset the boundary
        FontMetrics fm = g.getFontMetrics();
        int strWidth = fm.stringWidth(content);
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        if(p1.x <= p2.x) p2.x = p1.x + strWidth;
        else p1.x = p2.x + strWidth;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Text text = new Text(this.content, this.color, p1.x, p1.y, p2.x, p2.y);
        text.setThickness(this.thickness);
        return text;
    }
}
