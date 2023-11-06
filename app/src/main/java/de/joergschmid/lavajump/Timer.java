package de.joergschmid.lavajump;

public class Timer {

    private long start;
    private long pause;
    private long pauseTime = 0;
    private long lastPauseTime = 0;
    private boolean markerPaused = false;
    private long marker;

    public Timer() {
        start = System.currentTimeMillis();
    }

    public void pausePlayTime() {
        pause = System.currentTimeMillis();
        markerPaused = true;
    }

    public void resumePlayTime() {
        lastPauseTime = System.currentTimeMillis() - pause;
        pauseTime += lastPauseTime;
    }

    public long getPlayTime() {
        return System.currentTimeMillis() - start - pauseTime;
    }

    public void setMarker() {
        marker = System.currentTimeMillis();
    }

    public long getMarkerTimeElapsed() {
        if(markerPaused) {
            markerPaused = false;
            return System.currentTimeMillis() - marker - lastPauseTime;
        }
        return System.currentTimeMillis() - marker;
    }
}
