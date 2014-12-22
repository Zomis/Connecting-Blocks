package net.zomis.connblocks.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.gdx.mapload.MapLoader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(LibGdxTestRunner.class)
public class ConnTest {

    @Test
    public void test() throws URISyntaxException {
        MapLoader loader = new MapLoader();
        URL uri = getClass().getClassLoader().getResource("test.tmx");
        URL tilesPng = getClass().getClassLoader().getResource("tiles.png");
        FileHandle internal = null;
        internal = new FileHandle(new File(uri.toURI()));
        BlockMap map = loader.load(internal, new FileHandle(new File(tilesPng.toURI())));
        assertNotNull(map);
        assertEquals(2, map.getConnections().size());
    }

    @Test
    public void load() {
    }

}
