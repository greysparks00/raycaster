package rendering;

public class DebugStats {

    private static long lastFpsTime = System.nanoTime();
    private static int frames = 0;
    private static int fps = 0;

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
}
