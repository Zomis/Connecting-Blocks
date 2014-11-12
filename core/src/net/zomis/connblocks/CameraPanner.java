package net.zomis.connblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Zomis on 2014-11-12.
 */
public class CameraPanner extends GestureDetector.GestureAdapter {
    private final OrthographicCamera camera;

    public CameraPanner(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.position.add(deltaX, deltaY, 0);
        return true;
    }

}
