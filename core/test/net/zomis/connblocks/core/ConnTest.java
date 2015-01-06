package net.zomis.connblocks.core;

import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.Direction4;
import org.junit.*;
import org.junit.runner.RunWith;

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
        connectionByColor(1).move(UP);
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
    public void connect() {
        BlockMap map = loadMap("connect.tmx");
        ConnectingBlocks conn = connectionAt(4, 1);
        assertEquals(2, map.getConnections().size());
        conn.move(LEFT);
        conn.move(LEFT);
        assertEquals(1, map.getConnections().size());
        conn.move(LEFT);
        conn.move(DOWN);
        conn.move(DOWN);
        assertTrue(map.checkForGoal());
    }

}
