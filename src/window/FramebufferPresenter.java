package window;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class FramebufferPresenter {

    private final int width;
    private final int height;

    private int textureId;

    private final ByteBuffer pixelBuffer;

    public FramebufferPresenter(int width, int height) {

        this.width = width;
        this.height = height;

        pixelBuffer = BufferUtils.createByteBuffer(
                width * height * 3
        );

        textureId = glGenTextures();

        glBindTexture(
                GL_TEXTURE_2D,
                textureId
        );

        glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER,
                GL_NEAREST
        );

        glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER,
                GL_NEAREST
        );

        glPixelStorei(
                GL_UNPACK_ALIGNMENT,
                1
        );

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                width,
                height,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                (ByteBuffer) null
        );

        glBindTexture(
                GL_TEXTURE_2D,
                0
        );
    }

    public void present(int[] pixels) {
        pixelBuffer.clear();

        for (int color : pixels) {
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            pixelBuffer.put((byte) r);
            pixelBuffer.put((byte) g);
            pixelBuffer.put((byte) b);
        }

        pixelBuffer.flip();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexSubImage2D(
                GL_TEXTURE_2D,
                0,
                0,
                0,
                width,
                height,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                pixelBuffer
        );

        glEnable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(-1.0f, -1.0f);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(1.0f, -1.0f);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(1.0f, 1.0f);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(-1.0f, 1.0f);

        glEnd();

        glDisable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * deletes textures at the end of the program
     */
    public void cleanup() {
        glDeleteTextures(textureId);
    }
}
