package rendering.font;

import misc.Color;

/**
 * handles construction of a text entity
 * which is an object that contains all the data surrounding a
 * piece of text drawn
 */
public class TextEntity {

    // construction
    private String text;
    private int x;
    private int y;
    private Color color;

    public TextEntity(String text, Color color, int x, int y) {
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /*
     * GETTERS
     */

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    /*
     * SETTERS
     */

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}