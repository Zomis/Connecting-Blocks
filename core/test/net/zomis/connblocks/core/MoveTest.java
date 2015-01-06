package net.zomis.connblocks.core;

import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.gdx.mapload.MapLoader;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Zomis on 2015-01-06.
 */
public class MoveTest {
    public BlockMap loadMap(String name) {
        try {
            MapLoader loader = new MapLoader();
            URL uri = getClass().getClassLoader().getResource(name);
            URL tilesPng = getClass().getClassLoader().getResource("tiles.png");
            FileHandle internal = new FileHandle(new File(uri.toURI()));
            BlockMap map = loader.load(internal, new FileHandle(new File(tilesPng.toURI())));
            assertNotNull("Unable to load map", map);
            return map;
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
