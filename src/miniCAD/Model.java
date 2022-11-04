package miniCAD;

import java.util.ArrayList;

public class Model {

    private ArrayList<Shape> shapes = new ArrayList<>();

    ArrayList<Shape> getShapes() {
        return shapes;
    }

    void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
}
