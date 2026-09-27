package scene;

import geometry.Mesh;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Mesh> objects = new ArrayList<>();

    public void add(Mesh mesh) {
        objects.add(mesh);
    }

    public void remove(Mesh mesh) {
        objects.remove(mesh);
    }

    public List<Mesh> getObjects() {
        return objects;
    }
}
