package rendering;

/**
 * binds words to commonly used rgb3 color codes.
 */
public enum Color {

    // colors
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    WHITE(255, 255, 255),
    BLACK(0, 0, 0);


    private final int r;
    private final int g;
    private final int b;

    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // getters for each color value
    public int r() { return r; }
    public int g() { return g; }
    public int b() { return b; }
}