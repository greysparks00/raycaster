package geometry;

import rendering.Color;

/**
 * base class for each shape.
 * contains all the getters and setters
 * each shape extends off of the Mesh class and
 * then creates its own vertices and edges.
 */
public class Mesh {

    // construction
    protected final Vertex3D[] vertices;
    protected final Edge[] edges;

    protected Vertex3D position = new Vertex3D(0, 0, 0);
    protected Vertex3D rotation = new Vertex3D(0, 0, 0);

    protected double scale = 1.0;

    protected Color color = Color.WHITE; // default to white color

    public Mesh(Vertex3D[] vertices, Edge[] edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    // getters
    public Vertex3D[] getVertices() {
        return vertices;
    }
    public Edge[] getEdges() {
        return edges;
    }

    public Vertex3D getPosition() {
        return position;
    }
    public Vertex3D getRotation() {
        return rotation;
    }

    public double getScale() {
        return scale;
    }

    public Color getColor() {
        return color;
    }

    // setters
    public void setPosition(double x, double y, double z) {
        position = new Vertex3D(x, y, z);
    }
    public void setRotation(double x, double y, double z) {
        rotation = new Vertex3D(x, y, z);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}