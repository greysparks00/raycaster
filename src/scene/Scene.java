package scene;

import camera.Camera;
import geometry.Mesh;
import rendering.Renderer;
import rendering.font.FontRenderer;
import window.FramebufferPresenter;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Mesh> objects = new ArrayList<>();

    /**
     * adds a mesh to the scene
     * @param mesh the mesh to add to the scene
     */
    public void add(Mesh mesh) {
        objects.add(mesh);
    }

    /**
     * removes a mesh from the scene
     * @param mesh the mesh to remove from the scene
     */
    public void remove(Mesh mesh) {
        objects.remove(mesh);
    }

    /*
        getters
     */

    /**
     * returns the list of the objects in the scene
     * @return the list of scene objects
     */
    public List<Mesh> getObjects() {
        return objects;
    }
}
