package geometry;

/**
 * 3d cube
 */
public class Cube extends Mesh {

    public Cube(double size) {
        super(
                createVertices(size),
                createEdges()
        );
    }

    /**
     * creates all the vertices relative to the center (0, 0, 0)
     * @param size how long each side should be
     * @return all the vertices
     */
    private static Vertex3D[] createVertices(double size) {
        // because we use the center for calculating, we need to
        // go up/down by half instead of the full size.
        double h = size / 2.0;

        return new Vertex3D[] {
                new Vertex3D(-h, -h, -h),
                new Vertex3D( h, -h, -h),
                new Vertex3D(-h,  h, -h),
                new Vertex3D( h,  h, -h),

                new Vertex3D(-h, -h,  h),
                new Vertex3D( h, -h,  h),
                new Vertex3D(-h,  h,  h),
                new Vertex3D( h,  h,  h)
        };
    }

    /**
     * creates all the edges relative to the center (0, 0, 0)
     * @return all the edges
     */
    private static Edge[] createEdges() {
        return new Edge[] {
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
    }
}
