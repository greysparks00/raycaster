import geometry.shapes3d.Cube;
import camera.Camera;
import rendering.DebugStats;
import rendering.Renderer;
import rendering.font.FontRenderer;
import scene.Scene;
import window.FramebufferPresenter;
import window.WindowService;

// keep class so we can use configuration
public class Main {
    void main() {
        // create
        WindowService window = new WindowService(1024, 768);
        window.init();

        Camera camera = new Camera();

        FontRenderer fontRenderer = new FontRenderer("assets/fonts/googlesans.ttf", 24f);

        Scene scene = new Scene();

        Renderer renderer = new Renderer(
                camera,
                fontRenderer,
                window.getWidth(),
                window.getHeight()
        );

        FramebufferPresenter presenter = new FramebufferPresenter(
                window.getWidth(),
                window.getHeight()
        );


        // TODO: REMOVE
        // debug stats creator
        DebugStats debugStats = new DebugStats(
                window,
                renderer,
                scene,
                camera,
                fontRenderer,
                presenter
        );

        scene.setDebugStats(debugStats);

        // add shit to scene
        double rot = 0.0;

        Cube cube1 = new Cube(2);
        cube1.setPosition(0, 0, 5);

        Cube cube2 = new Cube(1);
        cube2.setPosition(3, 0, 8);

        scene.add(cube1);
        scene.add(cube2);

        while (!window.shouldClose()) {
            renderer.render(scene);

            presenter.present(renderer.getPixels());

            window.update();

            // update scene
            cube1.setRotation(rot, rot, rot);
            cube2.setRotation(0.0, rot, 0.0);

            rot += 0.05;
        }

        // exited render loop; clean up and terminate
        presenter.cleanup();
        window.cleanup();
    }
}