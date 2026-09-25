package rendering;

import geometry.*;

public class Renderer {

    private final int width;
    private final int height;

    // stores window pixels
    private final int[] pixels;

    private double fov = Math.toRadians(90.0);

    // TEST TODO: DELETE
    private Cube cube = new Cube(2);
    private double testRot = 0;

    /*
     * CONSTRUCTOR
     */
    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
    }

    /**
     * draws the vertices and edges of a mesh
     * @param mesh the geometry object to draw
     */
    private void drawMesh(Mesh mesh) {
        Vertex3D[] vertices = mesh.getVertices();
        Edge[] edges = mesh.getEdges();

        Vertex3D position = mesh.getPosition();
        Vertex3D rotation = mesh.getRotation();

        Vertex3D[] transformedVertices = new Vertex3D[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            Vertex3D vertex = vertices[i];

            // scale vertex to scale factor
            Vertex3D scaled = new Vertex3D(
                    vertex.x() * mesh.getScale(),
                    vertex.y() * mesh.getScale(),
                    vertex.z() * mesh.getScale()
            );

            // rotate around local origin
            Vertex3D rotated = rotate(
                    scaled,
                    rotation.x(),
                    rotation.y(),
                    rotation.z()
            );

            // move into world position
            transformedVertices[i] = new Vertex3D(
                    rotated.x() + position.x(),
                    rotated.y() + position.y(),
                    rotated.z() + position.z()
            );
        }

        // draw edges
        for (Edge edge : edges) {
            Vertex3D a = transformedVertices[edge.a()];
            Vertex3D b = transformedVertices[edge.b()];

            draw3DLine(
                    a,
                    b,
                    mesh.getColor()
            );
        }
    }

    /**
     * rotates a vertex around the center by provided radians
     * @param point the 3d vertex to rotate
     * @param angleX the rotation on the x-axis (radian)
     * @param angleY the rotation on the y-axis (radian)
     * @param angleZ the rotation on the z-axis (radian)
     * @return the new rotated 3d vertex
     */
    private Vertex3D rotate(Vertex3D point, double angleX, double angleY, double angleZ) {
        double x = point.x();
        double y = point.y();
        double z = point.z();

        // X rotation
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);

        double newY = y * cosX - z * sinX;
        double newZ = y * sinX + z * cosX;

        y = newY;
        z = newZ;

        // Y rotation
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);

        double newX = x * cosY - z * sinY;
        newZ = x * sinY + z * cosY;

        x = newX;
        z = newZ;

        // Z rotation
        double cosZ = Math.cos(angleZ);
        double sinZ = Math.sin(angleZ);

        newX = x * cosZ - y * sinZ;
        newY = x * sinZ + y * cosZ;

        x = newX;
        y = newY;

        return new Vertex3D(
                x,
                y,
                z
        );
    }

    /**
     * creates a 3d line between one provided point and another by projecting 3d points
     * into 2d points
     * @param a the 3d point where the line starts
     * @param b the 3d point where the line ends
     * @param color the color of the line
     */
    private void draw3DLine(Vertex3D a, Vertex3D b, Color color) {
        Vertex2D a1 = project(a);
        Vertex2D a2 = project(b);

        // check if anything is null from when we projected the points
        if (a1 == null || a2 == null) {
            // something is null; do not draw this
            return;
        }

        drawLine(
                a1.x(),
                a1.y(),
                a2.x(),
                a2.y(),
                color
        );
    }

    /**
     * takes a 3d vertex and projects it into 2d space
     * @param point the 3d vertex to project into a 2d vertex
     * @return the projected 2d vertex
     */
    private Vertex2D project(Vertex3D point) {
        if (point.z() <= 0.1) {
            // don't project this point
            // it will fuck and crash because it is so offscreen
            return null;
        }

        double focalLength = (width / 2.0) / Math.tan(fov / 2.0);

        // width / 2 puts (0, 0) in the center of the screen
        int screenX = (int) (point.x() / point.z() * focalLength + ((double) width / 2));
        int screenY = (int) (-point.y() / point.z() * focalLength + ((double) height / 2));

        // create the new vertex and return it
        return new Vertex2D(screenX, screenY);
    }

    /**
     * draws a 2d line using the Bresenham algorithm
     * @param x0 first point x coordinate
     * @param y0 first point y coordinate
     * @param x1 second point x coordinate
     * @param y1 second point y coordinate
     * @param color the color to draw the line in
     */
    private void drawLine(int x0, int y0, int x1, int y1, Color color) {
        // calculate distances
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        // determine directions
        int sx = -1;
        int sy = -1;

        if (x0 < x1) {
            sx = 1;
        }

        if (y0 < y1) {
            sy = 1;
        }

        int error = dx - dy;

        // loop drawing until we reach endpoint
        while (true) {
            setPixel(x0, y0, color);

            // check if we are at endpoint
            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * error;

            // possibly move X
            if (e2 > -dy) {
                error -= dy;
                x0 += sx;
            }

            // possibly move Y
            if (e2 < dx) {
                error += dx;
                y0 += sy;
            }
        }
    }

    /**
     * sets a pixel on the window to a specific color
     * @param x the x coordinate
     * @param y the y coordinate
     * @param color the color to set the pixel to
     */
    private void setPixel(int x, int y, Color color) {
        // check if pixel is in screen
        if (!(x >= 0 && x < width && y >= 0 && y < height)) {
            return;
        }

        int index = y * width + x;

        pixels[index] = color.r() << 16 | color.g() << 8 | color.b();
    }

    /**
     * main render loop
     */
    public void render() {
        clear();

        cube.setScale(2);
        cube.setPosition(0, 0, 15);
        cube.setRotation(testRot, testRot, testRot);
        cube.setColor(Color.GREEN);

        drawMesh(cube);

        testRot += 0.015;
    }

    /**
     * clears the screen
     */
    public void clear() {
        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {
                setPixel(x, y, Color.BLACK);
            }

        }
    }

    /*
     * GETTERS
     */

    /**
     * @return the pixel data of the entire window
     */
    public int[] getPixels() {
        return pixels;
    }

    /*
     * SETTERS
     */

    /**
     * sets the fov
     * @param degrees how many degrees the fov should be (NOT RADIANS)
     */
    public void setFov(double degrees) {
        fov = Math.toRadians(degrees);
    }
}
