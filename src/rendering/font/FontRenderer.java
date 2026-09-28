package rendering.font;

import misc.Color;
import rendering.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FontRenderer {

    // store all created texts
    private final List<TextEntity> textEntityList = new ArrayList<>();

    // renderer object
    private Renderer renderer;

    // loaded font with size
    private Font font;

    /**
     * creates a font and handles rendering
     * @param path the file path of the font to load
     * @param size the size of the font
     */
    public FontRenderer(String path, float size) {
        try {
            font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(path)
            );

            font = font.deriveFont(size);

        } catch (FontFormatException | IOException exception) {
            throw new RuntimeException(
                    "Error loading Font: " + path,
                    exception
            );
        }
    }

    /**
     * creates a buffered image using the font and the provided text
     * and then sets each pixel to that image
     * @param textEntity the text entity to use for information
     *                   payload
     */
    public void drawText(TextEntity textEntity) {
        // get payload
        String text  = textEntity.getText();
        Color  color = textEntity.getColor();
        int x        = textEntity.getX();
        int y        = textEntity.getY();

        BufferedImage image = createTextImage(text, color);

        // loop through the entire image and set each pixel on the renderer to the image
        for (int py = 0; py < image.getHeight(); py++) {
            for (int px = 0; px < image.getWidth(); px++) {

                // find colors and alpha
                int argb = image.getRGB(px, py);

                int alpha = (argb >> 24) & 0xff;

                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;

                // not visible
                if (alpha == 0) {
                    continue;
                }

                // fully visible; no blending needed
                if (alpha == 255) {
                    renderer.setPixel(
                            x + px,
                            y + py,
                            r,
                            g,
                            b
                    );

                    continue;
                }

                renderer.blendPixel(
                        x + px,
                        y + py,
                        r,
                        g,
                        b,
                        alpha
                );
            }
        }
    }

    /**
     * creates a buffered image from the provided font and text
     * @param text the text to create the image in the font with
     * @param color the color to draw it in
     * @return the buffered image
     */
    private BufferedImage createTextImage(String text, Color color) {
        // calculate font dimensions
        BufferedImage temporary = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = temporary.createGraphics();
        graphics.setFont(font);

        FontMetrics fontMetrics = graphics.getFontMetrics();

        int width = fontMetrics.stringWidth(text);
        int height = fontMetrics.getHeight();

        graphics.dispose();

        // create final image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        graphics = image.createGraphics();

        graphics.setFont(font);
        graphics.setColor(
                new java.awt.Color(color.r(), color.g(), color.b())
        );

        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        // draw it
        graphics.drawString(text, 0, fontMetrics.getAscent());

        graphics.dispose();

        return image;
    }

    /**
     * creates a new text object and stores it to be rendered
     * @param text the text to display
     * @param color the color to display the text in
     * @param x the x coordinate of the text
     * @param y the y coordinate of the text
     */
    public TextEntity createText(String text, Color color, int x, int y) {
        TextEntity newTextEntity = new TextEntity(
                text,
                color,
                x,
                y
        );

        textEntityList.add(newTextEntity);

        return newTextEntity;
    }

    /**
     * removes a text entity from the drawing list
     * @param textEntity the text entity to remove
     */
    public void removeText(TextEntity textEntity) {
        textEntityList.remove(textEntity);
    }

    /**
     * gets the created text entities
     * @return the text entity list
     */
    public List<TextEntity> getTextEntityList() {
        return textEntityList;
    }

    /**
     * sets the main renderer
     * @param mainRenderer the main renderer object
     */
    public void setMainRenderer(Renderer mainRenderer) {
        if (renderer != null) {
            System.out.println("[FONT RENDERER] WARNING! Attempting to set main renderer when it is already set!");
        }

        renderer = mainRenderer;
    }
}
