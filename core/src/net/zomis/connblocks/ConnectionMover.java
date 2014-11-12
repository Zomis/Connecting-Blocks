package net.zomis.connblocks;

import com.badlogic.gdx.input.GestureDetector;
import net.zomis.ConnBlocks;
import net.zomis.Direction4;

/**
 * Created by Zomis on 2014-11-12.
 */
public class ConnectionMover extends GestureDetector.GestureAdapter {

    private final Runnable onGoal;
    private ConnectingBlocks connection;

    public ConnectionMover(Runnable onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (connection != null) {
            BlockMap map = connection.getMap();

            Direction4 dir;
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                dir = Direction4.direction(0, 0, (int) Math.signum(velocityX), 0);
            }
            else {
                dir = Direction4.direction(0, 0, 0, (int) Math.signum(velocityY));
            }
            ConnBlocks.log("Dir move " + dir + ": " + velocityX + ", " + velocityY);
            connection.move(dir);
            map.stateBasedEffects();
            if (onGoal != null && map.checkForGoal()) {
                onGoal.run();
            }
        }
        return true;
    }

    public void setConnection(ConnectingBlocks connection) {
        this.connection = connection;
    }

    public ConnectingBlocks getConnection() {
        return connection;
    }
}
