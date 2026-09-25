package rendering;

import geometry.Edge;
import geometry.Vertex2D;
import geometry.Vertex3D;

public class Renderer {

    // dimensions
    private final int width;
    private final int height;

    // store pixels
    private final int[] pixels;

    private double rotY = 0;

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

        if (y0 < y1) {
            sy = 1;
        }


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
        Vertex2D[] vertices = {
                new Vertex2D(originX, originY),
                new Vertex2D(originX + scaler, originY),
                new Vertex2D(originX, originY + scaler),
                new Vertex2D(originX + scaler, originY + scaler)
        };

        Edge[] edges = {
                new Edge(0, 1),
                new Edge(0, 2),
                new Edge(1, 3),
                new Edge(2, 3)
        };

        // draw edges
        for (Edge edge : edges) {
            Vertex2D a = vertices[edge.a()];
            Vertex2D b = vertices[edge.b()];

            drawLine(
                    a.x(),
                    a.y(),
                    b.x(),
                    b.y(),
                    255, 0, 0
            );
        }
    }

    private void drawCube(double centerX, double centerY, double centerZ, double size, double rotationX, double rotationY, double rotationZ) {
        double h = size / 2.0;

        Vertex3D[] vertices = {
                new Vertex3D(centerX - h, centerY - h, centerZ - h),
                new Vertex3D(centerX + h, centerY - h, centerZ - h),
                new Vertex3D(centerX - h, centerY + h, centerZ - h),
                new Vertex3D(centerX + h, centerY + h, centerZ - h),

                new Vertex3D(centerX - h, centerY - h, centerZ + h),
                new Vertex3D(centerX + h, centerY - h, centerZ + h),
                new Vertex3D(centerX - h, centerY + h, centerZ + h),
                new Vertex3D(centerX + h, centerY + h, centerZ + h)
        };

        Edge[] edges = {
                new Edge(0, 1),
                new Edge(1, 3),
                new Edge(3, 2),
                new Edge(2, 0),
                new Edge(4, 5),
                new Edge(5, 7),
                new Edge(7, 6),
                new Edge(6, 4),
                new Edge(0, 4),
                new Edge(1, 5),
                new Edge(2, 6),
                new Edge(3, 7)
        };

        // find center vertex
        Vertex3D center = new Vertex3D(centerX, centerY, centerZ);

        //rotate each vertex before drawing
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = rotate(
                    vertices[i],
                    center,
                    rotationX,
                    rotationY,
                    rotationZ
            );
        }

        // draw edges
        for (Edge edge : edges) {
            Vertex3D a = vertices[edge.a()];
            Vertex3D b = vertices[edge.b()];

            draw3DLine(a, b);
        }
    }

    // turns a 3d point into a 2d point
    private Vertex2D project(Vertex3D point) {
        int focalLength = 500; // TODO: MAKE NOT 500

        // width / 2 puts (0, 0) in the center of the screen
        int screenX = (int) (point.x() / point.z() * focalLength + ((double) width / 2));
        int screenY = (int) (-point.y() / point.z() * focalLength + ((double) height / 2));

        // create the new vertex and return it
        return new Vertex2D(screenX, screenY);
    }

    private Vertex3D rotate(Vertex3D point, Vertex3D center, double angleX, double angleY, double angleZ) {
        double x = point.x() - center.x();
        double y = point.y() - center.y();
        double z = point.z() - center.z();

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
                x + center.x(),
                y + center.y(),
                z + center.z()
        );
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

        drawCube(0, 0, 5, 2, Math.toRadians(rotY), Math.toRadians(rotY), Math.toRadians(rotY));

        rotY += 0.5;
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
