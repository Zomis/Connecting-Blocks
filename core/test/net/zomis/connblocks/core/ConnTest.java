package net.zomis.connblocks.core;

import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.gdx.mapload.MapLoader;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;
import static net.zomis.connblocks.Direction4.*;

@RunWith(LibGdxTestRunner.class)
public class ConnTest extends MoveTest {

    @Test
    @Ignore
    public void stackOverflow() {
        // this currently causes Stack Overflow error because connection is just going back-and-forth
        BlockMap map = loadMap("stackoverflow-moveup.tmx");
        assertEquals(1, map.getConnections().size());
        boolean moveResult = map.getConnections().iterator().next().move(Direction4.UP);
        assertTrue(moveResult);
    }

    @Test
    public void test() {
        BlockMap map = loadMap("test.tmx");
        assertNotNull(map);
        assertEquals(2, map.getConnections().size());
    }

    @Test
    public void load() {
    }

}
