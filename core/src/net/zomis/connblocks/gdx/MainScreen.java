package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.ConnectingGame;
import net.zomis.connblocks.levels.BlockLevelSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zomis on 2014-11-12.
 */
public class MainScreen implements Screen {

    private final ConnectionSelector inputHandler;
    private BlockMap map;
    private BlockMapRenderer renderer;
    private final ConnectingGame game;
    private final ConnectionMover mover;
    public final List<ConnectionActor> actors = new ArrayList<ConnectionActor>();

    public MainScreen(ConnectingGame game, ConnectionMover connectionMover, BlockLevelSet set) {
        this.game = game;
        this.mover = connectionMover;
        inputHandler = new ConnectionSelector(this, game, mover);

        this.setMap(game.helper.loadLevel(set.getLevel(0)));
    }

    @Override
    public void render(float delta) {
        renderer.render(game.batch, game.camera);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        game.inputHandler.addProcessor(this.inputHandler);
    }

    @Override
    public void hide() {
        game.inputHandler.removeProcessor(this.inputHandler);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public BlockMap getMap() {
        return map;
    }

    public void setMap(BlockMap map) {
        this.map = map;
        renderer = new BlockMapRenderer(game, map);
        mover.setConnection(map.getConnections().iterator().next());
        for (ConnectionActor actor : actors) {
            actor.remove();
        }

        for (ConnectingBlocks conn : map.getConnections()) {
            ConnectionActor actor = new ConnectionActor(game, conn);
            actors.add(actor);
            game.stage.addActor(actor);
        }
    }
}
