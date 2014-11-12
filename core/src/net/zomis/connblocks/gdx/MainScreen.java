package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingGame;
import net.zomis.connblocks.ConnectionMover;
import net.zomis.connblocks.levels.BlockLevelSet;

/**
 * Created by Zomis on 2014-11-12.
 */
public class MainScreen implements Screen {

    private BlockMap map;
    private BlockMapRenderer renderer;
    private final ConnectingGame game;
    private final ConnectionMover mover;

    public MainScreen(ConnectingGame game, ConnectionMover connectionMover, BlockLevelSet set) {
        this.game = game;
        this.mover = connectionMover;

        this.setMap(game.helper.loadLevel(set.getLevel(0)));
//        new Mesh()
//        new VertexAttribute()
    }

    @Override
    public void render(float delta) {

        renderer.render(game.batch, game.camera, mover.getConnection());

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public BlockMap getMap() {
        return map;
    }

    public void setMap(BlockMap map) {
        this.map = map;
        renderer = new BlockMapRenderer(map);
        mover.setConnection(map.getConnections().iterator().next());
    }
}
