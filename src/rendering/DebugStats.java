package rendering;

import camera.Camera;
import misc.Color;
import rendering.font.FontRenderer;
import rendering.font.TextEntity;
import scene.Scene;
import window.FramebufferPresenter;
import window.WindowService;

public class DebugStats {

    private final WindowService window;
    private final Renderer renderer;
    private final Scene scene;
    private final Camera camera;
    private final FontRenderer fontRenderer;
    private final FramebufferPresenter framebufferPresenter;

    private static long lastFpsTime = System.nanoTime();
    private static int frames = 0;
    private static int fps = 0;

    // text labels stored here
    // hack - yes
    private TextEntity debugInformationTitleText;
    private TextEntity windowDimensionsText;
    private TextEntity rendererServiceTitleText;
    private TextEntity fpsText;
    private TextEntity meshCountText;
    private TextEntity cameraServiceTitleText;
    private TextEntity cameraPositionText;
    private TextEntity cameraRotationText;
    private TextEntity fontRendererServiceTitleText;
    private TextEntity textCountText;

    public DebugStats(WindowService window, Renderer renderer, Scene scene, Camera camera, FontRenderer fontRenderer, FramebufferPresenter framebufferPresenter) {
        this.window = window;
        this.renderer = renderer;
        this.scene = scene;
        this.camera = camera;
        this.fontRenderer = fontRenderer;
        this.framebufferPresenter = framebufferPresenter;
    }

    public static int getFps() {
        frames++;

        long now = System.nanoTime();

        if (now - lastFpsTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastFpsTime = now;
        }

        return fps;
    }

    /**
     * creates all the text objects for displaying debug information
     * DOES NOT RENDER!
     */
    public void createDebugLabels() {
        // debug info title
        debugInformationTitleText = fontRenderer.createText(
                "`" + window.getWindowName() + "` debug information:",
                Color.YELLOW,
                0, 0
        );

        // window dimensions text
        windowDimensionsText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 20
        );

        // drawing info title
        rendererServiceTitleText = fontRenderer.createText(
                "rendering service:",
                Color.YELLOW,
                0, 60
        );

        // fps text
        fpsText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 80
        );

        // mesh count text
        meshCountText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 100
        );

        // camera service title
        cameraServiceTitleText = fontRenderer.createText(
                "camera service:",
                Color.YELLOW,
                0, 140
        );

        // camera position text
        cameraPositionText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 160
        );

        // camera rotation text
        cameraRotationText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 180
        );

        // font title text
        fontRendererServiceTitleText = fontRenderer.createText(
                "font renderer service",
                Color.YELLOW,
                0, 220
        );

        // text count text
        textCountText = fontRenderer.createText(
                "1",
                Color.WHITE,
                0, 240
        );
    }

    /**
     * handles updating all the debug labels
     */
    public void render() {
        // check if any of the labels have not been created
        // if they have not; create all of them
        // they should all be synced.
        // this is a hack and is shitty
        if (debugInformationTitleText == null) {
            createDebugLabels();
        }

        // everything should be created

        windowDimensionsText.setText("window rendering at: " + window.getWidth() + "px x " + window.getHeight() + "px");
        fpsText.setText("fps: " + getFps());
        meshCountText.setText("mesh count: " + scene.getObjects().size());
        cameraPositionText.setText("camera is positioned at: " + camera.getPosition());
        cameraRotationText.setText("camera is rotated at: " + camera.getRotation());
        textCountText.setText("text count: " + fontRenderer.getTextEntityList().size());
    }
}
