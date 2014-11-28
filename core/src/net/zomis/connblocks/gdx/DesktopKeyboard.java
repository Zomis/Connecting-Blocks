package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import net.zomis.Direction4;
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
        switch (keycode) {
            case Input.Keys.TAB:
                nextConnection();
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
            Iterator<ConnectingBlocks> it = game.getMainScreen().getMap().getConnections().iterator();
            game.getMainScreen().selectConnection(it.hasNext() ? it.next() : null);
            return true;
        }
        Iterator<ConnectingBlocks> it = connection.getMap().getConnections().iterator();
        while (it.hasNext()) {
            if (connection == it.next()) {
                ConnectingBlocks nextConnection =
                        it.hasNext() ? it.next() : connection.getMap().getConnections().iterator().next();
                game.getMainScreen().selectConnection(nextConnection);
                break;
            }
        }
        return true;
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
            Direction4 dir = null;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                dir = Direction4.LEFT;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                dir = Direction4.UP;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                dir = Direction4.RIGHT;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                dir = Direction4.DOWN;
            }
            int step = 4;
            if (dir != null) {
                game.camera.translate(dir.getDeltaX() * step, dir.getDeltaY() * step);
            }
        }

    }
}
