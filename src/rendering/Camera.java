package rendering;

import geometry.Vertex3D;

public class Camera {

    private Vertex3D position;
    private Vertex3D rotation;

    private double fov;

    public Camera() {
        this.position = new Vertex3D(0, 0, 0);
        this.rotation = new Vertex3D(0, 0, 0);

        this.fov = 90;
    }

    /*
     * GETTERS
     */
    public Vertex3D getPosition() {
        return position;
    }

    public Vertex3D getRotation() {
        return rotation;
    }

    public double getFov() {
        return fov;
    }

    /*
     * SETTERS
     */
    public void setPosition(Vertex3D position) {
        this.position = position;
    }

    public void setRotation(Vertex3D rotation) {
        this.rotation = rotation;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
