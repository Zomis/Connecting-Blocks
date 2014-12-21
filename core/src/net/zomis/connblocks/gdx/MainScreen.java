package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.events.ConnectionAddedEvent;
import net.zomis.connblocks.events.ConnectionMergeEvent;
import net.zomis.connblocks.levels.BlockLevelSet;
import net.zomis.events.EventConsumer;

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
    private final SpriteBatch myBatch = new SpriteBatch();

    public MainScreen(ConnectingGame game, ConnectionMover connectionMover, BlockLevelSet set) {
        this.game = game;
        this.mover = connectionMover;
        inputHandler = new ConnectionSelector(this, game);

        this.setMap(game.loadLevel(set, 0));
    }

    @Override
    public void render(float delta) {

        renderer.render(myBatch, game.camera);
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
        for (ConnectionActor actor : actors) {
            actor.remove();
        }
        actors.clear();
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
        for (ConnectionActor actor : actors) {
            actor.remove();
        }

        for (ConnectingBlocks conn : map.getConnections()) {
            ConnectionActor actor = new ConnectionActor(game, conn);
            actors.add(actor);
            game.stage.addActor(actor);
        }

        selectConnection(map.getConnections().iterator().next());

        map.getEventExecutor().registerHandler(ConnectionMergeEvent.class, new EventConsumer<ConnectionMergeEvent>() {
            @Override
            public void executeEvent(ConnectionMergeEvent connectionMergeEvent) {
                if (connectionMergeEvent.getSecondary() == mover.getConnection()) {
                    selectConnection(connectionMergeEvent.getPrimary());
                }
            }
        });
        map.getEventExecutor().registerHandler(ConnectionAddedEvent.class, new EventConsumer<ConnectionAddedEvent>() {
            @Override
            public void executeEvent(ConnectionAddedEvent event) {
                ConnectionActor actor = new ConnectionActor(game, event.getConnection());
                actors.add(actor);
                game.stage.addActor(actor);
            }
        });
    }

    public void selectConnection(ConnectingBlocks connection) {
        if (mover.getConnection() == connection) {
            return;
        }

        for (ConnectionActor actor : actors) {
            actor.clearActions();
            actor.getColor().a = 1.0f;
            if (actor.getConnection() == connection) {
                actor.addAction(Actions.forever(Actions.sequence(
                        Actions.alpha(0.3f, 1.5f),
                        Actions.alpha(0.8f, 1.5f)
                )));
            }
        }

        mover.setConnection(connection);
    }
}
