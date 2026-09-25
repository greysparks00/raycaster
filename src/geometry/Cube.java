package geometry;

public class Cube extends Mesh {

    public Cube(double size) {
        super(
                createVertices(size),
                createEdges()
        );
    }

    private static Vertex3D[] createVertices(double size) {
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
