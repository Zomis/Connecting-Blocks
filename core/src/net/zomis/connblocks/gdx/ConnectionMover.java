package net.zomis.connblocks.gdx;

import com.badlogic.gdx.input.GestureDetector;
import net.zomis.ConnBlocks;
import net.zomis.Direction4;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;

/**
 * Created by Zomis on 2014-11-12.
 */
public class ConnectionMover extends GestureDetector.GestureAdapter {

    private final Runnable onGoal;
    private ConnectingBlocks connection;
    private float totalX;
    private float totalY;
    private float STEP_DIVISOR = 50f;
    private boolean panning;
    private boolean mapFinished;

    public ConnectionMover(Runnable onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (!panning) {
            totalX = x;
            totalY = y;
            panning = true;
        }

        int xmove = (int) ((x - totalX) / STEP_DIVISOR);
        int ymove = (int) ((y - totalY) / STEP_DIVISOR);
        Direction4 dir = Direction4.direction(0, 0, xmove, ymove);
        if (connection != null && dir != null) {
            ConnBlocks.log("Move " + totalX + ", " + totalY + " dir " + dir + " move vars " + xmove + ", " + ymove);
            move(dir);
            totalX += Math.signum(xmove) * STEP_DIVISOR;
            totalY += Math.signum(ymove) * STEP_DIVISOR;
            return true;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        panning = false;
        ConnBlocks.log("pan stop, resetting");
        return false;
    }

    public void move(Direction4 dir) {
        if (mapFinished) {
            return;
        }
        if (connection == null) {
            return;
        }
        BlockMap map = connection.getMap();
        map.stateBasedEffects();
        connection.move(dir);
        if (onGoal != null && map.checkForGoal()) {
            onGoal.run();
            mapFinished = true;
        }
    }

    public void setConnection(ConnectingBlocks connection) {
        this.connection = connection;
        mapFinished = false;
    }

    public ConnectingBlocks getConnection() {
        return connection;
    }
}
