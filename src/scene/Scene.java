package scene;

import geometry.Mesh;
import rendering.DebugStats;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Mesh> objects = new ArrayList<>();

    private DebugStats debugStats;

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
     * SETTERS
     */

    /**
     * OPTIONAL - only for debugging
     *          - DO NOT SHIP WITH A DEBUG STATS
     * sets the debug stats object if needed
     *
     * @param debugStatsObject the created debug stats object
     */
    public void setDebugStats(DebugStats debugStatsObject) {
        debugStats = debugStatsObject;
    }

    /*
     * GETTERS
     */

    /**
     * returns the list of the objects in the scene
     * @return the list of scene objects
     */
    public List<Mesh> getObjects() {
        return objects;
    }

    /**
     * gets the debug stats object for debug information
     * @return the debug stats object
     */
    public DebugStats getDebugStats() {
        return debugStats;
    }
}
