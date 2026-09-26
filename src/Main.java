import rendering.Camera;
import rendering.Renderer;
import window.FramebufferPresenter;
import window.WindowService;

void main() {
    // create
    WindowService window = new WindowService(1024, 768);
    window.init();

    Camera camera = new Camera();

    Renderer renderer = new Renderer(
            camera,
            window.getWidth(),
            window.getHeight()
    );

    FramebufferPresenter presenter = new FramebufferPresenter(
            window.getWidth(),
            window.getHeight()
    );

    while (!window.shouldClose()) {
        renderer.render();

        presenter.present(
                renderer.getPixels()
        );

        window.update();
    }

    // exited render loop; clean up and terminate
    presenter.cleanup();
    window.cleanup();
}
