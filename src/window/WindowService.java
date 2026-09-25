// WindowService.java

package window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowService {

    private long window;

    private final int width;
    private final int height;

    private final String WINDOW_NAME = "app";

    public WindowService(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init() {
        System.out.println(
                "[WINDOW SERVICE] Loaded LWJGL."
        );

        // setup error callback
        GLFWErrorCallback
                .createPrint(System.err)
                .set();

        // check if library is initialized
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // parameters
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // create window object
        window = glfwCreateWindow(
                width,
                height,
                WINDOW_NAME,
                NULL,
                NULL
        );

        // no window
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetKeyCallback(
                window, (window, key, scancode, action, mods) -> {
                    // if ESCAPE is pressed then close the window
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                        glfwSetWindowShouldClose(window, true);
                    }
                }
        );

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(
                    window,
                    pWidth,
                    pHeight
            );

            GLFWVidMode videoMode = glfwGetVideoMode(
                    glfwGetPrimaryMonitor()
            );

            if (videoMode != null) {
                glfwSetWindowPos(
                        window,
                        (videoMode.width() - pWidth.get(0)) / 2,
                        (videoMode.height() - pHeight.get(0)) / 2
                );
            }
        }

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);

        if (callback != null) {
            callback.free();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}