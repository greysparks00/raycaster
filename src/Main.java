import geometry.shapes3d.Cube;
import camera.Camera;
import rendering.Renderer;
import scene.Scene;
import window.FramebufferPresenter;
import window.WindowService;

void main() {
    // create
    WindowService window = new WindowService(1024, 768);
    window.init();

    Camera camera = new Camera();

    Scene scene = new Scene();

    Renderer renderer = new Renderer(
            camera,
            window.getWidth(),
            window.getHeight()
    );

    FramebufferPresenter presenter = new FramebufferPresenter(
            window.getWidth(),
            window.getHeight()
    );

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
