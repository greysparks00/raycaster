package rendering;

import camera.Camera;
import geometry.*;
import misc.Color;
import rendering.font.FontRenderer;
import scene.Scene;

public class Renderer {

    private final FontRenderer fontRenderer = new FontRenderer("assets/fonts/googlesans.ttf", 24f);

    private final Camera camera;

    private final int width;
    private final int height;

    // stores window pixels
    private final int[] pixels;

    // the closest a line/point can be to the camera so it still gets drawn
    private static final double NEAR_PLANE = 0.1;

    /*
     * CONSTRUCTOR
     */
    public Renderer(Camera camera, int width, int height) {
        this.camera = camera;
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
    }

    /*

    DRAWING METHODS

     */

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
            Vertex3D worldVertex = new Vertex3D(
                    rotated.x() + position.x(),
                    rotated.y() + position.y(),
                    rotated.z() + position.z()
            );

            transformedVertices[i] = worldToCamera(worldVertex);
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
     * creates a 3d line between one provided point and another by projecting 3d points
     * into 2d points
     * @param a the 3d point where the line starts
     * @param b the 3d point where the line ends
     * @param color the color of the line
     */
    private void draw3DLine(Vertex3D a, Vertex3D b, Color color) {
        boolean aVisible = a.z() >= NEAR_PLANE;
        boolean bVisible = b.z() >= NEAR_PLANE;

        /*
        check if points are visible
         */

        // no points are visible
        if (!aVisible && !bVisible) {
            return;
        }

        // point a is not visible
        if (!aVisible) {
            a = intersectNearPlane(a, b);
        }

        // point b is not visible
        if (!bVisible) {
            b = intersectNearPlane(b, a);
        }

        // change to world space
        Vertex2D a1 = project(a);
        Vertex2D a2 = project(b);

        // draw it
        drawLine(
                a1.x(),
                a1.y(),
                a2.x(),
                a2.y(),
                color
        );
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
    public void setPixel(int x, int y, Color color) {
        // check if pixel is in screen
        if (!(x >= 0 && x < width && y >= 0 && y < height)) {
            return;
        }

        pixels[y * width + x] = color.toRGB();
    }

    public void setPixel(int x, int y, int r, int g, int b) {
        // check if pixel is in screen
        if (!(x >= 0 && x < width && y >= 0 && y < height)) {
            return;
        }

        pixels[y * width + x] = (r << 16) | (g << 8) | b;
    }

    /**
     * blends a pixels color according to alpha
     * @param x the x position
     * @param y the y position
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @param alpha the alpha value
     */
    public void blendPixel(int x, int y, int r, int g, int b, int alpha) {
        // output = source * alpha + destination * (1 - alpha)

        // bounds check
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        // fully transparent
        if (alpha <= 0) {
            return;
        }

        // fully visible
        if (alpha >= 255) {
            setPixel(x, y, r, g, b);
            return;
        }

        int index = y * width + x;

        // existing framebuffer color
        int existing = pixels[index];

        int dstR = (existing >> 16) & 0xFF;
        int dstG = (existing >> 8) & 0xFF;
        int dstB = existing & 0xFF;

        // convert 0-255 alpha into 0.0-1.0
        double a = alpha / 255.0;

        // blend source color with existing destination color
        int outR = (int) (r * a + dstR * (1.0 - a));
        int outG = (int) (g * a + dstG * (1.0 - a));
        int outB = (int) (b * a + dstB * (1.0 - a));

        // pack rgb back into framebuffer
        pixels[index] = (outR << 16) | (outG << 8) | outB;
    }

    /*

    PROJECTION METHODS

     */

    /**
     * takes a 3d vertex and projects it into 2d space
     * @param point the 3d vertex to project into a 2d vertex
     * @return the projected 2d vertex
     */
    private Vertex2D project(Vertex3D point) {
        double focalLength = (width / 2.0) / Math.tan(camera.getFov() / 2.0);

        // width / 2 puts (0, 0) in the center of the screen
        int screenX = (int) (point.x() / point.z() * focalLength + ((double) width / 2));
        int screenY = (int) (-point.y() / point.z() * focalLength + ((double) height / 2));

        // create the new vertex and return it
        return new Vertex2D(screenX, screenY);
    }

    /**
     * takes a 3d point and returns the position and rotation relative to the camera
     * @param point the 3d point in the world
     * @return the camera position of the point
     */
    private Vertex3D worldToCamera(Vertex3D point) {
        Vertex3D cameraPosition = camera.getPosition();
        Vertex3D cameraRotation = camera.getRotation();

        double x = point.x() - cameraPosition.x();
        double y = point.y() - cameraPosition.y();
        double z = point.z() - cameraPosition.z();

        Vertex3D relative = new Vertex3D(x, y, z);

        return rotate(
                relative,
                -cameraRotation.x(),
                -cameraRotation.y(),
                -cameraRotation.z()
        );
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

    /*

    HELPER METHODS

     */

    /**
     * finds the 3d vertex where the line segment (a, b) crosses the
     * near plane point
     * @param a point a
     * @param b point b
     * @return the 3d vertex where the line segment crosses the near plane
     */
    private Vertex3D intersectNearPlane(Vertex3D a, Vertex3D b) {
        // P(t) = A + t(B - A)
        double t = (NEAR_PLANE - a.z()) / (b.z() - a.z());

        // find where the line crosses near plane
        double x = a.x() + t * (b.x() - a.x());
        double y = a.y() + t * (b.y() - a.y());

        return new Vertex3D(x, y, NEAR_PLANE);
    }

    /**
     * main render loop
     */
    public void render(Scene scene) {
        clear();

        // loop through all objects in scene and draw them
        for (Mesh mesh : scene.getObjects()) {
            drawMesh(mesh);
        }

        fontRenderer.drawText(this, "FPS: " + DebugStats.getFps(), 0, 0, Color.WHITE);
        fontRenderer.drawText(this, "GREEN", 20, 60, Color.GREEN);
        fontRenderer.drawText(this, "WHITE", 20, 100, Color.WHITE);
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
}
