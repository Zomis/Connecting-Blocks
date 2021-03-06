package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import net.zomis.connblocks.ConnBlocks;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.ConnectingBlocks;

import java.util.Iterator;

/**
 * Created by Zomis on 2014-11-28.
 */
public class DesktopKeyboard extends InputAdapter {
    private final ConnectingGame game;

    public DesktopKeyboard(ConnectingGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            return false;
        }
        switch (keycode) {
            case Input.Keys.TAB:
                nextConnection();
                break;
            case Input.Keys.R:
                game.resetMap();
                break;
            case Input.Keys.P:
                game.previousLevel();
                break;
            case Input.Keys.N:
                game.nextLevel();
                break;
            case Input.Keys.ESCAPE:
                game.backToMenu();
                break;
            case Input.Keys.PLUS:
                game.camera.zoom -= 0.1f;
                break;
            case Input.Keys.MINUS:
                game.camera.zoom += 0.1f;
                break;
            case Input.Keys.UP:
                direction(Direction4.UP);
                break;
            case Input.Keys.DOWN:
                direction(Direction4.DOWN);
                break;
            case Input.Keys.LEFT:
                direction(Direction4.LEFT);
                break;
            case Input.Keys.RIGHT:
                direction(Direction4.RIGHT);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean nextConnection() {
        ConnectingBlocks connection = game.getConnectionMover().getConnection();
        if (game.getMainScreen() == null) {
            return false;
        }
        if (connection == null) {
            // No connection selected, select the first one that appears
            Iterator<ConnectingBlocks> it = game.getMainScreen().getMap().getConnections().iterator();
            game.getMainScreen().selectConnection(it.hasNext() ? it.next() : null);
            return true;
        }
        Iterator<ConnectingBlocks> it = connection.getMap().getConnections().iterator();
        while (it.hasNext()) {
            if (connection == it.next()) {
                // We are on the current connection
                ConnectingBlocks nextConnection;
                do {
                    if (!it.hasNext()) {
                        it = connection.getMap().getConnections().iterator();
                    }
                    nextConnection = it.next();
                }
                while (!nextConnection.isControllable() && nextConnection != connection);
                game.getMainScreen().selectConnection(nextConnection);
                return true;
            }
        }
        return false;
    }

    private void direction(Direction4 direction) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            return;
        }

        // Move connection
        game.getConnectionMover().move(direction);
    }

    public void checkInputs() {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            // Pan camera
            int deltaX = 0;
            int deltaY = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                deltaX -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                deltaY -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                deltaX += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                deltaY += 1;
            }
            int step = 4;
            ConnBlocks.log("pan " + deltaX + ", " + deltaY);
            game.camera.translate(deltaX * step, deltaY * step);
        }

    }
}
