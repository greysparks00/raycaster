// WindowService.java

package window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
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

    /**
     * handles creating and defining the window
     * and shows it, but it does not handle we do
     * not handle the rendering here in this file.
     */
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

        // key callback
        try (GLFWKeyCallback keyCallback = glfwSetKeyCallback(
                window, (window, key, scancode, action, mods) -> {
                    // handle keys here

                    // if ESCAPE is pressed then close the window
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                        glfwSetWindowShouldClose(window, true);
                    }

                }
        )) {
            if (keyCallback != null) {
                keyCallback.free();
            }
        }

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

    /**
     * ran by rendering/Renderer
     * updates the window every frame and
     * handles the events completed
     */
    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * returns whether the window should close or not
     * @return boolean if the window should close
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * used to clean up at the end of the program
     * destroys all remenants and callbacks
     */
    public void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();

        try (GLFWErrorCallback callback = glfwSetErrorCallback(null)) {
            if (callback != null) {
                callback.free();
            }
        }
    }

    // getters
    public int getWidth() { return width; }   // make WindowService the only file to tell what
    public int getHeight() { return height; } // the window dimensions are
}