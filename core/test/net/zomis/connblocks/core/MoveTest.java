package net.zomis.connblocks.core;

import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.gdx.mapload.MapLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Zomis on 2015-01-06.
 */
public class MoveTest {
    protected BlockMap map;

    public BlockMap loadMap(String name) {
        try {
            MapLoader loader = new MapLoader();
            URL uri = getClass().getClassLoader().getResource(name);
            URL tilesPng = getClass().getClassLoader().getResource("tiles.png");
            FileHandle internal = new FileHandle(new File(uri.toURI()));
            BlockMap map = loader.load(internal, new FileHandle(new File(tilesPng.toURI())));
            assertNotNull("Unable to load map", map);
            this.map = map;
            return map;
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    public ConnectingBlocks connectionByColor(int bitPattern) {
        ConnectingBlocks result = null;
        for (ConnectingBlocks conn : map.getConnections()) {
            if (!conn.isControllable()) {
                continue;
            }
            if ((conn.getConnectGroups() & bitPattern) != bitPattern) {
                continue;
            }
            if (result != null) {
                throw new AssertionError("Multiple connections exists with pattern " + bitPattern);
            }
            result = conn;
        }
        return result;
    }

    public ConnectingBlocks conn() {
        ConnectingBlocks result = null;
        for (ConnectingBlocks conn : map.getConnections()) {
            if (!conn.isControllable()) {
                continue;
            }
            if (result != null) {
                throw new AssertionError("Multiple controllable connections exists");
            }
            result = conn;
        }
        return result;
    }

    public ConnectingBlocks connectionAt(int x, int y) {
        BlockTile pos = map.pos(x, y);
        if (pos == null) {
            throw new NullPointerException("Position " + x + ", " + y + " is out of range");
        }
        return pos.getBlock() != null ? pos.getBlock().getConnection() : null;
    }

    public boolean move(ConnectingBlocks conn, Direction4 dir) {
        BlockMap map = conn.getMap();
        conn.move(dir);
        map.stateBasedEffects();
        return map.checkForGoal();
    }

}
