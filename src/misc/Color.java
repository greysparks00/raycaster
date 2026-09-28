package misc;

import java.util.Objects;

/**
 * binds words to commonly used rgb3 color codes.
 */
public class Color {

    // colors
    public static final Color RED   = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE  = new Color(0, 0, 255);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color YELLOW = new Color(255, 255, 0);

    private final int r;
    private final int g;
    private final int b;

    public Color(int r, int g, int b) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
    }

    /**
     * helper that turns a color object into RGB
     * @return the rgb of the color
     */
    public int toRGB() {
        return (r << 16) | (g << 8) | b;
    }

    /**
     * clamps a color code between 0 and 255
     * @param value the value to clamp
     * @return the clamped value
     */
    private static int clamp(int value) {
        return Math.clamp(value, 0, 255);
    }

    /**
     * replaces the standard equals method used for enums
     * @param o the reference object with which to compare.
     * @return true or false whether the object equals another
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color color)) return false;
        return r == color.r && g == color.g && b == color.b;
    }

    /**
     * replaces the standard hashcode method used for enums
     * @return the hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    // getters for each color value
    public int r() { return r; }
    public int g() { return g; }
    public int b() { return b; }
}