package rendering;

import geometry.Edge;
import geometry.Vertex2D;
import geometry.Vertex3D;

import java.util.Arrays;

public class Renderer {

    // dimensions
    private final int width;
    private final int height;

    // store pixels
    private final int[] pixels;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
    }

    private void setPixel(int x, int y, int r, int g, int b) {
        // check if pixel is in screen
        if (!(x >= 0 && x < width && y >= 0 && y < height)) {
            return;
        }

        int index = y * width + x;

        pixels[index] = r << 16 | g << 8 | b;
    }

    // Bresenham algorithm
    private void drawLine(int x0, int y0, int x1, int y1, int r, int g, int b) {
        // calculate distances
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        // determine directions
        int sx = -1;
        int sy = -1;

        if (x0 < x1) {
            sx = 1;
        }
        ;
        if (y0 < y1) {
            sy = 1;
        }
        ;

        int error = dx - dy;

        // loop drawing until we reach endpoint
        while (true) {
            setPixel(x0, y0, r, g, b);

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

    private void drawSquare(int originX, int originY, int scaler) {
        Vertex2D v1 = new Vertex2D(originX, originY);
        Vertex2D v2 = new Vertex2D(originX + scaler, originY);
        Vertex2D v3 = new Vertex2D(originX, originY + scaler);
        Vertex2D v4 = new Vertex2D(originX + scaler, originY + scaler);

        Edge e1 = new Edge(v1, v2);
        Edge e2 = new Edge(v1, v3);
        Edge e3 = new Edge(v2, v4);
        Edge e4 = new Edge(v3, v4);

        // draw edges
        for (Edge edge : Arrays.asList(e1, e2, e3, e4)) {
            drawLine(
                    edge.a().x(),
                    edge.a().y(),
                    edge.b().x(),
                    edge.b().y(),
                    255, 0, 0
            );
        }
    }

    // turns a 3d point into a 2d point
    private Vertex2D project(Vertex3D point) {
        int focalLength = 500; // TODO: MAKE NOT 500

        // width / 2 puts (0, 0) in the center of the screen
        int screenX = (int) (point.x() / point.z() * focalLength + ((double) width / 2));
        int screenY = (int) (point.y() / point.z() * focalLength + ((double) height / 2));

        // create the new vertex and return it
        return new Vertex2D(screenX, screenY);
    }

    private void draw3DLine(Vertex3D a, Vertex3D b) {
        Vertex2D a1 = project(a);
        Vertex2D a2 = project(b);

        drawLine(
                a1.x(),
                a1.y(),
                a2.x(),
                a2.y(),
                255, 0, 0
        );
    }

    public void render() {
        clear();

//        // draw test square
//        drawLine(100, 100, 800, 500, 255, 255, 255);
//        drawLine(800, 100, 100, 500, 255, 0, 0);
//        drawLine(500, 50, 500, 700, 0, 255, 0);
//        drawLine(50, 400, 950, 400, 0, 0, 255);
//        drawSquare(width / 2, height / 2, 100);

        draw3DLine(
                new Vertex3D(-1, 0, 5),
                new Vertex3D(1, 0, 5)
        );
    }

    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setPixel(x, y, 0, 0, 0);
            }
        }
    }

    public int[] getPixels() {
        return pixels;
    }
}
