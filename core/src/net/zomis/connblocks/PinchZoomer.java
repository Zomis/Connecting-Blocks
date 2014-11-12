package net.zomis.connblocks;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Zomis on 2014-11-12.
 */
public class PinchZoomer extends GestureDetector.GestureAdapter {

    private final OrthographicCamera camera;

    private float startingInitialDistance;
    private float startingZoom;

    public PinchZoomer(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (startingInitialDistance != initialDistance) {
            startingInitialDistance = initialDistance;
            startingZoom = camera.zoom;
        }
        camera.zoom = startingZoom * (initialDistance / distance);
        return true;
    }

}
