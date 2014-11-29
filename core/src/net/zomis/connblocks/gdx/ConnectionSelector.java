package net.zomis.connblocks.gdx;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import net.zomis.connblocks.ConnBlocks;
import net.zomis.connblocks.*;

public class ConnectionSelector extends InputAdapter {
    private final Vector3 click = new Vector3();
    private final ConnectingGame game;
    private final MainScreen mainScreen;

    public ConnectionSelector(MainScreen mainScreen, ConnectingGame game) {
        this.game = game;
        this.mainScreen = mainScreen;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (mainScreen.getMap() == null) {
            return false;
        }
        click.set(screenX, screenY, 0);
        game.camera.unproject(click);
        int x = (int) (click.x / BlockMapRenderer.size);
        int y = (int) (click.y / BlockMapRenderer.size);

        ConnBlocks.log("click at " + x + ", " + y);
        BlockTile tile = mainScreen.getMap().pos(x, y);
        if (tile == null) {
            return false;
        }
        ConnectingBlocks connection = tile.getConnection();
        if (connection == null) {
            return false;
        }

        mainScreen.selectConnection(connection);

        return true;
    }
}
